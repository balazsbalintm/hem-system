package hu.bme.analytics.hems;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class HemsProps extends Properties{
	private static final long serialVersionUID = 1L;
	public static final String RM_CSVPATH = "app.rm.csvpath";
	public static final String RM_MODELPATH = "app.rm.modelpath";
	
	private static HemsProps instance = null;
	public static HemsProps get() {
		return instance;
	}
	
	public static void init(String path) {
		if (instance == null) {
			try {
				
				instance = new HemsProps();
				instance.load(new FileInputStream(new File(path)));
				
			} catch (IOException e) {e.printStackTrace();}
		}
	}
	
	protected HemsProps() {
	}
}
