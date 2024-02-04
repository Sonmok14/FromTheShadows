package net.sonmok14.fromtheshadows.server.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.sonmok14.fromtheshadows.client.renderer.items.DevilSplitterRenderer;
import net.sonmok14.fromtheshadows.server.config.FTSConfig;
import net.sonmok14.fromtheshadows.server.entity.FTSMobType;
import net.sonmok14.fromtheshadows.server.utils.registry.ItemRegistry;
import net.sonmok14.fromtheshadows.server.utils.registry.ToolMaterialRegistry;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class DevilSplitterItem extends SwordItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Multimap<Attribute, AttributeModifier> attributeModifierMultimap;
    public DevilSplitterItem(Item.Properties properties) {
        super(ToolMaterialRegistry.DEVIL_SPLITTER, 1, -2.4F, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier",  FTSConfig.SERVER.devil_splitter_damage.get(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.8F, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(UUID.fromString("D5CD480F-F2CF-4162-955E-B9A2C8EB7425"), "Tool modifier", 3.0F, AttributeModifier.Operation.ADDITION));
        this.attributeModifierMultimap = builder.build();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DevilSplitterRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new DevilSplitterRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }


    @Override
    public boolean hurtEnemy(ItemStack p_43278_, LivingEntity target, LivingEntity attacker) {
        if (target.getMobType() == FTSMobType.DEMON) {
            if (!target.isAlive()) {
                p_43278_.hurtAndBreak(-5, attacker, p -> p.broadcastBreakEvent(attacker.getUsedItemHand()));
            }
            if (attacker.isSprinting()) {
                target.hurt(attacker.damageSources().mobAttack(attacker), (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.25f);
                attacker.heal((float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE) / 10);
            }
        }
        return super.hurtEnemy(p_43278_, target, attacker);
    }

    @Override
    public int getEnchantmentValue() {
        return 18;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifierMultimap : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public boolean canGrindstoneRepair(ItemStack stack) {
        return false;
    }



    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.category == EnchantmentCategory.WEAPON;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    public boolean isValidRepairItem(ItemStack p_41134_, ItemStack p_41135_) {
        return p_41135_.is(ItemRegistry.BOTTLE_OF_BLOOD.get());
    }
    @Override
    public boolean isRepairable(ItemStack stack) {
        return super.isRepairable(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("fromtheshadows.devil_splitter.text").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

}
