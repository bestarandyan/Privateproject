package com.qingfengweb.baoqi.propertyInsurance.ext;

import com.qingfengweb.baoqi.propertyInsurance.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;




public class ImageAdapter extends BaseAdapter{

	private Context mContext;

	public ImageAdapter(Context context) {

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

		R.drawable.home_btn1, R.drawable.home_btn2,
		R.drawable.home_btn3, R.drawable.home_btn4,
		R.drawable.home_btn5, R.drawable.home_btn6,
		R.drawable.home_btn7, R.drawable.home_btn8,
		R.drawable.home_btn9, R.drawable.home_btn10,
		R.drawable.home_btn11, R.drawable.home_btn12,
		R.drawable.home_btn13, R.drawable.home_btn14,
		R.drawable.home_btn16,R.drawable.home_btn15
		};

@Override
public long getItemId(int position) {
	// TODO Auto-generated method stub
	return position;
}
}