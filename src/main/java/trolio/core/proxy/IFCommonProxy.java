package trolio.core.proxy;

import static trolio.core.helpers.ConfigHelper.setCategory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.Level;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import trolio.InsaneFarming;
import trolio.core.IFApiLoader;
import trolio.core.helpers.ConfigHelper;

public class IFCommonProxy 
{
	private static final List<Class> LIST = new ArrayList<>();
	private static final boolean ENABLE_LOGGING = true;
	
	public List<Class> getList()
	{
		return LIST;
	}
	
	public void setup(@Nonnull ASMDataTable table)
	{
		List<Triple<Integer, String, String>> unsorted = new ArrayList<>();
		Set<ASMData> datas = new HashSet<>(table.getAll(IFApiLoader.class.getCanonicalName()));
		
		for (ASMDataTable.ASMData data : datas)
		{
			try 
			{
				String clazz = data.getClassName();
				Map<String, Object> map = data.getAnnotationInfo();
				String mods = map.get("mods") != null ? (String) map.get("mods") : "";
				int value = mods.equals("") ? map.get("priority") != null? (int) map.get("Priority") : 1 : -5000;
				unsorted.add(Triple.of(value, mods, clazz));
			} catch (Exception e) { e.printStackTrace(); }
		}
		
		Comparator<Triple<Integer, String, String>> priority = (str1, str2) -> str1.getLeft() < str2.getLeft() ? 1 : str1.getLeft() > str2.getLeft() ? -1 : 0;
		Collections.sort(unsorted, priority);
		
		triple:
			for (Triple<Integer, String, String> entry : unsorted)
			{
				try
				{
					if (!entry.getMiddle().equals(""))
					{
						String[] mods = entry.getMiddle().replace(" ", " ").split(",");
						
						for (String mod : mods)
						{
							if (!isModLoaded(mod))
							{
								continue triple;
							}
						}
						LIST.add(Class.forName(entry.getRight()));
					}
				} catch (Exception e)
					{
						InsaneFarming.LOGGER.log(Level.ERROR, "Insane Farming failed to load the following class: " + entry.getMiddle());
						InsaneFarming.LOGGER.log(Level.ERROR, "If this is a mod related class, try updating your version of that mod before reporting!");
					}
				}
			}
	
	private boolean isModLoaded (String mod)
	{
		return Loader.isModLoaded(mod) || Loader.isModLoaded(mod.toLowerCase(Locale.ENGLISH));
	}
	
	public void setupConfig (File file)
	{
		ConfigHelper.setConfig(new Configuration(file));
	}
	
	@SuppressWarnings("unchecked")
	public void configure()
	{
		Configuration config = ConfigHelper.getConfig();
		
		for (Class c : LIST)
		{
			try
			{
				Method configure = c.getMethod("configure");
				
				try
				{
					config.load();
					setCategory(c.getSimpleName().replace("IF", ""));
					configure.invoke(null);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					if (config.hasChanged())
					{
						config.save();
					}
				}
				
			} catch (NoSuchMethodException e) {}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void load (String stage)
	{
		for (Class c : LIST)
		{
			try
			{
				c.getMethod(stage).invoke(null);
			} catch (NoClassDefFoundError | NoSuchMethodException e) {}
			catch (Exception e)
			{
				if (ENABLE_LOGGING)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean isClient()
	{
		return false;
	}
	
	public void loadAPI (ASMDataTable data)
	{
		IFApiLoader.load(data, isClient());
	}
}
