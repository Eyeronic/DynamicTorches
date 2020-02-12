package com.eyeronic.edt.init;

import java.util.ArrayList;
import java.util.List;

import com.eyeronic.edt.DynamicTorches;
import com.eyeronic.edt.config.ModConfig;
import com.eyeronic.edt.objects.blocks.BlockDynamicTorch;
import com.eyeronic.edt.util.Reference;

import net.minecraft.block.Block;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class BlockInit {
	private static boolean cleanUpActive = false;
	
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block BLOCK_TORCH_SUBSTITUTE = new BlockDynamicTorch();
		
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(event.getModID().equals(Reference.MODID))
		{
			if(ModConfig.isCleanUp) {
				cleanUpActive = true;
				DynamicTorches.log("Starting clean up.");
			}
			else if (!ModConfig.isCleanUp && cleanUpActive) {
				DynamicTorches.log("Stopping clean up.");
				cleanUpActive = false;
			}
		}
	}
}
