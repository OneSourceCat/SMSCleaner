package ynu.software.shixun.main;



import ynu.software.shixun.R;
import ynu.software.shixun.blacklistfilter.RubbishItem;
import ynu.software.shixun.blacklistfilter.RubbishMessage;
import ynu.software.shixun.blacklistfilter.WatchRubbishMessage;
import android.os.Bundle;
import android.os.Environment;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity_message extends TabActivity {
	private TabHost tabHost;
	public static Context context;
	final int HEAP_SIZE = 8 * 1024* 1024 ; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("¶ÌÐÅ·À»¤");
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.activity_main_message);
		context=MainActivity_message.this;
		makeTab();
		System.out.println(Environment.getExternalStorageDirectory());
		
	}
	

	public void makeTab() {
		if (this.tabHost == null) {
			tabHost = getTabHost();
			TabSpec firsttab = tabHost.newTabSpec("messageTab");
			TabSpec sencondtab = tabHost.newTabSpec("selfTab");
			TabSpec thirdtab = tabHost.newTabSpec("setTab");

			firsttab.setIndicator("",
					getResources().getDrawable(R.drawable.message)).setContent(
					new Intent(this, WatchRubbishMessage.class));
			sencondtab.setIndicator("",
					getResources().getDrawable(R.drawable.self)).setContent(
					new Intent(this, SelfActivity.class));
			thirdtab.setIndicator("",
					getResources().getDrawable(R.drawable.set)).setContent(
					new Intent(this, SetActivity.class));

			tabHost.addTab(firsttab);
			tabHost.addTab(sencondtab);
			tabHost.addTab(thirdtab);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
