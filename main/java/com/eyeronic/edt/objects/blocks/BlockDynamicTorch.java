package com.eyeronic.edt.objects.blocks;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import com.eyeronic.edt.DynamicTorches;
import com.eyeronic.edt.config.ModConfig;
import com.eyeronic.edt.init.BlockInit;
import com.eyeronic.edt.init.ItemInit;
import com.eyeronic.edt.util.IHasModel;
import com.eyeronic.edt.util.network.Messages;
import com.eyeronic.edt.util.network.PacketCleanUp;
import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class BlockDynamicTorch extends BlockTorch implements IHasModel {
	protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.6000000238418579D, 0.6000000238418579D);
    protected static final AxisAlignedBB TORCH_NORTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.699999988079071D, 0.6499999761581421D, 0.800000011920929D, 1.0D);
    protected static final AxisAlignedBB TORCH_SOUTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.0D, 0.6499999761581421D, 0.800000011920929D, 0.30000001192092896D);
    protected static final AxisAlignedBB TORCH_WEST_AABB = new AxisAlignedBB(0.699999988079071D, 0.20000000298023224D, 0.3499999940395355D, 1.0D, 0.800000011920929D, 0.6499999761581421D);
    protected static final AxisAlignedBB TORCH_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3499999940395355D, 0.30000001192092896D, 0.800000011920929D, 0.6499999761581421D);
    protected static final AxisAlignedBB TORCH_NORTH_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.6499999761581421D, 0.3499999940395355D, 0.800000011920929D, 1.0D);
    protected static final AxisAlignedBB TORCH_NORTH_WEST_AABB = new AxisAlignedBB(0.6499999761581421D, 0.20000000298023224D, 0.6499999761581421D, 1.0D, 0.800000011920929D, 1.0D);
    protected static final AxisAlignedBB TORCH_SOUTH_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.0D, 0.3499999940395355D, 0.800000011920929D, 0.3499999940395355D);
    protected static final AxisAlignedBB TORCH_SOUTH_WEST_AABB = new AxisAlignedBB(0.6499999761581421D, 0.20000000298023224D, 0.0D, 1.0D, 0.800000011920929D, 0.3499999940395355D);
	    
	public static final PropertyDirection EXTRA_FACING = PropertyDirection.create("extrafacing", new Predicate<EnumFacing>()
    {
        @Override
		public boolean apply(@Nullable EnumFacing p_apply_1_)
        {
            return p_apply_1_ != EnumFacing.DOWN;
        }
    });
	
	public BlockDynamicTorch() {
		super();
		setUnlocalizedName("minecraft:torch");
		setRegistryName("minecraft:torch");
		setLightLevel(0.9375F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP).withProperty(EXTRA_FACING, EnumFacing.UP));
		if(ModConfig.dynamicTorchesEnabled) {
		    BlockInit.BLOCKS.add(this);
			ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		}
    	MinecraftForge.EVENT_BUS.register(this);
	}
		
	@Override
	protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING, EXTRA_FACING});
	}
	
	@Override
	public void registerModels() {
		if(ModConfig.dynamicTorchesEnabled)
			DynamicTorches.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if(!ModConfig.dynamicTorchesEnabled)
				return getBoundingBoxVanilla(state);
		return getBoundingBoxModded(state);
	}
	
	private AxisAlignedBB getBoundingBoxVanilla(IBlockState state) {
		switch (state.getValue(FACING)) {
	        case EAST:
	            return TORCH_EAST_AABB;
	        case WEST:
	            return TORCH_WEST_AABB;
	        case SOUTH:
	            return TORCH_SOUTH_AABB;
	        case NORTH:
	            return TORCH_NORTH_AABB;
	        default:
	        	return STANDING_AABB;
		}
	}
	
	private AxisAlignedBB getBoundingBoxModded(IBlockState state) {
		switch(state.getValue(FACING))
        {
            case EAST:
            	switch(state.getValue(EXTRA_FACING)) {
					case NORTH:
						return TORCH_NORTH_EAST_AABB;
					case SOUTH:
						return TORCH_SOUTH_EAST_AABB;
					default:
						return TORCH_EAST_AABB;
            	}
            case WEST:
            	switch(state.getValue(EXTRA_FACING)) {
					case NORTH:
						return TORCH_NORTH_WEST_AABB;
					case SOUTH:
						return TORCH_SOUTH_WEST_AABB;
					default:
						return TORCH_WEST_AABB;
            	}
            case SOUTH:
            	switch(state.getValue(EXTRA_FACING)) {
					case EAST:
						return TORCH_SOUTH_EAST_AABB;
					case WEST:
						return TORCH_SOUTH_WEST_AABB;
					default:
						return TORCH_SOUTH_AABB;
            	}
            case NORTH:
            	switch(state.getValue(EXTRA_FACING)) {
					case EAST:
						return TORCH_NORTH_EAST_AABB;
					case WEST:
						return TORCH_NORTH_WEST_AABB;
					default:
						return TORCH_NORTH_AABB;
            	}
            default:
                return STANDING_AABB;
        }
	}

    @Override
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
    	EnumFacing facing = state.getValue(FACING), extrafacing = state.getValue(EXTRA_FACING);
    	
    	int meta = 0;
    	
    	switch(facing) {
			case DOWN:
				meta = 0;
				break;
			case UP:
				meta =  5;
				break;
			case NORTH:
				switch(extrafacing) {
					case WEST:
						meta =  6;
						break;
					case EAST:
						meta =  7;
						break;
					case UP:
					default:
						meta =  4;
						break;
					}
				break;
			case SOUTH:
				switch(extrafacing) {
					case WEST:
						meta =  8;
						break;
					case EAST:
						meta =  9;
						break;
					case UP:
					default:
						meta =  3;
						break;
					}
				break;
			case WEST:
				meta =  2;
				break;
			case EAST:
				meta =  1;
				break;
			default:
				meta =  5;
				break;
    	}
    	return meta;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	switch(meta) {
	    	case 0:
	    		return getDefaultState().withProperty(FACING, EnumFacing.DOWN).withProperty(EXTRA_FACING, EnumFacing.UP);
	    	case 5:
	    		return getDefaultState().withProperty(FACING, EnumFacing.UP).withProperty(EXTRA_FACING, EnumFacing.UP);
	    	case 2:
	    		return getDefaultState().withProperty(FACING, EnumFacing.WEST).withProperty(EXTRA_FACING, EnumFacing.UP);
	    	case 1:
	    		return getDefaultState().withProperty(FACING, EnumFacing.EAST).withProperty(EXTRA_FACING, EnumFacing.UP);
	    	case 6:
				return getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTRA_FACING, EnumFacing.WEST);
	    	case 7:
				return getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTRA_FACING, EnumFacing.EAST);
	    	case 4:
	    		return getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTRA_FACING, EnumFacing.UP);
	    	case 8:				
				return getDefaultState().withProperty(FACING, EnumFacing.SOUTH).withProperty(EXTRA_FACING, EnumFacing.WEST);
	    	case 9:
				return getDefaultState().withProperty(FACING, EnumFacing.SOUTH).withProperty(EXTRA_FACING, EnumFacing.EAST);
	    	case 3:
	    		return getDefaultState().withProperty(FACING, EnumFacing.SOUTH).withProperty(EXTRA_FACING, EnumFacing.UP);
    		default:
    			return getDefaultState();
    	}
    }
    
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
	    EnumFacing enumfacing = stateIn.getValue(FACING);
    	EnumFacing extrafacing = stateIn.getValue(EXTRA_FACING);
	    double d0 = pos.getX() + 0.5D;
	    double d1 = pos.getY() + 0.7D;
	    double d2 = pos.getZ() + 0.5D;
	    double d3 = 0.22D;
	    double d4 = 0.27D;
	
	    if (enumfacing.getAxis().isHorizontal())
	    {
	    	EnumFacing enumfacing1 = enumfacing.getOpposite();
	    	EnumFacing extrafacing1 = extrafacing.getOpposite();
			worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.27D * (enumfacing1.getFrontOffsetX() + extrafacing1.getFrontOffsetX()), d1 + 0.22D, d2 + 0.27D * (enumfacing1.getFrontOffsetZ() + extrafacing1.getFrontOffsetZ()), 0.0D, 0.0D, 0.0D);
	        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.27D * (enumfacing1.getFrontOffsetX() + extrafacing1.getFrontOffsetX()), d1 + 0.22D, d2 + 0.27D * (enumfacing1.getFrontOffsetZ() + extrafacing1.getFrontOffsetZ()), 0.0D, 0.0D, 0.0D);
	    }
	    else
	    {
	        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
	    }
	    
	    //TODO: react on load up if there is such a function, maybe implement update for first run of mod as well (when loading up vanilla worlds)
	    if(ModConfig.isCleanUp && !ModConfig.isFirstRun) {
	    	if(!stateIn.getValue(EXTRA_FACING).equals(EnumFacing.UP)) {
	    		resetToVanilla(pos, stateIn);
	    	}
	    }
	    else if(ModConfig.isFirstRun)
	    	if(stateIn.getValue(EXTRA_FACING).equals(EnumFacing.UP)) {
	    		setToDynamicPosition(worldIn, pos, stateIn);
	    	}
    }
    
    @Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		IBlockState state = getDefaultState();
		ArrayList<Pair<EnumFacing, EnumFacing>> validPairs = getValidFacings(world, pos);
		ArrayList<EnumFacing> validFacings = new ArrayList<EnumFacing>(), validExtraFacings = new ArrayList<EnumFacing>();
		
		for(Pair<EnumFacing, EnumFacing> pair : validPairs) {
			validFacings.add(pair.getLeft());
			validExtraFacings.add(pair.getRight());
		}
							
		switch(facing) {
			case EAST:
			{
				if(validFacings.contains(EnumFacing.NORTH) && hitZ > 0.65)
					return state.withProperty(FACING, EnumFacing.NORTH).withProperty(EXTRA_FACING, EnumFacing.EAST);
				else if(validFacings.contains(EnumFacing.SOUTH) && hitZ < 0.35)
					return state.withProperty(FACING, EnumFacing.SOUTH).withProperty(EXTRA_FACING, EnumFacing.EAST);
				else if(validFacings.contains(EnumFacing.EAST))
					return state.withProperty(FACING, EnumFacing.EAST).withProperty(EXTRA_FACING, EnumFacing.UP);
			}
			case WEST:
			{
				if(validFacings.contains(EnumFacing.NORTH) && hitZ > 0.65)
					return state.withProperty(FACING, EnumFacing.NORTH).withProperty(EXTRA_FACING, EnumFacing.WEST);
				else if(validFacings.contains(EnumFacing.SOUTH) && hitZ < 0.35)
					return state.withProperty(FACING, EnumFacing.SOUTH).withProperty(EXTRA_FACING, EnumFacing.WEST); 
				else if(validFacings.contains(EnumFacing.WEST))
					return state.withProperty(FACING, EnumFacing.WEST).withProperty(EXTRA_FACING, EnumFacing.UP);
			}
			case NORTH:
			{
				state = state.withProperty(FACING, EnumFacing.NORTH);
				if(validFacings.contains(EnumFacing.EAST) && hitX < 0.35)
					return state.withProperty(EXTRA_FACING, EnumFacing.EAST);
				else if(validFacings.contains(EnumFacing.WEST) && hitX > 0.65)
					return state.withProperty(EXTRA_FACING, EnumFacing.WEST);
				else if(validFacings.contains(EnumFacing.NORTH))
					return state;
			}
			case SOUTH:
			{
				state = state.withProperty(FACING, EnumFacing.SOUTH);
				if(validFacings.contains(EnumFacing.EAST) && hitX < 0.35)
					return state.withProperty(EXTRA_FACING, EnumFacing.EAST);
				else if(validFacings.contains(EnumFacing.WEST) && hitX > 0.65)
					return state.withProperty(EXTRA_FACING, EnumFacing.WEST);
				else if(validFacings.contains(EnumFacing.SOUTH))
					return state;
			}
			case DOWN:
			{
				state = state.withProperty(EXTRA_FACING, EnumFacing.UP);
				if(validFacings.contains(EnumFacing.NORTH) && hitZ > 0.65)
					return state.withProperty(FACING, EnumFacing.NORTH);
				else if(validFacings.contains(EnumFacing.SOUTH) && hitZ < 0.35)
					return state.withProperty(FACING, EnumFacing.SOUTH);
				else if(validFacings.contains(EnumFacing.EAST) && hitX < 0.35)
					return state.withProperty(FACING, EnumFacing.EAST);
				else if(validFacings.contains(EnumFacing.WEST) && hitX > 0.65)
					return state.withProperty(FACING, EnumFacing.WEST);
			}
			default:
				return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(EXTRA_FACING, EnumFacing.UP);//state.withProperty(FACING, validPairs.get(new Random().nextInt(validPairs.size())).getLeft()).withProperty(EXTRA_FACING, validPairs.get(new Random().nextInt(validPairs.size())).getRight());
		}		
	}
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    	IBlockState otherState = worldIn.getBlockState(fromPos);
		
		EnumFacing facing = (state.getValue(FACING));
		EnumFacing extraFacing = (state.getValue(EXTRA_FACING));
		ArrayList<Pair<EnumFacing, EnumFacing>> validPairs = getValidFacings(worldIn, pos);
		ArrayList<EnumFacing> validFacings = new ArrayList<EnumFacing>();
		
		for(Pair<EnumFacing,EnumFacing> pair : validPairs) {
			validFacings.add(pair.getLeft());
		}
		
		//if neighbor changed from solid to air or vice versa (blockIn is old state of neighbor, otherState is new state of neighbor) 
		if((blockIn.getBlockState().getBlock().equals(Blocks.AIR)) || (otherState.getBlock().equals(Blocks.AIR))) {		
			if(validPairs.isEmpty() || (validPairs.size() == 1 && EnumFacing.UP.equals(validPairs.get(0).getLeft()) && EnumFacing.UP.equals(validPairs.get(0).getRight()) && !ModConfig.shouldTorchesSwitchToGround))
				worldIn.destroyBlock(pos, true);
			else if(validPairs.size() == 1)
				setBlockState(worldIn, pos, state, validPairs.get(0).getLeft(), validPairs.get(0).getRight());
			else {
				switch(facing) {
				case NORTH:
					switch(extraFacing) {
					case EAST:
						if(fromPos.equals(pos.west()))
							extraFacing = EnumFacing.UP;
						else if(fromPos.equals(pos.south())) {
							facing = EnumFacing.EAST;
							extraFacing = EnumFacing.UP;
						}
						break;
					case UP:
						if(fromPos.equals(pos.west()) && validFacings.contains(EnumFacing.EAST))
							extraFacing = EnumFacing.EAST;
						else if(fromPos.equals(pos.east()) && validFacings.contains(EnumFacing.WEST))
							extraFacing = EnumFacing.WEST;
						else if(fromPos.equals(pos.south()))
							if(validFacings.contains(EnumFacing.EAST))
								facing = EnumFacing.EAST;
							else if(validFacings.contains(EnumFacing.SOUTH))
								facing = EnumFacing.SOUTH;
							else if(validFacings.contains(EnumFacing.WEST))
								facing = EnumFacing.WEST;
						break;
					case WEST:
						if(fromPos.equals(pos.east()))
							extraFacing = EnumFacing.UP;
						else if(fromPos.equals(pos.south())) {
							facing = EnumFacing.WEST;
							extraFacing = EnumFacing.UP;
						}
						break;
					default:
						break;
					}
					break;
				case EAST:
					switch(extraFacing) {
					case UP:
						if(fromPos.equals(pos.south()) && validFacings.contains(EnumFacing.NORTH)) {
							facing = EnumFacing.NORTH;
							extraFacing = EnumFacing.EAST;
						}
						else if(fromPos.equals(pos.north()) && validFacings.contains(EnumFacing.SOUTH)) {
							facing = EnumFacing.SOUTH;
							extraFacing = EnumFacing.EAST;
						}
						else if(fromPos.equals(pos.west()))
							if(validFacings.contains(EnumFacing.SOUTH))
								facing = EnumFacing.SOUTH;
							else if(validFacings.contains(EnumFacing.WEST))
								facing = EnumFacing.WEST;
							else if(validFacings.contains(EnumFacing.NORTH))
								facing = EnumFacing.NORTH;
						break;
					default:
						break;
					}
					break;
				case SOUTH:
					switch(extraFacing) {
					case EAST:
						if(fromPos.equals(pos.west()))
							extraFacing = EnumFacing.UP;
						else if(fromPos.equals(pos.north())) {
							facing = EnumFacing.EAST;
							extraFacing = EnumFacing.UP;
						}
						break;
					case UP:
						if(fromPos.equals(pos.west()) && validFacings.contains(EnumFacing.EAST))
							extraFacing = EnumFacing.EAST;
						else if(fromPos.equals(pos.east()) && validFacings.contains(EnumFacing.WEST))
							extraFacing = EnumFacing.WEST;
						else if(fromPos.equals(pos.north()))
							if(validFacings.contains(EnumFacing.WEST))
								facing = EnumFacing.WEST;
							else if(validFacings.contains(EnumFacing.NORTH))
								facing = EnumFacing.NORTH;
							else if(validFacings.contains(EnumFacing.EAST))
								facing = EnumFacing.EAST;
						break;
					case WEST:
						if(fromPos.equals(pos.east()))
							extraFacing = EnumFacing.UP;
						else if(fromPos.equals(pos.north())) {
							facing = EnumFacing.WEST;
							extraFacing = EnumFacing.UP;
						}
						break;
					default:
						break;
					}
					break;
				case WEST:
					switch(extraFacing) {
					case UP:
						if(fromPos.equals(pos.south()) && validFacings.contains(EnumFacing.NORTH)) {
							facing = EnumFacing.NORTH;
							extraFacing = EnumFacing.WEST;
						}
						else if(fromPos.equals(pos.north()) && validFacings.contains(EnumFacing.SOUTH)) {
							facing = EnumFacing.SOUTH;
							extraFacing = EnumFacing.WEST;
						}
						else if(fromPos.equals(pos.east()))
							if(validFacings.contains(EnumFacing.NORTH))
								facing = EnumFacing.NORTH;
							else if(validFacings.contains(EnumFacing.EAST))
								facing = EnumFacing.EAST;
							else if(validFacings.contains(EnumFacing.SOUTH))
								facing = EnumFacing.SOUTH;
						break;
					default:
						break;
					}
					break;
				case UP:
					switch(extraFacing) {
					case UP:
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
								
				setBlockState(worldIn, pos, state, facing, extraFacing);
			}
		}
    }

	private ArrayList<Pair<EnumFacing, EnumFacing>> getValidFacings(World worldIn, BlockPos pos) {
		ArrayList<Pair<EnumFacing, EnumFacing>> validFacings = new ArrayList<Pair<EnumFacing, EnumFacing>>();

		Pair<EnumFacing, EnumFacing> nu, ne, eu, se, su, sw, wu, nw, uu;
		nu = Pair.of(EnumFacing.NORTH, EnumFacing.UP);
		ne = Pair.of(EnumFacing.NORTH, EnumFacing.EAST);
		eu = Pair.of(EnumFacing.EAST, EnumFacing.UP);
		se = Pair.of(EnumFacing.SOUTH, EnumFacing.EAST);
		su = Pair.of(EnumFacing.SOUTH, EnumFacing.UP);
		sw = Pair.of(EnumFacing.SOUTH, EnumFacing.WEST);
		wu = Pair.of(EnumFacing.WEST, EnumFacing.UP);
		nw = Pair.of(EnumFacing.NORTH, EnumFacing.WEST);
		uu = Pair.of(EnumFacing.UP, EnumFacing.UP);
		
		validFacings.add(nu);
		validFacings.add(ne);
		validFacings.add(eu);
		validFacings.add(se);
		validFacings.add(su);
		validFacings.add(sw);
		validFacings.add(wu);
		validFacings.add(nw);
		validFacings.add(uu);
		
		if(!worldIn.getBlockState(pos.south()).isSideSolid(worldIn, pos.south(), EnumFacing.NORTH)) {
			validFacings.remove(nw);
			validFacings.remove(nu);
			validFacings.remove(ne);
		}
		if(!worldIn.getBlockState(pos.west()).isSideSolid(worldIn, pos.west(), EnumFacing.EAST)) {
			validFacings.remove(ne);
			validFacings.remove(eu);
			validFacings.remove(se);
		}
		if(!worldIn.getBlockState(pos.north()).isSideSolid(worldIn, pos.north(), EnumFacing.SOUTH)) {
			validFacings.remove(se);
			validFacings.remove(su);
			validFacings.remove(sw);
		}
		if(!worldIn.getBlockState(pos.east()).isSideSolid(worldIn, pos.east(), EnumFacing.WEST)) {
			validFacings.remove(sw);
			validFacings.remove(wu);
			validFacings.remove(nw);
		}
		if(!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP)) {
			validFacings.remove(uu);
		}
		
		return validFacings;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote && ModConfig.dynamicTorchesEnabled) {
			setToNextFacing(worldIn, pos, state, getValidFacings(worldIn, pos));
		}
		
		return true;
	}
	
	private void setToNextFacing(World worldIn, BlockPos pos, IBlockState state, ArrayList<Pair<EnumFacing, EnumFacing>> validFacings) {
		EnumFacing facing = state.getValue(FACING);
		EnumFacing extraFacing = state.getValue(EXTRA_FACING);
		
		Pair<EnumFacing, EnumFacing> currentPair = Pair.of(facing, extraFacing);
		int currentIndex = validFacings.indexOf(currentPair);
		
		if(currentIndex < validFacings.size()-1) {
			facing = validFacings.get(currentIndex+1).getLeft();
			extraFacing = validFacings.get(currentIndex+1).getRight();
		}
		else {
			facing = validFacings.get(0).getLeft();
			extraFacing = validFacings.get(0).getRight();
		}
		
		setBlockState(worldIn, pos, state, facing, extraFacing);
	}
	
	private void setBlockState(World worldIn, BlockPos pos, IBlockState state, EnumFacing facing, EnumFacing extraFacing) {
		worldIn.setBlockState(pos, state.withProperty(FACING, facing).withProperty(EXTRA_FACING, extraFacing));
	}
	
	private void resetToVanilla(BlockPos pos, IBlockState state) {
		DynamicTorches.log("Resetting to vanilla position");
		Messages.INSTANCE.sendToServer(new PacketCleanUp(pos, state, state.getValue(FACING), EnumFacing.UP));
	}

	private void setToDynamicPosition(World world, BlockPos pos, IBlockState state) {
		ArrayList<Pair<EnumFacing, EnumFacing>> validPairs = getValidFacings(world, pos);
		
		EnumFacing facing = state.getValue(FACING), extrafacing = state.getValue(EXTRA_FACING);
		Pair<EnumFacing, EnumFacing> nu = Pair.of(EnumFacing.NORTH, EnumFacing.UP),
									 su = Pair.of(EnumFacing.SOUTH, EnumFacing.UP),
									 wu = Pair.of(EnumFacing.WEST, EnumFacing.UP),
									 eu = Pair.of(EnumFacing.EAST, EnumFacing.UP),
									 
									 currentPair = Pair.of(facing, extrafacing);
		
		int currentIndex = validPairs.indexOf(currentPair);
		
		ArrayList<EnumFacing> validFacings = new ArrayList<EnumFacing>();
		for(Pair<EnumFacing, EnumFacing> pair : validPairs)
			validFacings.add(pair.getLeft());
		
		switch(facing) {
		case EAST:
			if(validFacings.contains(EnumFacing.NORTH)) {
				facing = EnumFacing.NORTH;
				extrafacing = EnumFacing.EAST;
			}
			else if(validFacings.contains(EnumFacing.SOUTH)) {
				facing = EnumFacing.SOUTH;
				extrafacing = EnumFacing.EAST;
			}
			break;
		case NORTH:
			if(validFacings.contains(EnumFacing.EAST)) {
				extrafacing = EnumFacing.EAST;
			}
			else if(validFacings.contains(EnumFacing.WEST)) {
				extrafacing = EnumFacing.WEST;
			}
			break;
		case SOUTH:
			if(validFacings.contains(EnumFacing.EAST)) {
				extrafacing = EnumFacing.EAST;
			}
			else if(validFacings.contains(EnumFacing.WEST)) {
				extrafacing = EnumFacing.WEST;
			}
			break;
		case WEST:
			if(validFacings.contains(EnumFacing.NORTH)) {
				facing = EnumFacing.NORTH;
				extrafacing = EnumFacing.WEST;
			}
			else if(validFacings.contains(EnumFacing.SOUTH)) {
				facing = EnumFacing.SOUTH;
				extrafacing = EnumFacing.WEST;
			}
		default:
			break;
		}
		
		if(!extrafacing.equals(EnumFacing.UP)) {
			DynamicTorches.log("Moving to corner position.");
			Messages.INSTANCE.sendToServer(new PacketCleanUp(pos, state, facing, extrafacing));
		}
	}
}
