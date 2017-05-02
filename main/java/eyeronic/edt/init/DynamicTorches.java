package eyeronic.edt.init;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
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
	public static boolean isFirstRun = true;

	@SidedProxy(clientSide = "eyeronic.edt.proxy.ClientProxy", serverSide = "eyeronic.edt.proxy.ServerProxy")
	public static ServerProxy proxy;

	@Mod.Instance(Reference.MOD_ID)
	public static DynamicTorches instance;
	
	private int[] metadataHistory = {0, 0, 0, 0};

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
		MinecraftForge.EVENT_BUS.register(instance);

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
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		Action action = event.action;
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		Block block = world.getBlock(x, y, z);
		
		if(player.isSneaking() && action.equals(Action.RIGHT_CLICK_BLOCK))
		{
			System.out.println("sneaky right click");
			if(block.getClass().equals(DTBlock.class))
			{
				((DTBlock) block).moveTorchManually(world, x, y, z);
				event.setCanceled(true);
			}
		}
	}
	
	//TODO: probably different data structure
	//TODO: increase history? 9 possible states --> check maximum of 8 previous metadata values for new metadata value not present
	//TODO: Reset metadata if new torch is clicked? check world position to find out if clicked block changed
	public void updateMetadataHistory(int metadata)
	{
		System.out.println("Old History: " + metadataHistory[0] + ", " + metadataHistory[1] + ", " + metadataHistory[2] + ", " + metadataHistory[3]);
		int[] tmpHistory = new int[4];
		for(int i = 0; i<3; i++)
		{
			tmpHistory[i] = metadataHistory[i+1];
		}
		tmpHistory[3] = metadata;
		metadataHistory = tmpHistory;
		System.out.println("New History: " + metadataHistory[0] + ", " + metadataHistory[1] + ", " + metadataHistory[2] + ", " + metadataHistory[3]);
	}
	
	public int[] getMetadataHistory()
	{
		return metadataHistory;
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
			
			Property isFirstRunProp =
					config.get(Configuration.CATEGORY_SPLITTER + "first run",
							"isFirstRun",
							"true",
							"Whether this is the first time this mod is used",
							Property.Type.BOOLEAN);

			// Get the boolean value, also set the property value to boolean
			dynamicTorchesEnabled = isDynamicTorchesEnabledProp.getBoolean();
			shouldTorchesSwitchToGround = shouldTorchesMoveProp.getBoolean();
			isFirstRun = isFirstRunProp.getBoolean();
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
