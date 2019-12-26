package com.eyeronic.edt.util.network;

import com.eyeronic.edt.DynamicTorches;
import com.eyeronic.edt.objects.blocks.BlockDynamicTorch;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketCleanUp implements IMessage{
	private boolean isCleanUpActive = false;
	private BlockPos pos;
	private IBlockState state;
	private EnumFacing facing, extraFacing;
	
	public PacketCleanUp() {
		// TODO Auto-generated constructor stub
	}
	
	public PacketCleanUp(BlockPos pos, IBlockState state, EnumFacing facing, EnumFacing extraFacing) {
		//this.isCleanUpActive = isCleanUpActive;
		this.pos = pos;
		this.state = state;
		this.facing = facing;
		this.extraFacing = extraFacing;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		//isCleanUpActive = buf.readBoolean();
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		pos = new BlockPos(x, y, z);
		facing = EnumFacing.values()[buf.readInt()];
		extraFacing = EnumFacing.values()[buf.readInt()];
	}

	@Override
	public void toBytes(ByteBuf buf) {
		//buf.writeBoolean(isCleanUpActive);
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(facing.getIndex());
		buf.writeInt(extraFacing.getIndex());
	}

	public boolean isCleanUpActive() {
		return isCleanUpActive;
	}

	public void setCleanUpActive(boolean isCleanUpActive) {
		this.isCleanUpActive = isCleanUpActive;
	}

	public BlockPos getPos() {
		return pos;
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
	}

	public EnumFacing getFacing() {
		return facing;
	}

	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}

	public EnumFacing getExtraFacing() {
		return extraFacing;
	}

	public void setExtraFacing(EnumFacing extraFacing) {
		this.extraFacing = extraFacing;
	}

	public static class CleanUpMessageHandler implements IMessageHandler<PacketCleanUp, IMessage> {
		@Override
		public IMessage onMessage(PacketCleanUp message, MessageContext ctx) {
			//DynamicTorches.proxy.addScheduledTaskClient(() -> handle(message, ctx));
			handle(message, ctx);
			return null;
		}
		
		private void handle(PacketCleanUp message, MessageContext ctx) {
			if(ctx.side == Side.SERVER) {
				DynamicTorches.log("Message recieved server side! Pos: " + message.getPos() + ", Facing: " + message.getFacing() + ", ExtraFacing: " + message.getExtraFacing());
				int playerPermissionLevel = ctx.getServerHandler().player.getServer().getPlayerList().getOppedPlayers().getPermissionLevel(ctx.getServerHandler().player.getGameProfile());
				DynamicTorches.log("opped playerlist length: " + ctx.getServerHandler().player.getServer().getPlayerList().getOppedPlayerNames().length + "\npermission level for player " + ctx.getServerHandler().player + " is " + playerPermissionLevel);
				if(playerPermissionLevel >= 1 || ctx.getServerHandler().player.getServer().getServerOwner().equals(ctx.getServerHandler().player.getName())) {
					ctx.getServerHandler().player.world.setBlockState(message.getPos(), ctx.getServerHandler().player.world.getBlockState(message.getPos()).withProperty(BlockTorch.FACING, message.getFacing()).withProperty(BlockDynamicTorch.EXTRA_FACING,  message.getExtraFacing()));
				}
				else
					return;
			}		
			//client side pretty much does nothing, as setblock is handled server side only
			else {
				DynamicTorches.log("Message recieved client side! Pos: " + message.getPos() + ", Facing: " + message.getFacing() + ", ExtraFacing: " + message.getExtraFacing());
			}
		}
	}
}
