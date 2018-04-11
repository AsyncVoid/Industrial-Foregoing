package com.buuz135.industrial.jei.bioreactor;

import com.buuz135.industrial.utils.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BioReactorRecipeCategory implements IRecipeCategory<BioReactorRecipeWrapper> {

    private IGuiHelper guiHelper;

    public BioReactorRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
    }

    @Override
    public String getUid() {
        return "bioreactor_category";
    }

    @Override
    public String getTitle() {
        return "Bioreactor accepted items";
    }
/*
    @Override
    public String getModName() {
        return Reference.NAME;
    }
*/
    @Override
    public IDrawable getBackground() {
        return guiHelper.createDrawable(new ResourceLocation(Reference.MOD_ID, "textures/gui/jei.png"), 0, 0, 82, 26);
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, BioReactorRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStackGroup = recipeLayout.getItemStacks();
        guiItemStackGroup.init(0, true, 0, 4);
        guiItemStackGroup.init(1, false, 60, 4);
        guiItemStackGroup.set(0, ingredients.getInputs(ItemStack.class).get(0));
        guiItemStackGroup.set(1, ingredients.getOutputs(ItemStack.class).get(0));
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return new ArrayList<String>();
    }

	@Override
	public void drawAnimations(Minecraft arg0) {
		//TODO Auto-generated method stub
		
	}

	@Override
	public void setRecipe(IRecipeLayout arg0, BioReactorRecipeWrapper arg1) {
		//TODO Auto-generated method stub
		
	}
}
