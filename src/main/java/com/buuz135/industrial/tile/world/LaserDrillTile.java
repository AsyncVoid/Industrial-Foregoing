package com.buuz135.industrial.tile.world;

import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.utils.WorkUtils;
import net.minecraft.util.math.BlockPos;


public class LaserDrillTile extends CustomElectricMachine {

    public LaserDrillTile() {
        super(LaserDrillTile.class.getName().hashCode());

    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
    }

    @Override
    protected float performWork() {
        if (WorkUtils.isDisabled(this.getBlockType())) return 0;

        BlockPos pos = getLaserBasePos();
        if (pos != null) {
            LaserBaseTile tile = (LaserBaseTile) this.worldObj.getTileEntity(pos);
            tile.increaseWork();
            return 1;
        }
        return 0;
    }

    public BlockPos getLaserBasePos() {
        BlockPos pos = this.pos.offset(this.getFacing().getOpposite(), 2);
        if (this.worldObj.getTileEntity(pos) != null && this.worldObj.getTileEntity(pos) instanceof LaserBaseTile) return pos;
        return null;
    }
}
