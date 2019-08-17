package trolio.core.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PenguinPacketHandler implements IMessageHandler<PenguinPacket, IMessage>
{
	@Override
	public IMessage onMessage (final PenguinPacket msg, final MessageContext ctx)
	{
		if (ctx.side == Side.CLIENT)
		{
			Minecraft.getMinecraft().addScheduledTask(() -> msg.handleQueuedClient(ctx.getClientHandler()));
		}
		else
		{
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> msg.handleQueuedServer(ctx.getServerHandler()));
		}
		return null;
	}
}
