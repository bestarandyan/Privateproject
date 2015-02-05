package guoTeng.LoadIC2ReadCard;
import com.google.zxing.client.android.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;

	public ImageAdapter(Context context) {

		this.mContext = context;

	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {

		return position;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// 定义一个ImageView,显示在GridView里
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(280,280));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(mThumbIds[position]);

		return imageView;
	}

	// 展示图片
	private Integer[] mThumbIds = {

	R.drawable.home_btn2,R.drawable.erweima,R.drawable.home_btn3};

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}