package com.vnc.draw.tools;


import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.LinearGradient;  
import android.graphics.Paint;  
import android.graphics.Shader;  
import android.util.AttributeSet;
import android.view.MotionEvent;  
import android.view.View;

public class UniversalColorView  extends View {
	private Paint mPaint;


	private int mCurrentX = 0, mCurrentY = 0;//�����Ѱ��
	private int mCurrentColor, mDefaultColor;//��ɫ�ĵ�ǰֵ��Ĭ��ֵ
	private float mCurrentHue = 0;//Hue�ĵ�ǰֵ
	private final int[] mHueBarColors = new int[258];//�Ϸ���������ɫ������Ӧ����256�֣���0~255����֪��Ϊɶ��258
	private int[] mMainColors = new int[65536];//�·���ɫ�����ɫ����


	public int mcolor;// ����ʹ�õ���ɫ


	// ���캯����
	public UniversalColorView(Context c, AttributeSet attrSet) {
	        super(c, attrSet);
	       
	int defaultColor=0xFF3299CC;//Ĭ����ɫ
	mDefaultColor = defaultColor;//Ĭ����ɫ
	int color=0xFF3299CC;//��ǰ��ɫ
	float[] hsv = new float[3];//hsv��ʽ����ɫ
	Color.colorToHSV(color, hsv);//����ǰ����ɫ�ĳ�hsv��ʽ
	mCurrentHue = hsv[0];//����ǰ��ɫ��hsv[0]��ֵ��mCurrentHue,���ڲ���
	updateMainColors();//��������ɫ
	mCurrentColor = color;//��ǰ��ɫ
	int index = 0;
	for(int k=0;k<6;k++){
	for (float i=0; i<256; i += 256/42) 
	{
	    switch(k)
	    {
	    case 0://��ɫ (#f00) to �ۺ� (#f0f)
	    mHueBarColors[index] = Color.rgb(255, 0, (int) i);
	    break;
	    case 1:// �ۺ� (#f0f) to ��ɫ (#00f)
	    mHueBarColors[index] = Color.rgb(255-(int) i, 0, 255);
	    break;
	    case 2:// ��ɫ (#00f) to ����ɫ (#0ff)
	    mHueBarColors[index] = Color.rgb(0, (int) i, 255);
	    break;
	    case 3:// ����ɫ (#0ff) to ��ɫ (#0f0)
	    mHueBarColors[index] = Color.rgb(0, 255, 255-(int) i);
	    break;
	    case 4:// ��ɫ (#ff0) to ��ɫ (#f00)
	    mHueBarColors[index] = Color.rgb((int) i, 255, 0);
	    break;
	    case 5:// ��ɫ (#ff0) to ��ɫ (#f00)
	    mHueBarColors[index] = Color.rgb(255, 255-(int) i, 0);
	    break;
	    }
	    index++;
	}
	}
	// ���廭�ʣ������Ϊ��ָ��
	mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	mPaint.setTextAlign(Paint.Align.CENTER);
	mPaint.setTextSize(12);
	}
	// ����ɫ������ɫ�õ��������ɫ��
	private void updateMainColors()
	{
	int mainColor = getCurrentMainColor();//���õ�����ɫ��Ȼ��洢������
	int index = 0;//�˴�ָ�벻ͬ�����ָ��
	int[] topColors = new int[256];
	for (int y=0; y<256; y++)
	{
	for (int x=0; x<256; x++)
	{
	// �õ�����ɫ65536������x,yȷ��
	if (y == 0)
	{
	mMainColors[index] = 
	Color.rgb(
	255-(255-Color.red(mainColor))*x/255, 
	255-(255-Color.green(mainColor))*x/255,
	255-(255-Color.blue(mainColor))*x/255
	);
	topColors[x] = mMainColors[index];
	}
	else
	mMainColors[index] = 
	Color.rgb(
	(255-y)*Color.red(topColors[x])/255, 
	(255-y)*Color.green(topColors[x])/255, 
	(255-y)*Color.blue(topColors[x])/255);
	index++;
	}
	}
	}
	private int getCurrentMainColor()
	{
	int translatedHue = 255-(int)(mCurrentHue*255/360);
	int index = 0;
	for(int k=0;k<6;k++)
	{
	for (float i=0; i<256; i += 256/42) 
	{
	    switch(k)
	    {
	    case 0:if (index == translatedHue)return Color.rgb(255, 0, (int) i);
	    break;
	    case 1:if (index == translatedHue)return Color.rgb(255-(int) i, 0, 255);
	    break;
	    case 2:if (index == translatedHue)return Color.rgb(0, (int) i, 255);
	    break;
	    case 3:if (index == translatedHue)return Color.rgb(0, 255, 255-(int) i);
	    break;
	    case 4:if (index == translatedHue)return Color.rgb((int) i, 255, 0);
	    break;
	    case 5:if (index == translatedHue)return Color.rgb(255, 255-(int) i, 0);
	    break;
	    }
	    index++;
	}
	}
	return Color.RED;
	}


