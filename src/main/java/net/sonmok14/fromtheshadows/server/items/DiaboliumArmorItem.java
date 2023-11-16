package net.sonmok14.fromtheshadows.server.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.sonmok14.fromtheshadows.server.utils.registry.EffectRegistry;
import net.sonmok14.fromtheshadows.server.utils.registry.ItemRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiaboliumArmorItem extends GeoArmorItem implements IAnimatable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public DiaboliumArmorItem(ArmorMaterial materialIn, EquipmentSlot type, Properties builder) {
        super(materialIn, type, builder.stacksTo(1));
    }



    @Override
    public boolean hurtEnemy(ItemStack p_41395_, LivingEntity p_41396_, LivingEntity p_41397_) {
        if (p_41396_ instanceof Player) {
            Player player = (Player) p_41396_;
            List<Item> equipmentList = new ArrayList<>();
            player.getAllSlots().forEach((x) -> equipmentList.add(x.getItem()));

            List<Item> armorList = equipmentList.subList(2, 6);

            boolean isWearingAll = armorList
                    .containsAll(Arrays.asList(ItemRegistry.DIABOLIUM_LEGGINGS.get(),
                            ItemRegistry.DIABOLIUM_CHEST.get(), ItemRegistry.DIABOLIUM_HEAD.get()));

            if (isWearingAll && player.doHurtTarget(p_41397_) && !p_41397_.hasEffect(EffectRegistry.BLEEDING.get())) {
                p_41397_.addEffect(new MobEffectInstance(EffectRegistry.BLEEDING.get(), 200), p_41396_);
            }


        }
      return true;
    }


    public boolean isValidRepairItem(ItemStack p_41134_, ItemStack p_41135_) {
        return p_41135_.is(ItemRegistry.DIABOLIUM_INGOT.get());
    }

    @Override
    public void registerControllers(AnimationData data) {}

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return super.isRepairable(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("fromtheshadows.diablium_armor.text").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("fromtheshadows.diablium_armor.text2").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}

