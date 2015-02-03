package ynu.software.shixun.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBoperator {

	private Context mContext;
	private SQLiteDatabase mSqLiteDatabase;
	
	private String DATABASE_NAME = "bayesmessage";
	private String TABLE_NAME = "message";
	public String COLUMN_ID = "_id";
	public String COLUMN_NUMBER = "number";
	public String COLUMN_CONTENT = "content";
	public String COLUMN_LEARNED = "learned";
	
	public DBoperator (Context context){
		mContext = context;
	}
	
	public void openOrCreateDataBase(){
		mSqLiteDatabase = mContext.openOrCreateDatabase(DATABASE_NAME, mContext.MODE_PRIVATE, null);
		if(!tabIsExist(TABLE_NAME)){
		mSqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+"("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COLUMN_NUMBER+" VARCHAR(20) NOT NULL,"
				+COLUMN_CONTENT+" VARCHAR(100) NOT NULL,"+COLUMN_LEARNED+" VARCHAR(5) NOT NULL)");
		}
	}
		
	public  long insert(String num,String content){
		ContentValues mContentValues = new ContentValues();
		mContentValues.put(COLUMN_NUMBER, num);
		mContentValues.put(COLUMN_CONTENT, content);
		mContentValues.put(COLUMN_LEARNED, "N");
		return mSqLiteDatabase.insert(TABLE_NAME, null, mContentValues);
	}
	
	 public boolean tabIsExist(String tabName){
         boolean result = false;
         if(tabName == null){
                 return false;
         }
         Cursor cursor = null;
         try {
                
                 String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"' ";
                 cursor = mSqLiteDatabase.rawQuery(sql, null);
                 if(cursor.moveToNext()){
                         int count = cursor.getInt(0);
                         if(count>0){
                                 result = true;
                         }
                 }
                 
         } catch (Exception e) {
                 // TODO: handle exception
         }                
         return result;
 }
	
	public void delete(long _id){
		this.openOrCreateDataBase();
		mSqLiteDatabase.delete(TABLE_NAME, COLUMN_ID+"=?", new String[]{String.valueOf(_id)});
		this.closeDataBase();
	}
	
	public void update(long _id){
		this.openOrCreateDataBase();
		ContentValues mContentValues=new ContentValues();
		mContentValues.put(COLUMN_LEARNED,"Y");
		mSqLiteDatabase.update(TABLE_NAME, mContentValues, COLUMN_ID+"=?", new String[]{String.valueOf(_id)});
		this.closeDataBase();
	}
	
	public Cursor selectAll(){
		return mSqLiteDatabase.query(TABLE_NAME, new String[]{COLUMN_NUMBER,COLUMN_CONTENT,COLUMN_ID,COLUMN_LEARNED}, null, null, null, null, null);
	}
	
	public void closeDataBase(){
		mSqLiteDatabase.close();
		mSqLiteDatabase=null;
	}
}
