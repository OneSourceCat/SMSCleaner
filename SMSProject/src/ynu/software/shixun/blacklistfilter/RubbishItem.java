package ynu.software.shixun.blacklistfilter;



import ynu.software.shixun.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
/**
 * �鿴�������ŵ��µ�activity
 * @author Administrator
 *
 */
public class RubbishItem extends Activity {

	private TextView tv_rubbishitem;
	private TextView tv_rubbishnumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rubbishitem);
		
		tv_rubbishitem = (TextView) this.findViewById(R.id.tv_rubbishitem);
		tv_rubbishnumber = (TextView) this.findViewById(R.id.tv_rubbishnumber);
		
		//�õ��������ŵ�ֵ
		Intent intent = getIntent();
		String rubbishString_content =intent.getExtras().getString("message");
		String rubbishNumber = intent.getExtras().getString("number");
		tv_rubbishnumber.setText("��Դ���룺"+rubbishNumber);
	    tv_rubbishitem.setText("�������ݣ�"+rubbishString_content);
	    
	}
	//���ذ�ť
	public void backFormer(View view){
		finish();
	}
	
}
