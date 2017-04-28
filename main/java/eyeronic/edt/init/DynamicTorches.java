package eyeronic.edt.init;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.ExistingSubstitutionException;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import eyeronic.edt.DTBlock;
import eyeronic.edt.DTItem;
import eyeronic.edt.Reference;
import eyeronic.edt.proxy.ServerProxy;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, guiFactory = Reference.GUI_FACTORY)
public class DynamicTorches {
	public static Configuration config;

	private static Item itemDynamicTorch;
	private static Block blockDynamicTorch;
	public static int dtRenderID;

	public static boolean dynamicTorchesEnabled = true;
	public static boolean shouldTorchesSwitchToGround = false;

	@SidedProxy(clientSide = "eyeronic.edt.proxy.ClientProxy", serverSide = "eyeronic.edt.proxy.ServerProxy")
	public static ServerProxy proxy;

	@Mod.Instance(Reference.MOD_ID)
	public static DynamicTorches instance;

	@EventHandler
	/**
	 * Item and Block initialization and registering
	 * Config handling
	 * 
	 * @param event FMLPreInitializationEvent
	 */
	public void preInit(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		syncConfig();
	}

	@EventHandler
	/**
	 * Proxy, TileEntity, Entity, GUI, and Packet registering
	 * 
	 * @param event FMLInitializationEvent
	 */
	public void init(FMLInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(instance);

		if(dynamicTorchesEnabled)
		{
			dtRenderID = RenderingRegistry.getNextAvailableRenderId();
			blockDynamicTorch = new DTBlock().setBlockName("BlockDynamicTorch").setBlockTextureName("minecraft:torch_on");
			itemDynamicTorch = new DTItem(blockDynamicTorch).setUnlocalizedName("ItemDynamicTorch");//.setTextureName("ect:itemDynamicTorch");

			try 
			{
				GameRegistry.addSubstitutionAlias(ItemBlock.itemRegistry.getNameForObject(ItemBlock.getItemFromBlock(Blocks.torch)), Type.ITEM, itemDynamicTorch);
				GameRegistry.addSubstitutionAlias(Block.blockRegistry.getNameForObject(Blocks.torch), Type.BLOCK, blockDynamicTorch);
			} 
			catch (ExistingSubstitutionException e) 
			{
				System.err.println("Substitution failed!");
				e.printStackTrace();
			}
		}
	}

	@EventHandler
	/**
	 * 
	 * @param event FMLPostInitializationEvent
	 */
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.registerRenderThings();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if(eventArgs.modID.equals(Reference.MOD_ID))
		{
			config.save();
			syncConfig();
		}
	}

	/**
	 * Load/save variables from/to config file
	 */
	public static void syncConfig() { // Gets called from preInit or on in-game changes to the config
		try 
		{
			// Load config
			config.load();

			// Read props from config
			Property isDynamicTorchesEnabledProp = 
					config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
							"dynamicTorchesEnabled", // Property name
							"true", // Default value
							"Whether 'Dynamic Torches' is enabled", //Comment
							Property.Type.BOOLEAN); // Type

			Property shouldTorchesMoveProp =
					config.get(Configuration.CATEGORY_GENERAL,
							"shouldTorchesSwitchToGround",
							"true",
							"Whether torches should switch to ground position if wall is broken and vice versa.\nRequires you to restart Minecraft.",
							Property.Type.BOOLEAN);

			// Get the boolean value, also set the property value to boolean
			dynamicTorchesEnabled = isDynamicTorchesEnabledProp.getBoolean();
			shouldTorchesSwitchToGround = shouldTorchesMoveProp.getBoolean();
		} 
		catch (Exception e) 
		{
			// Failed reading/writing, just continue
		} 
		finally 
		{
			// Save props to config IF config changed
			if (config.hasChanged()) 
			{
				config.save();
			}
		}
	}
}
