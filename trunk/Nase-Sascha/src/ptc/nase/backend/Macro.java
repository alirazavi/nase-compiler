package ptc.nase.backend;

public class Macro 
{
	public static String environment()
	{
		return 	
				
		"% MACRO environment\n" +

		"PROGEP  IS @\n" +
		"% -----------------------------------------------------------------------\n" +
		"% And here we go....\n" +
		"% -----------------------------------------------------------------------\n" +
		
		"ACCU      GREG 0 %Accumulator\n" +
		"OP2       GREG 0 %Register for 2nd operand\n" +
		"FLAG      GREG 0 %Flag reg.\n" +
		"MINUSONE  GREG -1 %Reg. just holding -1; the value of TRUE\n" +
		"OFFSET    GREG 0 %Offset register\n" +
		
		"          LOC Data_Segment %Switch to Data_Segment by default\n" +
		
		"PROMPT    GREG @ %Pointer register to prompt string\n" +
		"          BYTE  \"?\",0,0,0,0,0,0,0\n" +
		
		"NEWLINE   GREG @ %Pointer register to newline string\n" +
		"          BYTE  #a,0,0,0,0,0,0,0\n\n";
		
		
	}
	
	public static String comment(String comment)
	{
		return "%% " + comment + "\n";
	}
	
	public static String openConstDefinitionTable()
	{
		return 
			
		"% MACRO openConstDefinitionTable\n" +
		
		"CONST     GREG @ %Pointer register to const definition table\n\n";
	}
	
	/***
	* openConstDefinitionTable must be called first in order to fix base adress in CONST
	**/
	public static String defineConstValue(long value)
	{
		return
				
		"% MACRO defineConstValue\n" +
		
		"          OCTA " + value + " %%Value of constant\n\n";		
	}
	
	public static String allocateStaticMemory(long dataSize)
	{
		return
				
		"% MACRO allocateStaticMemory\n" +
				
		"ENVDSA    GREG @ %Base pointer register to that area\n" +
		"          LOC @+" + dataSize + "*8 %%Set location pointer to end of data area\n\n";
	}
	
	public static String programStart()
	{
		return
				
		"% MACRO programStart\n" +
		
		"          LOC PROGEP %Switch to code segment; prog starts at end of prolog code\n" + 
		"Main      SWYM 1,2,3 %Main programm starts here with a noop instruction\n\n";
	}
	
	public static String programEnd()
	{
		return
				
		"% MACRO programEnd\n" +
		
		"End       TRAP 0,Halt,0 %Programm end\n\n";
	}
	
	
	public static String label(String label)
	{
		return
				
		"% MACRO label\n" + 
		
		label + " SWYM 1,2,3 %%Jump destination filled with a noop instruction for syntactic reasons\n\n";
	}
	
	public static String jump(String target)
	{
		return
				
		"% MACRO jump\n" +
		
		"          JMP " + target + " %%Jump to destination\n\n";	
	}
	
	public static String jumpOnFalse(String target)
	{
		return
		
		"% MACRO jumpOnFalse\n" + 
		
		"          BNN ACCU," + target + " %%Jump to destination if ACCU >= 0\n\n";
	}
	
	public static String loadAccu()
	{
		return
				
		"TODO";
	}
	
	public static String readValueIntoAccu()
	{
		return
		
		"% MACRO readIntoAccu\n" + 
		
		"          LDA $255,PROMPT,0 %Adress of prompt string\n" +
		"          TRAP 0,Fputs,StdOut %Print out prompt string\n" +
		"          TRIP LsZahl,ACCU,0 %Integer einlesen\n\n";
	}
	
	public static String writeOutAccuValue()
	{
		return
				
		"% MACRO writeOutAccu\n" +
		
		"          TRIP DrZahl,ACCU,0 %Integer ausgeben\n" +
		
		"          LDA $255,NEWLINE,0 %Adress of newline string\n" +
		"          TRAP 0,Fputs,StdOut %Print out newline string\n\n";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
