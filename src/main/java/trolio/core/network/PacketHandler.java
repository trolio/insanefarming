package trolio.core.network;

import static trolio.core.lib.IFModInfo.MODID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler 
{
	private static PenguinNetwork INSTANCE = new PenguinNetwork(MODID);
	
	public static void registerPacket (Class clazz)
	{
		registerPacket(clazz, Side.CLIENT);
		registerPacket(clazz, Side.SERVER);
	}
	
	public static registerPacket (Class clazz, Side side)
	{
		INSTANCE.registerPacket(clazz, side);
	}
	
	public static void sendToClient (IMessage msg, EntityPlayer player)
	{
		INSTANCE.sendToClient (msg, (EntityPlayerMP) player);
	}
	
	public static void sendToServer (IMessage msg)
	{
		INSTANCE.sendToServer (msg);
	}
	
	public static void sendToEveryone (IMessage msg)
	{
		INSTANCE.sendToEveryone (msg);
	}
	
	public static sendToDimension (int dimension, IMessage msg)
	{
		INSTANCE.sendToDimension (dimension, msg);
	}
	
	public static sendToAllAround (IMessage packet, int dim, double x, double y, double z, int distance)
	{
		INSTANCE.sendToAllAround (packet, dim, x, y, z, distance);
	}
}
