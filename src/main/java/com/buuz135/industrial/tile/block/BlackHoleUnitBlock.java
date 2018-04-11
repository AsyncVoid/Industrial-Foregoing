package com.buuz135.industrial.tile.block;

import com.buuz135.industrial.proxy.ItemRegistry;
import com.buuz135.industrial.tile.misc.BlackHoleUnitTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.TeslaCoreLib;

import java.util.Arrays;
import java.util.List;

public class BlackHoleUnitBlock extends CustomOrientedBlock<BlackHoleUnitTile> {

    public BlackHoleUnitBlock() {
        super("black_hole_unit", BlackHoleUnitTile.class, Material.ROCK, 0, 0);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world.getTileEntity(pos) instanceof BlackHoleUnitTile) {
            BlackHoleUnitTile tile = (BlackHoleUnitTile) world.getTileEntity(pos);
            ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1);
            if (tile.getAmount() > 0) {
                if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setInteger(BlackHoleUnitTile.NBT_AMOUNT, tile.getAmount());
                stack.getTagCompound().setString(BlackHoleUnitTile.NBT_ITEMSTACK, tile.getStack().getItem().getRegistryName().toString());
                stack.getTagCompound().setInteger(BlackHoleUnitTile.NBT_META, tile.getStack().getMetadata());
                if (tile.getStack().hasTagCompound())
                    stack.getTagCompound().setTag(BlackHoleUnitTile.NBT_ITEM_NBT, tile.getStack().getTagCompound());
            }
            float f = 0.7F;
            float d0 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            float d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            float d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5F;
            EntityItem entityitem = new EntityItem(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, stack);
            entityitem.setDefaultPickupDelay();
            if (stack.hasTagCompound()) {
                entityitem.getEntityItem().setTagCompound(stack.getTagCompound().copy());
            }
            world.spawnEntityInWorld(entityitem); //TODO world.spawnEntity(entityitem);

        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Arrays.asList();
    }


    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (stack.hasTagCompound() && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof BlackHoleUnitTile) {
            BlackHoleUnitTile tile = (BlackHoleUnitTile) world.getTileEntity(pos);
            if (stack.getTagCompound().hasKey(BlackHoleUnitTile.NBT_ITEMSTACK) && stack.getTagCompound().hasKey(BlackHoleUnitTile.NBT_META)) {
                ItemStack item = new ItemStack(Item.getByNameOrId(stack.getTagCompound().getString(BlackHoleUnitTile.NBT_ITEMSTACK)), 1, stack.getTagCompound().getInteger(BlackHoleUnitTile.NBT_META));
                if (stack.getTagCompound().hasKey(BlackHoleUnitTile.NBT_ITEM_NBT))
                    item.setTagCompound(stack.getTagCompound().getCompoundTag(BlackHoleUnitTile.NBT_ITEM_NBT));
                tile.setStack(item);
            }
            if (stack.getTagCompound().hasKey(BlackHoleUnitTile.NBT_AMOUNT))
                tile.setAmount(stack.getTagCompound().getInteger(BlackHoleUnitTile.NBT_AMOUNT));
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        if (stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey(BlackHoleUnitTile.NBT_ITEMSTACK) && stack.getTagCompound().hasKey(BlackHoleUnitTile.NBT_META)) {
                tooltip.add(new TextComponentTranslation("text.display.item").getUnformattedText() + " " + new TextComponentTranslation(new ItemStack(Item.getByNameOrId(stack.getTagCompound().getString(BlackHoleUnitTile.NBT_ITEMSTACK)), 1, stack.getTagCompound().getInteger(BlackHoleUnitTile.NBT_META)).getUnlocalizedName() + ".name").getUnformattedText());
            }
            if (stack.getTagCompound().hasKey(BlackHoleUnitTile.NBT_AMOUNT))
                tooltip.add(new TextComponentTranslation("text.display.amount").getUnformattedText() + " " + stack.getTagCompound().getInteger(BlackHoleUnitTile.NBT_AMOUNT));
        }

        tooltip.add("\"the BHU\"");
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this), "ppp", "eae", "cmc",
                'p', ItemRegistry.plastic,
                'e', Items.ENDER_EYE,
                'a', Items.ENDER_PEARL,
                'c', "chestWood",
                'm', TeslaCoreLib.machineCase);
    }

    public ItemStack getItemStack(ItemStack blackHole) {
        NBTTagCompound compound = blackHole.getTagCompound();
        ItemStack stack = null; //TODO  ItemStack.EMPTY
        if (compound == null || !compound.hasKey(BlackHoleUnitTile.NBT_ITEMSTACK)) return stack;
        Item item = Item.getByNameOrId(compound.getString(BlackHoleUnitTile.NBT_ITEMSTACK));
        if (item != null) {
            stack = new ItemStack(item, 1, compound.hasKey(BlackHoleUnitTile.NBT_META) ? compound.getInteger(BlackHoleUnitTile.NBT_META) : 0);
            if (compound.hasKey(BlackHoleUnitTile.NBT_ITEM_NBT))
                stack.setTagCompound(compound.getCompoundTag(BlackHoleUnitTile.NBT_ITEM_NBT));
        }
        return stack;
    }

    public int getAmount(ItemStack blackHole) {
        NBTTagCompound compound = blackHole.getTagCompound();
        int amount = 0;
        if (compound != null && compound.hasKey(BlackHoleUnitTile.NBT_AMOUNT)) {
            amount = compound.getInteger(BlackHoleUnitTile.NBT_AMOUNT);
        }
        return amount;
    }

    public void setAmount(ItemStack blackHole, int amount) {
        NBTTagCompound compound = blackHole.getTagCompound();
        if (compound != null) {
            compound.setInteger(BlackHoleUnitTile.NBT_AMOUNT, amount);
        }
    }

}
