package ynu.software.shixun.main;

import ynu.software.shixun.R;
import ynu.software.shixun.bayes.TestFramework;
import ynu.software.shixun.blacklistfilter.BlackNumberActivity;
import ynu.software.shixun.blacklistfilter.WhiteNumberActivity;
import ynu.software.shixun.smsfraud_register.SmsContent;
import ynu.software.shixun.smsfraud_register.Api;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;

public class SetActivity extends PreferenceActivity {
	public PreferenceScreen whitelist;
	public PreferenceScreen blacklist;
	public static ContentResolver cr;
	public static String[] projection;
	public static Cursor cur=null;
	public static NotificationManager notifyManager;
	public static SmsContent content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);   
		addPreferencesFromResource(R.xml.setting);
		whitelist=(PreferenceScreen) findPreference("whitelist");
		blacklist=(PreferenceScreen) findPreference("blacklist");
		if(SetActivity.this.getSharedPreferences("ynu.software.shixun_preferences", 0).getBoolean("fraudmessage", true)){
			SetActivity.cr=SetActivity.this.getContentResolver();
			SetActivity.cur=SetActivity.cr.query(Uri.parse(Api.SMS_URI_INBOX),SetActivity.projection, null, null, null); 
			SetActivity.projection=new String[]{"_id"};
			SetActivity.notifyManager=(NotificationManager)SetActivity.this.getSystemService(SetActivity.NOTIFICATION_SERVICE);
			Api.smsCountPre=Api.getNumber(SetActivity.this);
			Api.intentCount=0;
			SetActivity.content=new SmsContent(new Handler(), SetActivity.this);
			SetActivity.cr.registerContentObserver(Uri.parse(Api.SMS_URI_ALL), true, SetActivity.content);
			Api.registed=true;
			Log.i("chongrui", "registed");
		}
	}
	
	public static void alert(Context ctx, CharSequence msg) {
    	if (ctx != null) {
        	new AlertDialog.Builder(ctx).setNeutralButton(android.R.string.ok, null).setMessage(msg).show();
    	}
    }
	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Api.catched){
			alert(this, "您收到一条欺诈短信("+Api.date+")");					
			Api.catched=false;
		}
	}
	
	@Override
	@Deprecated
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		// TODO Auto-generated method stub
		String key=preference.getKey();
		if(key.equals("whitelist")){
			Intent intent=new Intent(this,WhiteNumberActivity.class);
			startActivity(intent);
		}
		if(key.equals("blacklist")){
			Intent intent=new Intent(this,BlackNumberActivity.class);
			startActivity(intent);
		}
		if(key.equals("help")){
			new HelpDialog(this).show();
		}
		if(key.equals("enable")){
			TestFramework.bayes_enable=SetActivity.this.getSharedPreferences("ynu.software.shixun_preferences", 0).getBoolean("enable", true);
		}
		if(key.equals("fraudmessage")){
//			if(Api.fraudenable==true){
//				Api.fraudenable=true;
//				Log.i("mmy","fraudfalse");
//			}else{
//				Api.fraudenable=false;
////				SetActivity.content=new SmsContent(new Handler(), context);
////				SetActivity.cr.registerContentObserver(Uri.parse(Api.SMS_URI_ALL), true, SetActivity.content);
////				Api.registed=true;
//				Log.i("mmy", "fraudtrue");
//			}
			Api.fraudenable=SetActivity.this.getSharedPreferences("ynu.software.shixun_preferences", 0).getBoolean("fraudmessage", true);
			if(Api.fraudenable&&!Api.registed){
				cr=getContentResolver();
				cur=cr.query(Uri.parse(Api.SMS_URI_INBOX), projection, null, null, null); 
				projection=new String[]{"_id"};
				notifyManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				Api.smsCountPre=Api.getNumber(SetActivity.this);
				Api.intentCount=0;
				content=new SmsContent(new Handler(), SetActivity.this);
				cr.registerContentObserver(Uri.parse(Api.SMS_URI_ALL), true, content);
				Api.registed=true;
			}
			if(!Api.fraudenable){
				cr.unregisterContentObserver(content);
				Api.registed=false;
			}
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
}
