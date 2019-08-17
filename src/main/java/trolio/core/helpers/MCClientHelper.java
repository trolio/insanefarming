package trolio.core.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly (Side.CLIENT)
public class MCClientHelper 
{
	public static Minecraft getMinecraft()
	{
		return FMLClientHandler.instance().getClient();
	}
	
	public static EntityPlayerSP getPlayer()
	{
		return getMinecraft().player;
	}
	
	public static World getWorld()
	{
		return getPlayer().world;
	}
	
	public static void UpdateRender (BlockPos pos)
	{
		refresh(getDimension(), pos);
	}
	
	public static void refresh (int dimension, BlockPos pos)
	{
		if (getWorld().provider.getDimension() == dimension)
		{
			getWorld().markBlockRangeForRenderUpdate(pos, pos);
		}
	}
	
	public static void refresh()
	{
		getMinecraft().renderGlobal.loadRenderers();
	}
	
	public static int getDimension()
	{
		return getWorld().provider.getDimension();
	}
	
	public static TileEntity getTile (PenguinPacketLocation msg)
	{
		return getWorld().getTileEntity(msg.pos);
	}
}
