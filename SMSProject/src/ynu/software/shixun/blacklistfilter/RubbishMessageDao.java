package ynu.software.shixun.blacklistfilter;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RubbishMessageDao{

	Context context;
	RubbishMessageDBHelper helper;
	public RubbishMessageDao(Context context,RubbishMessageDBHelper helper){
		this.context = context;
		this.helper=helper;
	}
	
	/**
	 * 将垃圾短信加入数据库
	 * @param number
	 */
	public void add(String content,String time,String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("insert into rubbishmessage (content,time,number) values(?,?,?)",new String[]{content,time,number});
			db.close();
		}
	}
	/**
	 * 将短信清除删除
	 * @param number
	 */
	public void delete(String content) {
		SQLiteDatabase db=helper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("delete from rubbishmessage where content=?", new String[]{content});
			db.close();
		}
	}

	/**
	 * 返回所有的垃圾短信
	 * @return  黑名单号码集
	 */
	public List<RubbishMessage> getMessages() {
		SQLiteDatabase db =helper.getReadableDatabase();
		List<RubbishMessage> messages = new ArrayList<RubbishMessage>();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from rubbishmessage", null);
			while (cursor.moveToNext()) {
				String  number = cursor.getString(cursor.getColumnIndex("number"));
				String content = cursor.getString(cursor.getColumnIndex("content"));
				String time = cursor.getString(cursor.getColumnIndex("time"));
				
				RubbishMessage message= new RubbishMessage();
				message.setContent(content);
				message.setNumber(number);
				message.setTime(time);
				messages.add(message);
			}	
			cursor.close();
		}
		db.close();
		
		return messages;
	}
	
}
















