	@Override
	protected void onDraw(Canvas canvas) {
	//���ȣ���һ�������Ϸ��Ĳ���~~~~~~~~~~~~~~~~
	// ����ǰHueת��
	int translatedHue = 255-(int)(mCurrentHue*255/360);
	// �������ֳ�������ɫ���Ҳ�����ö��鷳
	for (int x=0; x<256; x++)
	{
	if (translatedHue != x)//����������ѡ�е���ɫ����������ʾ
	{
	mPaint.setColor(mHueBarColors[x]);
	mPaint.setStrokeWidth(1);
	}
	else // �����ѡ�е���ɫ������ʾһ�����ߣ���ʾѡ��
	{
	mPaint.setColor(Color.BLACK);
	mPaint.setStrokeWidth(3);
	}
	canvas.drawLine(x+10, 0, x+10, 40, mPaint);//��ÿ���߻��Ƴ���
	}
	//Ȼ�󣬵ڶ�����������ķ���~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ʹ���ߵĽ�����ʾ
	for (int x=0; x<256; x++)
	{
	int[] colors = new int[2];
	colors[0] = mMainColors[x];
	colors[1] = Color.BLACK;
	Shader shader = new LinearGradient(0, 50, 0, 306, colors, null, Shader.TileMode.REPEAT);
	mPaint.setShader(shader);
	canvas.drawLine(x+10, 50, x+10, 306, mPaint);
	}
	mPaint.setShader(null);


	// ������ʾ��ɫѡ�е�СԲȦ
	if (mCurrentX != 0 && mCurrentY != 0)
	{
	mPaint.setStyle(Paint.Style.STROKE);
	mPaint.setColor(Color.BLACK);
	canvas.drawCircle(mCurrentX, mCurrentY, 10, mPaint);
	}


	// ���Ʊ�ʾѡ�еİ�ť
	mPaint.setStyle(Paint.Style.FILL);
	mPaint.setColor(mCurrentColor);
	canvas.drawRect(10, 316, 138, 356, mPaint);


	// ������ɫ�����������������ɫ
	if (Color.red(mCurrentColor)+Color.green(mCurrentColor)+Color.blue(mCurrentColor) < 384)
	mPaint.setColor(Color.WHITE);
	else
	mPaint.setColor(Color.BLACK);
	canvas.drawText("1", 74, 340, mPaint);


	// ���Ʊ�ʾĬ�ϵ���ɫ�İ�ť
	mPaint.setStyle(Paint.Style.FILL);
	mPaint.setColor(mDefaultColor);
	canvas.drawRect(138, 316, 266, 356, mPaint);


	// ������ɫ�����������������ɫ
	if (Color.red(mDefaultColor)+Color.green(mDefaultColor)+Color.blue(mDefaultColor) < 384)
	mPaint.setColor(Color.WHITE);
	else
	mPaint.setColor(Color.BLACK);
	canvas.drawText("2", 202, 340, mPaint);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	setMeasuredDimension(276, 366);//view�Ĵ�С
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
	// if (event.getAction() != MotionEvent.ACTION_DOWN) return true;
	float x = event.getX();
	float y = event.getY();
	// ����������Ϸ��Ĳ���
	if (x > 10 && x < 266 && y > 0 && y < 40)
	{
	// ����mCurrentHue
	mCurrentHue = (255-x)*360/255;
	updateMainColors();
	// ����mCurrentColor
	int transX = mCurrentX-10;
	int transY = mCurrentY-60;
	int index = 256*(transY-1)+transX;
	if (index > 0 && index < mMainColors.length)
	mCurrentColor = mMainColors[256*(transY-1)+transX];
	//ǿ���ػ�view
	invalidate();
	}
	//������µ����·��ķ���
	if (x > 10 && x < 266 && y > 50 && y < 306)
	{
	mCurrentX = (int) x;
	mCurrentY = (int) y;
	int transX = mCurrentX-10;
	int transY = mCurrentY-60;
	int index = 256*(transY-1)+transX;
	if (index > 0 && index < mMainColors.length)
	{
	// ���µ�ǰ����ɫ
	mCurrentColor = mMainColors[index];
	// ǿ��ˢ��
	invalidate();
	}
	}


	// ������µ����İ�ť
	if (x > 10 && x < 138 && y > 316 && y < 356)
	mcolor=mCurrentColor;
	// ������µ��Ҳ�İ�ť
	if (x > 138 && x < 266 && y > 316 && y < 356)
	mcolor=mDefaultColor;


	return true;
	}
	}

