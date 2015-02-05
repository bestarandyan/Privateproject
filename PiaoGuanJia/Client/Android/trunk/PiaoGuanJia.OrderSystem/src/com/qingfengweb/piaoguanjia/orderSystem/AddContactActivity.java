package com.qingfengweb.piaoguanjia.orderSystem;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.qingfengweb.piaoguanjia.orderSystem.util.MessageBox;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

@ContentView(R.layout.activity_addcontact)
public class AddContactActivity extends BaseActivity {

	@ViewInject(R.id.parent)
	private View parent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		parent.setOnTouchListener(this);
	}
	
	@OnClick({ R.id.linear_back, R.id.btn_keep, R.id.checkBox })
	public void btnonClick(View v) {
		if (v.getId() == R.id.linear_back) {
			finish();
		} else if (v.getId() == R.id.btn_keep) {
			finish();
		}
		super.onClick(v);
	}
}
