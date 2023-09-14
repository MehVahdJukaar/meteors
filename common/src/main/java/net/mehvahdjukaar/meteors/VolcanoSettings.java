package net.mehvahdjukaar.meteors;

import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;

import java.util.function.Supplier;

public class VolcanoSettings {



    public static final Supplier<Integer> ERUPTION_NUMBER;
    public static final Supplier<Integer> TIME_TO_FINAL_ERUPTION;

    public static final Supplier<Double> POWER_INCREASE_AMPL;
    public static final Supplier<Integer> POWER_INCREASE_EXPONENT;
    public static final Supplier<Double> PEAK_SHARPNESS;
    public static final Supplier<Double> QUIET_TIME_BASE_INCREASE;

    public static final Supplier<Integer> MIN_Y;
    public static final Supplier<Integer> MAX_Y;


    static {
        var builder = ConfigBuilder.create(Meteors.MOD_ID, ConfigType.COMMON);

        ERUPTION_NUMBER = builder.comment("(n) Number of eruptions. This and max time determine eruption frequency")
                .define("explosion_count", 12, 0, 1000000);
        TIME_TO_FINAL_ERUPTION = builder.comment("(T) Time it takes for final eruption to occur in seconds. Essentially determines the shape of the courses below")
                .define("time_to_final_eruption", 1200, 0, 100000000);

        POWER_INCREASE_AMPL = builder.comment("(a) Volcano activity follows a sine multiplied by a polinomnia. This determines the scalar multiplier of that polynomial")
                .define("power_amplitude", 0.0000000005, 0, 1000000);
        POWER_INCREASE_EXPONENT = builder.comment("(k) Volcano activity follows a sine multiplied by a polynomial. This determines the rank of the polynomial (1 is a line, increase to have steeper increase as time goes on)")
                .define("power_exponent", 3, 0, 1000000);

        PEAK_SHARPNESS = builder.comment("(s) This determines how sharp (how long) the eruption activities will be. Essentially this is just the exponent of the sine wave. at 1 you get balanced quite time and eruption time")
                    .define("peak_sharpness", 0.5d, 0, 100000);

        QUIET_TIME_BASE_INCREASE = builder.comment("(b) This is the angular coefficient of a line that will be added to the final equation. Essentially makes it so as time goes on even during quite times some activity can be noticed")
                .define("base_steepness_increase", 0.000002, 0, 100);


        MIN_Y = builder.comment("Minimum y lava level")
                        .define("lava_level_min", 0, -64, 320);

        MAX_Y = builder.comment("Max y lava level")
                .define("lava_level_max", 64, -64, 320);

        builder.buildAndRegister();
    }

    public static void init() {
    }
}
