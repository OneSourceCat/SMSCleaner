package ynu.software.shixun.blacklistfilter;

import java.util.ArrayList;
import java.util.List;

import ynu.software.shixun.R;

import com.android.*;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WatchRubbishMessage extends Activity {

	private ListView lv_rubbishmessage;
	private RubbishMessageDao dao;
	private List<RubbishMessage> messages;
	private RubbishMessageAdapter adapter= new RubbishMessageAdapter();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//设置全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置布局文件
		setContentView(R.layout.rubbishmessage);
		//数据库接口
		dao= new RubbishMessageDao(WatchRubbishMessage.this, 
				new RubbishMessageDBHelper(WatchRubbishMessage.this));
		//寻找ListView控件
		lv_rubbishmessage=(ListView) this.findViewById(R.id.lv_rubbishmessage);
		//为listview注册上下文菜单
		registerForContextMenu(lv_rubbishmessage);
		//更新数据源集合
		messages=dao.getMessages();
		//为ListView设置数据适配器
		lv_rubbishmessage.setAdapter(adapter);
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		dao= new RubbishMessageDao(WatchRubbishMessage.this, new RubbishMessageDBHelper(WatchRubbishMessage.this));
		registerForContextMenu(lv_rubbishmessage);
		messages=dao.getMessages();
		lv_rubbishmessage.setAdapter(adapter);
	}


	/*
	   * 创建上下文菜单
	   * */
	  @Override
	  public void onCreateContextMenu(ContextMenu menu, View v,
	                                  ContextMenuInfo menuInfo) {
	      super.onCreateContextMenu(menu, v, menuInfo);
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.context_menu, menu);
	  }
	
	  /*
	   * 处理菜单的点击事件
	   * */
	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
	      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	      //当前条目的id
    	  int id=(int) info.id;
    	  RubbishMessage message = messages.get(id);
    	  String content=message.getContent();
	      switch (item.getItemId()) {
	          case R.id.watch_message:
	              //进入查看的activity查看message
	        	  Intent intent = new Intent(WatchRubbishMessage.this,RubbishItem.class);
	        	  intent.putExtra("message", content);
	        	  System.out.println("message--number:"+message.getNumber());
	        	  intent.putExtra("number", message.getNumber());
	        	  startActivity(intent);
	              break;
	              //删除垃圾邮箱中的短信
	          case R.id.delete_message:
	        	  dao.delete(message.getContent());
	        	  //通知listview更新界面
	        	  messages=dao.getMessages();
	        	  adapter.notifyDataSetChanged();
	              break;
	              //短信恢复
	          case R.id.back_message:
	        	  ContentResolver resolver = getContentResolver();
	        	  ContentValues value = new ContentValues();
	        	  value.put("body", message.getContent());
	        	  value.put("address", message.getNumber());
	        	  value.put("date", message.getTime());
	        	  resolver.insert(Uri.parse("content://sms/"), value);
	        	  Toast.makeText(getApplicationContext(), "恢复成功", 1).show();
	        	  dao.delete(message.getContent());
	        	  messages = dao.getMessages();
	        	  adapter.notifyDataSetChanged();
	        	  value=null;
	        	  break;
	      }
	      
	      return false;
	  }
	  
	  
	private class RubbishMessageAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return messages.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return messages.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(WatchRubbishMessage.this, R.layout.rubbishmessage_item, null);
			TextView tv = (TextView) view.findViewById(R.id.tv_rubbishmessage_item);
			tv.setText(messages.get(position).getContent());
			return view;
		}
		
	}
	
}




















































