package com.buuz135.industrial.tile.generator;

import com.buuz135.industrial.proxy.FluidsRegistry;
import com.buuz135.industrial.proxy.client.infopiece.BioreactorEfficiencyInfoPiece;
import com.buuz135.industrial.tile.CustomColoredItemHandler;
import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.tile.block.BioReactorBlock;
import com.buuz135.industrial.utils.WorkUtils;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;

import java.util.List;

public class BioReactorTile extends CustomElectricMachine {

    private ItemStackHandler input;
    private IFluidTank tank;

    public BioReactorTile() {
        super(BioReactorTile.class.getName().hashCode());
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        tank = this.addFluidTank(FluidsRegistry.BIOFUEL, 8000, EnumDyeColor.PURPLE, "Biofuel tank", new BoundingRectangle(48, 25, 18, 54));
        input = new ItemStackHandler(9) {
            @Override
            protected void onContentsChanged(int slot) {
                BioReactorTile.this.markDirty();
            }

            @Override
            public int getStackLimit(int slot, ItemStack stack) { //TODO getSlotLimit(int slot)
                return 16;
            }
        };
        this.addInventory(new CustomColoredItemHandler(input, EnumDyeColor.BLUE, "Input items", 18 * 5, 25, 3, 3) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
            	for(ItemStack stack1 : ((BioReactorBlock) BioReactorTile.this.getBlockType()).getItemsAccepted())
            	{
            		if(stack.getItem().equals(stack1.getItem()))
            		{
            			return !alreadyContains(input, stack, 16);
            		}
            	}
            	return false;
                //TODO return ((BioReactorBlock) BioReactorTile.this.getBlockType()).getItemsAccepted().stream().anyMatch(stack1 -> stack.getItem().equals(stack1.getItem())) && !alreadyContains(input, stack, 16);
            }

            @Override
            public boolean canExtractItem(int slot) {
                return false;
            }
        });
        this.addInventoryToStorage(input, "input");
    }


    @Override
    public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer container) {
        List<IGuiContainerPiece> pieces = super.getGuiContainerPieces(container);
        pieces.add(new BioreactorEfficiencyInfoPiece(this, 149, 25));
        return pieces;
    }


    @Override
    protected float performWork() {
        if (WorkUtils.isDisabled(this.getBlockType())) return 0;

        if (getEfficiency() < 0) return 0;
        FluidStack stack = new FluidStack(FluidsRegistry.BIOFUEL, getProducedAmountItem() * getItemAmount());
        if (tank.getFluid() == null || (stack.amount + tank.getFluidAmount() <= tank.getCapacity())) {
            tank.fill(stack, true);
            for (int i = 0; i < input.getSlots(); ++i) {
                if (input.getStackInSlot(i) != null) { //TODO !.isEmpty()
                	ItemStack is = input.getStackInSlot(i);
                	is.stackSize -= 1;
                    if(is.stackSize < 1)
                    	input.setStackInSlot(i, null);
                }
                //TODO input.getStackInSlot(i).setCount(input.getStackInSlot(i).getCount() - 1);
            }
            return 1;
        }
        return 0;
    }

    private boolean alreadyContains(ItemStackHandler handler, ItemStack stack, int amountAtleast) {
        
    	for (int i = 0; i < handler.getSlots(); ++i) { //TODO .getCount()
            if (stack.getItem().equals(handler.getStackInSlot(i).getItem()) && stack.getMetadata() == handler.getStackInSlot(i).getMetadata() && handler.getStackInSlot(i).stackSize >= amountAtleast)
                return true;
        }
        return false;
    }


    public int getItemAmount() {
        int am = 0;
        for (int i = 0; i < input.getSlots(); ++i) {
            if (input.getStackInSlot(i) != null) ++am; //TODO !.isEmpty()
        }
        return am;
    }

    public float getEfficiency() {
        return (getItemAmount() - 1) / 8f;
    }

    public int getProducedAmountItem() {
        float eff = getEfficiency();
        if (eff < 0) return 0;
        int base = ((BioReactorBlock) this.getBlockType()).getBaseAmount();
        return (int) (getEfficiency() * base + base);
    }

    @Override
    public void protectedUpdate() {
        super.protectedUpdate();
    }
}
