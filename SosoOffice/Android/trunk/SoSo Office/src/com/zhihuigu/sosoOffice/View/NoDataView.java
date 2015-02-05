/**
 * 
 */
package com.zhihuigu.sosoOffice.View;

import com.zhihuigu.sosoOffice.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/4/10
 *列表刷新后没数据时的体现
 */
public class NoDataView extends View{
	Context context;
	public NoDataView(Context context) {
		super(context);
		this.context = context;
	}
	public NoDataView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public NoDataView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	public LinearLayout getView(String msg){
		LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_nodata, null);
		ImageView noDataImg = (ImageView) layout.findViewById(R.id.nodataImg);
		TextView noDataText = (TextView) layout.findViewById(R.id.nodataText);
		noDataText.setText(msg);
		return layout;
	}
	
	

}
