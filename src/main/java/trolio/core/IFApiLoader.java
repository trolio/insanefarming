package trolio.core;

import static trolio.core.lib.IFModInfo.MODID;
import static trolio.core.network.PacketHandler.registerPacket;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.jcraft.jogg.Packet;

import net.minecraft.command.ICommand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;
import scala.reflect.api.Trees.IfApi;
import trolio.api.IFApi;
import trolio.core.util.annotations.IFApiImplementation;
import trolio.quests.IFQuest;

public class IFApiLoader 
{
	public static void init (@Nonnull ASMDataTable table)
	{
		Set<ASMData> datas = new HashSet<> (table.getAll(IFApiImplementation.class.getCanonicalName()));
		
		for (ASMDataTable.ASMData data : datas)
		{
			try
			{
				Class clazz = Class.forName(data.getClassName());
				Object instance = clazz.getField("INSTANCE").get(null);
				Class[] interfaces = clazz.getInterfaces();
				
				if (interfaces != null && interfaces.length > 0)
				{
					for (Class inter : interfaces)
					{
						for (Field f : IFApi.class.getFields())
						{
							if (f.getType().equals(inter))
							{
								f.set(null, instance);
							}
						}
					}
				}
			}catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	@SuppressWarnings("unchecked")
	private static <I extends Impl> void load (Class type, IForgeRegistry registry, ResourceLocation resource, Class<I> clazz)
	{
		try
		{
			if (type.isAssignableFrom(clazz))
			{
				registry.register(clazz.newInstance().setRegistryName(resource));
			}
		} catch (Exception e) {}
	}
	
	public static void load (@Nonnull ASMDataTable asm, boolean isClient)
	{
		registerQuests(asm);
		registerEvents(asm, isClient);
		registerPackets(asm);
		registerCommands(asm);
		registerTasks(asm);
		if (IFCore.DEBUG_MODE && isClient)
		{
			registerDebugCommand(asm);
		}
	}
	
	private static void registerQuests (@Nonnull ASMDataTable asm)
	{
		String annotationClassName = IFQuest.class.getCanonicalName();
		Set<ASMData> asmDatas = new HashSet<>(asm.getAll(annotationClassName));
		
		for (ASMDataTable.ASMData asmData : asmDatas)
		{
			try
			{
				Map<String, Object> data = asmData.getAnnotationInfo();
				Class clazz = Class.forName(asmData.getClassName());
				String extra = data.get("value") != null ? (String) data.get("value") : "";
				String domain = data.get("mod") != null ? (String) data.get("mod") : "insanefarming";
				ResourceLocation resource = new ResourceLocation(domain, extra);
				load (Quest.class, Quest.REGISTRY, resource, clazz);
				Quest.REGISTRY.getValue(resource).onRegistered();
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	private static void registerTasks (@Nonnull ASMDataTable asm)
	{
		String annotationClassName = IFTask.class.getCanonicalName();
		Set<ASMData> asmDatas = new HashSet<>(asm.getAll(annotationClassName));
		
		for (ASMDataTable.ASMData asmData : asmDatas)
		{
			try
			{
				Map<String, Object> data = asmData.getAnnotationInfo();
				Class clazz = Class.forName(asmData.getClassName());
				String value = data.get("value") != null ? (String) data.get("value") : "";
				
				if (!value.equals(""))
				{
					ResourceLocation resource = value.contains(":") ? new ResourceLocation(value) : new ResourceLocation (MODID, value);
					TaskElement.REGISTRY.put(resource, clazz);
				}
			} catch (Exception e ) { e.printStackTrace(); }
		}
	}
	
	private static void registerCommands (@Nonnull ASMDataTable asm)
	{
		String annotationClassName = IFCommand.class.getCanonicalName();
		Set<ASMData> asmDatas = new HashSet<>(asm.getAll(annotationClassName));
		
		for (ASMDataTable.ASMData asmData : asmDatas)
		{
			try
			{
				Class clazz = Class.forName(asmData.getClassName());
				
				if (ICommand.class .isAssignableFrom(clazz))
				{
					CommandManager.INSTANCE.addSubCommand((ICommand) clazz.newInstance());
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void registerDebugCommand (@Nonnull ASMDataTable asm)
	{
		String annotationClassName = IFDebugCommand.class.getCanonicalName();
		Set<ASMData> asmDatas = new HashSet(asm.getAll(annotationClassName));
		
		for (ASMDataTable.ASMData asmData : asmDatas)
		{
			try
			{
				Map<String, Object> data = asmData.getAnnotationInfo();
				Boolean sub = data.get("value") != null ? (Boolean) data.get("value") : false;
				Class clazz = Class.forName(asmData.getClassName());
				
				if (!sub)
				{
					if (ICommand.class.isAssignableFrom(clazz))
					{
						CommandManager.INSTANCE.addSubCommand((ICommand) clazz.newInstance());
					}
				}
				else
				{
					List list = (List) Class.forName("trolio.debug.CommandExportBuilder").getField("commands").get(null);
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	private static void registerEvents (@Nonnull ASMDataTable asm, boolean isClient)
	{
		String annotationClassName = IFEvents.class.getCanonicalName();
		Set<ASMData> asmDatas = new HashSet(asm.getAll(annotationClassName));
		
		for (ASMDataTable.ASMData asmData : asmDatas)
		{
			try
			{
				Map <String, Object> data = asmData.getAnnotationInfo();
				String side = data.get("value") != null ? ReflectionHelper.getPrivateValue(ModAnnotation.EnumHolder.class, (ModAnnotation.EnumHolder) data.get("value"), "value") : "";
				
				if (side.equals("CLIENT") && isClient || side.equals(""))
				{
					Class clazz = Class.forName(asmData.getClassName());
					Method register = getMethod("register");
					
					if (register == null || ((Boolean)register.invoke(null)))
					{
						Field INSTANCE = getField(clazz, "INSTANCE");
						
						if (INSTANCE == null)
						{
							MinecraftForge.EVENT_BUS.register(clazz.newInstance());
						}
						else
						{
							MinecraftForge.EVENT_BUS.register(INSTANCE.get(null));
						}
					}
				}
			} catch (Exception e) {}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void registerPackets (@Nonnull ASMDataTable asm)
	{
		String annotationClassName = Packet.class.getCanonicalName();
		Set<ASMData> asmDatas = new HashSet<>(asm.getAll(annotationClassName));
		
		Map<String, Side> sidedPackets = new HashMap();
		Map<String, String> unsidedPackets = new HashMap();
		
		for (ASMDataTable.ASMData asmData : asmDatas)
		{
			try
			{
				Map<String, Object> data = asmData.getAnnotationInfo();
				String s = data.get("value") != null ? ReflectionHelper.getPrivateValue(ModAnnotation.EnumHolder.class, (ModAnnotation.EnumHolder) data.get("value"), "value") : "BOTH";
				Side side = s.equals("CLIENT") ? Side.CLIENT : s.equals("SERVER") ? Side.SERVER : null;
				
				if (side == null)
				{
					unsidedPackets.put(asmData.getClassName(), asmData.getClassName());
				}
				else
				{
					sidedPackets.put(asmData.getClassName(), side);
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
		
		Comparator <String> alphabetical = (str1, str2) ->
		{
			int res = String.CASE_INSENSITIVE_ORDER.compare (str1, str2);
			
			if (res == 0)
			{
				str1.compareTo(str2);
			}
			return res;
		};
		
		List<String> namesSided = Lists.newArrayList(sidedPackets.keySet());
		Collections.sort(namesSided, alphabetical);
		List<String> namesUnsided = Lists.newArrayList(unsidedPackets.keySet());
		Collections.sort(namesUnsided, alphabetical);
		
		for (String sided : namesSided)
		{
			Side side = sidedPackets.get(sided);
			
			try
			{
				Class<?> asmClass = Class.forName(sided);
				registerPacket(asmClass, side);
			} catch (Exception e) {}
		}
		
		for (String unsided : namesUnsided)
		{
			try
			{
				Class<?> asmClass = Class.forName(unsided);
				registerPacket(asmClass);
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Method getMethod (Class clazz, String method)
	{
		try
		{
			return clazz.getMethod(method);
		} catch (NoSuchMethodException e) { return null; }
	}
	
	private static Field getField (Class clazz, String field)
	{
		try
		{
			return clazz.getField(field);
		} catch (NoSuchFieldException e) { return null; }
	}
}
