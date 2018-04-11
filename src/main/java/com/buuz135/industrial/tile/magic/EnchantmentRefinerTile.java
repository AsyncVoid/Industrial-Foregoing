package com.buuz135.industrial.tile.magic;

import com.buuz135.industrial.tile.CustomColoredItemHandler;
import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.utils.WorkUtils;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class EnchantmentRefinerTile extends CustomElectricMachine {

    private ItemStackHandler input;
    private ItemStackHandler outputNoEnch;
    private ItemStackHandler outputEnch;


    public EnchantmentRefinerTile() {
        super(EnchantmentRefinerTile.class.getName().hashCode());
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();

        this.initInputInv();
        this.initOutputInv();
    }

    private void initInputInv() {
        input = new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                EnchantmentRefinerTile.this.markDirty();
            }
        };

        this.addInventory(new CustomColoredItemHandler(this.input, EnumDyeColor.GREEN, "Input items", 18 * 3, 25, 1, 3) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return true;
            }

            @Override
            public boolean canExtractItem(int slot) {
                return false;
            }
        });
        this.addInventoryToStorage(this.input, "ench_ref_in");

    }

    private void initOutputInv() {
        outputEnch = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                EnchantmentRefinerTile.this.markDirty();
            }
        };
        this.addInventory(new CustomColoredItemHandler(outputEnch, EnumDyeColor.PURPLE, "Enchanted Items", 18 * 4 + 14, 25, 4, 1) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return false;
            }

            @Override
            public boolean canExtractItem(int slot) {
                return true;
            }

        });
        this.addInventoryToStorage(outputEnch, "ench_ref_out_yes");

        outputNoEnch = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                EnchantmentRefinerTile.this.markDirty();
            }
        };
        this.addInventory(new CustomColoredItemHandler(outputNoEnch, EnumDyeColor.YELLOW, "No enchanted Items", 18 * 4 + 14, 25 + 18 * 2, 4, 1) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return false;
            }

            @Override
            public boolean canExtractItem(int slot) {
                return true;
            }

        });
        this.addInventoryToStorage(outputNoEnch, "ench_ref_out_no");
    }

    /*public ItemStack getFirstItem() {
        for (int i = 0; i < input.getSlots(); ++i) {
            if (input.getStackInSlot(i) != null) {  //TODO !input.getStackInSlot(i).isEmpty()
                return input.getStackInSlot(i);
            }
        }
        return null; //TODO ItemStack.EMPTY
    }*/
    public int getFirstItemSlot() {
        for (int i = 0; i < input.getSlots(); ++i) {
            if (input.getStackInSlot(i) != null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected float performWork() {
        if (WorkUtils.isDisabled(this.getBlockType())) return 0;
        int slot = getFirstItemSlot();
        ItemStack stack = input.getStackInSlot(slot); //getFirstItem();
        if (stack == null) { //TODO stack.isEmpty()
            return 0;
        }
        ItemStack out = stack.copy();
        out.stackSize = 1;  //TODO out.setCount(1);
        if (stack.isItemEnchanted() || stack.getItem().equals(Items.ENCHANTED_BOOK)) {
            if (ItemHandlerHelper.insertItem(outputEnch, out, true) == null) {  //TODO .isEmpty()
                ItemHandlerHelper.insertItem(outputEnch, out, false);
                stack.stackSize -= 1; //TODO stack.setCount(stack.getCount() - 1);
                if(stack.stackSize < 1)
                	input.setStackInSlot(slot, null);
                return 500;
            }
        } else if (ItemHandlerHelper.insertItem(outputNoEnch, out, true) == null) {//TODO .isEmpty()
            ItemHandlerHelper.insertItem(outputNoEnch, out, false);
            stack.stackSize -= 1; //TODO stack.setCount(stack.getCount() - 1);
            if(stack.stackSize < 1)
            	input.setStackInSlot(slot, null);
            return 500;
        }

        return 0;
    }
}
