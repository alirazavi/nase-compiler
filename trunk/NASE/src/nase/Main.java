package nase;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	public static final Logger logger = LoggerFactory.getLogger(Main.class);

	private static File   naseFile; // file that NASE will attempt to compile
	private static String filename; // name of file without extension
	
	/**
	 * Program entry point
	 * @param args console parameters
	 */
	public static void main(String[] args) {
		
		checkCallingParameters(args);
		
		removeFileExtension();
		
		cleanOldFiles();
		
		intializeModules();
		
		// compilation phases
		phase1();
		phase2();
		phase3();
		
		// remove _nase_progstub.mms
		removeProgStub();
	}

	/**
	 * 
	 */
	private static void removeProgStub() {
		// TODO implement
	}

	/**
	 * 
	 */
	private static void phase3() {
		logger.info("Beginning compilation phase 3");
		// TODO implement
		logger.info("Phase 3 complete");
		logger.info("Compilation successful");
	}

	/**
	 * 
	 */
	private static void phase2() {
		logger.info("Beginning compilation phase 2");
		// TODO implement
		logger.info("Phase 2 complete");
	}

	/**
	 * 
	 */
	private static void phase1() {
		logger.info("Beginning compilation phase 1");
		// TODO implement
		logger.info("Phase 1 complete");
	}

	/**
	 * 
	 */
	private static void intializeModules() {
		logger.info("Initializing modules");
		// TODO implement
		logger.info("Modules initialized");
	}

	/**
	 * Removes file extension from file name.
	 * i.e. "test123.nase" becomes "test123"
	 */
	private static void removeFileExtension() {
		if (filename.endsWith(".nase")) {
			logger.info("Stripping file extension .nase");
			filename = filename.substring(0, filename.length() - ".nase".length());
		}
		logger.info("Name set to: " + filename);
	}

	/**
	 * 
	 */
	private static void cleanOldFiles() {
		logger.info("Cleaning old files");
		unlinkListingFile();
 		unlinkListingIntermediateFiles();
		unlinkMMixFiles();
		logger.info("Files cleaned");
	}

	/**
	 * 
	 */
	private static void unlinkMMixFiles() {
		// TODO implement
	}

	/**
	 * 
	 */
	private static void unlinkListingIntermediateFiles() {
		// TODO implement
	}

	/**
	 * 
	 */
	private static void unlinkListingFile() {
		// TODO implement
	}

	/**
	 * Checks calling parameters of application
	 * @param args calling parameters
	 */
	private static void checkCallingParameters(String[] args) {
		// if missing arguments, display usage info
		if (args.length < 1) {
			logger.error("No file argument passed. Usage: <filename>.nase." +
					"If extension is missing, then \".nase\" is assumed");	
			System.exit(-1);
		}
		
		// if argument found and file path valid: set as file name
		String filePath = args[0];
		File file = new File(filePath);
		logger.info("Checking if path " + filePath + " is valid");
		if (file.canRead()) {
			filename = file.getName();
			naseFile = file;
			logger.info("File found");
		} else {
			logger.error("File cannot be found. Exiting!");
			System.exit(-1);
		}
	}
}
