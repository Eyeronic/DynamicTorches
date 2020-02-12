package com.eyeronic.edt.proxy;

import com.eyeronic.edt.util.Reference;
import com.eyeronic.edt.util.network.Messages;
import com.google.common.util.concurrent.ListenableFuture;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public class CommonProxy {
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Messages.registerMessages(Reference.MODID);
	}
	
	public void registerItemRenderer(Item item, int meta, String id) {
		
	}
}
