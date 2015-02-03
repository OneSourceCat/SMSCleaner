package ynu.software.shixun.smsfraud_register;

import ynu.software.shixun.main.SetActivity;
import android.content.Context;

public class Api {
	public final static String SMS_URI_ALL = "content://sms/"; 
	public final static String SMS_URI_INBOX = "content://sms/inbox"; 
	final static String SMS_URI_SEND = "content://sms/sent"; 
	final static String SMS_URI_DRAFT = "content://sms/draft"; 
	
	public static final String PREFS_NAME = "ynu.software.shixun.main_preferences";
	public static final String PREF_ENABLED	= "fraudmessage";
//	public static final String PREF_TIME = "Time";

	public static int intentCount=0;
	public static int smsCountPre=-1;
	public static int smsCountCurrent;
	
	public static boolean fraudenable=false;
	public static boolean catched=false;
	public static boolean registed=false;
	public static String date;
	
	public static int getNumber(Context context){
		if (SetActivity.cur.moveToFirst()) { 
			return SetActivity.cur.getCount();
		}else{
			return 0;				
		}
	}
	
	public static boolean isEnabled(Context context) {
		if (context == null) return false;
		fraudenable = context.getSharedPreferences(PREFS_NAME, 0).getBoolean(PREF_ENABLED, false);
		return fraudenable;
	}
	
}
