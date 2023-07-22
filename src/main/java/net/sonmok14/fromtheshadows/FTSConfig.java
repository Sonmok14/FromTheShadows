package net.sonmok14.fromtheshadows;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

public class FTSConfig {
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


        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("Nehemoth");
            this.nehemoth_health = builder.translation("text.fromtheshadows.config.nehemoth_health")
                    .defineInRange("Sets Nehemoth Max Health", 100, 1, Double.MAX_VALUE);
            this.nehemoth_ranged_damage = builder.translation("text.fromtheshadows.config.nehemoth_ranged")
                    .defineInRange("Sets Nehemoth Ranged Damage", 3, 1, Double.MAX_VALUE);
            this.nehemoth_laser_damage = builder.translation("text.fromtheshadows.config.nehemoth_laser")
                    .defineInRange("Sets Nehemoth Laser Damage", 3, 1, Double.MAX_VALUE);
            this.nehemoth_melee_damage = builder.translation("text.fromtheshadows.config.nehemoth_melee")
                    .defineInRange("Sets Nehemoth Melee Damage", 7, 1, Double.MAX_VALUE);
            builder.pop();
            builder.push("Froglin");
            this.froglin_health = builder.translation("text.fromtheshadows.config.froglin_health")
                    .defineInRange("Sets Froglin Max Health", 35, 1, Double.MAX_VALUE);
            this.froglin_vomit_damage = builder.translation("text.fromtheshadows.config.froglin_vomit")
                    .defineInRange("Sets Froglin Projetile Damage", 7, 1, Double.MAX_VALUE);
            this.froglin_melee_damage = builder.translation("text.fromtheshadows.config.froglin_melee")
                    .defineInRange("Sets Froglin Melee Damage", 5, 1, Double.MAX_VALUE);
            builder.pop();
            builder.push("Bulldrogioth");
            this.bulldrogioth_health = builder.translation("text.fromtheshadows.config.bulldrogioth_health")
                    .defineInRange("Sets Bulldrogioth Max Health", 110, 1, Double.MAX_VALUE);
            this.bulldrogioth_melee_damage = builder.translation("text.fromtheshadows.config.bulldrogioth_melee_damage")
                    .defineInRange("Sets Bulldrogioth Melee Damage", 15, 1, Double.MAX_VALUE);
            builder.pop();
            builder.push("Cleric");
            this.cleric_health = builder.translation("text.fromtheshadows.config.cleric_health")
                    .defineInRange("Sets Cleric Max Health", 25, 1, Double.MAX_VALUE);
            this.cleric_projectile_damage = builder.translation("text.fromtheshadows.config.cleric_projectile_damage")
                    .defineInRange("Sets Cleric Projectile Damage", 5, 1, Double.MAX_VALUE);
            this.cleric_melee_damage = builder.translation("text.fromtheshadows.config.cleric_melee_damage")
                    .defineInRange("Sets Cleric Melee Damage", 1, 1, Double.MAX_VALUE);
            builder.pop();
        }
    }
    public static final Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        Pair<Server, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER = commonSpecPair.getLeft();
        SERVER_SPEC = commonSpecPair.getRight();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
}
