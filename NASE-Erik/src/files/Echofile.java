package files;

import java.io.FileWriter;
import java.io.IOException;

public class Echofile {
	
	private FileWriter echofile;	
	
	public Echofile(String filename){
		
		try {
			echofile = new FileWriter(filename +".lst1");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		echoWriteLine(String.format("Compilation listing of <%s>\n", filename));
		echoWriteLineNr(1);
	}
	
	
	public void echoWriteLineNr(int line){
		try {
			echofile.write(String.format("%05d: ", line));
		} catch (IOException e) {
			System.err.println("File not available!");
			e.printStackTrace();
		}
	}
	
	public void echoWriteChar(char ch){
		try {
			echofile.write(ch);
		} catch (IOException e) {
			System.err.println("File not available!");
			e.printStackTrace();
		}
	}
	
	public void echoWriteLine(String pLine){
		try {
			echofile.write(String.format("%s\n", pLine));
		} catch (IOException e) {
			System.err.println("File not available!");
			e.printStackTrace();
		}		
	}
	
	public void close() throws IOException{
		echofile.close();
	}


}
