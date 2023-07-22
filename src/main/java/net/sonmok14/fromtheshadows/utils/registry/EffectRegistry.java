package net.sonmok14.fromtheshadows.utils.registry;

import com.mojang.blaze3d.shaders.Effect;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.effect.EffectHealblock;
import net.sonmok14.fromtheshadows.effect.EffectHemorrhage;
import net.sonmok14.fromtheshadows.misc.ProperBrewingRecipe;

public class EffectRegistry {


;
    public static final DeferredRegister<Potion> POTION = DeferredRegister.create(ForgeRegistries.POTIONS, Fromtheshadows.MODID);
    public static final DeferredRegister<MobEffect> EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Fromtheshadows.MODID);


    public static final RegistryObject<MobEffect> HEAL_BLOCK = EFFECT.register("heal_block", ()-> new EffectHealblock(MobEffectCategory.HARMFUL, 0X78828E));
    public static final RegistryObject<MobEffect> BLEEDING = EFFECT.register("bleeding", ()-> new EffectHemorrhage(MobEffectCategory.HARMFUL, 0XCA2D2D));
    public static final RegistryObject<Potion> FRENZY_POTION = POTION.register("frenzy", ()-> new Potion(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 0),(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 1))));
    public static final RegistryObject<Potion> BULLDROGIOTH_POTION = POTION.register("bulldrogioth", ()-> new Potion(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 3),(new MobEffectInstance(MobEffects.HUNGER, 200, 2))));
    public static ItemStack createPotion(RegistryObject<Potion> potion){
        return  PotionUtils.setPotion(new ItemStack(Items.POTION), potion.get());
    }

    public static ItemStack createPotion(Potion potion){
        return  PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
    }


    public static void init() {
        BrewingRecipeRegistry.addRecipe(new ProperBrewingRecipe(Ingredient.of(createPotion(Potions.MUNDANE)), Ingredient.of(ItemRegistry.CRIMSON_SHELL.get()), createPotion(EffectRegistry.BULLDROGIOTH_POTION)));
        BrewingRecipeRegistry.addRecipe(new ProperBrewingRecipe(Ingredient.of(createPotion(Potions.STRENGTH)), Ingredient.of(ItemRegistry.BOTTLE_OF_BLOOD.get()), createPotion(EffectRegistry.FRENZY_POTION)));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(Items.IRON_INGOT), Ingredient.of(ItemRegistry.BOTTLE_OF_BLOOD.get()), new ItemStack(ItemRegistry.DIABOLIUM_INGOT.get()));
        BrewingRecipeRegistry.addRecipe(new ProperBrewingRecipe(Ingredient.of(createPotion(Potions.AWKWARD)), Ingredient.of(ItemRegistry.FROGLIN_LEG.get()), createPotion(Potions.LEAPING)));
        BrewingRecipeRegistry.addRecipe(Ingredient.of(createPotion(Potions.WATER)), Ingredient.of(ItemRegistry.CRYSTALLIZED_BLOOD.get()), new ItemStack(ItemRegistry.BOTTLE_OF_BLOOD.get()));
    }
}


