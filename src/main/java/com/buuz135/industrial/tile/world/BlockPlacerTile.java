package com.buuz135.industrial.tile.world;

import com.buuz135.industrial.tile.CustomColoredItemHandler;
import com.buuz135.industrial.tile.WorkingAreaElectricMachine;
import com.buuz135.industrial.utils.BlockUtils;
import com.buuz135.industrial.utils.WorkUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class BlockPlacerTile extends WorkingAreaElectricMachine {

    private ItemStackHandler inItems;

    public BlockPlacerTile() {
        super(BlockPlacerTile.class.getName().hashCode(), 0, 0, true);
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        inItems = new ItemStackHandler(3 * 6) {
            @Override
            protected void onContentsChanged(int slot) {
                BlockPlacerTile.this.markDirty();
            }
        };
        this.addInventory(new CustomColoredItemHandler(inItems, EnumDyeColor.BLUE, "Input items", 18 * 3, 25, 6, 3) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return true;
            }

            @Override
            public boolean canExtractItem(int slot) {
                return true;
            }

        });
        this.addInventoryToStorage(inItems, "block_destroyer_out");
    }

    @Override
    public AxisAlignedBB getWorkingArea() {
        return new AxisAlignedBB(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX() + 1, this.pos.getY() + 1, this.pos.getZ() + 1).offset(new BlockPos(0, 0, 0).offset(this.getFacing().getOpposite()));
    }

    @Override
    public float work() {
        if (WorkUtils.isDisabled(this.getBlockType())) return 0;
        List<BlockPos> blockPosList = BlockUtils.getBlockPosInAABB(getWorkingArea());
        for (BlockPos pos : blockPosList) {
            if (this.worldObj.isAirBlock(pos)) {
            	int slot = getFirstSlotHasBlock();
                //TODO ItemStack stack = getFirstStackHasBlock();
                if (slot < 0)  //stack == null TODO stack.isEmpty()
                	return 0; 
                ItemStack stack = inItems.getStackInSlot(slot);
                this.worldObj.setBlockState(pos, Block.getBlockFromItem(stack.getItem()).getDefaultState());
                stack.stackSize -= 1;  //TODO stack.setCount(stack.stackSize - 1)
                if(stack.stackSize < 1)
                	inItems.setStackInSlot(slot, null);
                return 1;
            }
        }
        return 0;
    }

    /*private ItemStack getFirstStackHasBlock() {
        for (int i = 0; i < inItems.getSlots(); ++i) {  //TODO !inItems.getStackInSlot(i).isEmpty()
            if (inItems.getStackInSlot(i) != null && !Block.getBlockFromItem(inItems.getStackInSlot(i).getItem()).equals(Blocks.AIR))
                return inItems.getStackInSlot(i);
        }
        return null;  //TODO ItemStack.EMPTY
    }*/
    private int getFirstSlotHasBlock() {
        for (int i = 0; i < inItems.getSlots(); ++i) {
            if (inItems.getStackInSlot(i) != null && Block.getBlockFromItem(inItems.getStackInSlot(i).getItem()) != null)
                return i;
        }
        return -1;
    }
}
