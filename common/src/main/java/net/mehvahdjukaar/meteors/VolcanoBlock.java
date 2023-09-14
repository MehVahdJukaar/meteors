package net.mehvahdjukaar.meteors;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class VolcanoBlock extends BaseEntityBlock{

    public VolcanoBlock() {
        super(Properties.copy(Blocks.BARRIER));
    }
    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VolcanoBlockTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, Meteors.VOLCANO_TILE.get(), VolcanoBlockTile::tick);

    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(Minecraft.getInstance().player.isCreative()){
            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK_MARKER, state),
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if(player.isSecondaryUseActive()) {
            for (int i = 0; i < 30; i++) {
                MeteorEntity me = new MeteorEntity(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, level);
                me.shootFromRotation(player, (float) (-90f + 20 * level.random.nextGaussian()), 180 * level.random.nextFloat(), 0, 1+(float)level.random.nextFloat()*3, 1.5f);
                level.addFreshEntity(me);
            }
        }
        MeteorEntity me = new MeteorEntity( pos.getX()+0.5, pos.getY()+1.5, pos.getZ()+0.5, level);
        me.shootFromRotation(player, (float)(-90f + 20*level.random.nextGaussian()), 180*level.random.nextFloat() ,  0, 1, 1.5f);
        me.setNoDamages();
        level.addFreshEntity(me);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
