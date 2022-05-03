package slurpreborn.config;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;

import org.apache.commons.lang3.tuple.Pair;

import slurpreborn.SlurpReborn;

@Mod.EventBusSubscriber(modid = SlurpReborn.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHandler {

    private ConfigHandler() {}

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Common.CONFIG_SPEC);
    }

    public static final class Common {

        public static final ForgeConfigSpec CONFIG_SPEC;
        private static final Common CONFIG;

        private static IntValue DRINK_AMOUNT;
        private static DoubleValue DRINK_HYDRATION;
        private static IntValue EFFECT_POTENCY;
        private static IntValue EFFECT_DURATION;
        private static DoubleValue EFFECT_CHANCE;

        static {
            Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);

            CONFIG_SPEC = specPair.getRight();
            CONFIG = specPair.getLeft();
        }

        Common(ForgeConfigSpec.Builder builder) {
            DRINK_AMOUNT = builder
                .comment("Amount of water that can drank per interaction in half shanks. Default 1")
                .defineInRange("DRINK_AMOUNT", 1, 1, 20);
            DRINK_HYDRATION = builder
                .comment("Amount of hydration gained per drink. Default 0.2")
                .defineInRange("DRINK_HYDRATION", 0.2, 0.0, 20.0);
            EFFECT_POTENCY = builder
                .comment("Potency of thirst effect when drinking from an open water source. Default 12")
                .defineInRange("EFFECT_POTENCY", 12, 1, 255);
            EFFECT_DURATION = builder
                .comment("Duration of thirst effect in ticks (20/second). Default 200")
                .defineInRange("EFFECT_DURATION", 200, 1, 6000);
            EFFECT_CHANCE = builder
                .comment("Chance to give thirst status effect. Default 0.2")
                .defineInRange("EFFECT_CHANCE", 0.2, 0.0, 1.0);
        }

        public static int drinkAmount() {
            return CONFIG.DRINK_AMOUNT.get();
        }

        public static float drinkHydration() {
            double hydration = CONFIG.DRINK_HYDRATION.get();

            return (float) hydration;
        }

        public static int effectPotency() {
            return CONFIG.EFFECT_POTENCY.get();
        }

        public static int effectDuration() {
            return CONFIG.EFFECT_DURATION.get();
        }

        public static float effectChance() {
            double chance = CONFIG.EFFECT_CHANCE.get();

            return (float) chance;
        }

    }

}
