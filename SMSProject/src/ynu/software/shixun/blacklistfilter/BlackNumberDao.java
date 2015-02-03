package ynu.software.shixun.blacklistfilter;

import java.util.ArrayList;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlackNumberDao{

	Context context;
	BlackNumberDBHelper helper;
	public BlackNumberDao(Context context,BlackNumberDBHelper helper){
		this.context = context;
		this.helper=helper;
	}
	/**
	 * �鿴ָ���绰�����Ƿ��ں������г���
	 * @param number
	 * @return boolean
	 */
	public boolean find(String number) {
		SQLiteDatabase db = helper.getReadableDatabase();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select number from blacknumber where number=?", new String[]{number});
			if(cursor.moveToFirst()){
				return true;
			}
			db.close();
			cursor.close();
		}
	    return false;
		
	}
	
	/**
	 * ��������������
	 * @param number
	 */
	public void add(String number) {
		if(find(number)){
		   return;
	    }
		
		SQLiteDatabase db = helper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("insert into blacknumber (number) values(?)",new String[]{number});
			db.close();
		}
	}
	/**
	 * ������ɾ��
	 * @param number
	 */
	public void delete(String number) {
		SQLiteDatabase db=helper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("delete from blacknumber where number=?", new String[]{number});
			db.close();
		}
	}
	/**
	 * ���º�����
	 * @param oldnumber
	 * @param newnumber
	 */
	public void update(String oldnumber,String newnumber){
		SQLiteDatabase db = helper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("update blacknumber set number=? where number=?", new String[]{oldnumber,newnumber});
		    db.close();
		}
	}
	/**
	 * �������еĺ���
	 * @return  ���������뼯
	 */
	public List<String> getNumbers() {
		SQLiteDatabase db =helper.getReadableDatabase();
		List<String> numbers = new ArrayList<String>();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select number from blacknumber", null);
			while (cursor.moveToNext()) {
				String  number = cursor.getString(cursor.getColumnIndex("number"));
				numbers.add(number);
			}	
			cursor.close();
		}
		db.close();
		
		return numbers;
	}
	
}
















































