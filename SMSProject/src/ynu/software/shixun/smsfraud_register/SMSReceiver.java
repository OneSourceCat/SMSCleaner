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
	private SharedPreferences sp;   //����������ĵĺ���
	private SharedPreferences bayessp;
	private boolean isrealbase;//�Ƿ���α��վ����
	private boolean islocalbayesup; //�Ƿ���ȫ������
	private boolean isSMSFraud;//�Ƿ���������թ����
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
		//����©������
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
		//�ڰ������ͱ�Ҷ˹���߼�
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
	    	//�õ�SmsMessage���󣬽��б���
	    	for (SmsMessage smsMessage : message) {
				number = smsMessage.getOriginatingAddress();
				content += smsMessage.getMessageBody();
				time = TimeUtil.getCurrentTime();
				//α��վ�����ж�
				centernumber = smsMessage.getServiceCenterAddress();
	    	}
	    	    Log.i("chongrui","content:" + content);
				Log.i("chongrui","centernumber:"+centernumber);
			    Editor editor=sp.edit();
			    editor.putString("centernumber", centernumber);
			    editor.commit();
			    Log.i("chongrui","number:"+number);
			    //���������ʵ��վ����    �򵯳���
				if(isrealbase == true){
					Log.i("chongrui", "�û�������α��վ����...");
					if(!isRealBase(centernumber)){
					   //����notification
					   showNotify(context);
					   rubbishDao.add(content.trim(), time, number);
					  }
				}else{
					Log.i("chongrui", "�û�û������α��վ���أ�����ڰ������ͱ�Ҷ˹ģ��...");
				}
				  
				/**
				 * 1.�����ж��Ƿ��Ǻ���������
				 * 2.�Ƿ��ǰ�����
				 * 3.�Ƿ���İ������------->��Ҷ˹
				 */
				if(blackdao.find(number) || blackdao.find("+86"+number)){
					//���������Ž��д���
					Log.i("chongrui", "���ֺ��������ţ���������������Ŵ������ݿ⣡");
					rubbishDao.add(content.trim(), time, number);
					abortBroadcast();
					//Notify�û�һ��
					NotificationManager mNotificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notify=new Notification(android.R.drawable.alert_dark_frame,"Your Sms",System.currentTimeMillis()+3);
					notify.flags = Notification.FLAG_AUTO_CANCEL;
					PendingIntent mPendingIntent=PendingIntent.getActivity(context, 1234, 
							new Intent(context, WatchRubbishMessage.class),0);
					notify.setLatestEventInfo(context, 
							"�յ�����������鿴��", "*****ע�⣡*****", mPendingIntent);
					mNotificationManager.notify(1234, notify);
					Toast.makeText(context, "�յ��������ţ���ǰ��������鿴��", 1).show();
				}else if(whitenumberdao.find(number)){
					Log.i("chongrui", "���������룬����");
					//do nothing
				}else{
					Log.i("chongrui","enter bayes");
					//������İ������ʹ�ñ�Ҷ˹�ж�
					if(islocalbayesup == true && iscloudenable == false){
						//�߳�ͬ������������
						//�����߳���ʵ�ֱ�Ҷ˹���ˣ��������߳�û��ִ����ʱ�����߳�Ҫ����������
						Toast.makeText(context, "�߳̿�ʼ", 1).show();
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
						//�û�������������ģʽ
						Log.i("chongrui","�û�������������ģʽ");
						//����������ʵ����߳�
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
	 * ���߳�  �������㱾�ر�Ҷ˹ģ��
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
				Log.i("chongrui", "��Ҷ˹�ж�Ϊ��������");
				//do nothing
			}else if(resultString.equals("bad")){
				Log.i("chongrui", "��Ҷ˹�ж�Ϊ�������ţ�");
				rubbishDao.add(content, time, number);
				abortBroadcast();
			}
			Log.i("chongrui", "�߳̽���");
		}
	}
	/**
	 * ���������ƶ˱�Ҷ˹ģ��
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
   	   	        //����������� 
   	   	    	//����ע��path�е�IP��ַ��ʹ�õ�������������PC��android��ͬ��һ��������ʹ��������ַ����ʾ
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
					Log.i("chongrui","��� :"+result);
				}
				Log.i("chongrui", "�ƶ��жϣ�����");
				Message msg = Message.obtain();
				if(result.equals("good")){
					Log.i("chongrui", "�ƶ˱�Ҷ˹�ж�Ϊ��������");
					//do nothing
				}else if(result.equals("��������")){
					Log.i("chongrui", "�ƶ˱�Ҷ˹�ж�Ϊ�������ţ�");
					Log.i("chongrui", "sub Thread ends!");
					rubbishDao.add(content, time, number);
					operator.openOrCreateDataBase();
					operator.insert(number, content);//����Ҷ˹���ݿ������
					operator.closeDataBase();
					abortBroadcast();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("chongrui", "exception happen!!sub Thread ends!");
				//bug������  ��Ȼ��operator.insert(number, content);�����쳣
				//�����������һ��abortBroadcast����ֹͣ�㲥    ��Ȼ�͵�����= =
				abortBroadcast();
				e.printStackTrace();
			}
		   	   	
		}
	}
	
	
	
	/**
	 * ��ʾnotification��ʾ�û�α��վ
	 */
	@SuppressWarnings("deprecation")
	private void showNotify(Context context) {
		mNotificationManager=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		   Notification notify=new Notification(android.R.drawable.alert_dark_frame,"Your Sms",System.currentTimeMillis()+3);
		   notify.flags=Notification.FLAG_AUTO_CANCEL;
		   PendingIntent mPendingIntent=PendingIntent.getActivity(context, 1234, 
					new Intent(context, RubbishItem.class),0);
			notify.setLatestEventInfo(context, 
					"ע�⣡���ص�α��վ����", "*****ע��*****", mPendingIntent);
			mNotificationManager.notify(1234, notify);
	}

	/**
	 * �ж϶����Ƿ�Ϊ��ʵ��վ����
	 * @return   
	 */
	private boolean isRealBase(String gatenumber){
		//��ȡ�������ĺ���
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


