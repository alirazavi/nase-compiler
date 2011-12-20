package ptc.nase.backend;

import java.text.DecimalFormat;

public class Label 
{
	private static long currentLabel = 0;
	private static DecimalFormat formater = new DecimalFormat("00000");
	
	public static String generateNextLabel()
	{
		return "NCL" + formater.format(currentLabel++);
	}
}
