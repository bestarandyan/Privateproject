/**
 * 
 */
package com.zhihuigu.sosoOffice.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * @author  刘星星
 * @createDate 2013/1/30
 * 自定义滚动条
 *
 */
public class MyScrollView extends ScrollView{
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);    
	} 
	public MyScrollView(Context context) {
		super(context);
	}
	public boolean isScrollable = true;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(isScrollable){
			return super.onTouchEvent(ev);
		}else{
			return false;
		}
		
	}
	public void setScrollable(boolean scrollable){
		isScrollable = scrollable;
	}
}
