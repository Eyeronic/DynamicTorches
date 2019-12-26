package com.eyeronic.edt.config;

import com.eyeronic.edt.util.Reference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MODID)
public class ModConfig {
	//TODO: add functions that will allow torches to be moved to a valid position when set to false (i.e. NORTH, EAST, SOUTH, WEST, UP)
	@Config.Name("Dynamic Torches active")
	@Config.Comment("Only available while in main menu.\nWARNING! Deletes torches not placed in vanilla positions!")
	@Config.RequiresMcRestart
	@Config.RequiresWorldRestart
	public static boolean dynamicTorchesEnabled = true;
	
	@Config.Name("Torches switch to ground")
	@Config.Comment("If enabled torches will switch to the ground when the wall they are attached to breaks and a valid block is directly under them.")
	public static boolean shouldTorchesSwitchToGround = false;
	
	// TODO: clean up mode that actually updates blockstates and/or meta data saved --> DONE! Just takes a while since you can't run this on WorldEvent.Load.
	@Config.Name("Cleanup mode active")
	@Config.Comment("Enable before uninstalling EDT to save torches in vanilla positions where possible. Will take some time depending on number of torches placed in corners.")
	public static boolean isCleanUp = false;
	
	//TODO: implement method to update torch positions on first run
	@Config.Name("First run mode active")
	@Config.Comment("Enable to automatically move all loaded torches to corner positions where possible. Will take some time depending on number of torches loaded.")
	public static boolean isFirstRun = true;
	
	
	@Mod.EventBusSubscriber(modid = Reference.MODID)
	private static class ConfigurationHandler {
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(Reference.MODID))
			{
				ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
