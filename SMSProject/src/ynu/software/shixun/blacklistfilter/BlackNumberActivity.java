package ynu.software.shixun.blacklistfilter;
import java.util.ArrayList;
import java.util.List;

import ynu.software.shixun.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class BlackNumberActivity extends Activity implements OnClickListener {

	private Button bt_add_balcknumber;
	private Button bt_rubbishmessage_entry;
	private Button bt_add_blacknumber_from_contact;
	private EditText et_blacknumber; 
	private BlackNumberDao dao;
	private ListView lv_blacknumber;
	private BlackNumberAdapter adapter;
	private List<String> numbers=  new ArrayList<String>();
	private String blacknumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.blacknumber);
		//数据库接口类
		dao= new BlackNumberDao(BlackNumberActivity.this, new BlackNumberDBHelper(BlackNumberActivity.this));
		lv_blacknumber=(ListView) this.findViewById(R.id.lv_blacknumber);
		registerForContextMenu(lv_blacknumber);
		
		
		bt_add_balcknumber=(Button) this.findViewById(R.id.bt_add_balcknumber);
		bt_rubbishmessage_entry=(Button) this.findViewById(R.id.bt_rubbishmessage_entry);
		bt_add_blacknumber_from_contact = (Button) this.findViewById(R.id.bt_add_from_contact);
		bt_rubbishmessage_entry.setOnClickListener(this);
		bt_add_balcknumber.setOnClickListener(this);
		bt_add_blacknumber_from_contact.setOnClickListener(this);
		numbers=dao.getNumbers();
		adapter= new BlackNumberAdapter();
		lv_blacknumber.setAdapter(adapter);
	}
	
	/**
	 * 添加黑名单号码
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.bt_add_balcknumber:
			//弹出对话框添加黑名单号码
			et_blacknumber= new EditText(BlackNumberActivity.this);
			AlertDialog.Builder builder = new AlertDialog.Builder(BlackNumberActivity.this);
			builder.setTitle("请输入黑名单号码");
			builder.setView(et_blacknumber);
			builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String number = et_blacknumber.getText().toString();
					if(TextUtils.isEmpty(number)){
						Toast.makeText(getApplicationContext(), "号码不能为空！", 1).show();
					}
					blacknumber=number;
					dao.add(number);
					numbers=dao.getNumbers();
					dialog.dismiss();
					adapter.notifyDataSetChanged();
				}
			});
			builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			
			builder.create();
			builder.show();
			break;
        //进入垃圾邮箱
		case R.id.bt_rubbishmessage_entry:
			Intent intent = new Intent(BlackNumberActivity.this,WatchRubbishMessage.class);
			startActivity(intent);
			break;
			
	    //从联系人添加
		case R.id.bt_add_from_contact:
			Intent intent2 = new Intent(BlackNumberActivity.this,AddBlackNumberFromContact.class);
			startActivityForResult(intent2, 0);
			break;
		}
		
		
	}
	
	
	/*
	   * 创建上下文菜单
	   * */
	  @Override
	  public void onCreateContextMenu(ContextMenu menu, View v,
	                                  ContextMenuInfo menuInfo) {
	      super.onCreateContextMenu(menu, v, menuInfo);
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.black_menu, menu);
	  }
	
	  /*
	   * 处理菜单的点击事件
	   * */
	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
	      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	      //当前条目的id
  	  int id=(int) info.id;
  	  String number = numbers.get(id);
	      switch (item.getItemId()) {
	          case R.id.delete_number:
	              
	        	  dao.delete(number);
	        	  
	        	  //通知listview更新界面
	        	  numbers = dao.getNumbers();
	        	  adapter.notifyDataSetChanged();
	              break;
	      }
	      
	      return false;
	  }
	
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		System.out.println("hello");
		if(resultCode==0 && data != null){
			String number = data.getStringExtra("number");
			dao.add(number);
			numbers = dao.getNumbers();
			adapter.notifyDataSetChanged();
		}else if(resultCode == 1 && data!=null){
			//导入成功
			numbers = (List<String>) data.getSerializableExtra("newnumbers");
			adapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}





	private class BlackNumberAdapter extends BaseAdapter{

		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return numbers.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return numbers.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(BlackNumberActivity.this, R.layout.blacknumber_item, null);
			TextView tv = (TextView) view.findViewById(R.id.tv_blacknumber_item);
			tv.setText(numbers.get(position));
			return view;
		}
		
	}
	
	
	
	
}































































