package ynu.software.shixun.smsfraud_register;

import ynu.software.shixun.main.MyService;
import ynu.software.shixun.main.SetActivity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

public class BootBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("Hello Booted!");
		Intent bootintent = new Intent(context,MyService.class);
		context.startService(bootintent);
		// TODO Auto-generated method stub
		if(Intent.ACTION_BOOT_COMPLETED.equals(bootintent.getAction())){
			if(Api.isEnabled(context)){
				SetActivity.cr=context.getContentResolver();
				SetActivity.cur=SetActivity.cr.query(Uri.parse(Api.SMS_URI_INBOX),SetActivity.projection, null, null, null); 
				SetActivity.projection=new String[]{"_id"};
				SetActivity.notifyManager=(NotificationManager)context.getSystemService(SetActivity.NOTIFICATION_SERVICE);
				Api.smsCountPre=Api.getNumber(context);
				Api.intentCount=0;
				SetActivity.content=new SmsContent(new Handler(), context);
				SetActivity.cr.registerContentObserver(Uri.parse(Api.SMS_URI_ALL), true, SetActivity.content);
				Api.registed=true;
			}
		}
	}

}
