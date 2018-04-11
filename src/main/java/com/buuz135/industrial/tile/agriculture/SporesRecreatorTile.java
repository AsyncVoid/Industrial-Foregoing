package com.buuz135.industrial.tile.agriculture;

import com.buuz135.industrial.tile.CustomColoredItemHandler;
import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.utils.WorkUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;

public class SporesRecreatorTile extends CustomElectricMachine {

    private IFluidTank waterTank;
    private ItemStackHandler input;
    private ItemStackHandler output;

    public SporesRecreatorTile() {
        super(SporesRecreatorTile.class.getName().hashCode());
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        waterTank = this.addFluidTank(FluidRegistry.WATER, 8000, EnumDyeColor.BLUE, "Water tank", new BoundingRectangle(18 * 2 + 13, 25, 18, 54));
        input = new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                SporesRecreatorTile.this.markDirty();
            }
        };
        this.addInventory(new CustomColoredItemHandler(input, EnumDyeColor.RED, "Input items", 18 * 5, 25, 1, 3) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                Block block = Block.getBlockFromItem(stack.getItem());
                return block.equals(Blocks.BROWN_MUSHROOM) || block.equals(Blocks.RED_MUSHROOM);
            }

            @Override
            public boolean canExtractItem(int slot) {
                return super.canExtractItem(slot);
            }
        });
        this.addInventoryToStorage(input, "input");
        output = new ItemStackHandler(9) {
            @Override
            protected void onContentsChanged(int slot) {
                SporesRecreatorTile.this.markDirty();
            }
        };
        this.addInventory(new CustomColoredItemHandler(output, EnumDyeColor.ORANGE, "Output items", 18 * 6 + 5, 25, 3, 3) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return false;
            }

            @Override
            public boolean canExtractItem(int slot) {
                return true;
            }
        });
        this.addInventoryToStorage(output, "output");
    }

    @Override
    protected float performWork() {
        if (WorkUtils.isDisabled(this.getBlockType())) return 0;
        int slot = getFirstSlot();
        ItemStack stack = input.getStackInSlot(slot);//getFirstItem(); //TODO !stack.isEmpty()                                        .isEmpty()
        if (stack != null && waterTank.getFluidAmount() >= 500 && ItemHandlerHelper.insertItem(output, stack.copy(), true) == null) {
            ItemStack out = stack.copy();
            out.stackSize = 2;//TODO out.setCount(2);
            ItemHandlerHelper.insertItem(output, out, false);
            waterTank.drain(500, true);
            stack.stackSize -= 1;//TODO stack.setCount(stack.getCount() - 1);
            if(stack.stackSize < 1)
            	input.setStackInSlot(slot, null);
            return 1;
        }
        return 0;
    }

    private int getFirstSlot() {
        for (int i = 0; i < input.getSlots(); ++i) //TODO !.isEmpty()
            if (input.getStackInSlot(i) != null) return i;
        return -1; //TODO ItemStack.EMPTY
    }
    /*
    private ItemStack getFirstItem() {
        for (int i = 0; i < input.getSlots(); ++i) //TODO !.isEmpty()
            if (input.getStackInSlot(i) != null) return input.getStackInSlot(i);
        return null; //TODO ItemStack.EMPTY
    }*/
}
