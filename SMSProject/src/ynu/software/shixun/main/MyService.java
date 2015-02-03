package ynu.software.shixun.main;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.net.InterfaceAddress;

import ynu.software.shixun.smsfraud_register.SMSReceiver;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class MyService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(Integer.MAX_VALUE);
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		SMSReceiver receiver = new SMSReceiver();
		registerReceiver(receiver, filter);
	}

}


