package ptc.nase.backend;

public class Storage 
{
	private static long nextFreeConstantAdress = 0;
	private static long nextFreeVariableAdress = 0;
	
	public static long getAndPushConstantAdress()
	{
		return nextFreeConstantAdress++;
	}
	
	public static long getNextFreeMemoryAdress()
	{
		return nextFreeVariableAdress;
	}
	
	public static long getAndPushMemoryAdress()
	{
		return nextFreeVariableAdress++;
	}
	
	public static void popMemoryAdress()
	{
		nextFreeVariableAdress--;
	}
	
}
