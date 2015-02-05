/**
 * 
 */
package com.zhihuigu.sosoOffice.View;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.zhihuigu.sosoOffice.AccurateSearchActivity;
import com.zhihuigu.sosoOffice.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/1/28
 * 双向区域选择器，
 * 能控制上限下限。
 *
 */
public class AreaSelectView extends View{
	public LinearLayout layoutLeft,layoutRight;
	RelativeLayout parentLayout;//真个控件，可以用来计算最大值
	public int currentx = 0;
	public int leftx = 0;
	public int rightx = 0;
	public ImageView leftImage,rightImage;
	public ImageView leftBtn,rightBtn;
	public Context context;
	public TextView minText,maxText;//展示最小值和最大值的文本框
	public TextView minUnitText,maxUnitText;//单位
	public AccurateSearchActivity activity;//因为这个activity含有滚动条，申明至此是为了控制其滚动条的滚动，让本控件的拉动更顺畅
	public AreaSelectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public AreaSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public AreaSelectView(Context context,AccurateSearchActivity activity,float minValue,float maxValue) {
		super(context);
		this.context = context;
		this.activity = activity;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	public AreaSelectView(Context context,float minValue,float maxValue) {
		super(context);
		this.context = context;
		this.minValue = minValue;
		this.maxValue = maxValue;
		
	}
	/**
	 * 拉动按钮的监听事件
	 * @author 刘星星
	 * @createDate 2013/1/29
	 *
	 */
	class ViewTouchListener implements OnTouchListener{

		public boolean onTouch(View v, MotionEvent event) {
			if(activity!=null){
				activity.scrollView.setScrollable(false);
			}
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				layoutLeft.setGravity(Gravity.CENTER_VERTICAL);
				layoutRight.setGravity(Gravity.CENTER_VERTICAL);
				currentx = (int) event.getX();
				
				initData();
			}else if(event.getAction() == MotionEvent.ACTION_MOVE){
				if(v == leftBtn){//左边的按钮控件监听
					if(currentx>event.getX()){//向左滑
						if(leftBtn.getLeft() > 0){//只有在左边的滑块的左边的位置大于0的时候才容许往左滑动
							leftx = (int)(currentx-event.getX());//得到滑动的位置
							if(leftImage.getWidth()-leftx > leftBtn.getWidth()){//判断拉条的宽度是否大于滑块的宽度
								LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(leftImage.getWidth()-leftx, leftImage.getHeight());
								leftImage.setLayoutParams(param);
							}else{//如果小于或等于滑块的宽度，则设置其宽度就为滑块的宽度
								LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(leftBtn.getWidth(), leftImage.getHeight());
								leftImage.setLayoutParams(param);
							}
						}
						if(layoutLeft.getWidth()<leftBtn.getWidth()){
							  LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(leftBtn.getWidth(), leftImage.getHeight());
								leftImage.setLayoutParams(param);
						  }
					}else{//向右滑
						if(layoutLeft.getWidth() < (parentLayout.getWidth()-layoutRight.getWidth())){
							leftx = (int) (event.getX()-currentx);
							LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(leftImage.getWidth()+leftx, leftImage.getHeight());
							leftImage.setLayoutParams(param);
						}
						if(layoutLeft.getWidth()>(parentLayout.getWidth()-layoutRight.getWidth())){
							  LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(parentLayout.getWidth()-layoutRight.getWidth(), leftImage.getHeight());
								leftImage.setLayoutParams(param);
						  }
					}
					 setMinValue();//设置最小值
				}else if(v == rightBtn){ //右边的按钮控件监听
					if(currentx<event.getX()){//向右滑
	   					rightx = (int) (event.getX()-currentx);
	   					if(rightImage.getWidth()-rightx>0){//判断右边的拉条的宽度是否大于手指移动的距离
	   						LinearLayout.LayoutParams param = new LinearLayout.LayoutParams((int) (rightImage.getWidth()-rightx), rightImage.getHeight());
		   					param.setMargins(-2, 0, 0, 0);
		   					rightImage.setLayoutParams(param);
	   					}else{//小于的时候就让拉条的宽度设为0 极为最大值
	   						LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, rightImage.getHeight());
		   					param.setMargins(-2, 0, 0, 0);
		   					rightImage.setLayoutParams(param);
	   					}
	   				}else{//向左滑
	   					if(layoutLeft.getRight()<layoutRight.getLeft()){//向左滑动时，必须让右边滑块的位置永远在坐标滑块的右边
		   					rightx = (int) (currentx-event.getX());
		   					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams((int) (rightImage.getWidth()+(rightx)), rightImage.getHeight());
		   					param.setMargins(-2, 0, 0, 0);
		   					rightImage.setLayoutParams(param);
		   				}
	   				}
					setMaxValue();//设置最大值
				}
			}else if(event.getAction() == MotionEvent.ACTION_UP){
				if(activity!=null){
//					activity.scrollView.setScrollable(true);
				}
				if(v == leftBtn){//左边的按钮控件监听
					if(leftBtn.getLeft() < 0){//当左边的滑块的左边的坐标的x<0时 让拉条的宽度恰好为滑块的宽度，因为滑块和拉条是右对齐的
						LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(leftBtn.getWidth()-3, leftImage.getHeight());
						leftImage.setLayoutParams(param);
					}
				}else if(v == rightBtn){ //右边的按钮控件监听
					
				}
			}
			return true;
		}
	}
	
	
	/**
	 * 获取到控件
	 * @param unit 计量单位
	 * @return 想要的双向拉动控件
	 * @createDate 2013/1/29
	 */
	public View getView(String unit){
		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout l = (LinearLayout) inflater.inflate(R.layout.view_areaselect, null);
		leftBtn = (ImageView) l.findViewById(R.id.btn1);
		rightBtn = (ImageView)  l.findViewById(R.id.btn2);
		parentLayout = (RelativeLayout) l.findViewById(R.id.parentLayout);
        layoutLeft = (LinearLayout)  l.findViewById(R.id.layout);
        layoutRight = (LinearLayout) l.findViewById(R.id.layout1);
        leftImage = (ImageView) l.findViewById(R.id.leftImg);
        rightImage = (ImageView) l.findViewById(R.id.rightImg);
        minText = (TextView) l.findViewById(R.id.MinText);
        maxText = (TextView) l.findViewById(R.id.MaxText);
        minUnitText = (TextView) l.findViewById(R.id.MinUnitText);
        maxUnitText = (TextView) l.findViewById(R.id.MaxUnitText);
        leftBtn.setOnTouchListener(new ViewTouchListener());
        rightBtn.setOnTouchListener(new ViewTouchListener());
        countValue = parentLayout.getWidth();
        unitStr = unit;
        minText.setText(String.valueOf(minValue));
        maxText.setText(String.valueOf(maxValue));
        minUnitText.setText(unitStr);
        maxUnitText.setText(unitStr);
        return l;
	}
	public float minValue = 0;//最小值
	public float maxValue = 0;//最大值
	public float currentMinValue;//当前最小值
	public float currentMaxValue;//当前最大值
	public float countValue = 0;//总共的大小
	public float leftScale = 0.0f;//左边的控件长度和所代表的最小值的比例
	public float rightScale = 0.0f;//右边的控件的长度和所代表的最大值的比例
	public String unitStr = "元";
	public float initLeft = 0.0f;//最开始的左边的值
	public float initRight = 0.0f;//最开始的右边的值
	public boolean init = true;
	/**
	 * 初始化上下限控件的值和位置
	 * @author 刘星星
	 * @createDate 2013/1/30
	 */
	public void initData(){
		int   scale   =   1;//设置位数   
		int   roundingMode   =   4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.   
		if(init){
			initLeft = leftBtn.getRight();
			initRight = rightBtn.getWidth();
			init = false;
		}
		countValue = parentLayout.getWidth()-initLeft-initRight;
		leftScale = (maxValue-minValue)/(countValue);
		rightScale =leftScale;
		currentMinValue = (layoutLeft.getWidth()-initLeft)*leftScale;
		currentMaxValue =(countValue-rightImage.getWidth())*rightScale;
		
		String min="0";
		
		String max="20";
		try {
			BigDecimal bd = new BigDecimal((double) currentMinValue);
			bd = bd.setScale(scale, roundingMode);
			currentMinValue = bd.floatValue();
			if (currentMinValue > 100.00f) {
				currentMinValue = 100.0f;
			}
			min = String.valueOf(currentMinValue);
			BigDecimal bd1 = new BigDecimal((double) currentMaxValue);
			bd1 = bd1.setScale(scale, roundingMode);
			currentMaxValue = bd1.floatValue();
			if (currentMaxValue < currentMinValue) {
				currentMaxValue = currentMinValue;
			}
			max = String.valueOf(currentMaxValue);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		minText.setText(min);
		maxText.setText(max);
	}
	/**
	 * 设置最小值
	 * @author 刘星星
	 * @createDate 2013/1/30
	 */
	private void setMinValue(){
		  int   scale   =   1;//设置位数   
		  int   roundingMode   =   4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.   
		  
		  currentMinValue = (layoutLeft.getWidth()-initLeft)*leftScale;
		  BigDecimal   bd   =   new   BigDecimal((double)currentMinValue);   
		  bd   =   bd.setScale(scale,roundingMode);   
		  currentMinValue   =   bd.floatValue();  
		  if(currentMinValue > 100.00f){
			  currentMinValue = 100.0f;
		  }
		  if(currentMaxValue < currentMinValue){
				  currentMinValue = currentMaxValue;
			}
		  String min = String.valueOf(currentMinValue);
		  
		  minText.setText(min);
	}
	/**
	 * 设置最大值
	 * @author 刘星星
	 * @createDate 2013/1/30
	 */
	private void setMaxValue(){
		int   scale   =   1;//设置位数   
		int   roundingMode   =   4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.   
		currentMaxValue =(countValue-rightImage.getWidth())*rightScale;
		BigDecimal   bd   =   new   BigDecimal((double)currentMaxValue);   
		bd   =   bd.setScale(scale,roundingMode);   
		currentMaxValue   =   bd.floatValue();  
		if(currentMaxValue < 0.00f){
			currentMaxValue = 0.0f;
		  }
		if(currentMaxValue<currentMinValue){
			currentMaxValue = currentMinValue;
		}
		String max = String.valueOf(currentMaxValue);
		maxText.setText(max);
	}
}
