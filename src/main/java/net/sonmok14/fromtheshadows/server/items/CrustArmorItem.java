package net.sonmok14.fromtheshadows.server.items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.sonmok14.fromtheshadows.server.utils.registry.ItemRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class CrustArmorItem extends GeoArmorItem implements IAnimatable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public CrustArmorItem(ArmorMaterial materialIn, EquipmentSlot type, Properties builder) {
        super(materialIn, type, builder.stacksTo(1));
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public boolean isValidRepairItem(ItemStack p_41134_, ItemStack p_41135_) {
        return p_41135_.is(ItemRegistry.CRIMSON_SHELL.get());
    }
    @Override
    public boolean isRepairable(ItemStack stack) {
        return super.isRepairable(stack);
    }
}

