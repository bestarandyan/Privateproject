package com.qingfengweb.baoqi.insuranceShow.ext;

import com.qingfengweb.baoqi.insuranceShow.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;




public class InstructionAdapter extends BaseAdapter{

	private Context mContext;

	public InstructionAdapter(Context context) {

		this.mContext=context;

	}

	@Override
	public int getCount() {
		return mThumbIds.length;
		}

	@Override
	public Object getItem(int position) {

		return position;

	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		//定义一个ImageView,显示在GridView里

		ImageView imageView;
		if(convertView==null){

			imageView=new ImageView(mContext);

			imageView.setLayoutParams(new GridView.LayoutParams(154, 132));

			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

			imageView.setPadding(8, 8, 8, 8);

		}else{

			imageView = (ImageView) convertView;
			}

		imageView.setImageResource(mThumbIds[position]);

		return imageView;
	}


//展示图片
	private Integer[] mThumbIds = {

		R.drawable.explain_btn1, R.drawable.explain_btn2,
		R.drawable.explain_btn3, R.drawable.explain_btn4,
		R.drawable.explain_btn5, R.drawable.explain_btn6,
		R.drawable.explain_btn7, R.drawable.explain_btn8,
		R.drawable.explain_btn9, R.drawable.explain_btn10,
		R.drawable.explain_btn11, R.drawable.explain_btn12,
		R.drawable.explain_btn13, R.drawable.explain_btn14,
		R.drawable.explain_btn15, R.drawable.explain_btn16,
		R.drawable.explain_btn17, 
		};

@Override
public long getItemId(int position) {
	// TODO Auto-generated method stub
	return position;
}
}