package ynu.software.shixun.main;

import java.util.ArrayList;



import ynu.software.shixun.R;
import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MessageActivity extends Activity {
	public static ArrayList<Item> items=null;
	ListView listView=null;
	public static DBoperator mDBoperator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    
		setContentView(R.layout.activity_message);
		items=new ArrayList<Item>();
		listView=(ListView) findViewById(R.id.messagelist);
		mDBoperator=new DBoperator(MessageActivity.this);
		mDBoperator.openOrCreateDataBase();
		mDBoperator.insert("11111", "22222222");
		mDBoperator.insert("11111", "22222222");
		mDBoperator.insert("11111", "22222222");
		mDBoperator.insert("11111", "22222222");
		mDBoperator.insert("11111", "22222222");
		mDBoperator.insert("11111", "22222222");
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
		}while(mCursor.moveToNext());
		}
		mDBoperator.closeDataBase();
		ShowItem();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("mmy", "before showitem");
		ShowItem();
	}
	
	public void ShowItem(){
		final LayoutInflater inflater = getLayoutInflater();
		final Item[] itemsarray = items.toArray(new Item[items.size()]);
		
		ListAdapter adapter=new ArrayAdapter<Item>(this, R.layout.messageitem,R.id.messagenum ,itemsarray){

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				final ListEntry entry;
				if(convertView==null){
					convertView=inflater.inflate(R.layout.messageitem, parent,false);
					entry=new ListEntry();
					entry.num=(TextView) convertView.findViewById(R.id.messagenum);
					entry.text=(TextView)convertView.findViewById(R.id.messagetext);
					entry.zoom=(ImageButton)convertView.findViewById(R.id.messagebutton);
					entry.zoom.setOnClickListener(new OnClickListener() {						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub	
							new AlertDialog.Builder(MessageActivity.this).setTitle(entry.num.getText().toString()).setMessage(entry.text.getText().toString())
							.setPositiveButton("É¾³ý", new DialogInterface.OnClickListener(){ 
								@Override 
				                public void onClick(DialogInterface dialog, int which) { 
									items.remove(position);
									mDBoperator.delete(itemsarray[position]._id);
									ShowItem();
				                	// TODO Auto-generated method stub  
				                } 
				            }).setNegativeButton("È¡Ïû", new DialogInterface.OnClickListener() {								
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
								}
							}).create().show();
						}
					});
					convertView.setTag(entry);
				}else{
					entry=(ListEntry) convertView.getTag();
				}
				entry.num.setText(itemsarray[position].num);
				entry.text.setText(itemsarray[position].text);
				return super.getView(position, convertView, parent);
			}
			
		};
		this.listView.setAdapter(adapter);
	}
	
	
	private class ListEntry {
		TextView num;
		TextView text;
		ImageButton zoom;
	}
}
