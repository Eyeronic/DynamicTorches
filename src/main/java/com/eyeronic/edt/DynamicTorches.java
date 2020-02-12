package com.eyeronic.edt;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.Logger;

import com.eyeronic.edt.proxy.CommonProxy;
import com.eyeronic.edt.util.Reference;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, canBeDeactivated = Reference.CAN_BE_DEACTIVATED)
public class DynamicTorches
{
    private static Logger logger;
    
    @Instance
    public static DynamicTorches instance;
    
    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	
    }

	public static void log(String string) {
		logger.info(string);
	}
}
