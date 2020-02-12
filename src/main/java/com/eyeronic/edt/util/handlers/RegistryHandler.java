package com.eyeronic.edt.util.handlers;

import com.eyeronic.edt.DynamicTorches;
import com.eyeronic.edt.init.BlockInit;
import com.eyeronic.edt.init.ItemInit;
import com.eyeronic.edt.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.GameData;

@EventBusSubscriber
public class RegistryHandler {
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event){
		event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event){
		event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
		//DynamicTorches.log("onBlockRegister: " + BlockInit.BLOCK_TORCH_SUBSTITUTE.getLocalizedName());
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event){
		for(Item item : ItemInit.ITEMS)
			if(item instanceof IHasModel)
				((IHasModel) item).registerModels();
		
		for(Block block : BlockInit.BLOCKS)
			if(block instanceof IHasModel)
				((IHasModel) block).registerModels();
	}
}
