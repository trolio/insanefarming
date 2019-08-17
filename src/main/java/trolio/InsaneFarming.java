package trolio;

import static trolio.core.lib.IFModInfo.*;

import net.minecraftforge.fml.common.Mod;

@Mod (modid = MODID, name = MODNAME, version = VERSION, guiFactory = GUI_FACTORY)
public class InsaneFarming 
{
	@SidedProxy (ClientSide = JAVAPATH + "core.proxy.IFClientProxy", serverSide = JAVAPATH + "core.proxy.IFServerProxy")
	public static IFCommonProxy proxy;
}
