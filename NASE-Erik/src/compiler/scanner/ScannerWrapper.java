package compiler.scanner;

import files.FileManager;

public abstract class ScannerWrapper {

	private final boolean DEBUG_SCANNER = true;
	
	public abstract int getCurrentSymbol();
	public abstract int lookAheadOneSymbol();
	public abstract boolean getNextSymbol();
	public abstract void skipToDelimiter();
	public abstract void skipBlockEndSymbol();
	public abstract int getLine();
	public abstract int getColumn();
	
	protected void scannerDebugOutText(String message){
		if(DEBUG_SCANNER)
			FileManager.getInstance().getListing().write(message);
	}
	
	protected void scannerDebugOutTokenSymbol(String token, int sym){
		if(DEBUG_SCANNER)
			FileManager.getInstance().getListing().write(String.format("Scanned symbol: %s has ID: %d", token, sym));
	}
}
