package trolio.core.proxy;

import static trolio.core.helpers.ConfigHelper.setCategory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Triple;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

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
		Set<ASMData> dates = new HashSet<>(table.getAll(IFLoader.class.getCanonicalName()));
	}
}
