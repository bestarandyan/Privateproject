package com.qingfengweb.piaoguanjia.orderSystem;

import java.util.ArrayList;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingfengweb.piaoguanjia.orderSystem.adapter.ImageGalleryAdapter;
import com.qingfengweb.piaoguanjia.orderSystem.adapter.ProduceAdapter;
import com.qingfengweb.piaoguanjia.orderSystem.util.MessageBox;
import com.qingfengweb.piaoguanjia.orderSystem.view.XListView;
import com.qingfengweb.piaoguanjia.orderSystem.view.XListView.IXListViewListener;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProduceDetailActivity extends MainFrameActivity {

	private BaseAdapter adapter;
	private ArrayList<String> list;
	@ViewInject(R.id.parent)
	private View parent;
	@ViewInject(R.id.bottomGunLinear)
	private LinearLayout bottomLinear;
	@ViewInject(R.id.gallery)
	private Gallery gallery;

	@ViewInject(R.id.rel)
	private RelativeLayout rel;
	@ViewInject(R.id.include_view1)
	private View include_view1;
	@ViewInject(R.id.include_view2)
	private View include_view2;
	@ViewInject(R.id.include_view3)
	private View include_view3;

	@ViewInject(R.id.tab1)
	private LinearLayout tab1;
	@ViewInject(R.id.tab2)
	private LinearLayout tab2;
	@ViewInject(R.id.tab3)
	private LinearLayout tab3;

	@ViewInject(R.id.linear_btn1)
	private LinearLayout linear_btn1;
	@ViewInject(R.id.linear_btn2)
	private LinearLayout linear_btn2;
	@ViewInject(R.id.linear_btn3)
	private LinearLayout linear_btn3;
	@ViewInject(R.id.linear_btn4)
	private LinearLayout linear_btn4;
	@ViewInject(R.id.linear_pay)
	private LinearLayout linear_pay;
	@ViewInject(R.id.tv1)
	private TextView tv1;
	@ViewInject(R.id.tv2)
	private TextView tv2;
	@ViewInject(R.id.tv3)
	private TextView tv3;
	@ViewInject(R.id.tv4)
	private TextView tv4;

	@ViewInject(R.id.tv_tab1)
	private TextView tv_tab1;
	@ViewInject(R.id.tv_tab2)
	private TextView tv_tab2;
	@ViewInject(R.id.tv_tab3)
	private TextView tv_tab3;

	@ViewInject(R.id.iv_tab1)
	private ImageView iv_tab1;
	@ViewInject(R.id.iv_tab2)
	private ImageView iv_tab2;
	@ViewInject(R.id.iv_tab3)
	private ImageView iv_tab3;

	@ViewInject(R.id.iv_lianxiren)
	private ImageView iv_lianxiren;

	@ViewInject(R.id.iv_tongxulu)
	private ImageView iv_tongxulu;
	private final int REQUEST_CONTACT = 1;

	@ViewInject(R.id.linear_rili)
	private LinearLayout linear_rili;
	@ViewInject(R.id.et_num1)
	private EditText et_num1;
	@ViewInject(R.id.et_num2)
	private EditText et_num2;

	@ViewInject(R.id.iv_jia)
	private ImageView iv_jia;

	@ViewInject(R.id.iv_jia1)
	private ImageView iv_jia1;
	@ViewInject(R.id.iv_jian)
	private ImageView iv_jian;
	@ViewInject(R.id.iv_jian1)
	private ImageView iv_jian1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LayoutInflater.from(this).inflate(
				R.layout.activity_producedetail, null);
		addCenterView(view);
		ViewUtils.inject(this);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				MyApplication.widthPixels, MyApplication.widthPixels / 3);
		rel.setLayoutParams(param);
		parent.setOnTouchListener(this);
		tv_title.setText("填写订单");
		initview();
		notifyAdapter();
	}

	@OnClick({ R.id.tab1, R.id.tab2, R.id.tab3, R.id.linear_btn1,
			R.id.linear_btn2, R.id.linear_btn3, R.id.linear_btn4,
			R.id.linear_pay, R.id.iv_lianxiren, R.id.iv_tongxulu,
			R.id.linear_rili,R.id.iv_jia,R.id.iv_jia1,R.id.iv_jian,R.id.iv_jian1 })
	public void onbtnClick(View v) {
		if (v == tab1) {
			iv_tab1.setBackgroundResource(R.drawable.product_view_ico1);
			iv_tab2.setBackgroundResource(R.drawable.product_view_ico2);
			iv_tab3.setBackgroundResource(R.drawable.product_view_ico2);
			tv_tab1.setTextColor(Color.parseColor("#FF8201"));
			tv_tab2.setTextColor(Color.parseColor("#535353"));
			tv_tab3.setTextColor(Color.parseColor("#535353"));
			include_view1.setVisibility(View.VISIBLE);
			include_view2.setVisibility(View.GONE);
			include_view3.setVisibility(View.GONE);
		} else if (v == tab2) {
			iv_tab2.setBackgroundResource(R.drawable.product_view_ico1);
			iv_tab1.setBackgroundResource(R.drawable.product_view_ico2);
			iv_tab3.setBackgroundResource(R.drawable.product_view_ico2);
			tv_tab2.setTextColor(Color.parseColor("#FF8201"));
			tv_tab1.setTextColor(Color.parseColor("#535353"));
			tv_tab3.setTextColor(Color.parseColor("#535353"));
			include_view2.setVisibility(View.VISIBLE);
			include_view1.setVisibility(View.GONE);
			include_view3.setVisibility(View.GONE);
		} else if (v == tab3) {
			iv_tab3.setBackgroundResource(R.drawable.product_view_ico1);
			iv_tab2.setBackgroundResource(R.drawable.product_view_ico2);
			iv_tab1.setBackgroundResource(R.drawable.product_view_ico2);
			tv_tab3.setTextColor(Color.parseColor("#FF8201"));
			tv_tab2.setTextColor(Color.parseColor("#535353"));
			tv_tab1.setTextColor(Color.parseColor("#535353"));
			include_view3.setVisibility(View.VISIBLE);
			include_view2.setVisibility(View.GONE);
			include_view1.setVisibility(View.GONE);
		} else if (v == linear_btn1) {
			tv1.setVisibility(View.VISIBLE);
			tv2.setVisibility(View.GONE);
			tv3.setVisibility(View.GONE);
			tv4.setVisibility(View.GONE);
		} else if (v == linear_btn2) {
			tv2.setVisibility(View.VISIBLE);
			tv1.setVisibility(View.GONE);
			tv3.setVisibility(View.GONE);
			tv4.setVisibility(View.GONE);
		} else if (v == linear_btn3) {
			tv3.setVisibility(View.VISIBLE);
			tv2.setVisibility(View.GONE);
			tv1.setVisibility(View.GONE);
			tv4.setVisibility(View.GONE);
		} else if (v == linear_btn4) {
			tv4.setVisibility(View.VISIBLE);
			tv2.setVisibility(View.GONE);
			tv3.setVisibility(View.GONE);
			tv1.setVisibility(View.GONE);
		} else if (v == linear_pay) {
			Intent i = new Intent(this, PaymentActivity.class);
			startActivity(i);
		} else if (v == iv_lianxiren) {
			Intent i = new Intent(this, ContactActivity.class);
			startActivity(i);
		} else if (v == iv_tongxulu) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_PICK);
			intent.setData(ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, REQUEST_CONTACT);
		} else if (v == linear_rili) {
			Intent i = new Intent(this, CalendarSelectActivity.class);
			startActivity(i);
		} else if (v == iv_jia) {
			try {
				int n = Integer.parseInt(et_num1.getText().toString());
				et_num1.setText(n+1+"");
			} catch (Exception e) {
			}
		} else if (v == iv_jia1) {
			try {
				int n = Integer.parseInt(et_num2.getText().toString());
				et_num2.setText(n+1+"");
			} catch (Exception e) {
			}
		} else if (v.getId() == R.id.iv_jian) {
			try {
				int n = Integer.parseInt(et_num1.getText().toString());
				if(n-1>0){
					et_num1.setText(n-1+"");
				}else if(n-1==0){
					et_num1.setText("0");
				}
			} catch (Exception e) {
			}
		} else if (v == iv_jian1) {
			try {
				int n = Integer.parseInt(et_num2.getText().toString());
				if(n-1>0){
					et_num2.setText(n-1+"");
				}else if(n-1==0){
					et_num2.setText("0");
				}
			} catch (Exception e) {
			}
		}
		super.onClick(v);
	}

	private void initview() {
		getlist();
		bottomLinear.removeAllViews();
		for (int i = 0; i < list.size(); i++) {
			if (list.size() > 1) {
				bottomLinear.addView(addBottomImageView());
				bottomLinear.getChildAt(i).setBackgroundResource(
						R.drawable.stores_dot);
			}

		}
		if (bottomLinear.getChildCount() > 0) {
			bottomLinear.getChildAt(0).setBackgroundResource(
					R.drawable.stores_dot_on);
		}
		gallery.setOnItemSelectedListener(new GalleryItemSelectListener());
	}

	class GalleryItemSelectListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			for (int i = 0; i < list.size(); i++) {
				if (bottomLinear.getChildAt(i) != null) {
					if (i == position) {
						bottomLinear.getChildAt(i).setBackgroundResource(
								R.drawable.stores_dot_on);
					} else {
						bottomLinear.getChildAt(i).setBackgroundResource(
								R.drawable.stores_dot);
					}
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	private ImageView addBottomImageView() {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 0, 0, 0);
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(lp);
		return imageView;
	}

	private void notifyAdapter() {
		getlist();
		ImageGalleryAdapter adapter = new ImageGalleryAdapter(this, list);
		gallery.setAdapter(adapter);
	}

	private void getlist() {
		if (list == null) {
			list = new ArrayList<String>();
		}
		list.clear();
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
	}
}
