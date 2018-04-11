package com.buuz135.industrial.tile.generator;

import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.utils.WorkUtils;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;

public class LavaFabricatorTile extends CustomElectricMachine {

    private IFluidTank tank;

    public LavaFabricatorTile() {
        super(LavaFabricatorTile.class.getName().hashCode());
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        tank = this.addFluidTank(FluidRegistry.LAVA, 8000, EnumDyeColor.ORANGE, "Lava tank", new BoundingRectangle(50, 25, 18, 54));
    }

    @Override
    protected float performWork() {
        if (WorkUtils.isDisabled(this.getBlockType())) return 0;

        if (tank.getFluid() == null || tank.getFluidAmount() <= 7000) {
            tank.fill(new FluidStack(FluidRegistry.LAVA, 1000), true);
            return 1;
        }
        return 0;
    }

    @Override
    protected boolean acceptsFluidItem(ItemStack stack) {
        return stack.getItem().equals(Items.BUCKET);
    }

    @Override
    protected void processFluidItems(ItemStackHandler fluidItems) {
        ItemStack stack = fluidItems.getStackInSlot(0); //TODO !stack.isEmpty() && fluidItems.getStackInSlot(1).isEmpty()
        if (stack != null && fluidItems.getStackInSlot(1) == null && tank.getFluidAmount() >= 1000) {
            ItemStack out = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, FluidRegistry.LAVA);
            tank.drain(1000, true);
            //TODO stack.setCount(stack.getCount() - 1);
            stack.stackSize -= 1; 
            if(stack.stackSize < 1)
            	fluidItems.setStackInSlot(0, null);
            
            fluidItems.setStackInSlot(1, out);
        }
    }
}
