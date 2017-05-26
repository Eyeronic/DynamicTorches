package eyeronic.edt;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import eyeronic.edt.init.DynamicTorches;

public class DTRenderBlocks extends RenderBlocks {
	
	public DTRenderBlocks(IBlockAccess blockAccess) {
		super(blockAccess);
	}
	
	@Override
	/**
	 * @see net.minecraft.client.renderer.RenderBlocks#renderBlockByRenderType(net.minecraft.block.Block, int, int, int)
	 */
	public boolean renderBlockByRenderType(Block block, int x, int y, int z) 
	{
		int modelID = block.getRenderType();
		
		if(DynamicTorches.dtRenderID == modelID)
		{
			return this.renderBlockTorch(block, x, y, z);
		}
		
		return super.renderBlockByRenderType(block, x, y, z);
	}
	
	@Override
	/**
	 * @see net.minecraft.client.renderer.RenderBlocks#renderBlockTorch(net.minecraft.block.Block, int, int, int)
	 */
	public boolean renderBlockTorch(Block block, int x, int y, int z)
    {		
        int metadata = this.blockAccess.getBlockMetadata(x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, x, y, z));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        double d0 = 0.4000000059604645D;
        double d1 = 0.5D - d0;
        double d2 = 0.20000000298023224D;

        //North-East
		if (metadata == 6)
        {
            this.renderTorchAtAngle(block, (double)x - d1, (double)y + d2, (double)z + d1, -d0, d0, 0);
        }
        //North-West
        else if (metadata == 7)
        {
            this.renderTorchAtAngle(block, (double)x + d1, (double)y + d2, (double)z + d1, d0, d0, 0);
        }
        //South-East
        else if (metadata == 8)
        {
            this.renderTorchAtAngle(block, (double)x - d1, (double)y + d2, (double)z - d1, -d0, -d0, 0);
        }
        //South-West
        else if (metadata == 9)
        {
            this.renderTorchAtAngle(block, (double)x + d1, (double)y + d2, (double)z - d1, d0, -d0, 0);
        }
        else
        {
        	if(metadata == 0)
        	{
        		
        	}
        	//System.out.println("RenderBlockTorch with metadata: " + metadata + " @ " + x + ", " + y + ", " + z);
        	return super.renderBlockTorch(block, x, y, z);
        }
        
        return true;
    }
}
