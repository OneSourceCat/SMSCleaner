package ynu.software.shixun.blacklistfilter;



import ynu.software.shixun.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
/**
 * 查看垃圾短信的新的activity
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
		
		//得到垃圾短信的值
		Intent intent = getIntent();
		String rubbishString_content =intent.getExtras().getString("message");
		String rubbishNumber = intent.getExtras().getString("number");
		tv_rubbishnumber.setText("来源号码："+rubbishNumber);
	    tv_rubbishitem.setText("短信内容："+rubbishString_content);
	    
	}
	//返回按钮
	public void backFormer(View view){
		finish();
	}
	
}
