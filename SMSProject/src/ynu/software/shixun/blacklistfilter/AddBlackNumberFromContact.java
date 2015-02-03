package ynu.software.shixun.blacklistfilter;
import java.io.Serializable;
import java.util.List;

import ynu.software.shixun.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class AddBlackNumberFromContact extends Activity implements OnClickListener {
	private ListView lv_contact;
	private Button bt_add_once;
	private List<ContactInfo> infos;
	private ContactAdapter adapter;
	private BlackNumberDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_number_from_contact);
		ContactInfoProvider provider = new ContactInfoProvider(this);
		infos = provider.getContactInfos(); //得到联系信息
		dao = new BlackNumberDao(AddBlackNumberFromContact.this, new BlackNumberDBHelper(AddBlackNumberFromContact.this));
		//设置控件
		lv_contact = (ListView) this.findViewById(R.id.lv_contact_item);
		bt_add_once = (Button) this.findViewById(R.id.bt_add_onece);
		//设置点击事件
		bt_add_once.setOnClickListener(this);
		
		adapter = new ContactAdapter();
		lv_contact.setAdapter(adapter);
		//设置listview的
		lv_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				ContactInfo info = (ContactInfo) lv_contact.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.putExtra("number", info.getNumber());
				setResult(0, intent);
				System.out.println("add here");
				finish();
			}
		});
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//一键导入联系人
		//使用intent传递对象
		case R.id.bt_add_onece:
			List<String> newinfos;
			for(int i=0;i<infos.size();i++){
				dao.add(infos.get(i).getNumber());
			}
			Toast.makeText(getApplicationContext(), "导入成功!", 1).show();
			newinfos = dao.getNumbers();
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("newnumbers", (Serializable) newinfos);
			intent.putExtras(bundle);
			setResult(1,intent);
			finish();
			break;
		}
	}
	
	//ListView的数据适配器
	private class ContactAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return infos.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ContactInfo info = infos.get(position);
			View view = View.inflate(AddBlackNumberFromContact.this, R.layout.contact_item, null);
			TextView tv1 = (TextView) view.findViewById(R.id.tv_name);
			TextView tv2 = (TextView) view.findViewById(R.id.tv_number);
			tv1.setText(info.getName());
			tv2.setText(info.getNumber());
			return view;
		}
		
	}
	
	
}



























