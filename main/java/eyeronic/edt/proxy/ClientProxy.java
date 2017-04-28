package eyeronic.edt.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import eyeronic.edt.DTBlockRenderingHandler;
import eyeronic.edt.init.DynamicTorches;

public class ClientProxy extends ServerProxy {

	//TODO: remove TESR, no parameters
	public void registerRenderThings() 
	{ 
		RenderingRegistry.registerBlockHandler(DynamicTorches.dtRenderID, new DTBlockRenderingHandler());
		
		/*TileEntitySpecialRenderer torchRenderer = new CTRenderer(new ResourceLocation("ect:textures/items/itemCornerTorch.png"));
		ClientRegistry.bindTileEntitySpecialRenderer(CTTileEntity.class, torchRenderer);
		MinecraftForgeClient.registerItemRenderer(itemCornerTorch, new CTItemRenderer(torchRenderer, new CTTileEntity()));*/
	} 

	//TODO: use for TESR, maybe no parameters
	public void registerTileEntitySpecialRenderer() 
	{ 
		/*TileEntitySpecialRenderer torchRenderer = new CTRenderer(new ResourceLocation("ect:textures/items/itemCornerTorch.png"));
		ClientRegistry.bindTileEntitySpecialRenderer(CTTileEntity.class, torchRenderer);
		MinecraftForgeClient.registerItemRenderer(itemCornerTorch, new CTItemRenderer(torchRenderer, new CTTileEntity()));*/
	}
}
