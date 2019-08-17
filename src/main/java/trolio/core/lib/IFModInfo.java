package trolio.core.lib;

import net.minecraft.util.ResourceLocation;

public class IFModInfo 
{
	public static final String MODID = "insnefarming";
	public static final String MODNAME = "Insane Farming";
	public static final String VERSION = "@VERSION@";
	public static final String GUI_FACTORY = "trolio.core.util.IFGuiFactory";
	public static final String JAVAPATH = "trolio.";
	public static final String CAPNAME = "IF";
	public static final String COMMANDNAME = "if";
	public static final String GIFTPATH = "trolio.npcs.gift.Gifts";
	public static final String SCHEDULEPATH = "trolio.npcs.schedule.Schedule";
	public static final String CROPSTATES = "trolio.crops.handlers.state.StateHandler";
	public static final String DROPHANDLERS = "trolio.crops.handlers.drop.DropHandler";
	public static final String GROWTHHANDERS = "trolio.crops.handlers.growth.GrowthHandler";
	public static final String RULES = "trolio.crops.handlers.rules.SpecialRules";
	
	//textures
	public static final ResourceLocation ELEMENTS = new ResourceLocation(MODID, "textures/gui/gui_elements.png");
	public static final ResourceLocation TOOLELEMENTS = new ResourceLocation(MODID, "textures/gui/gui_toolelements.png");
	public static final ResourceLocation ICONS = new ResourceLocation(MODID, "textures/gui/icons.png");
	
}
