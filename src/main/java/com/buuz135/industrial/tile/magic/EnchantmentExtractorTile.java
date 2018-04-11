package com.buuz135.industrial.tile.magic;

import com.buuz135.industrial.tile.CustomColoredItemHandler;
import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.utils.WorkUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;


public class EnchantmentExtractorTile extends CustomElectricMachine {

    private ItemStackHandler inBook;
    private ItemStackHandler inEnchanted;
    private ItemStackHandler outItem;
    private ItemStackHandler outEnchanted;

    public EnchantmentExtractorTile() {
        super(EnchantmentExtractorTile.class.getName().hashCode());
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        initInputInv();
        initOutputInv();
    }

    private void initInputInv() {
        inBook = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                EnchantmentExtractorTile.this.markDirty();
            }
        };

        super.addInventory(new CustomColoredItemHandler(this.inBook, EnumDyeColor.BROWN, "Input books", 18 * 3, 25, 1, 1) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return stack.getItem().equals(Items.BOOK);
            }

            @Override
            public boolean canExtractItem(int slot) {
                return false;
            }

        });
        super.addInventoryToStorage(this.inBook, "ench_books");

        inEnchanted = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                EnchantmentExtractorTile.this.markDirty();
            }
        };
        this.addInventory(new CustomColoredItemHandler(this.inEnchanted, EnumDyeColor.PURPLE, "Input enchanted items", 18 * 3, 25 + 18 * 2, 1, 1) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return stack.isItemEnchanted();
            }

            @Override
            public boolean canExtractItem(int slot) {
                return false;
            }
        });
        this.addInventoryToStorage(this.inEnchanted, "ench_ext_in_items");
    }


    private void initOutputInv() {
        outEnchanted = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                EnchantmentExtractorTile.this.markDirty();
            }
        };
        this.addInventory(new CustomColoredItemHandler(outEnchanted, EnumDyeColor.MAGENTA, "Enchanted Books", 18 * 4 + 14, 25, 4, 1) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return false;
            }

            @Override
            public boolean canExtractItem(int slot) {
                return true;
            }

        });
        this.addInventoryToStorage(outEnchanted, "ench_ext_out_book");

        outItem = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                EnchantmentExtractorTile.this.markDirty();
            }
        };
        this.addInventory(new CustomColoredItemHandler(outItem, EnumDyeColor.YELLOW, "Enchantless Items", 18 * 4 + 14, 25 + 18 * 2, 4, 1) {
            @Override
            public boolean canInsertItem(int slot, ItemStack stack) {
                return false;
            }

            @Override
            public boolean canExtractItem(int slot) {
                return true;
            }

        });
        this.addInventoryToStorage(outItem, "ench_ext_out_items");
    }

    private boolean hasBooks() {
    	//TODO return !inBook.getStackInSlot(0).isEmpty();
        return inBook.getStackInSlot(0) != null;
    }

    private ItemStack getItem() {
        return inEnchanted.getStackInSlot(0);
    }

    @Override
    protected float performWork() {
        if (WorkUtils.isDisabled(this.getBlockType())) return 0;
        				//TODO  getItem().isEmpty()
        if (!hasBooks() || getItem() == null) return 0;
        ItemStack enchantedItem = getItem();
        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK); //TODO .isEmpty()
        if (ItemHandlerHelper.insertItem(outEnchanted, enchantedBook, true) == null && ItemHandlerHelper.insertItem(outItem, enchantedItem, true) == null) {
            NBTTagCompound base = (NBTTagCompound) enchantedItem.getEnchantmentTagList().get(0);
            Items.ENCHANTED_BOOK.addEnchantment(enchantedBook, new EnchantmentData(Enchantment.getEnchantmentByID(base.getShort("id")), base.getShort("lvl")));
            enchantedItem.getEnchantmentTagList().removeTag(0);
            ItemHandlerHelper.insertItem(outEnchanted, enchantedBook, false);
            ItemHandlerHelper.insertItem(outItem, enchantedItem.copy(), false);
            //TODO inBook.getStackInSlot(0).setCount(inBook.getStackInSlot(0).getCount() - 1);
            inBook.getStackInSlot(0).stackSize -= 1;
            enchantedItem.stackSize -= 1;
            if(inBook.getStackInSlot(0).stackSize < 1)
            	inBook.setStackInSlot(0, null);
            if(enchantedItem.stackSize < 1)
            	inEnchanted.setStackInSlot(0, null);
            
            //TODO enchantedItem.setCount(enchantedItem.getCount() - 1);
            return 500;
        }

        return 0;
    }
}
