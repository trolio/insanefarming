package trolio.core;

import net.minecraft.item.EnumRarity;

@IFLoader(priority = IFCore)
public class IFCore 
{
	public static final Fluid Goddess = RegisterFluid (new Fluid("goddess_water", new ResourceLocation(MODID), "blocks/goddess_still"), new ResourceLocation(MODID, "blocks/goddess_flow")).setRarity(EnumRarity.RARE));
}
