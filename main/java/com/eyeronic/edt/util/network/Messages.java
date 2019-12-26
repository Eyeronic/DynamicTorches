package com.eyeronic.edt.util.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Messages extends SimpleNetworkWrapper {
	public static SimpleNetworkWrapper INSTANCE;
	
	private static int ID = 0;
	private static int nextID() { return ID++; }
	
	public Messages(String channelName) {
		super(channelName);
		// TODO Auto-generated constructor stub
	}

	public static void registerMessages(String channelName) {
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		
		//Server Side
		INSTANCE.registerMessage(PacketCleanUp.CleanUpMessageHandler.class, PacketCleanUp.class, nextID(), Side.SERVER);
		
		//Client Side
		INSTANCE.registerMessage(PacketCleanUp.CleanUpMessageHandler.class, PacketCleanUp.class, nextID(), Side.CLIENT);
	}
}
