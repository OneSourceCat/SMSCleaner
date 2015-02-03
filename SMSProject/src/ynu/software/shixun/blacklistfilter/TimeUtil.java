package ynu.software.shixun.blacklistfilter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	public static String getCurrentTime(){
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格
		String time = dateFormat.format( now ); 
        return time;
	}
}
