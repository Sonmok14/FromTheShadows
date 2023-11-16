package net.sonmok14.fromtheshadows.server.utils.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;

import java.util.List;

public class ToolMaterialRegistry {
    public static final Tier THIRST_FOR_BLOOD = TierSortingRegistry.registerTier(
            new ForgeTier(2, 12, 5.0F, 7.0F, 20,
                    BlockTags.create(new ResourceLocation(Fromtheshadows.MODID, "needs_thirstforblood")),
                    () -> Ingredient.of(Items.IRON_INGOT)),
            new ResourceLocation("thirstforblood"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));

    public static final Tier DEVIL_SPLITTER = TierSortingRegistry.registerTier(
            new ForgeTier(2, 350, 5.0F, 7.0F, 20,
                    BlockTags.create(new ResourceLocation(Fromtheshadows.MODID, "needs_devilsplitter")),
                    () -> Ingredient.of(Items.IRON_INGOT)),
            new ResourceLocation("devilsplitter"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));
}
