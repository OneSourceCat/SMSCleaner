package ynu.software.shixun.main;

import ynu.software.shixun.R;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

public class HelpDialog extends AlertDialog {

	public HelpDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		final View view = getLayoutInflater().inflate(R.layout.help, null);
		setButton("ȷ��", (OnClickListener)null);
		setTitle("���ŷ���");
		setView(view);
	}

}
