package trolio.core.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PenguinNetwork 
{
	private final SimpleNetworkWrapper INSTANCE;
	private final PenguinPacketHandler handler;
	private int id;
	
	public PenguinNetwork (String name)
	{
		INSTANCE = new SimpleNetworkWrapper(name);
		handler = new PenguinPacketHandler();
	}
	
	public void registerPacket (Class clazz, Side side)
	{
		INSTANCE.registerMessage(handler, clazz, id++, side);
	}
	
	public void sendToClient (IMessage msg, EntityPlayerMP player)
	{
		INSTANCE.sendTo(msg, player);
	}
	
	public void sendToServer (IMessage msg)
	{
		INSTANCE.sendToServer(msg);
	}
	
	public void sendToEveryone (IMessage msg)
	{
		INSTANCE.sendToAll(msg);
	}
	
	public void sendToDimension (int dimension, IMessage msg)
	{
		INSTANCE.sendToDimension(msg, dimension);
	}
	
	public void sendToAllAround (IMessage packet, int dim, double x, double y, double z,  int distance)
	{
		INSTANCE.sendToAllAround (packet, new TargetPoint(dim, x, y, z, distance));
	}
	
	public Packet<?> getPacketFrom (IMessage packet)
	{
		return INSTANCE.getPacketFrom(packet);
	}
}
