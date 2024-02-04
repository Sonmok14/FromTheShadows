package net.sonmok14.fromtheshadows.server.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.sonmok14.fromtheshadows.client.renderer.items.ThirstforBloodRenderer;
import net.sonmok14.fromtheshadows.server.config.FTSConfig;
import net.sonmok14.fromtheshadows.server.entity.projectiles.PlayerBreathEntity;
import net.sonmok14.fromtheshadows.server.entity.projectiles.ScreenShakeEntity;
import net.sonmok14.fromtheshadows.server.utils.registry.EntityRegistry;
import net.sonmok14.fromtheshadows.server.utils.registry.SoundRegistry;
import net.sonmok14.fromtheshadows.server.utils.registry.ToolMaterialRegistry;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ThirstforBloodItem extends SwordItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Multimap<Attribute, AttributeModifier> attributeModifierMultimap;
    public ThirstforBloodItem(Item.Properties properties) {
        super(ToolMaterialRegistry.THIRST_FOR_BLOOD, 1, -2.4F, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", FTSConfig.SERVER.thirst_for_blood_damage.get(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.4F, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(UUID.fromString("914C2B49-1AD0-451A-A2F3-2ED609F0F291"), "Tool modifier", 2.0F, AttributeModifier.Operation.ADDITION));
        this.attributeModifierMultimap = builder.build();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private ThirstforBloodRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new ThirstforBloodRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public int getEnchantmentValue() {
        return 18;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifierMultimap : super.getDefaultAttributeModifiers(equipmentSlot);
    }


    public boolean mineBlock(ItemStack p_43282_, Level p_43283_, BlockState p_43284_, BlockPos p_43285_, LivingEntity p_43286_) {
        if (p_43284_.getDestroySpeed(p_43283_, p_43285_) != 0.0F) {
            p_43282_.hurtAndBreak(0, p_43286_, (p_43276_) -> {
                p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack heldItemStack, LivingEntity target, LivingEntity attacker) {
        heldItemStack.hurtAndBreak(0, attacker, (p_43296_) -> {
            p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
            if (!target.isAlive() && !(target.getMaxHealth() < 10)) {
                heldItemStack.hurtAndBreak(-1, attacker, p -> p.broadcastBreakEvent(attacker.getUsedItemHand()));
            }

        return true;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        final ItemStack itemStack = user.getItemInHand(hand);
        user.startUsingItem(hand);

            if (itemStack.getDamageValue() < itemStack.getMaxDamage() - 1) {
                user.getCooldowns().addCooldown(this, 40);
                user.playSound(SoundRegistry.SOUL_LASER.get(), 3f, 0.8F + user.getRandom().nextFloat() * 0.1F);
                float radius1 = 0.2f;
                if (!world.isClientSide) {
                    ScreenShakeEntity.ScreenShake(world, user.position(), 5, 0.03f, 15, 10);
                    PlayerBreathEntity beam = new PlayerBreathEntity(EntityRegistry.PLAYER_BREATH.get(), user.level(), user, user.getX(), user.getY() + 1.2f, user.getZ(), (float) ((user.yHeadRot + 90) * Math.PI / 180), (float) (-user.getXRot() * Math.PI / 180), 10);
                    world.addFreshEntity(beam);

                    itemStack.hurtAndBreak(1, user, p -> p.broadcastBreakEvent(user.getUsedItemHand()));
                }
            }
        return InteractionResultHolder.pass(itemStack);
        }



    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean canGrindstoneRepair(ItemStack stack) {
        return false;
    }



    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.category != EnchantmentCategory.BREAKABLE && enchantment.category == EnchantmentCategory.WEAPON;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }



    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("fromtheshadows.thirst_for_blood.text").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.translatable("fromtheshadows.thirst_for_blood.text2").withStyle(ChatFormatting.BLUE));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

}
