package ptc.nase.frontend;

import java.io.IOException;

import ptc.nase.SymbolTable;

public interface IScanner 
{
	public long getCurrentLine();
	public long getCurrentColumn();
	public SymbolTable getSymbolTable();
	public boolean getNextSymbol() throws IOException;
	public int getCurrentSymbol();
	public void skipToDelimiter() throws IOException;
}
