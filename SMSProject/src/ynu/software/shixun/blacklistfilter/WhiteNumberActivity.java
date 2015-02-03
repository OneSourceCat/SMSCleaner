package ynu.software.shixun.blacklistfilter;

import java.util.List;

import ynu.software.shixun.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class WhiteNumberActivity extends Activity implements OnClickListener {
	private ListView lv_whitenumber;
	private Button bt_add_allcontact;
	private List<ContactInfo> infos;
	private WhiteNumberDao dao;
	private List<String> numbers;
	private MyListAdapter adapter;
	private Button bt_add_whitenumber;
	private EditText et_whitenumber; 
	private String whitenumebr;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
				Toast.makeText(getApplicationContext(), "����ɹ�", 1).show();
				numbers = dao.getNumbers();
				adapter.notifyDataSetChanged();
			}
		};
	}; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.whitenumber);
		dao = new WhiteNumberDao(this, new WhiteNumberDBHelper(this));
		
		lv_whitenumber = (ListView) this.findViewById(R.id.lv_whitenumber1);
		bt_add_allcontact = (Button) this.findViewById(R.id.bt_add_allcontact);
		bt_add_whitenumber  =(Button) this.findViewById(R.id.bt_add_whitenumber);
		//ע�������Ĳ˵�
		registerForContextMenu(lv_whitenumber);
		
		numbers = dao.getNumbers();
		adapter = new MyListAdapter();
		lv_whitenumber.setAdapter(adapter);
		//��Ӱ��������� 
		bt_add_whitenumber.setOnClickListener(this);
		//һ�����������
		bt_add_allcontact.setOnClickListener(this);

	}
	
	/**
	 * ������ֵ���¼�
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_add_allcontact:
			new Thread(){
				public void run(){
					ContactInfoProvider provider = new ContactInfoProvider(WhiteNumberActivity.this);
					infos = provider.getContactInfos();
					Log.i("chongrui", "info:"+infos);
					for(int i=0;i<infos.size();i++){
						dao.add(infos.get(i).getNumber());
						Log.i("chongrui", "sb:"+infos.get(i).getNumber());
					}
					
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
				
			}.start();
			break;

		case R.id.bt_add_whitenumber:
			//�����Ի�����Ӻ���������
			et_whitenumber= new EditText(WhiteNumberActivity.this);
			AlertDialog.Builder builder = new AlertDialog.Builder(WhiteNumberActivity.this);
			builder.setTitle("�������ֻ�����");
			builder.setView(et_whitenumber);
			builder.setPositiveButton("ȷ��", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String number = et_whitenumber.getText().toString();
					if(TextUtils.isEmpty(number)){
						Toast.makeText(getApplicationContext(), "���벻��Ϊ�գ�", 1).show();
					}
					whitenumebr=number;
					dao.add(number);
					numbers=dao.getNumbers();
					dialog.dismiss();
					adapter.notifyDataSetChanged();
				}
			});
			builder.setNegativeButton("ȡ��", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			
			builder.create();
			builder.show();
			break;
		}
	}
	
	
	private class MyListAdapter extends BaseAdapter{

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
			View view = View.inflate(WhiteNumberActivity.this, R.layout.whitenumber_item, null);
			TextView tv = (TextView) view.findViewById(R.id.tv_whitenumber_item);
			tv.setText(numbers.get(position));
			return view;
		}
		
	}
	
	
	
	/*
	   * ���������Ĳ˵�
	   * */
	  @Override
	  public void onCreateContextMenu(ContextMenu menu, View v,
	                                  ContextMenuInfo menuInfo) {
	      super.onCreateContextMenu(menu, v, menuInfo);
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.white_menu, menu);
	  }
	
	  /*
	   * ����˵��ĵ���¼�
	   * */
	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
	      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	      //��ǰ��Ŀ��id
	  int id=(int) info.id;
	  String number = numbers.get(id);
	      switch (item.getItemId()) {
	          case R.id.delete_whitenumber:
	              
	        	  dao.delete(number);
	        	  
	        	  //֪ͨlistview���½���
	        	  numbers = dao.getNumbers();
	        	  adapter.notifyDataSetChanged();
	              break;
	      }
	      
	      return false;
	  }
    
	
	
	
	
}































