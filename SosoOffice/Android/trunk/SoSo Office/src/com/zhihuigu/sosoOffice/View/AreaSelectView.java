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
 * @author ������
 * @createDate 2013/1/28
 * ˫������ѡ������
 * �ܿ����������ޡ�
 *
 */
public class AreaSelectView extends View{
	public LinearLayout layoutLeft,layoutRight;
	RelativeLayout parentLayout;//����ؼ������������������ֵ
	public int currentx = 0;
	public int leftx = 0;
	public int rightx = 0;
	public ImageView leftImage,rightImage;
	public ImageView leftBtn,rightBtn;
	public Context context;
	public TextView minText,maxText;//չʾ��Сֵ�����ֵ���ı���
	public TextView minUnitText,maxUnitText;//��λ
	public AccurateSearchActivity activity;//��Ϊ���activity���й�����������������Ϊ�˿�����������Ĺ������ñ��ؼ���������˳��
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
	 * ������ť�ļ����¼�
	 * @author ������
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
				if(v == leftBtn){//��ߵİ�ť�ؼ�����
					if(currentx>event.getX()){//����
						if(leftBtn.getLeft() > 0){//ֻ������ߵĻ������ߵ�λ�ô���0��ʱ����������󻬶�
							leftx = (int)(currentx-event.getX());//�õ�������λ��
							if(leftImage.getWidth()-leftx > leftBtn.getWidth()){//�ж������Ŀ���Ƿ���ڻ���Ŀ��
								LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(leftImage.getWidth()-leftx, leftImage.getHeight());
								leftImage.setLayoutParams(param);
							}else{//���С�ڻ���ڻ���Ŀ�ȣ����������Ⱦ�Ϊ����Ŀ��
								LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(leftBtn.getWidth(), leftImage.getHeight());
								leftImage.setLayoutParams(param);
							}
						}
						if(layoutLeft.getWidth()<leftBtn.getWidth()){
							  LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(leftBtn.getWidth(), leftImage.getHeight());
								leftImage.setLayoutParams(param);
						  }
					}else{//���һ�
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
					 setMinValue();//������Сֵ
				}else if(v == rightBtn){ //�ұߵİ�ť�ؼ�����
					if(currentx<event.getX()){//���һ�
	   					rightx = (int) (event.getX()-currentx);
	   					if(rightImage.getWidth()-rightx>0){//�ж��ұߵ������Ŀ���Ƿ������ָ�ƶ��ľ���
	   						LinearLayout.LayoutParams param = new LinearLayout.LayoutParams((int) (rightImage.getWidth()-rightx), rightImage.getHeight());
		   					param.setMargins(-2, 0, 0, 0);
		   					rightImage.setLayoutParams(param);
	   					}else{//С�ڵ�ʱ����������Ŀ����Ϊ0 ��Ϊ���ֵ
	   						LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, rightImage.getHeight());
		   					param.setMargins(-2, 0, 0, 0);
		   					rightImage.setLayoutParams(param);
	   					}
	   				}else{//����
	   					if(layoutLeft.getRight()<layoutRight.getLeft()){//���󻬶�ʱ���������ұ߻����λ����Զ�����껬����ұ�
		   					rightx = (int) (currentx-event.getX());
		   					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams((int) (rightImage.getWidth()+(rightx)), rightImage.getHeight());
		   					param.setMargins(-2, 0, 0, 0);
		   					rightImage.setLayoutParams(param);
		   				}
	   				}
					setMaxValue();//�������ֵ
				}
			}else if(event.getAction() == MotionEvent.ACTION_UP){
				if(activity!=null){
//					activity.scrollView.setScrollable(true);
				}
				if(v == leftBtn){//��ߵİ�ť�ؼ�����
					if(leftBtn.getLeft() < 0){//����ߵĻ������ߵ������x<0ʱ �������Ŀ��ǡ��Ϊ����Ŀ�ȣ���Ϊ������������Ҷ����
						LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(leftBtn.getWidth()-3, leftImage.getHeight());
						leftImage.setLayoutParams(param);
					}
				}else if(v == rightBtn){ //�ұߵİ�ť�ؼ�����
					
				}
			}
			return true;
		}
	}
	
	
	/**
	 * ��ȡ���ؼ�
	 * @param unit ������λ
	 * @return ��Ҫ��˫�������ؼ�
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
	public float minValue = 0;//��Сֵ
	public float maxValue = 0;//���ֵ
	public float currentMinValue;//��ǰ��Сֵ
	public float currentMaxValue;//��ǰ���ֵ
	public float countValue = 0;//�ܹ��Ĵ�С
	public float leftScale = 0.0f;//��ߵĿؼ����Ⱥ����������Сֵ�ı���
	public float rightScale = 0.0f;//�ұߵĿؼ��ĳ��Ⱥ�����������ֵ�ı���
	public String unitStr = "Ԫ";
	public float initLeft = 0.0f;//�ʼ����ߵ�ֵ
	public float initRight = 0.0f;//�ʼ���ұߵ�ֵ
	public boolean init = true;
	/**
	 * ��ʼ�������޿ؼ���ֵ��λ��
	 * @author ������
	 * @createDate 2013/1/30
	 */
	public void initData(){
		int   scale   =   1;//����λ��   
		int   roundingMode   =   4;//��ʾ�������룬����ѡ��������ֵ��ʽ������ȥβ���ȵ�.   
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
	 * ������Сֵ
	 * @author ������
	 * @createDate 2013/1/30
	 */
	private void setMinValue(){
		  int   scale   =   1;//����λ��   
		  int   roundingMode   =   4;//��ʾ�������룬����ѡ��������ֵ��ʽ������ȥβ���ȵ�.   
		  
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
	 * �������ֵ
	 * @author ������
	 * @createDate 2013/1/30
	 */
	private void setMaxValue(){
		int   scale   =   1;//����λ��   
		int   roundingMode   =   4;//��ʾ�������룬����ѡ��������ֵ��ʽ������ȥβ���ȵ�.   
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
