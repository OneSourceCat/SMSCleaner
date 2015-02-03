package ynu.software.shixun.smsfraud_register;

import java.text.SimpleDateFormat;

import ynu.software.shixun.R;
import ynu.software.shixun.main.SetActivity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class SmsContent extends ContentObserver {

	private static Context cxt;
	
	public SmsContent(Handler handler,Context context) {
		super(handler);
		// TODO Auto-generated constructor stub
		cxt=context;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onChange(boolean selfChange) {
		Log.i("chongrui", "DBreceived");
		if(cxt.getSharedPreferences("ynu.software.shixun_preferences", 0).getBoolean("fraudmessage", true)==false){
			Log.i("chongrui", "returned");
			return;
		}
		
		// TODO Auto-generated method stub
		super.onChange(selfChange);
		SetActivity.cur=SetActivity.cr.query(Uri.parse(Api.SMS_URI_INBOX), SetActivity.projection, null, null, null); 
		Api.smsCountCurrent=Api.getNumber(cxt);	
		
		if(Api.intentCount<Api.smsCountCurrent-Api.smsCountPre){
			Api.catched=true;
			SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd   hh:mm:ss");     
			Api.date=sDateFormat.format(new java.util.Date()); 
			@SuppressWarnings("deprecation")
			Notification notify=new Notification(R.drawable.logo, "您收到一条欺诈短信", System.currentTimeMillis());
			PendingIntent pending=PendingIntent.getActivity(cxt, 0, new Intent(cxt, SetActivity.class), 0);
			Log.i("mmy", "notify");
			notify.flags=Notification.FLAG_AUTO_CANCEL;
			notify.setLatestEventInfo(cxt, "您收到欺诈短信", Api.date, pending);
			SetActivity.notifyManager.notify(222222, notify);
			Api.intentCount=0;
			Api.smsCountPre=Api.smsCountCurrent;
		}
	}		
}
