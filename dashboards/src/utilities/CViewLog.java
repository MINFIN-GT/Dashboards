package utilities;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;

public class CViewLog {
	
	
	static public ArrayList<String> getListLogs(String path){
		ArrayList<String> ret = new ArrayList<String>();
		File folder = new File(path);
		File[] files = folder.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().startsWith("log");
		    }
		});
		if(files!=null){
			for(File file : files){
				if(file.isFile())
					ret.add(file.getName());
			}
		}
		Collections.sort(ret);
		return ret;
	}

}
