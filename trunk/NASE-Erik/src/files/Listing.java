package files;

import java.io.FileWriter;
import java.io.IOException;

public class Listing {

	private FileWriter fw;

	public Listing(String filename) {
		try {
			fw = new FileWriter(filename+".lst2");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() throws IOException{
		fw.close();
	}
	
	public void write(char ch){
		try {
			fw.append(ch);
		} catch (IOException e) {
			System.err.println("[ERROR]\t");
			e.printStackTrace();
		}
	}
	
	public void write(String line){
		try {
			fw.write(line+'\n');
		} catch (IOException e) {
			System.err.println("[ERROR]\t");
			e.printStackTrace();
		}
	}
	
	public void writeInternalError(String line){
		write("[INTERNAL ERROR]\t"+line);
	}
}
