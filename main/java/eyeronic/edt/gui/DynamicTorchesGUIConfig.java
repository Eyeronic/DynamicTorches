package eyeronic.edt.gui;

import java.util.List;

import cpw.mods.fml.client.config.GuiConfig;
import eyeronic.edt.Reference;
import eyeronic.edt.init.DynamicTorches;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class DynamicTorchesGUIConfig extends GuiConfig {
	public DynamicTorchesGUIConfig(GuiScreen parentScreen)
	{
		super(parentScreen, // Let Forge know the GUI we were at before
	            new ConfigElement(DynamicTorches.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), // What category of config to show in the GUI, can be something else (HAS TO BE IN YOUR CONFIG ALREADY!!!)
	            Reference.MOD_ID, // The MODID
	            true, // Whether changing config requires you to relog/restart world
	            false, // Whether changing config requires you to relaunch Minecraft
	            GuiConfig.getAbridgedConfigPath(DynamicTorches.config.toString())); // Config title; this will return the config path
	}
	
	@Override
    public void initGui()
    {
        // You can add buttons and initialize fields here
        super.initGui();
    }

    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // You can do things like create animations, draw additional elements, etc. here
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        // You can process any additional buttons you may have added here
        super.actionPerformed(button);
    }
    
    @Override
    public void drawToolTip(List stringList, int x, int y) {
    	super.drawToolTip(stringList, x, y);
    }
    
    @Override
    protected void drawHoveringText(List stringList, int x,
    		int y, FontRenderer font) {
    	super.drawHoveringText(stringList, x, y, font);
    }
}
