package com.buuz135.industrial.tile.block;


import com.buuz135.industrial.IndustrialForegoing;
import com.buuz135.industrial.proxy.ItemRegistry;
import com.buuz135.industrial.utils.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.Utils;


public class SliderBlock extends Block {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public SliderBlock() {
        super(Material.ROCK);
        setRegistryName(Reference.MOD_ID, "slider");
        setCreativeTab(IndustrialForegoing.creativeTab);
        setUnlocalizedName("slider");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    public void register() {
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), this.getRegistryName());
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 3), "ppp", "iii", 'p', ItemRegistry.plastic, 'i', "ingotIron"));
    }

    public void registerRenderer() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this)
                , 0
                , new ModelResourceLocation(this.getRegistryName(), "inventory")
        );
    }

    @Override
    public boolean canSpawnInBlock() {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 1, (1F / 16F), 1);
    }

    //TODO getCollisionBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 1, (0.25F / 16F), 1);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        EnumFacing facing = state.getValue(FACING).getOpposite();
        Vec3i vec3i = facing.getDirectionVec();
        BlockPos p = entityIn.getPosition();
        float value = 1000000;
        entityIn.setPosition(p.getX() + vec3i.getX() / value, p.getY() + vec3i.getY() / value, p.getZ() + vec3i.getZ() / value);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, Utils.getFacingFromEntity(pos, placer)), 2);
    }
}
