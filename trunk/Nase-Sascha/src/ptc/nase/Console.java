package ptc.nase;

public class Console
{
	public static void display(String message)
	{
		System.out.print(message);
	}
	
	public static void fatalError(String message)
	{
		String errorMsg;
		
		errorMsg = "\nFataler Fehler: " + message;
		
		display(errorMsg);
	}
	
	public static int errorStop(String message)
	{
		fatalError(message);
		display("Ausführung abgebrochen!\n\n");
		return 0;
	}
}
