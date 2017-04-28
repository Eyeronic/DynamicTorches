package eyeronic.edt;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import eyeronic.edt.init.DynamicTorches;

public class DTBlockRenderingHandler implements ISimpleBlockRenderingHandler {
	private DTRenderBlocks ctRenderer;

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer)
	{
		//CTBlock
		if(block != null && block.getClass().equals(DTBlock.class))
		{
			DTBlock ctBlock = ((DTBlock) block);

			if(ctRenderer == null)
			{
				ctRenderer = new DTRenderBlocks(world);
			}
			else if(DTRenderBlocks.class.equals(renderer.getClass()))
			{
				ctRenderer = (DTRenderBlocks) renderer;
			}

			return ctRenderer.renderBlockByRenderType(ctBlock, x, y, z);
		}
		//different type of block
		else
		{				
			return new RenderBlocks(world).renderBlockByRenderType(block, x, y, z);
		}

	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRenderId() 
	{
		return DynamicTorches.dtRenderID;
	}

}
