package com.zhihuigu.sosoOffice.View;

import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.Adapter.AutoCompleteAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

public class AdvancedAutoCompleteTextView extends RelativeLayout{

	private Context context;
	private AutoCompleteTextView tv;
	public AdvancedAutoCompleteTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	public AdvancedAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initViews();
	}

	private void initViews() {
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		tv=new AutoCompleteTextView(context);
		tv.setSingleLine(true);
		tv.setBackgroundResource(R.drawable.soso_et_background);
		tv.setLayoutParams(params);
		tv.setPadding(10, 0, 40, 0);
		tv.setSingleLine(true);
		
		RelativeLayout.LayoutParams p=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		p.addRule(RelativeLayout.CENTER_VERTICAL);
		p.rightMargin=10;
//		ImageView iv=new ImageView(context);
//		iv.setLayoutParams(p);
//		iv.setScaleType(ScaleType.FIT_CENTER);
//		iv.setImageResource(R.drawable.delete);
//		iv.setClickable(true);
//		iv.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				tv.setText("");
//			}
//		});
//			
		this.addView(tv);
//		this.addView(iv);
		
		
	}
	
	public void setAdapter(AutoCompleteAdapter adapter){
		tv.setAdapter(adapter);
	}
	
	public void setThreshold(int threshold){
		tv.setThreshold(threshold);
	}
	
	public AutoCompleteTextView getAutoCompleteTextView(){
		return tv;
	}
}
