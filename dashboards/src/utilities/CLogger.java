package utilities;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CLogger {

	private static Logger log;
	
	public CLogger(){
		
	}
	
	static public void write(String str, Object obj, Throwable e){
		log=Logger.getLogger(obj.getClass());
		log.error(str,e);
	}
	
	static public void write_simple(String error_num, Object obj, String error){
		log=Logger.getLogger(obj.getClass());
		log.error(String.join(" ",obj.toString(), error_num,"\n"+error));		
	}	
	
	static public void writeConsole(String mensaje) {
		DateTime date= new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("d/M/y k:m:s.S");
		System.out.println(fmt.print(date)+" "+mensaje);
	}
}
