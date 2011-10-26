package files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

	private static FileManager instance = null;
	
	private Echofile echofile;
	private Infile infile;
	private Listing listing;
	private String filename;
	
	private FileManager(){
		 
	}
	
	public void init(String filename){
		echofile = new Echofile(filename);
		infile = new Infile(filename);
		listing = new Listing(filename);	
		this.filename = filename;
		instance.getInfile().readNextChar();
	}
	
	
	public static FileManager getInstance(){
		if(instance == null){
			instance = new FileManager();
		}
		return instance;
	}

	public boolean closeOutFiles(){
		boolean success = true;
		try {
			echofile.close();
			listing.close();
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
				
		return success;
	}
	
	public boolean mergeOutFiles(){
		boolean success = true;
		
		try {
			FileWriter fw = new FileWriter(filename + ".lst");
			BufferedReader lst1 = new BufferedReader(new FileReader(filename + ".lst1"));
			BufferedReader lst2 = new BufferedReader(new FileReader(filename + ".lst2"));
			String line = null;
			while((line = lst1.readLine()) !=  null){
				fw.write(line+"\n");
			}
			while((line = lst2.readLine()) !=  null){
				fw.write(line+"\n");
			}
			lst1.close();
			lst2.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}	
		
		return  success;
	}
	
	
	public Echofile getEchofile() {
		return echofile;
	}

	public Infile getInfile() {
		return infile;
	}

	public Listing getListing() {
		return listing;
	}
	
	
	
	
}
