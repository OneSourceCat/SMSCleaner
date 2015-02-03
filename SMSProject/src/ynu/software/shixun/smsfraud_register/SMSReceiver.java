package ynu.software.shixun.smsfraud_register;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ynu.software.shixun.bayes.SMSFilterSystem;
import ynu.software.shixun.bayes.StreamTools;
import ynu.software.shixun.blacklistfilter.BlackNumberDBHelper;
import ynu.software.shixun.blacklistfilter.BlackNumberDao;
import ynu.software.shixun.blacklistfilter.RubbishItem;
import ynu.software.shixun.blacklistfilter.RubbishMessageDBHelper;
import ynu.software.shixun.blacklistfilter.RubbishMessageDao;
import ynu.software.shixun.blacklistfilter.TimeUtil;
import ynu.software.shixun.blacklistfilter.WatchRubbishMessage;
import ynu.software.shixun.blacklistfilter.WhiteNumberDBHelper;
import ynu.software.shixun.blacklistfilter.WhiteNumberDao;
import ynu.software.shixun.main.DBoperator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
	private SharedPreferences sp;   //保存短信中心的号码
	private SharedPreferences bayessp;
	private boolean isrealbase;//是否开启伪基站拦截
	private boolean islocalbayesup; //是否开启全部拦截
	private boolean isSMSFraud;//是否开启短信欺诈拦截
	private boolean iscloudenable;
	private String centernumber;
	private NotificationManager mNotificationManager;
	private String number;
	private String content="";
	private String time;
	private BlackNumberDao blackdao;
	private RubbishMessageDao rubbishDao;
	private DBoperator operator;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(final Context context, Intent intent) {
		//短信漏洞防护
		if("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())){
			Api.intentCount++;
			Log.i("chongrui", "intentreceived");
		}
		
		operator = new DBoperator(context);
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		bayessp = context.getSharedPreferences("ynu.software.shixun_preferences", context.MODE_PRIVATE);
		isrealbase = bayessp.getBoolean("message", true);
		islocalbayesup = bayessp.getBoolean("enable", false);
		iscloudenable = bayessp.getBoolean("cloudenable", true);
		//黑白名单和贝叶斯的逻辑
		blackdao = new BlackNumberDao(context, new BlackNumberDBHelper(context));
		rubbishDao= new RubbishMessageDao(context, new RubbishMessageDBHelper(context));
	    WhiteNumberDao whitenumberdao = new WhiteNumberDao(context, new WhiteNumberDBHelper(context)); 
		Bundle bundle =  intent.getExtras();
	    if(bundle!=null){
	    	Object[] objs = (Object[]) bundle.get("pdus");
	    	SmsMessage[] message = new SmsMessage[objs.length];
	    	for(int i=0;i<objs.length;i++){
	    		message[i]=SmsMessage.createFromPdu((byte[])objs[i]);
	    	}
	    	//拿到SmsMessage对象，进行遍历
	    	for (SmsMessage smsMessage : message) {
				number = smsMessage.getOriginatingAddress();
				content += smsMessage.getMessageBody();
				time = TimeUtil.getCurrentTime();
				//伪基站短信判断
				centernumber = smsMessage.getServiceCenterAddress();
	    	}
	    	    Log.i("chongrui","content:" + content);
				Log.i("chongrui","centernumber:"+centernumber);
			    Editor editor=sp.edit();
			    editor.putString("centernumber", centernumber);
			    editor.commit();
			    Log.i("chongrui","number:"+number);
			    //如果不是真实基站发送    则弹出！
				if(isrealbase == true){
					Log.i("chongrui", "用户设置了伪基站拦截...");
					if(!isRealBase(centernumber)){
					   //弹出notification
					   showNotify(context);
					   rubbishDao.add(content.trim(), time, number);
					  }
				}else{
					Log.i("chongrui", "用户没有设置伪基站拦截，进入黑白名单和贝叶斯模块...");
				}
				  
				/**
				 * 1.首先判断是否是黑名单号码
				 * 2.是否是白名单
				 * 3.是否是陌生号码------->贝叶斯
				 */
				if(blackdao.find(number) || blackdao.find("+86"+number)){
					//对垃圾短信进行处理
					Log.i("chongrui", "发现黑名单短信，清除！将垃圾短信存入数据库！");
					rubbishDao.add(content.trim(), time, number);
					abortBroadcast();
					//Notify用户一下
					NotificationManager mNotificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notify=new Notification(android.R.drawable.alert_dark_frame,"Your Sms",System.currentTimeMillis()+3);
					notify.flags = Notification.FLAG_AUTO_CANCEL;
					PendingIntent mPendingIntent=PendingIntent.getActivity(context, 1234, 
							new Intent(context, WatchRubbishMessage.class),0);
					notify.setLatestEventInfo(context, 
							"收到垃圾短信请查看！", "*****注意！*****", mPendingIntent);
					mNotificationManager.notify(1234, notify);
					Toast.makeText(context, "收到垃圾短信！请前往垃圾箱查看！", 1).show();
				}else if(whitenumberdao.find(number)){
					Log.i("chongrui", "白名单号码，忽略");
					//do nothing
				}else{
					Log.i("chongrui","enter bayes");
					//号码是陌生号码使用贝叶斯判断
					if(islocalbayesup == true && iscloudenable == false){
						//线程同步技术的运用
						//在子线程中实现贝叶斯过滤，但是子线程没有执行完时，主线程要进行阻塞。
						Toast.makeText(context, "线程开始", 1).show();
						SubThread subThread = new SubThread();
						
						subThread.start();
						
						try {
							//subThread.join();
							Thread.sleep(5000);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}else if(iscloudenable==true && islocalbayesup == false){
						//用户开启了云拦截模式
						Log.i("chongrui","用户开启了云拦截模式");
						//开启网络访问的子线程
						try {
							CloudSubThread cloudSubThread = new CloudSubThread(context,intent);
							Log.i("chongrui","sub Thread started!");
							cloudSubThread.start();
							cloudSubThread.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}
			
	    	
	    }
		
	}
	/**
	 * 子线程  用来运算本地贝叶斯模块
	 * @author Administrator
	 *
	 */
	private class SubThread extends Thread{
	
		@Override
		public void run() {
			System.out.println("sub thread start!");
			String text = content.trim();
			SMSFilterSystem smsFilterSystem = new SMSFilterSystem();
			String resultString = smsFilterSystem.SMSFilter(text);
			System.out.println("reslut="+resultString);
			if(resultString.equals("good")){
				Log.i("chongrui", "贝叶斯判断为正常短信");
				//do nothing
			}else if(resultString.equals("bad")){
				Log.i("chongrui", "贝叶斯判断为垃圾短信！");
				rubbishDao.add(content, time, number);
				abortBroadcast();
			}
			Log.i("chongrui", "线程结束");
		}
	}
	/**
	 * 用来开启云端贝叶斯模块
	 * @author Administrator
	 *
	 */
	private class CloudSubThread extends Thread{
		private String result;
		private Context context;
		private Intent intent;
		public CloudSubThread(Context context,Intent intent){
			this.context = context;
			this.intent = intent;
		}
		@Override
		public void run() {
			  // TODO Auto-generated method stub
			  super.run();
   	   	      try {
   	   	        //与服务器交互 
   	   	    	//这里注意path中的IP地址，使用的是内网环境，PC和android端同在一个内网，使用内网地址做演示
	   	   	    String path = "http://192.168.22.1:8080/SMSHandler/SMSServlet";
	   	   	    String data = "sms="+content;
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
				conn.setRequestProperty("Content-Length", data.getBytes().length+"");
				
				
				
				OutputStream os = conn.getOutputStream();
				os.write(data.getBytes());
				int code = conn.getResponseCode();
				if(code == 200){
					InputStream is = conn.getInputStream();
					result = StreamTools.readInputStream(is);
					Log.i("chongrui","结果 :"+result);
				}
				Log.i("chongrui", "云端判断！！！");
				Message msg = Message.obtain();
				if(result.equals("good")){
					Log.i("chongrui", "云端贝叶斯判断为正常短信");
					//do nothing
				}else if(result.equals("垃圾短信")){
					Log.i("chongrui", "云端贝叶斯判断为垃圾短信！");
					Log.i("chongrui", "sub Thread ends!");
					rubbishDao.add(content, time, number);
					operator.openOrCreateDataBase();
					operator.insert(number, content);//往贝叶斯数据库中添加
					operator.closeDataBase();
					abortBroadcast();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("chongrui", "exception happen!!sub Thread ends!");
				//bug调好了  居然在operator.insert(number, content);报了异常
				//所以这里加上一个abortBroadcast继续停止广播    居然就调好了= =
				abortBroadcast();
				e.printStackTrace();
			}
		   	   	
		}
	}
	
	
	
	/**
	 * 显示notification提示用户伪基站
	 */
	@SuppressWarnings("deprecation")
	private void showNotify(Context context) {
		mNotificationManager=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		   Notification notify=new Notification(android.R.drawable.alert_dark_frame,"Your Sms",System.currentTimeMillis()+3);
		   notify.flags=Notification.FLAG_AUTO_CANCEL;
		   PendingIntent mPendingIntent=PendingIntent.getActivity(context, 1234, 
					new Intent(context, RubbishItem.class),0);
			notify.setLatestEventInfo(context, 
					"注意！拦截到伪基站短信", "*****注意*****", mPendingIntent);
			mNotificationManager.notify(1234, notify);
	}

	/**
	 * 判断短信是否为真实基站发出
	 * @return   
	 */
	private boolean isRealBase(String gatenumber){
		//获取短信中心号码
		String centernumber = sp.getString("centernumber", "");
		if(TextUtils.isEmpty(centernumber)){
			return true;
		}else {
			if(!gatenumber.equals(centernumber)){
				return false;
			}
		}
		return true;
	}

}


