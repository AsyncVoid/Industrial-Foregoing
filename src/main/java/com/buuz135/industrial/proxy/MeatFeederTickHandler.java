package com.buuz135.industrial.proxy;

import com.buuz135.industrial.item.MeatFeederItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MeatFeederTickHandler {

    @SubscribeEvent
    public void onTick(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntityLiving().getEntityWorld().isRemote && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (player.getFoodStats().needFood()) {
                for (ItemStack stack : player.inventory.mainInventory) {
                	if (stack == null) continue;//TODO dont need to check null
                    if (stack.getItem().equals(ItemRegistry.meatFeederItem)) { 
                        int filledAmount = ((MeatFeederItem) stack.getItem()).getFilledAmount(stack);
                        if (filledAmount >= 200) {
                            ((MeatFeederItem) stack.getItem()).drain(stack, 200);
                            player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel() + 1);
                            return;
                        }
                    }
                }
            }
        }
    }
}
