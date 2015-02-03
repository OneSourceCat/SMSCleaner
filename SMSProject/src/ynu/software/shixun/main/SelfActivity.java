package ynu.software.shixun.main;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import ynu.software.shixun.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelfActivity extends Activity {
	Button selfLearningButton;
	public static ArrayList<Item> items=null;
	public final static String SMS_URI_INBOX = "content://sms/inbox";
	private String[] projection ;
	static String mSendString;
	static StringBuilder mStringBuilder;
	public static DBoperator mDBoperator;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(getApplicationContext(), "上传成功", 1).show();
				break;

			case 0:
				Toast.makeText(getApplicationContext(), "上传失败", 1).show();
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//设置无标题显示
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		//设置全屏显示
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);    
		//设置布局文件
		setContentView(R.layout.activity_self);
		projection = new String[]{"_id","body"};
		//显示Button
		ShowButton();
			
			items=new ArrayList<Item>();
			mDBoperator=new DBoperator(SelfActivity.this);
			mDBoperator.openOrCreateDataBase();
//			mDBoperator.insert("11111", "22222222");
//			mDBoperator.insert("11111", "22222222");
//			mDBoperator.insert("11111", "22222222");
//			mDBoperator.insert("11111", "22222222");
//			mDBoperator.insert("11111", "22222222");
//			mDBoperator.insert("11111", "22222222");
			Cursor mCursor=mDBoperator.selectAll();
			Log.i("chongrui", "Cursor="+mCursor);
			mCursor.moveToFirst();
			int id=mCursor.getColumnIndex(mDBoperator.COLUMN_ID);
			int num=mCursor.getColumnIndex(mDBoperator.COLUMN_NUMBER);
			int content=mCursor.getColumnIndex(mDBoperator.COLUMN_CONTENT);
			int learned=mCursor.getColumnIndex(mDBoperator.COLUMN_LEARNED); 
			Log.i("chongrui", "learned="+learned);
			if(mCursor.getCount()!=0){
			do{
				
				boolean tmpLearned;
				if(mCursor.getString(learned).equals("Y")){
					tmpLearned=true;
				}else {
					tmpLearned=false;
				}
				
				items.add(new Item(mCursor.getString(num), mCursor.getString(content), mCursor.getLong(id), tmpLearned));
				Log.i("chongrui", mCursor.getString(content));
			}while(mCursor.moveToNext());
			}
			mDBoperator.closeDataBase();
	}
	
	void ShowButton(){
		selfLearningButton=(Button) findViewById(R.id.button_selflearning);
		selfLearningButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("mmy", "clicked1");
				Dialog dialog=createLoadingDialog(MainActivity_message.context, "请稍等");
				putFiles();
				Log.v("mmy", "clicked2");
			}
		});
	}
	
	public void putFiles(){
		int i=0;
		mStringBuilder = new StringBuilder();
		Item tmpItem;
		if(SelfActivity.items==null){
			Log.i("chongrui", "return");
			return;
		}
		Iterator<Item> mIterator=SelfActivity.items.iterator();
		while(mIterator.hasNext()){
//			if((tmpItem=mIterator.next()).learned==false){
				tmpItem=mIterator.next();
				++i;
				Log.i("chongrui", "bad");
				tmpItem.learned=true;
				SelfActivity.mDBoperator.update(tmpItem._id);
				putNegativeMessage(tmpItem._id, tmpItem.text);
			}
//		}
		putPositiveMessage(i);
				
	}
	
	private void putPositiveMessage(int n){
		int i=0;
		if(n==0){
			return;
		}
		ContentResolver mContentResolver=SelfActivity.this.getContentResolver();
		Cursor mCursor=mContentResolver.query(Uri.parse(SMS_URI_INBOX), projection, null, null, null);
		int bodyIndex=mCursor.getColumnIndex("body");
		int id=mCursor.getColumnIndex("_id");
			mCursor.moveToFirst();
			do{
				Log.i("chongrui", "good");
				++i;
				mStringBuilder.append("1"+mCursor.getInt(id)+"|");
				mStringBuilder.append(mCursor.getString(bodyIndex)+"|");
			}while(mCursor.moveToNext()&&i<n);
		mSendString = mStringBuilder.toString();
		
		Log.i("chongrui", "threadstartbefore");
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				Log.i("chongrui", "threadstart");
				Message msg=Message.obtain();
				String path = "http://192.168.22.1:8080/SMSHandler/UploadStringServlet";
				URL url;
				
				try {
					String data = "strings="+mSendString;
					url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setDoInput(true);
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
					//conn.setRequestProperty("Content-Length", mSendString.getBytes().length+"");
				    
					OutputStream os = conn.getOutputStream();
					os.write(data.getBytes());
					int code = conn.getResponseCode();
					if(code == 200){
						Log.i("chongrui", "200");
						msg.what=1;					
						handler.sendMessage(msg);
					}else{
						Log.i("chongrui", "!200");
						msg.what=0;
						handler.sendMessage(msg);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					msg.what=0;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
				
				
			}
		}.start();
		
	}
	
	private void putNegativeMessage(long _id,String content){
		_id+=1000;
		mStringBuilder.append("0"+_id+"|");
		mStringBuilder.append(content+"|");
	}
	
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, R.anim.loading_animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;

	}
}
