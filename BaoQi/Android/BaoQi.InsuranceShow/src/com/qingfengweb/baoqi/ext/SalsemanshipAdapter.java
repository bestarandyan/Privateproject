package com.qingfengweb.baoqi.ext;
import com.qingfengweb.baoqi.bean.CustomerDataBean;
import com.qingfengweb.baoqi.insuranceShow.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
public class SalsemanshipAdapter extends BaseAdapter {
	private Context mContext;
	private ViewHolder vh = null;
	public SalsemanshipAdapter(Context context) {

		this.mContext=context;

	}
	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int arg0) {
		return mThumbIds[arg0];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private String[] args={"2012中国人寿保险资格考试模拟真题两套","2012中国人寿保险资格考试模拟真题两套","2012中国人寿保险资格考试模拟真题两套","2012中国人寿保险资格考试模拟真题两套","2012中国人寿保险资格考试模拟真题两套","2012中国人寿保险资格考试模拟真题两套","2012中国人寿保险资格考试模拟真题两套"};
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ImageView imageView;
//		if(convertView==null){
//			imageView=new ImageView(mContext);
//			imageView.setLayoutParams(new GridView.LayoutParams(154, 145));
//			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//			imageView.setPadding(8, 8, 8, 8);
//		}else{
//			imageView = (ImageView) convertView;
//			}
//
//		imageView.setImageResource(mThumbIds[position]);
//		return imageView;
//	}
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null
					|| (convertView != null && convertView.getTag() == null)) {
				convertView = ((LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.image_layout, null);
				vh = new ViewHolder(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.setContent(mThumbIds[position],args[position]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	private class ViewHolder {
		private ImageView imageView;
		private TextView people;

		public ViewHolder(View layout) {
			imageView=(ImageView) layout.findViewById(R.id.image);
			people=(TextView) layout.findViewById(R.id.text);
			
		}

		public void setContent(Integer args,String text) {
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
			imageView.setImageResource(args);
			people.setText(text);
		}
	}
	
	//展示图片
		private Integer[] mThumbIds = {
			R.drawable.questions_new_tu, R.drawable.questions_new_tu,
			R.drawable.questions_notpass_tu, R.drawable.questions_notpass_tu,
			R.drawable.questions_pass_tu, R.drawable.questions_pass_tu,
			R.drawable.questions_pass_tu
			};
		
		
}
