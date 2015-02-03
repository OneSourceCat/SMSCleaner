package ynu.software.shixun.blacklistfilter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ContactInfoProvider {
	//从联系人列表拿到信息 
	private Context context;
	public ContactInfoProvider(Context context) {
		this.context = context;
	}
    public List<ContactInfo> getContactInfos(){
    	List<ContactInfo> infos = new ArrayList<ContactInfo>();
    	//获取联系人信息
    	ContentResolver resolver = context.getContentResolver();
    	Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri datauri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(uri, null, null, null, null);
		while(cursor.moveToNext()){
			ContactInfo info = new ContactInfo();
			String id = cursor.getString(cursor.getColumnIndex("contact_id"));
			String name = cursor.getString(cursor.getColumnIndex("display_name"));
			info.setName(name);
			Cursor datacursor = resolver.query(datauri, null, "raw_contact_id=?", new String[]{id}, null);
			while(datacursor.moveToNext()){
				String mimetype = datacursor.getString(datacursor.getColumnIndex("mimetype"));
				if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
					String number = datacursor.getString(datacursor.getColumnIndex("data1"));
					number.replace("-", "");
					info.setNumber(number);
				}
			}
			datacursor.close();
			infos.add(info);
			info=null;
		}
		cursor.close();
    	return infos;
    }
}





























