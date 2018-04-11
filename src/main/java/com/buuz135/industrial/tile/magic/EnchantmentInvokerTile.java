package com.buuz135.industrial.tile.magic;

import com.buuz135.industrial.proxy.FluidsRegistry;
import com.buuz135.industrial.tile.CustomColoredItemHandler;
import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.utils.WorkUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;

public class EnchantmentInvokerTile extends CustomElectricMachine {

    private IFluidTank essenceTank;
    private ItemStackHandler input;
    private ItemStackHandler output;

    public EnchantmentInvokerTile() {
        super(EnchantmentInvokerTile.class.getName().hashCode());
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        input = new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                EnchantmentInvokerTile.this.markDirty();
            }
        };
        this.addInventory(new CustomColoredItemHandler(input, EnumDyeColor.BLUE, "Input items", 18 * 2 + 13, 25, 1, 3) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return stack.isItemEnchantable();
            }

            @Override
            public boolean canExtractItem(int slot) {
                return super.canExtractItem(slot);
            }
        });
        this.addInventoryToStorage(input, "input");
        essenceTank = this.addFluidTank(FluidsRegistry.ESSENCE, 32000, EnumDyeColor.LIME, "Experience tank", new BoundingRectangle(18 * 4, 25, 18, 54));
        output = new ItemStackHandler(9) {
            @Override
            protected void onContentsChanged(int slot) {
                EnchantmentInvokerTile.this.markDirty();
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
        if (WorkUtils.isDisabled(this.getBlockType())) return 0;//enchantment_invoker
        int slot = getFirstItemSlot();
        ItemStack stack = input.getStackInSlot(slot); //getFirstItem();         //TODO !stack.isEmpty()                                             .isEmpty()
        if (essenceTank.getFluidAmount() >= 3000 && stack != null && ItemHandlerHelper.insertItem(output, stack, true) == null) {
            essenceTank.drain(3000, true);
            ItemHandlerHelper.insertItem(output, EnchantmentHelper.addRandomEnchantment(this.worldObj.rand, stack.copy(), 30, true), false);
            input.setStackInSlot(slot, null);
            //stack.stackSize = 0; //TODO stack.setCount(0);
            return 1;
        }
        return 0;
    }

    private int getFirstItemSlot() {
        for (int i = 0; i < input.getSlots(); ++i) 
            if (input.getStackInSlot(i) != null)
            	return i;
        return -1; 
    }
    
    /*private ItemStack getFirstItem() {
        for (int i = 0; i < input.getSlots(); ++i) //TODO !input.getStackInSlot(i).isEmpty()
            if (input.getStackInSlot(i) != null) return input.getStackInSlot(i);
        return null;  //TODO ItemStack.EMPTY
    }*/
}
