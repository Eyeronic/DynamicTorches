package eyeronic.edt;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eyeronic.edt.init.DynamicTorches;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DTBlock extends BlockTorch {

	private int renderType;

	public DTBlock() 
	{
		//TODO: setters really needed?
		super();
		System.out.println(Material.circuits == getMaterial());
		this.setLightLevel(super.getAmbientOcclusionLightValue());
		this.setTickRandomly(true);
		this.setRenderType(DynamicTorches.dtRenderID);
	}

	/**
	 * Checks if a torch can be placed in the given world on top of another block at given position.
	 * 
	 * @param world World object the block will be placed in.
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 * 
	 * @return true if the block at coordinates has a solid top or you can place a torch on top of it, else false.
	 */
	private boolean canPlaceOnTop(World world, int x, int y, int z)
	{
		if (World.doesBlockHaveSolidTopSurface(world, x, y, z))
		{
			return true;
		}
		else
		{
			Block block = world.getBlock(x, y, z);
			return block.canPlaceTorchOnTop(world, x, y, z);
		}
	}

	@Override
	/**
	 * Returns RenderType of this block.
	 * 
	 * -1: No Render (Signs, pure Tile Entities)
	 *  0: Standard Blocks
	 *  1: Flower, Reed (crossed sprites)
	 *  2: Torch
	 *  3: Fire
	 *  4: Fluids
	 *  5: RedstoneWire
	 *  6: Crops
	 *  7: Door
	 *  8: Ladder
	 *  9: MinecartTrack
	 * 10: Stairs
	 * 11: Fence
	 * 12: Lever
	 * 13: Cactus
	 * 14: Bed
	 * 15: RedstoneRepeater 
	 * 16: Piston Base
	 * 17: Piston Extension
	 * 18: Panes (iron bars and glass panes)
	 * 19: Melon and Pumpkin stems
	 * 20: Vines
	 * 21: Fence gates
	 * 22: Chests
	 * 23: LilyPads
	 * 24: Cauldrons
	 * 25: Brewing stands
	 * 26: End Portal Frames
	 * 27: DragonEgg
	 * 28: Cocoa
	 * 29: TripWire source
	 * 30: TripWire
	 * 31: Logs
	 * 32: Wall
	 * 33: FlowerPot
	 * 34: Beacon
	 * 35: Anvil
	 * 36: RedstoneDiode
	 * 37: RedstoneComparator
	 * 38: Hopper
	 * 39: Quartz
	 * 40: DoublePlant
	 * 41: StainedGlassPane
	 *  
	 */
	public int getRenderType() 
	{
		return this.renderType;
	}

	/**
	 * Changes RenderType of this block.
	 * 
	 * -1: No Render (Signs, pure Tile Entities)
	 *  0: Standard Blocks
	 *  1: Flower, Reed (crossed sprites)
	 *  2: Torch
	 *  3: Fire
	 *  4: Fluids
	 *  5: RedstoneWire
	 *  6: Crops
	 *  7: Door
	 *  8: Ladder
	 *  9: MinecartTrack
	 * 10: Stairs
	 * 11: Fence
	 * 12: Lever
	 * 13: Cactus
	 * 14: Bed
	 * 15: RedstoneRepeater 
	 * 16: Piston Base
	 * 17: Piston Extension
	 * 18: Panes (iron bars and glass panes)
	 * 19: Melon and Pumpkin stems
	 * 20: Vines
	 * 21: Fence gates
	 * 22: Chests
	 * 23: LilyPads
	 * 24: Cauldrons
	 * 25: Brewing stands
	 * 26: End Portal Frames
	 * 27: DragonEgg
	 * 28: Cocoa
	 * 29: TripWire source
	 * 30: TripWire
	 * 31: Logs
	 * 32: Wall
	 * 33: FlowerPot
	 * 34: Beacon
	 * 35: Anvil
	 * 36: RedstoneDiode
	 * 37: RedstoneComparator
	 * 38: Hopper
	 * 39: Quartz
	 * 40: DoublePlant
	 * 41: StainedGlassPane
	 *  
	 */
	public void setRenderType(int newRenderType)
	{
		this.renderType = newRenderType;
	}

	@Override
	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		int j1 = metadata;

		//placing on top and placeable on top of given block
		if (side == 1 && this.canPlaceOnTop(world, x, y - 1, z))
		{
			j1 = 5;
		}

		//placing on north side of given block and north side is solid
		if (side == 2 && world.isSideSolid(x, y, z + 1, NORTH, true))
		{
			//North-East
			if(world.isSideSolid(x - 1, y, z, EAST, true))
			{
				j1 = 6;
			}
			//North-West
			else if(world.isSideSolid(x + 1, y, z, WEST, true))
			{
				j1 = 7;
			}
			//North
			else 
			{
				j1 = 4;
			}
		}

		//placing on south side of given block and south side is solid
		if (side == 3 && world.isSideSolid(x, y, z - 1, SOUTH, true))
		{
			//South-East
			if(world.isSideSolid(x - 1, y, z, EAST, true))
			{
				j1 = 8;
			}
			//South-West
			else if(world.isSideSolid(x + 1, y, z, WEST, true))
			{
				j1 = 9;
			}
			//South
			else
			{
				j1 = 3;
			}
		}

		//placing on west side of given block and west side is solid
		if (side == 4 && world.isSideSolid(x + 1, y, z, WEST, true))
		{
			//North-West
			if(world.isSideSolid(x, y, z + 1, NORTH, true))
			{
				j1 = 7;
			}
			//South-West
			else if(world.isSideSolid(x, y, z - 1, SOUTH, true))
			{
				j1 = 9;
			}
			//West
			else 
			{
				j1 = 2;
			}
		}

		//placing on east side of given block and east side is solid
		if (side == 5 && world.isSideSolid(x - 1, y, z, EAST, true))
		{
			//North-East
			if(world.isSideSolid(x, y, z + 1, NORTH, true))
			{
				j1 = 6;
			}
			//South-East
			else if(world.isSideSolid(x, y, z - 1, SOUTH, true))
			{
				j1 = 8;
			}
			//East
			else 
			{
				j1 = 1;
			}
		}
		
		return j1;
	}

	@Override
	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World world, int x, int y, int z)
	{
		if (world.getBlockMetadata(x, y, z) != 0)
		{
			this.updateTorchPosition(world, x, y, z);
		}
		else
		{
			this.moveTorchAutomatically(world, x, y, z);
		}
	}

	/**
	 * Called when neighbor block of a torch changes.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	private void updateTorchPosition(World world, int x, int y, int z) {
		//South-East
		if (world.isSideSolid(x - 1, y, z, EAST, true) && world.isSideSolid(x, y, z - 1, SOUTH, true) && world.getBlockMetadata(x, y, z) != 5)
		{
			world.setBlockMetadataWithNotify(x, y, z, 8, 2);
		}
		//South-West
		else if (world.isSideSolid(x + 1, y, z, WEST, true) && world.isSideSolid(x, y, z - 1, SOUTH, true) && world.getBlockMetadata(x, y, z) != 5)
		{
			world.setBlockMetadataWithNotify(x, y, z, 9, 2);
		}
		//North-West
		else if (world.isSideSolid(x, y, z + 1, NORTH, true) && world.isSideSolid(x + 1, y, z, WEST, true) && world.getBlockMetadata(x, y, z) != 5)
		{
			world.setBlockMetadataWithNotify(x, y, z, 7, 2);
		}
		//North-East
		else if (world.isSideSolid(x, y, z + 1, NORTH, true) && world.isSideSolid(x - 1, y, z, EAST, true) && world.getBlockMetadata(x, y, z) != 5)
		{
			world.setBlockMetadataWithNotify(x, y, z, 6, 2);
		}
		else if (world.isSideSolid(x - 1, y, z, EAST, true) && world.getBlockMetadata(x, y, z) != 5)
		{
			world.setBlockMetadataWithNotify(x, y, z, 1, 2);
		}
		else if (world.isSideSolid(x + 1, y, z, WEST, true) && world.getBlockMetadata(x, y, z) != 5)
		{
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}
		else if (world.isSideSolid(x, y, z - 1, SOUTH, true) && world.getBlockMetadata(x, y, z) != 5)
		{
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}
		else if (world.isSideSolid(x, y, z + 1, NORTH, true) && world.getBlockMetadata(x, y, z) != 5)
		{
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
		else if (this.canPlaceOnTop(world, x, y - 1, z))
		{
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}
		else
		{
			System.out.println("Calling super.onBlockAdded");
			super.onBlockAdded(world, x, y, z);
		}
	}

	/**
	 * Called when block metadata at x, y, z is 0 (aka newly placed torch or 
	 * in case torches should automatically move to another valid position)
	 * 
	 * TODO: better name
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	private void moveTorchAutomatically(World world, int x, int y, int z) 
	{
		//South-East
		if (world.isSideSolid(x - 1, y, z, EAST, true) && world.isSideSolid(x, y, z - 1, SOUTH, true))
		{
			world.setBlockMetadataWithNotify(x, y, z, 8, 2);
		}
		//South-West
		else if (world.isSideSolid(x + 1, y, z, WEST, true) && world.isSideSolid(x, y, z - 1, SOUTH, true))
		{
			world.setBlockMetadataWithNotify(x, y, z, 9, 2);
		}
		//North-West
		else if (world.isSideSolid(x, y, z + 1, NORTH, true) && world.isSideSolid(x + 1, y, z, WEST, true))
		{
			world.setBlockMetadataWithNotify(x, y, z, 7, 2);
		}
		//North-East
		else if (world.isSideSolid(x, y, z + 1, NORTH, true) && world.isSideSolid(x - 1, y, z, EAST, true))
		{
			world.setBlockMetadataWithNotify(x, y, z, 6, 2);
		}
		else if (world.isSideSolid(x - 1, y, z, EAST, true))
		{
			world.setBlockMetadataWithNotify(x, y, z, 1, 2);
		}
		else if (world.isSideSolid(x + 1, y, z, WEST, true))
		{
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}
		else if (world.isSideSolid(x, y, z - 1, SOUTH, true))
		{
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}
		else if (world.isSideSolid(x, y, z + 1, NORTH, true))
		{
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
		else if (this.canPlaceOnTop(world, x, y - 1, z))
		{
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}
		else
		{
			System.out.println("Calling super.onBlockAdded.");
			super.onBlockAdded(world, x, y, z);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

		//should torches drop or move to another valid position?

		//true if torches should not move to any valid position and should not drop right now
		if(!DynamicTorches.shouldTorchesSwitchToGround && !this.func_150108_b(world, x, y, z, block) && !this.getClass().equals(block.getClass())) 
		{
			this.onBlockAdded(world, x, y, z);			
		}
		else if(!this.getClass().equals(block.getClass()))//true if torches should move to any valid position
		{
			//resetting metadata to 0 for rotation update
			//world.setBlockMetadataWithNotify(x, y, z, 0, 2);
			this.onBlockAdded(world, x, y, z);
		}

		//resetting available torch positions to choose from as these might have changed
		DynamicTorches.instance.resetMetadataOptions(world, x, y, z);
	}	

	/**
	 * Called when the player is sneaking and right clicking a torch. Moves the torch to the next available position.
	 * 
	 * @param world the current world
	 * @param x x coordinate of the torch
	 * @param y y coordinate of the torch
	 * @param z z coordinate of the torch
	 * @param metadataOptions the array of available positions the torch can be placed at
	 */
	public void moveTorchManually(World world, int x, int y, int z, int[] metadataOptions)
	{
		int nextMetadata = getNextMetadata(metadataOptions, metadataOptions[9]);

		world.setBlockMetadataWithNotify(x, y, z, nextMetadata, 2);
		DynamicTorches.instance.updateOptions(world, x, y, z);
	}

	public int getMixedBrightnessForBlock(IBlockAccess blockAccess, int x, int y, int z)
	{
		Block block = blockAccess.getBlock(x, y, z);

		/*System.out.println(blockAccess);
		System.out.println(block);
		System.out.println(blockAccess.getBlockMetadata(x, y, z));

		//TODO: Use ChunkCoords from DynamicTorches.onChunkEntered to redraw torches when crossing chunk borders?
		System.out.println("Block: " + block);
		if(blockAccess.getBlockMetadata(x, y, z) == 0 && block.getClass().equals(this.getClass()))
		{
			System.out.println("METADATA IS 0!");
		}*/
		int l;

		if(!block.getClass().equals(BlockAir.class))
			l = blockAccess.getLightBrightnessForSkyBlocks(x, y, z, block.getLightValue(blockAccess, x, y, z));//240;//EnumSkyBlock.Sky.defaultLightValue;
		else
		{
			l = 0;
		}

		if (l == 0 && block instanceof BlockSlab)
		{
			--y;
			block = blockAccess.getBlock(x, y, z);
			return blockAccess.getLightBrightnessForSkyBlocks(x, y, z, block.getLightValue(blockAccess, x, y, z));
		}
		else
		{
			return l;
		}
	}

	/**
	 * Returns the next available position for the given options and the current position
	 * 
	 * @param metadataOptions the available positions
	 * @param currentMetadata the current torch position (which is equal to the next position in metadataOptions[])
	 * @return the next available position (alias next metadata value the torch should be given)
	 */
	private int getNextMetadata(int[] metadataOptions, int currentMetadata)
	{		
		if(metadataOptions[currentMetadata] != 0 && currentMetadata != 9)
			return metadataOptions[currentMetadata];
		else if(currentMetadata != 9)
			return getNextMetadata(metadataOptions, currentMetadata+1);
		else
			return getNextMetadata(metadataOptions, 0);

	}

	/**
	 * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
	 * x, y, z, startVec, endVec
	 */
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec)
	{
		int l = world.getBlockMetadata(x, y, z);
		float f = 0.15F;

		//North-East
		if (l == 6)
		{
			this.setBlockBounds(0.0F, 0.2F, 1.0F - 2.0F * f, 2.0F * f, 0.8F, 1.0F);
		}
		//North-West
		else if (l == 7)
		{
			this.setBlockBounds(1.0F - 2.0F * f, 0.2F, 1.0F - 2.0F * f, 1.0F, 0.8F, 1.0F);
		}
		//South-East
		else if (l == 8)
		{
			this.setBlockBounds(0.0F, 0.2F, 0.0F, 2.0F * f, 0.8F, 2.0F * f);
		}
		//South-West
		else if (l == 9)
		{
			this.setBlockBounds(1.0F - 2.0F * f, 0.2F, 0.0F, 1.0F, 0.8F, 2.0F * f);
		}
		//Single Side
		else
		{
			return super.collisionRayTrace(world, x, y, z, startVec, endVec);
		}

		return this.basicCollisionRayTrace(world, x, y, z, startVec, endVec);
	}

	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random random)
	{
		int metadata = world.getBlockMetadata(x, y, z);
		double d0 = (double)((float)x + 0.5F);
		double d1 = (double)((float)y + 0.7F);
		double d2 = (double)((float)z + 0.5F);
		double d3 = 0.2199999988079071D;
		double d4 = 0.27000001072883606D;

		if (metadata == 6)
		{
			world.spawnParticle("smoke", d0 - d4, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", d0 - d4, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
		}
		else if (metadata == 7)
		{
			world.spawnParticle("smoke", d0 + d4, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", d0 + d4, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
		}
		else if (metadata == 8)
		{
			world.spawnParticle("smoke", d0 - d4, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", d0 - d4, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
		}
		else if (metadata == 9)
		{
			world.spawnParticle("smoke", d0 + d4, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
			world.spawnParticle("flame", d0 + d4, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
		}
		else
		{
			super.randomDisplayTick(world, x, y, z, random);
		}
	}

	/**
	 * Copy from Block to circumvent BlockTorch.collisionRayTrace.
	 * 
	 * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
	 * x, y, z, startVec, endVec
	 */
	private MovingObjectPosition basicCollisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec)
	{
		this.setBlockBoundsBasedOnState(world, x, y, z);
		startVec = startVec.addVector((double)(-x), (double)(-y), (double)(-z));
		endVec = endVec.addVector((double)(-x), (double)(-y), (double)(-z));
		Vec3 vec32 = startVec.getIntermediateWithXValue(endVec, this.minX);
		Vec3 vec33 = startVec.getIntermediateWithXValue(endVec, this.maxX);
		Vec3 vec34 = startVec.getIntermediateWithYValue(endVec, this.minY);
		Vec3 vec35 = startVec.getIntermediateWithYValue(endVec, this.maxY);
		Vec3 vec36 = startVec.getIntermediateWithZValue(endVec, this.minZ);
		Vec3 vec37 = startVec.getIntermediateWithZValue(endVec, this.maxZ);

		if (!this.isVecInsideYZBounds(vec32))
		{
			vec32 = null;
		}

		if (!this.isVecInsideYZBounds(vec33))
		{
			vec33 = null;
		}

		if (!this.isVecInsideXZBounds(vec34))
		{
			vec34 = null;
		}

		if (!this.isVecInsideXZBounds(vec35))
		{
			vec35 = null;
		}

		if (!this.isVecInsideXYBounds(vec36))
		{
			vec36 = null;
		}

		if (!this.isVecInsideXYBounds(vec37))
		{
			vec37 = null;
		}

		Vec3 vec38 = null;

		if (vec32 != null && (vec38 == null || startVec.squareDistanceTo(vec32) < startVec.squareDistanceTo(vec38)))
		{
			vec38 = vec32;
		}

		if (vec33 != null && (vec38 == null || startVec.squareDistanceTo(vec33) < startVec.squareDistanceTo(vec38)))
		{
			vec38 = vec33;
		}

		if (vec34 != null && (vec38 == null || startVec.squareDistanceTo(vec34) < startVec.squareDistanceTo(vec38)))
		{
			vec38 = vec34;
		}

		if (vec35 != null && (vec38 == null || startVec.squareDistanceTo(vec35) < startVec.squareDistanceTo(vec38)))
		{
			vec38 = vec35;
		}

		if (vec36 != null && (vec38 == null || startVec.squareDistanceTo(vec36) < startVec.squareDistanceTo(vec38)))
		{
			vec38 = vec36;
		}

		if (vec37 != null && (vec38 == null || startVec.squareDistanceTo(vec37) < startVec.squareDistanceTo(vec38)))
		{
			vec38 = vec37;
		}

		if (vec38 == null)
		{
			return null;
		}
		else
		{
			byte b0 = -1;

			if (vec38 == vec32)
			{
				b0 = 4;
			}

			if (vec38 == vec33)
			{
				b0 = 5;
			}

			if (vec38 == vec34)
			{
				b0 = 0;
			}

			if (vec38 == vec35)
			{
				b0 = 1;
			}

			if (vec38 == vec36)
			{
				b0 = 2;
			}

			if (vec38 == vec37)
			{
				b0 = 3;
			}

			return new MovingObjectPosition(x, y, z, b0, vec38.addVector((double)x, (double)y, (double)z));
		}
	}

	/**
	 * Copy from Block
	 * 
	 * Checks if a vector is within the Y and Z bounds of the block.
	 */
	private boolean isVecInsideYZBounds(Vec3 vec)
	{
		return vec == null ? false : vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
	}

	/**
	 * Copy from Block
	 * 
	 * Checks if a vector is within the X and Z bounds of the block.
	 */
	private boolean isVecInsideXZBounds(Vec3 vec)
	{
		return vec == null ? false : vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
	}

	/**
	 * Copy from Block
	 * 
	 * Checks if a vector is within the X and Y bounds of the block.
	 */
	private boolean isVecInsideXYBounds(Vec3 vec)
	{
		return vec == null ? false : vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY;
	}
}
