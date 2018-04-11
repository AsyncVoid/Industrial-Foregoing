package com.buuz135.industrial.jei;


import com.buuz135.industrial.jei.bioreactor.BioReactorRecipeCategory;
import com.buuz135.industrial.jei.bioreactor.BioReactorRecipeWrapper;
import com.buuz135.industrial.jei.laser.LaserRecipeCategory;
import com.buuz135.industrial.jei.laser.LaserRecipeWrapper;
import com.buuz135.industrial.jei.sludge.SludgeRefinerRecipeCategory;
import com.buuz135.industrial.jei.sludge.SludgeRefinerRecipeWrapper;
import com.buuz135.industrial.proxy.BlockRegistry;
import com.buuz135.industrial.proxy.ItemRegistry;
import com.buuz135.industrial.utils.ItemStackWeightedItem;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
//import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@JEIPlugin
public class JEICustomPlugin implements IModPlugin {


    private SludgeRefinerRecipeCategory sludgeRefinerRecipeCategory;
    private BioReactorRecipeCategory bioReactorRecipeCategory;
    private LaserRecipeCategory laserRecipeCategory;

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {
    	
    }
/*
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        sludgeRefinerRecipeCategory = new SludgeRefinerRecipeCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(sludgeRefinerRecipeCategory);
        bioReactorRecipeCategory = new BioReactorRecipeCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(bioReactorRecipeCategory);
        laserRecipeCategory = new LaserRecipeCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(laserRecipeCategory);
    }
*/
    @Override
    public void register(IModRegistry registry) {
        int maxWeight = WeightedRandom.getTotalWeight(BlockRegistry.sludgeRefinerBlock.getItemStackWeightedItems());
        List<SludgeRefinerRecipeWrapper> wrapperList = new ArrayList<SludgeRefinerRecipeWrapper>();
        //TODO BlockRegistry.sludgeRefinerBlock.getItemStackWeightedItems().forEach(itemStackWeightedItem -> wrapperList.add(new SludgeRefinerRecipeWrapper(itemStackWeightedItem, maxWeight)));
        for(ItemStackWeightedItem itemStackWeightedItem : BlockRegistry.sludgeRefinerBlock.getItemStackWeightedItems())
        {
        	wrapperList.add(new SludgeRefinerRecipeWrapper(itemStackWeightedItem, maxWeight));
        }
        
        //TODO: registry.addRecipes(wrapperList, sludgeRefinerRecipeCategory.getUid());
        registry.addRecipes(wrapperList);
        //TODO registry.addRecipeCatalyst(new ItemStack(BlockRegistry.sludgeRefinerBlock), sludgeRefinerRecipeCategory.getUid());


        List<BioReactorRecipeWrapper> bioreactor = new ArrayList<BioReactorRecipeWrapper>();
        for(ItemStack stack : BlockRegistry.bioReactorBlock.getItemsAccepted())
        {
        	bioreactor.add(new BioReactorRecipeWrapper(stack));
        }
        //TODO BlockRegistry.bioReactorBlock.getItemsAccepted().forEach(stack -> bioreactor.add(new BioReactorRecipeWrapper(stack)));
        //TODO registry.addRecipes(bioreactor, bioReactorRecipeCategory.getUid());
        registry.addRecipes(bioreactor);
        //TODO registry.addRecipeCatalyst(new ItemStack(BlockRegistry.bioReactorBlock), bioReactorRecipeCategory.getUid());

        List<ItemStackWeightedItem> item = new ArrayList<ItemStackWeightedItem>();
        
        
        //TODO BlockRegistry.laserBaseBlock.getColoreOres().keySet().forEach(integer -> item.addAll(BlockRegistry.laserBaseBlock.getColoreOres().get(integer)));
        for (Integer integer : BlockRegistry.laserBaseBlock.getColoreOres().keySet())
        {
        	item.addAll(BlockRegistry.laserBaseBlock.getColoreOres().get(integer));
        }
        
        final int laserMaxWeight = WeightedRandom.getTotalWeight(item);
        List<LaserRecipeWrapper> laserRecipeWrappers = new ArrayList<LaserRecipeWrapper>();
        //TODO BlockRegistry.laserBaseBlock.getColoreOres().keySet().forEach(integer -> BlockRegistry.laserBaseBlock.getColoreOres().get(integer).forEach(temp -> laserRecipeWrappers.add(new LaserRecipeWrapper(temp, laserMaxWeight, integer))));
        for(Integer integer : BlockRegistry.laserBaseBlock.getColoreOres().keySet())
        {
        	for(ItemStackWeightedItem temp : BlockRegistry.laserBaseBlock.getColoreOres().get(integer))
        	{
        		laserRecipeWrappers.add(new LaserRecipeWrapper(temp, laserMaxWeight, integer));
        	}
        }
        
        //TODO registry.addRecipes(laserRecipeWrappers, laserRecipeCategory.getUid());
        registry.addRecipes(laserRecipeWrappers);
        //TODO registry.addRecipeCatalyst(new ItemStack(BlockRegistry.laserDrillBlock), laserRecipeCategory.getUid());
        //TODO registry.addRecipeCatalyst(new ItemStack(BlockRegistry.laserBaseBlock), laserRecipeCategory.getUid());


        //Descriptions
        /*//TODO
        registry.addIngredientInfo(Arrays.asList(new ItemStack(ItemRegistry.meatFeederItem)), ItemStack.class, "The meat feeder will keep fed if it has liquid meat. (Don't ask where the meat comes, you won't like it)");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(ItemRegistry.mobImprisonmentToolItem)), ItemStack.class, "This tool can capture mobs to be used in the Mob duplicator.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(ItemRegistry.tinyDryRubber)), ItemStack.class, "Produced in the latex processing unit.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(ItemRegistry.fertilizer)), ItemStack.class, "It can be used in the Plant Enrich Material Injector to make plants frow faster. Produced in the Sewer Compost Solidifier");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(ItemRegistry.laserLensItem, 1, OreDictionary.WILDCARD_VALUE)), ItemStack.class, "When used in the laser it will increase the chance of the same colored ores.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(ItemRegistry.adultFilterAddomItem)), ItemStack.class, "It will change the filter to adults to the Animal Independence Selector.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(ItemRegistry.rangeAddonItem, 1, OreDictionary.WILDCARD_VALUE)), ItemStack.class, "The range addon will increase the range of the working area of some machines.");

        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.petrifiedFuelGeneratorBlock)), ItemStack.class, "This generator will generate power depending the burntime of the solid fuel. The more burn time it has the more power/tick will create. (All fuels will burn the same amount of time)");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.enchantmentRefinerBlock)), ItemStack.class, "The enchantent refiner, when provider with power, it will sort enchanted items from unenchanted items.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.enchantmentExtractorBlock)), ItemStack.class, "When provided with books and enchanted items it will remove the enchantment of the item and apply it to the book.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.enchantmentAplicatorBlock)), ItemStack.class, "When provided with an Essence, enchanted books and items it will apply the enchantment tothe item.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.mobRelocatorBlock)), ItemStack.class, "The Mob Relocator will kill any mob in front of it and produce some essence based on the health it has.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.potionEnervatorBlock)), ItemStack.class, "An advanced version of the Brewing Stand.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.animalIndependenceSelectorBlock)), ItemStack.class, "The Animal Independence Selector will move any baby animal (by default) from the front area, to the back area.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.animalStockIncreaserBlock)), ItemStack.class, "The Animal Stock Increaser will feed animals with their prefered food.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.cropSowerBlock)), ItemStack.class, "Plants crops and saplings.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.cropEnrichMaterialInjectorBlock)), ItemStack.class, "Consumes bone meal or fetilizer to make plants grow faster.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.cropRecolectorBlock)), ItemStack.class, "Recolects full grown crops and chops down trees.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.blackHoleUnitBlock)), ItemStack.class, "Holds almost an infinite amount of one item. It will keep the contents when broken.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.waterCondensatorBlock)), ItemStack.class, "Creates water when 2 or more water sources are touching one of the sides of the machine.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.waterResourcesCollectorBlock)), ItemStack.class, "When placed in top of a 3x3 of water it will collect resources from it. (Some people say that there is a tiny villager inside of it)");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.animalResourceHarvesterBlock)), ItemStack.class, "Collects milk and wool from animals.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.mobRelocatorBlock)), ItemStack.class, "Grinds down mobs to produce meat.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.mobDuplicatorBlock)), ItemStack.class, "Clones mobs when provided with with Essence and a filled Mob Imprisonment tool");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.blockDestroyerBlock)), ItemStack.class, "Breaks down blocks in front of it.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.blockPlacerBlock)), ItemStack.class, "Places down blocks in front of it from the inventory.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.treeFluidExtractorBlock)), ItemStack.class, "Collects latex from blocks of wood in front of it.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.latexProcessingUnitBlock)), ItemStack.class, "Creates tiny rubber from latex and water.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.sewageCompostSolidiferBlock)), ItemStack.class, "Produces fertilizer from Sewage.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.animalByproductRecolectorBlock)), ItemStack.class, "Collects sewage from animals.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.sludgeRefinerBlock)), ItemStack.class, "Refines Sludge into better items.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.mobDetectorBlock)), ItemStack.class, "Emits a redstone signal to the back depending of the amount of mobs it has in front.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.lavaFabricatorBlock)), ItemStack.class, "Creates lava using a big amount of energy.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.bioReactorBlock)), ItemStack.class, "Creates biofuel using different types of plants.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.biofuelGeneratorBlock)), ItemStack.class, "Creates power using biofuel.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.laserBaseBlock)), ItemStack.class, "Creates ores when charged with Laser Drills, can be focused with Laser Lenses");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.laserDrillBlock)), ItemStack.class, "Charges the Laser Base when it is a block away from it.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.oreProcessorBlock)), ItemStack.class, "Breaks down ores into components.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.blackHoleControllerBlock)), ItemStack.class, "Holds up to 9 Black Hole Units allowing you to access the items that are inside of them.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.dyeMixerBlock)), ItemStack.class, "Makes dyes more efficiently, needs a lens to select the color.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.enchantmentInvokerBlock)), ItemStack.class, "Enchants items with a level 30 enchant using 3 buckets of essence.");
        registry.addIngredientInfo(Arrays.asList(new ItemStack(BlockRegistry.sporesRecreatorBlock)), ItemStack.class, "Spreads mushrooms spores to grow mushrooms");
    */
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }
}
