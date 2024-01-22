package net.sonmok14.fromtheshadows.server;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

public class FTSConfig {
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        Pair<Server, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER = commonSpecPair.getLeft();
        SERVER_SPEC = commonSpecPair.getRight();
    }
    public static class Server {
        public final ConfigValue<Double> nehemoth_health;
        public final ConfigValue<Double> nehemoth_laser_damage;
        public final ConfigValue<Double> nehemoth_ranged_damage;
        public final ConfigValue<Double> nehemoth_melee_damage;


        public final ConfigValue<Double> froglin_health;
        public final ConfigValue<Double> froglin_vomit_damage;
        public final ConfigValue<Double> froglin_melee_damage;


        public final ConfigValue<Double> bulldrogioth_health;
        public final ConfigValue<Double> bulldrogioth_melee_damage;

        public final ConfigValue<Double> cleric_projectile_damage;
        public final ConfigValue<Double> cleric_melee_damage;
        public final ConfigValue<Double> cleric_health;

        public final ForgeConfigSpec.IntValue bulldrogiothShipwreckSpawnRate;
        public final ForgeConfigSpec.IntValue clericSpawnRate;
        public final ForgeConfigSpec.IntValue soulfirenehemothSpawnRate;
        public final ForgeConfigSpec.IntValue nehemothSpawnRate;
        public final ForgeConfigSpec.IntValue nehemothFortressSpawnRate;
        public final ForgeConfigSpec.IntValue bulldrogiothSpawnRate;
        public final ForgeConfigSpec.IntValue froglinSpawnRate;
//--------------------------------------------------------------------------------
        public final ConfigValue<Double> thirst_for_blood_damage;
        public final ConfigValue<Double> thirst_for_blood_laser_damage;
        public final ConfigValue<Double> devil_splitter_damage;
        public final ConfigValue<Double> diabolium_helmet_armor_value;
        public final ConfigValue<Double> diabolium_chestplate_armor_value;
        public final ConfigValue<Double> diabolium_leggings_armor_value;
        public final ConfigValue<Double> diabolium_armor_durability;
        public final ConfigValue<Double> crust_helmet_armor_value;
        public final ConfigValue<Double> crust_chestplate_armor_value;
        public final ConfigValue<Double> crust_leggings_armor_value;
        public final ConfigValue<Double> crust_armor_durability;
        public final ConfigValue<Double> plague_mask_armor_value;
        public final ConfigValue<Double> plague_mask_durability;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("Nehemoth");
            this.nehemoth_health = builder.translation("text.fromtheshadows.config.nehemoth_health")
                    .defineInRange("Sets Nehemoth Max Health", 120, 1, Double.MAX_VALUE);
            this.nehemoth_ranged_damage = builder.translation("text.fromtheshadows.config.nehemoth_ranged")
                    .defineInRange("Sets Nehemoth Ranged Damage", 3, 1, Double.MAX_VALUE);
            this.nehemoth_laser_damage = builder.translation("text.fromtheshadows.config.nehemoth_laser")
                    .defineInRange("Sets Nehemoth Laser Damage", 3, 1, Double.MAX_VALUE);
            this.nehemoth_melee_damage = builder.translation("text.fromtheshadows.config.nehemoth_melee")
                    .defineInRange("Sets Nehemoth Melee Damage", 8, 1, Double.MAX_VALUE);
            builder.pop();
            builder.push("Froglin");
            this.froglin_health = builder.translation("text.fromtheshadows.config.froglin_health")
                    .defineInRange("Sets Froglin Max Health", 25, 1, Double.MAX_VALUE);
            this.froglin_vomit_damage = builder.translation("text.fromtheshadows.config.froglin_vomit")
                    .defineInRange("Sets Froglin Projetile Damage", 7, 1, Double.MAX_VALUE);
            this.froglin_melee_damage = builder.translation("text.fromtheshadows.config.froglin_melee")
                    .defineInRange("Sets Froglin Melee Damage", 5, 1, Double.MAX_VALUE);
            builder.pop();
            builder.push("Bulldrogioth");
            this.bulldrogioth_health = builder.translation("text.fromtheshadows.config.bulldrogioth_health")
                    .defineInRange("Sets Bulldrogioth Max Health", 150, 1, Double.MAX_VALUE);
            this.bulldrogioth_melee_damage = builder.translation("text.fromtheshadows.config.bulldrogioth_melee_damage")
                    .defineInRange("Sets Bulldrogioth Melee Damage", 15, 1, Double.MAX_VALUE);
            builder.pop();
            builder.push("Cleric");
            this.cleric_health = builder.translation("text.fromtheshadows.config.cleric_health")
                    .defineInRange("Sets Cleric Max Health", 30, 1, Double.MAX_VALUE);
            this.cleric_projectile_damage = builder.translation("text.fromtheshadows.config.cleric_projectile_damage")
                    .defineInRange("Sets Cleric Projectile Damage", 5, 1, Double.MAX_VALUE);
            this.cleric_melee_damage = builder.translation("text.fromtheshadows.config.cleric_melee_damage")
                    .defineInRange("Sets Cleric Melee Damage", 1, 1, Double.MAX_VALUE);
            builder.pop();
            builder.push("SpawnRate");
            clericSpawnRate = builder.comment("Changed Cleric SpawnRate. [0 ~ 100]")
                    .defineInRange("Cleric SpawnRate", 5, 0, 100);
            soulfirenehemothSpawnRate = builder.comment("Changed Soulfire Nehemoth SpawnRate. [0 ~ 100]")
                    .defineInRange("SoulFire Nehemoth SpawnRate", 1, 0, 100);
            nehemothSpawnRate = builder.comment("Changed Nehemoth SpawnRate. [0 ~ 100]")
                    .defineInRange("Nehemoth SpawnRate", 2, 0, 100);
            nehemothFortressSpawnRate = builder.comment("Changed Nehemoth SpawnRate in Fortress. [0 ~ 100]")
                    .defineInRange("Nehemoth SpawnRate in Fortress", 1, 0, 100);
            bulldrogiothShipwreckSpawnRate = builder.comment("Changed Bulldrogioth SpawnRate in Ocean Ruin. [0 ~ 100]")
                    .defineInRange("Bulldrogioth SpawnRate in Ocean Ruin", 5, 0, 100);
            bulldrogiothSpawnRate = builder.comment("Changed Bulldrogioth SpawnRate. [0 ~ 100]")
                    .defineInRange("Bulldrogioth SpawnRate", 5, 0, 100);
            froglinSpawnRate = builder.comment("Changed Froglin SpawnRate. [0 ~ 100]")
                    .defineInRange("Froglin SpawnRate", 2, 0, 100);
            builder.pop();
            builder.push("Equipment");
            this.thirst_for_blood_damage = builder.translation("text.fromtheshadows.config.thirst_for_blood_damage")
                    .defineInRange("Sets Thirst For Blood Damage", 8, 1, Double.MAX_VALUE);
            this.thirst_for_blood_laser_damage = builder.translation("text.fromtheshadows.config.thirst_for_blood_laser_damage")
                    .defineInRange("Sets Thirst For Blood Laser Damage", 9, 1, Double.MAX_VALUE);
            this.devil_splitter_damage = builder.translation("text.fromtheshadows.config.devil_splitter_damage")
                    .defineInRange("Sets Devil Splitter Damage", 6.0D, 1, Double.MAX_VALUE);

            this.diabolium_helmet_armor_value = builder.translation("text.fromtheshadows.config.diabolium_helmet_armor_value")
                    .defineInRange("Sets Diabolium Helmet Armor Value", 3, 1, Double.MAX_VALUE);
            this.diabolium_chestplate_armor_value = builder.translation("text.fromtheshadows.config.diabolium_chestplate_armor_value")
                    .defineInRange("Sets Diabolium Chestplate Armor Value", 8, 1, Double.MAX_VALUE);
            this.diabolium_leggings_armor_value = builder.translation("text.fromtheshadows.config.diabolium_leggings_armor_value")
                    .defineInRange("Sets Diabolium Leggings Armor Value", 6, 1, Double.MAX_VALUE);
            this.diabolium_armor_durability = builder.translation("text.fromtheshadows.config.diabolium_armor_durability")
                    .defineInRange("Sets Diabolium Armor Durability", 24, 1, Double.MAX_VALUE);

            this.crust_helmet_armor_value = builder.translation("text.fromtheshadows.config.crust_helmet_armor_value")
                    .defineInRange("Sets Crust Helmet Armor Value", 4, 1, Double.MAX_VALUE);
            this.crust_chestplate_armor_value = builder.translation("text.fromtheshadows.config.crust_chestplate_armor_value")
                    .defineInRange("Sets Crust Chestplate Armor Value", 9, 1, Double.MAX_VALUE);
            this.crust_leggings_armor_value = builder.translation("text.fromtheshadows.config.crust_leggings_armor_value")
                    .defineInRange("Sets Crust Leggings Armor Value", 7, 1, Double.MAX_VALUE);
            this.crust_armor_durability = builder.translation("text.fromtheshadows.config.crust_armor_durability")
                    .defineInRange("Sets Crust Armor Durability", 30, 1, Double.MAX_VALUE);

            this.plague_mask_armor_value = builder.translation("text.fromtheshadows.config.plague_mask_armor_value")
                    .defineInRange("Sets Plague Doctor Mask Armor Value", 3, 1, Double.MAX_VALUE);
            this.plague_mask_durability = builder.translation("text.fromtheshadows.config.plague_mask_durability")
                    .defineInRange("Sets Plague Doctor Mask Durability", 8, 1, Double.MAX_VALUE);
            builder.pop();
        }
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
}
