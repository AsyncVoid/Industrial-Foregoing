package com.buuz135.industrial.tile.misc;

import com.buuz135.industrial.tile.CustomColoredItemHandler;
import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.utils.ItemStackUtils;
import com.buuz135.industrial.utils.WorkUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class OreProcessorTile extends CustomElectricMachine {

    private ItemStackHandler input;
    private ItemStackHandler output;

    public OreProcessorTile() {
        super(OreProcessorTile.class.getName().hashCode());
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        input = new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                OreProcessorTile.this.markDirty();
            }
        };
        this.addInventory(new CustomColoredItemHandler(input, EnumDyeColor.BLUE, "Ores input", 18 * 2 + 12, 25, 1, 3) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                if (ItemStackUtils.isOre(stack)) {
                    if (Block.getBlockFromItem(stack.getItem()) != Blocks.AIR) {
                        Block block = Block.getBlockFromItem(stack.getItem());
                        List<ItemStack> drops = block.getDrops(OreProcessorTile.this.worldObj, null, block.getDefaultState(), 0);
                        if (drops.size() > 0 && !drops.get(0).getItem().equals(stack.getItem())) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean canExtractItem(int slot) {
                return false;
            }
        });
        this.addInventoryToStorage(input, "input");
        output = new ItemStackHandler(3 * 5) {
            @Override
            protected void onContentsChanged(int slot) {
                OreProcessorTile.this.markDirty();
            }
        };
        this.addInventory(new CustomColoredItemHandler(output, EnumDyeColor.ORANGE, "Processed ores output", 18 * 4 + 2, 25, 5, 3) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return false;
            }

            @Override
            public boolean canExtractItem(int slot) {
                return true;
            }
        });
        this.addInventoryToStorage(output, "outout");
    }

    /*private ItemStack getFirstStack() {
        for (int i = 0; i < input.getSlots(); ++i) { //TODO !input.getStackInSlot(i).isEmpty()
            if (input.getStackInSlot(i) != null) 
            	return input.getStackInSlot(i);
        }
        return null;  //TODO ItemStack.EMPTY
    }*/
    private int getFirstStackSlot() {
        for (int i = 0; i < input.getSlots(); ++i) {
            if (input.getStackInSlot(i) != null) 
            	return i;
        }
        return -1;
    }

    @Override
    protected float performWork() {
        if (WorkUtils.isDisabled(this.getBlockType())) return 0;
        int slot = getFirstStackSlot();
        if(slot < 0) return 0;
        ItemStack stack = input.getStackInSlot(slot);
        //if (stack == null) return 0;  //TODO stack.isEmpty()
        Block block = Block.getBlockFromItem(stack.getItem());
        List<ItemStack> drops = block.getDrops(OreProcessorTile.this.worldObj, null, block.getDefaultState(), 0);
        boolean canInsert = true;
        for (ItemStack temp : drops) {		//TODO !ItemHandlerHelper.insertItem(output, temp, true).isEmpty()
            if (ItemHandlerHelper.insertItem(output, temp, true) == null) {
                canInsert = false;
                break;
            }
        }
        if (canInsert) {
            for (ItemStack temp : drops) {
                ItemHandlerHelper.insertItem(output, temp, false);
            }
            stack.stackSize -= 1; //TODO stack.setCount(stack.getCount() - 1);
            if(stack.stackSize < 1)
            	input.setStackInSlot(slot, null);
            return 1;
        }
        return 0;
    }
}
