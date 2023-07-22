package net.sonmok14.fromtheshadows.utils.event;

import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.raid.Raider;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.entity.BulldrogiothEntity;
import net.sonmok14.fromtheshadows.entity.FroglinEntity;
import net.sonmok14.fromtheshadows.entity.NehemothEntity;

@Mod.EventBusSubscriber(modid = Fromtheshadows.MODID)
public class EntityEvent {

    @SubscribeEvent()
    public static void addSpawn(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Villager) {
            Villager abstractVillager = (Villager) event.getEntity();
            abstractVillager.goalSelector.addGoal(2, new AvoidEntityGoal(abstractVillager, BulldrogiothEntity.class, 32.0F, 0.8F, 0.85F));
            abstractVillager.goalSelector.addGoal(1, new AvoidEntityGoal(abstractVillager, NehemothEntity.class, 16.0F, 0.8F, 0.85F));
            abstractVillager.goalSelector.addGoal(2, new AvoidEntityGoal(abstractVillager, FroglinEntity.class, 16.0F, 0.8F, 0.85F));
        }

        if (event.getEntity() instanceof Chicken) {
            Chicken abstractVillager = (Chicken) event.getEntity();
            abstractVillager.goalSelector.addGoal(1, new AvoidEntityGoal(abstractVillager, FroglinEntity.class, 16.0F, 0.8F, 1.4D));
        }


        if (event.getEntity() instanceof WanderingTrader) {
            WanderingTrader wanderingTraderEntity = (WanderingTrader) event.getEntity();
            wanderingTraderEntity.goalSelector.addGoal(1, new AvoidEntityGoal(wanderingTraderEntity, NehemothEntity.class, 16.0F, 0.8F, 0.85F));
            wanderingTraderEntity.goalSelector.addGoal(2, new AvoidEntityGoal(wanderingTraderEntity, FroglinEntity.class, 16.0F, 0.8F, 0.85F));
        }

        if (event.getEntity() instanceof Raider) {
            Raider raider = (Raider) event.getEntity();

            raider.goalSelector.addGoal(2, (new NearestAttackableTargetGoal<>(raider, NehemothEntity.class, true)).setUnseenMemoryTicks(300));
        }
    }
}
