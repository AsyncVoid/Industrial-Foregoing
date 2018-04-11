package com.buuz135.industrial.tile.mob;

import com.buuz135.industrial.tile.CustomColoredItemHandler;
import com.buuz135.industrial.tile.WorkingAreaElectricMachine;
import com.buuz135.industrial.utils.WorkUtils;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class AnimalStockIncreaserTile extends WorkingAreaElectricMachine {

    public ItemStackHandler inFeedItems;

    public AnimalStockIncreaserTile() {
        super(AnimalStockIncreaserTile.class.getName().hashCode(), 2, 2, false);
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        inFeedItems = new ItemStackHandler(3 * 6) {
            @Override
            protected void onContentsChanged(int slot) {
                AnimalStockIncreaserTile.this.markDirty();
            }
        };
        this.addInventory(new CustomColoredItemHandler(inFeedItems, EnumDyeColor.GREEN, "Food items", 18 * 3, 25, 6, 3) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return true;
            }

            @Override
            public boolean canExtractItem(int slot) {
                return false;
            }

        });
        this.addInventoryToStorage(inFeedItems, "animal_stock_in");
    }

    @Override
    public AxisAlignedBB getWorkingArea() {
        EnumFacing f = this.getFacing().getOpposite();
        BlockPos corner1 = new BlockPos(0, 0, 0).offset(f, getRadius() + 1);
        return new AxisAlignedBB(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX() + 1, this.pos.getY() + 1, this.pos.getZ() + 1).offset(corner1).expand(getRadius(), 0, getRadius()).setMaxY(this.getPos().getY() + getHeight());
    }

    @Override
    public float work() {
        if (WorkUtils.isDisabled(this.getBlockType())) return 0;

        AxisAlignedBB area = getWorkingArea();
        List<EntityAnimal> animals = this.worldObj.getEntitiesWithinAABB(EntityAnimal.class, area);
        if (animals.size() == 0 || animals.size() > 20) return 0;
        EntityAnimal animal1 = animals.get(0);  //TODO getFirstBreedingItem(animal1).isEmpty()  //getFirstBreedingItem(animal1) == null
        while ((animal1.isChild() || animal1.getGrowingAge() != 0 || getFirstBreedingItemSlot(animal1) < 0 || animal1.isInLove()) && animals.indexOf(animal1) + 1 < animals.size())
            animal1 = animals.get(animals.indexOf(animal1) + 1);
        if (animal1.isChild() || animal1.getGrowingAge() != 0) return 0;
        EntityAnimal animal2 = animals.get(0);  //TODO getFirstBreedingItem(animal2).isEmpty() //getFirstBreedingItem(animal1) == null
        while ((animal2.equals(animal1) || animal2.isChild() || animal2.getGrowingAge() != 0 || getFirstBreedingItemSlot(animal2) < 0 || animal1.isInLove()) && animals.indexOf(animal2) + 1 < animals.size())
            animal2 = animals.get(animals.indexOf(animal2) + 1);
        if (animal2.equals(animal1) || animal2.isChild() || animal2.getGrowingAge() != 0) return 0;
        if (animal1.getClass() != animal2.getClass()) return 0;
        //TODO ItemStack stack = getFirstBreedingItem(animal1);
        int slot = getFirstBreedingItemSlot(animal1);
        ItemStack stack = inFeedItems.getStackInSlot(slot);
        Item item = stack.getItem();
        stack.stackSize -= 1;   //TODO stack.setCount(stack.getCount() - 1);
        if(stack.stackSize < 1)
        	inFeedItems.setStackInSlot(slot, null);
        //TODO stack = getFirstBreedingItem(animal2);
        slot = getFirstBreedingItemSlot(animal2);
        if (slot < 0) {   //stack == null TODO stack.isEmpty()
            ItemHandlerHelper.insertItem(inFeedItems, new ItemStack(item, 1), false);
            return 0;
        }
        stack = inFeedItems.getStackInSlot(slot);
        stack.stackSize -= 1; //TODO stack.setCount(stack.getCount() - 1);
        if(stack.stackSize < 1)
        	inFeedItems.setStackInSlot(slot, null);;
        animal1.setInLove(null);
        animal2.setInLove(null);

        return 1;
    }

    /*TODO public ItemStack getFirstBreedingItem(EntityAnimal animal) {
        for (int i = 0; i < inFeedItems.getSlots(); ++i) {
            if (animal.isBreedingItem(inFeedItems.getStackInSlot(i))) return inFeedItems.getStackInSlot(i);
        }
        return null; //TODO ItemStack.EMPTY
    }*/
    public int getFirstBreedingItemSlot(EntityAnimal animal) {
        for (int i = 0; i < inFeedItems.getSlots(); ++i) {
            if (animal.isBreedingItem(inFeedItems.getStackInSlot(i)))
            	return i;
        }
        return -1;
    }
}
