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


	private int mCurrentX = 0, mCurrentY = 0;//用语句寻找
	private int mCurrentColor, mDefaultColor;//颜色的当前值和默认值
	private float mCurrentHue = 0;//Hue的当前值
	private final int[] mHueBarColors = new int[258];//上方彩条的颜色索引，应该是256种，从0~255，不知道为啥是258
	private int[] mMainColors = new int[65536];//下方颜色块的颜色索引


	public int mcolor;// 对外使用的颜色


	// 构造函数：
	public UniversalColorView(Context c, AttributeSet attrSet) {
	        super(c, attrSet);
	       
	int defaultColor=0xFF3299CC;//默认颜色
	mDefaultColor = defaultColor;//默认颜色
	int color=0xFF3299CC;//当前颜色
	float[] hsv = new float[3];//hsv格式的颜色
	Color.colorToHSV(color, hsv);//将当前的颜色改成hsv格式
	mCurrentHue = hsv[0];//将当前颜色的hsv[0]赋值给mCurrentHue,便于查找
	updateMainColors();//更新主颜色
	mCurrentColor = color;//当前颜色
	int index = 0;
	for(int k=0;k<6;k++){
	for (float i=0; i<256; i += 256/42) 
	{
	    switch(k)
	    {
	    case 0://红色 (#f00) to 粉红 (#f0f)
	    mHueBarColors[index] = Color.rgb(255, 0, (int) i);
	    break;
	    case 1:// 粉红 (#f0f) to 蓝色 (#00f)
	    mHueBarColors[index] = Color.rgb(255-(int) i, 0, 255);
	    break;
	    case 2:// 蓝色 (#00f) to 青蓝色 (#0ff)
	    mHueBarColors[index] = Color.rgb(0, (int) i, 255);
	    break;
	    case 3:// 青蓝色 (#0ff) to 绿色 (#0f0)
	    mHueBarColors[index] = Color.rgb(0, 255, 255-(int) i);
	    break;
	    case 4:// 黄色 (#ff0) to 红色 (#f00)
	    mHueBarColors[index] = Color.rgb((int) i, 255, 0);
	    break;
	    case 5:// 黄色 (#ff0) to 红色 (#f00)
	    mHueBarColors[index] = Color.rgb(255, 255-(int) i, 0);
	    break;
	    }
	    index++;
	}
	}
	// 定义画笔，否则成为空指针
	mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	mPaint.setTextAlign(Paint.Align.CENTER);
	mPaint.setTextSize(12);
	}
	// 由颜色条的颜色得到主块的颜色：
	private void updateMainColors()
	{
	int mainColor = getCurrentMainColor();//查表得到主颜色，然后存储在这里
	int index = 0;//此处指针不同上面的指针
	int[] topColors = new int[256];
	for (int y=0; y<256; y++)
	{
	for (int x=0; x<256; x++)
	{
	// 得到主颜色65536个，由x,y确定
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
	//首先，第一步绘制上方的彩条~~~~~~~~~~~~~~~~
	// 将当前Hue转换
	int translatedHue = 255-(int)(mCurrentHue*255/360);
	// 用线显现出所有颜色？我擦，这得多麻烦
	for (int x=0; x<256; x++)
	{
	if (translatedHue != x)//如果这个不是选中的颜色，则正常显示
	{
	mPaint.setColor(mHueBarColors[x]);
	mPaint.setStrokeWidth(1);
	}
	else // 如果是选中的颜色，则显示一条黑线，以示选中
	{
	mPaint.setColor(Color.BLACK);
	mPaint.setStrokeWidth(3);
	}
	canvas.drawLine(x+10, 0, x+10, 40, mPaint);//将每条线绘制出来
	}
	//然后，第二步绘制下面的方块~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// 使用线的渐变显示
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


	// 绘制显示颜色选中的小圆圈
	if (mCurrentX != 0 && mCurrentY != 0)
	{
	mPaint.setStyle(Paint.Style.STROKE);
	mPaint.setColor(Color.BLACK);
	canvas.drawCircle(mCurrentX, mCurrentY, 10, mPaint);
	}


	// 绘制表示选中的按钮
	mPaint.setStyle(Paint.Style.FILL);
	mPaint.setColor(mCurrentColor);
	canvas.drawRect(10, 316, 138, 356, mPaint);


	// 根据颜色的亮度设置字体的颜色
	if (Color.red(mCurrentColor)+Color.green(mCurrentColor)+Color.blue(mCurrentColor) < 384)
	mPaint.setColor(Color.WHITE);
	else
	mPaint.setColor(Color.BLACK);
	canvas.drawText("1", 74, 340, mPaint);


	// 绘制表示默认的颜色的按钮
	mPaint.setStyle(Paint.Style.FILL);
	mPaint.setColor(mDefaultColor);
	canvas.drawRect(138, 316, 266, 356, mPaint);


	// 根据颜色的亮度设置字体的颜色
	if (Color.red(mDefaultColor)+Color.green(mDefaultColor)+Color.blue(mDefaultColor) < 384)
	mPaint.setColor(Color.WHITE);
	else
	mPaint.setColor(Color.BLACK);
	canvas.drawText("2", 202, 340, mPaint);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	setMeasuredDimension(276, 366);//view的大小
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
	// if (event.getAction() != MotionEvent.ACTION_DOWN) return true;
	float x = event.getX();
	float y = event.getY();
	// 如果按的是上方的彩条
	if (x > 10 && x < 266 && y > 0 && y < 40)
	{
	// 更新mCurrentHue
	mCurrentHue = (255-x)*360/255;
	updateMainColors();
	// 更新mCurrentColor
	int transX = mCurrentX-10;
	int transY = mCurrentY-60;
	int index = 256*(transY-1)+transX;
	if (index > 0 && index < mMainColors.length)
	mCurrentColor = mMainColors[256*(transY-1)+transX];
	//强制重画view
	invalidate();
	}
	//如果按下的是下方的方块
	if (x > 10 && x < 266 && y > 50 && y < 306)
	{
	mCurrentX = (int) x;
	mCurrentY = (int) y;
	int transX = mCurrentX-10;
	int transY = mCurrentY-60;
	int index = 256*(transY-1)+transX;
	if (index > 0 && index < mMainColors.length)
	{
	// 更新当前的颜色
	mCurrentColor = mMainColors[index];
	// 强制刷新
	invalidate();
	}
	}


	// 如果按下的左侧的按钮
	if (x > 10 && x < 138 && y > 316 && y < 356)
	mcolor=mCurrentColor;
	// 如果按下的右侧的按钮
	if (x > 138 && x < 266 && y > 316 && y < 356)
	mcolor=mDefaultColor;


	return true;
	}
	}

