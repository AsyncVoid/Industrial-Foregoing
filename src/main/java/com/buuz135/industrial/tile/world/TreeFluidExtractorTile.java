package com.buuz135.industrial.tile.world;

import com.buuz135.industrial.proxy.FluidsRegistry;
import com.buuz135.industrial.utils.BlockUtils;
import com.buuz135.industrial.utils.WorkUtils;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.inventory.BoundingRectangle;
import net.ndrei.teslacorelib.tileentities.SidedTileEntity;

public class TreeFluidExtractorTile extends SidedTileEntity {

    private IFluidTank tank;
    private int tick;
    private int progress;
    private int id;

    public TreeFluidExtractorTile() {
        super(TreeFluidExtractorTile.class.getName().hashCode());
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        tank = this.addFluidTank(FluidsRegistry.LATEX, 8000, EnumDyeColor.GRAY, "Latex tank", new BoundingRectangle(17, 25, 18, 54));
        tick = 0;
    }

    @Override
    protected void createAddonsInventory() {

    }

    @Override
    protected void innerUpdate() {
        if (WorkUtils.isDisabled(this.getBlockType())) return;
        if (this.getWorld().isRemote) return;
        if (!BlockUtils.isLog(this.worldObj, this.pos.offset(this.getFacing().getOpposite()))) progress = 0;
        if (tick % 5 == 0 && BlockUtils.isLog(this.worldObj, this.pos.offset(this.getFacing().getOpposite()))) {
            tank.fill(new FluidStack(FluidsRegistry.LATEX, 1), true);
            if (id == 0) id = this.worldObj.rand.nextInt();
            if (worldObj.rand.nextDouble() <= 0.005) ++progress;
            if (progress > 7) {
                progress = 0;
                this.worldObj.setBlockToAir(this.pos.offset(this.getFacing().getOpposite()));
            }
            if (tick > 404 && progress > 0) {
                this.worldObj.sendBlockBreakProgress(this.worldObj.rand.nextInt(), this.pos.offset(this.getFacing().getOpposite()), progress - 1);
                tick = 0;
            }
        }
        ++tick;
    }


    @Override
    protected boolean acceptsFluidItem(ItemStack stack) {
        return stack.getItem().equals(Items.BUCKET);
    }

    @Override
    protected void processFluidItems(ItemStackHandler fluidItems) {
        ItemStack stack = fluidItems.getStackInSlot(0);  //TODO !stack.isEmpty() && fluidItems.getStackInSlot(1).isEmpty()
        if (stack != null && fluidItems.getStackInSlot(1) != null && tank.getFluidAmount() >= 1000) {
            ItemStack out = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, FluidsRegistry.LATEX);
            tank.drain(1000, true);
            stack.stackSize -= 1;  //TODO stack.setCount(stack.getCount() - 1)
            if(stack.stackSize < 1)
            	fluidItems.setStackInSlot(0, null);
            fluidItems.setStackInSlot(1, out);
        }
    }
}
