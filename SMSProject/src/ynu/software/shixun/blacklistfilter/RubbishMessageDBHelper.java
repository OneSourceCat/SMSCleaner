package ynu.software.shixun.blacklistfilter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RubbishMessageDBHelper extends SQLiteOpenHelper {

	public RubbishMessageDBHelper(Context context) {
		super(context, "rubbishmessage.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
        db.execSQL("create table rubbishmessage (_id integer primary key autoincrement,type varchar(3),time varchar(15),number varchar(15),content varchar(200))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
