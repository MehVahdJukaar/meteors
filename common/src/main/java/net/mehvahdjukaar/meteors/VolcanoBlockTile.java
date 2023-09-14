package net.mehvahdjukaar.meteors;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VolcanoBlockTile extends BlockEntity {

    private int tickCount;
    private int lavaLevel = VolcanoSettings.MIN_Y.get();

    public VolcanoBlockTile(BlockPos blockPos, BlockState blockState) {
        super(Meteors.VOLCANO_TILE.get(), blockPos, blockState);

    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("tickCount", tickCount);
        tag.putInt("lavaLevel", lavaLevel);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.tickCount = tag.getInt("tickCount");
        this.lavaLevel = tag.getInt("lavaLevel");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, VolcanoBlockTile e) {
        if (!level.isClientSide) e.tick(level, pos);
    }

    public void tick(Level level, BlockPos pos) {
        tickCount++;

        double chance = computeChance();

        if (level.random.nextFloat() < chance) {
            shootFromRotation(pos, (float) (-90f + 20 * level.random.nextGaussian()),
                    180 * level.random.nextFloat(), 0, 1, 1.5f);

        }




        //lava stuff
        if (tickCount % 20 == 0) {
            int maxTime = VolcanoSettings.TIME_TO_FINAL_ERUPTION.get();
            double a = VolcanoSettings.POWER_INCREASE_AMPL.get();
            int r = VolcanoSettings.POWER_INCREASE_EXPONENT.get();
            double mult = 1f / (a * intPower(maxTime, r));
            float time = (tickCount / 20f) % maxTime;

            double normalized = (a * intPower(time, r)) * mult;

            int ll = (int) Mth.map(normalized, 0, 1, VolcanoSettings.MIN_Y.get(),
                    VolcanoSettings.MAX_Y.get());

            if (ll > VolcanoSettings.MAX_Y.get()) {
                int aa = 1;
            }
            if (ll != lavaLevel) {
                lavaLevel = ll;
                fillWithLavaRecursive(Direction.NORTH, new HashSet<>(), level, new BlockPos(pos.getX(), lavaLevel, pos.getZ()), 200);
            }

            //y = ax^k
            //x = T -> x = 1;
            // maT^k = 1
            // m = 1/

            if (tickCount % 40 == 0) {
                for (var v : level.getServer().getPlayerList().getPlayers()) {
                    if (v.isCreative()) {
                        v.displayClientMessage(Component.literal(String.format("%.3f", this.computeChance())+
                                        String.format( " - t: %.2f", +100*time/maxTime)  ),
                                false);
                    }
                }
            }
        }
    }

    public void shootFromRotation(BlockPos pos, float x, float y, float z, float velocity, float inaccuracy) {
        MeteorEntity me = new MeteorEntity(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, level);


        float f = -Mth.sin(y * (float) (Math.PI / 180.0)) * Mth.cos(x * (float) (Math.PI / 180.0));
        float g = -Mth.sin((x + z) * (float) (Math.PI / 180.0));
        float h = Mth.cos(y * (float) (Math.PI / 180.0)) * Mth.cos(x * (float) (Math.PI / 180.0));
        me.shoot(f, g, h, velocity, inaccuracy);
        me.setNoDamages();
        level.addFreshEntity(me);
    }

    public double computeChance() {
//\left(a\cdot x\right)\operatorname{abs}\left(\sin\left(xf\right)\right)^{\frac{s}{f}}+b\cdot a\cdot x

        //time in seconds
        float time = tickCount / 20f;

        int maxTime = VolcanoSettings.TIME_TO_FINAL_ERUPTION.get();

        if (time >= maxTime) {
            this.level.explode(null, this.getBlockPos().getX(), this.getBlockPos().getY(),
                    this.getBlockPos().getZ(), 5, Level.ExplosionInteraction.TNT);
            this.level.removeBlock(worldPosition, false);
            tickCount = 0;
        }
        time = time % maxTime;

        float frequency = Mth.PI * VolcanoSettings.ERUPTION_NUMBER.get() / (float) maxTime;

        int increaseRank = VolcanoSettings.POWER_INCREASE_EXPONENT.get();

        double steepness = VolcanoSettings.PEAK_SHARPNESS.get();

        double b = VolcanoSettings.QUIET_TIME_BASE_INCREASE.get();
        double a = VolcanoSettings.POWER_INCREASE_AMPL.get();

        float s = Mth.abs(Mth.sin(time * frequency));

        double exp = steepness / frequency;
        double p = Math.pow(s, exp);

        return (a * intPower(time, increaseRank)) * p + (b * time);
    }

    public static double intPower(double base, int exponent) {
        if (exponent < 0) {
            throw new IllegalArgumentException("Exponent must be non-negative");
        }
        if (exponent == 0) {
            return 1.0; // Anything raised to the power of 0 is 1
        }

        double result = 1.0;
        for (int i = 0; i < exponent; i++) {
            result *= base;
        }

        return result;
    }


    private static final List<Direction> DIR_LIST = Direction.Plane.HORIZONTAL.stream().toList();

    public void fillWithLavaRecursive(Direction fromDir, Set<BlockPos> visited, Level level, BlockPos pos, int recursionLeft) {
        for (var d : DIR_LIST) {
            if (recursionLeft <= 0) return;
            if (d != fromDir) {
                BlockPos p = pos.relative(d);
                if (!visited.contains(p)) {
                    visited.add(p);
                    if (level.getBlockState(p).isAir()) {
                        level.setBlockAndUpdate(p, Blocks.LAVA.defaultBlockState());
                        recursionLeft -= 1;
                        fillWithLavaRecursive(d, visited, level, p, recursionLeft);
                    }
                }
            }
        }
    }

    public void addTime(int i) {
        tickCount+=i;
    }
}
