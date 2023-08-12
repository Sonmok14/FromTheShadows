package net.sonmok14.fromtheshadows.utils.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonmok14.fromtheshadows.utils.registry.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ServerEvents {
    @SubscribeEvent
    public void onLivingUpdateEvent(LivingEvent.LivingTickEvent event) {


        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getEntity();
            List<Item> equipmentList = new ArrayList<>();
            attacker.getAllSlots().forEach((x) -> equipmentList.add(x.getItem()));

            List<Item> armorList = new ArrayList<>(1);
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    if (attacker.getItemBySlot(slot) != null) {
                        armorList.add(attacker.getItemBySlot(slot).getItem());
                    }
                }

                boolean isWearingAll = armorList
                        .containsAll(Arrays.asList(ItemRegistry.PLAGUE_DOCTOR_MASK.get()));
                if (isWearingAll) {
                if(!attacker.level().isClientSide)
                {
                    boolean flag = attacker.removeEffect(EffectRegistry.PLAGUE.get());
                    if(flag) {
                        final ItemStack itemStack = attacker.getItemBySlot(EquipmentSlot.HEAD);
                        itemStack.hurtAndBreak(1, attacker, p -> p.broadcastBreakEvent(EquipmentSlot.HEAD));
                    }
                }
                }
            }
        }
    }
    @SubscribeEvent
    public void onLivingDamage(LivingHurtEvent event) {

        if (event.getSource() instanceof DamageSource) {
            if (event.getSource().getEntity() instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
                LivingEntity target = event.getEntity();
                List<Item> equipmentList = new ArrayList<>();
                attacker.getAllSlots().forEach((x) -> equipmentList.add(x.getItem()));
                if (EnchantmentHelper.getEnchantments(attacker.getMainHandItem()).containsKey(EnchantmentRegistry.FREEZING.get())) {
                    int i = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.FREEZING.get(), attacker.getMainHandItem());
                  if(i > 0 && target.getTicksFrozen() == 0)
                  {
                          target.playSound(SoundEvents.PLAYER_HURT_FREEZE, 2f, 1F);
                          int frozen = 250 * i;
                          target.setTicksFrozen(frozen);
                  }
                }
                List<Item> armorList = new ArrayList<>(3);
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                        if (attacker.getItemBySlot(slot) != null) {
                            armorList.add(attacker.getItemBySlot(slot).getItem());
                        }
                    }

                    boolean isWearingAll = armorList
                            .containsAll(Arrays.asList(ItemRegistry.DIABOLIUM_LEGGINGS.get(),
                                    ItemRegistry.DIABOLIUM_CHEST.get(), ItemRegistry.DIABOLIUM_HEAD.get()));
                    if (isWearingAll) {
                           attacker.heal((float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) / 5);
                       }
                    }
                }
            }
        }

    }