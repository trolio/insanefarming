package trolio.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import trolio.core.helpers.MCClientHelper;

public abstract class PenguinPacket implements IMessage
{
	public void handlePacket (EntityPlayer player) {}
	
	public boolean handleServerPacket(EntityPlayerMP player)
	{
		return false;
	}
	
	@Override
	public void toBytes (ByteBuf to) {}
	
	@Override
	public void fromBytes (ByteBuf from) {}
	
	@SideOnly (Side.CLIENT)
	public void handleQueuedClient (NetHandlerPlayClient handler)
	{
		handlePacket (MCClientHelper.getPlayer());
	}
	
	public void handleQueuedServer (NetHandlerPlayServer serverHandler)
	{
		if (!handleServerPacket (serverHandler.player))
		{
			handlePacket(serverHandler.player);
		}
	}
}
