package com.vnc.draw.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vnc.draw.activity.MainActivity;
import com.vnc.draw.activity.MyApplication;
import com.vnc.draw.activity.SendToServiceToDraw;
import com.vnc.draw.content.ISketchPadCallback;
import com.vnc.draw.content.ISketchPadTool;
import com.vnc.draw.content.IUndoCommand;
import com.vnc.draw.utils.BitmapUtil;
import com.vnc.draw.utils.CommonDef;
import com.vnc.draw.utils.SketchPadEraser;
import com.vnc.draw.utils.SketchPadPen;
/**
 * The SketchPadView class provides method to draw strokes on it like as a canvas or sketch pad.
 * We use touch event to draw strokes, when user touch down and touch move, we will remember these
 * point of touch move and set them to a Path object, then draw the Path object, so that user can see the 
 * strokes are drawing real time. When touch up event is occurring, we draw the path to a bitmap which
 * is hold by a canvas, and then draw the bitmap to canvas to display these strokes to user.
 * 
 * @author Li Hong
 * 
 * @date 2010/07/30
 *
 */
public class SketchPadView extends View implements IUndoCommand
{
    public static final int STROKE_NONE = 0;
    public static final int STROKE_PEN = 1;//任意笔  也就是默认笔 会画曲线
    public static final int STROKE_ERASER = 2;//橡皮擦
    public static final int STROKE_TEXT = 3;//键盘输入
    public static final int STROKE_SHAPE4 = 4;//圆角矩形
    public static final int STROKE_SHAPE5 = 5;//椭圆
    public static final int STROKE_SHAPE6 = 6;//圆
    public static final int STROKE_SHAPE7 = 7;//正方形
    public static final int STROKE_SHAPE8 = 8;//五边形
    public static final int STROKE_SHAPE9 = 9;//菱形
    public static final int STROKE_SHAPE10 = 10;//三角形
    public static final int STROKE_SHAPE11 = 11;//向右箭头
    public static final int STROKE_SHAPE12 = 12;//向左箭头
    public static final int STROKE_SHAPE13 = 13;//向上箭头
    public static final int STROKE_SHAPE14 = 14;//向下箭头
    public static final int STROKE_SHAPE15 = 15;//直角三角形
    public static final int STROKE_SHAPE16 = 16;//六边行
    public static final int STROKE_SHAPE17 = 17;//飞机
    public static final int STROKE_SHAPE18 = 18;//三角形和矩形的结合体
    public static final int STROKE_SHAPE19 = 19;//8角形
    public static final int STROKE_SHAPE20 = 20;//8边行
    public static final int STROKE_PICTURE = 21;//照片
    public static final int UNDO_SIZE = 200;
    public static int   flag =0;                    //油漆桶参数
    private boolean m_isEnableDraw = true;
    private boolean m_isDirty = false;
    private boolean m_isTouchUp = false;
    private boolean m_isSetForeBmp = false;
    private int m_bkColor = Color.WHITE;
    private int m_strokeType = STROKE_PEN;
    private int m_strokeColor = Color.BLACK;
    private int m_penSize = 2;
    private int m_eraserSize = CommonDef.MIDDLE_ERASER_WIDTH;
    private int m_canvasWidth = 100;
    private int m_canvasHeight = 100;
    private boolean m_canClear = true;
    private int imageLeft = 0;
    private int imageTop = 0;
    private int imageRight = 0;
    private int imageBottom = 0;
    private Paint m_bitmapPaint = null;
    private Bitmap m_foreBitmap = null;
    public static SketchPadUndoStack m_undoStack = null;
    private Bitmap m_tempForeBitmap = null;
    private Bitmap m_bkBitmap = null;
    public static Canvas m_canvas = null;
    public  ISketchPadTool m_curTool = null;
    private ISketchPadCallback m_callback = null;
    public int yx = 0 ,yy = 0;
    public static ProgressDialog progress;
    private SendToServiceToDraw sendService = null;
    Context context =null;
    public SketchPadView(Context context)
    {
        this(context, null);
        this.context = context;
    }
    
    public SketchPadView(Context context, AttributeSet attrs)
    {
    	
        super(context, attrs);
        this.context = context;
        initialize();
    }

    public SketchPadView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initialize();
    }
    
    public boolean isDirty()
    {
        return m_isDirty;
    }

    public void setDrawStrokeEnable(boolean isEnable)
    {
        m_isEnableDraw = isEnable;
    }
    
    public void setBkColor(int color)
    {
        if (m_bkColor != color)
        {
            m_bkColor = color;
            invalidate();
        }
    }
    public void addForeBitmap(Bitmap foreBitmap){

        if (foreBitmap != m_foreBitmap && null != foreBitmap)
        {
            // Recycle the bitmap.
            if (null != m_foreBitmap)
            {
                m_foreBitmap.recycle();
            }

            m_isSetForeBmp = true;
            
            // Remember the temporary fore bitmap.
            m_foreBitmap = BitmapUtil.getSizedBitmap(foreBitmap, foreBitmap.getWidth(), foreBitmap.getHeight());//.duplicateBitmap(foreBitmap);
            
            // Here create a new fore bitmap to avoid crashing when set bitmap to canvas. 
            m_foreBitmap = BitmapUtil.duplicateBitmap(foreBitmap);
            if (null != m_foreBitmap && null != m_canvas)
            {
                m_canvas.setBitmap(m_foreBitmap);
            }
            
            m_canClear = true;
            
            invalidate();
        }
    
    }

    public void setForeBitmap(Bitmap foreBitmap)
    {
        if (foreBitmap != m_foreBitmap && null != foreBitmap)
        {
            // Recycle the bitmap.
            if (null != m_foreBitmap)
            {
                m_foreBitmap.recycle();
            }

            m_isSetForeBmp = true;
            
            // Remember the temporary fore bitmap.
            m_tempForeBitmap = BitmapUtil.duplicateBitmap(foreBitmap);
            
            // Here create a new fore bitmap to avoid crashing when set bitmap to canvas. 
            m_foreBitmap = BitmapUtil.duplicateBitmap(foreBitmap);
            if (null != m_foreBitmap && null != m_canvas)
            {
//            	setStrokeType(STROKE_PEN);
            	m_canvas.setBitmap(m_foreBitmap);
//                m_canvas.drawBitmap(m_foreBitmap, 0, 0,null);
//                m_undoStack.push(m_curTool);
                
            }
            
            m_canClear = true;
            
            invalidate();
        }
        
        
    	
       /* if (foreBitmap != m_foreBitmap && null != foreBitmap){
        	// Recycle the bitmap.
            if (null != m_foreBitmap){
            	m_foreBitmap.recycle();
            }
            // Here create a new fore bitmap to avoid crashing when set bitmap to canvas. 
            m_foreBitmap = BitmapUtil.duplicateBitmap(foreBitmap);
            if (null != m_foreBitmap && null != m_canvas){  
            	m_canvas.setBitmap(m_foreBitmap);
            }    
            invalidate();
        }
    */
    }
    
    public Bitmap getForeBitmap()
    {
        return m_foreBitmap;
    }

    public void setBkBitmap(Bitmap bmp)
    {        
        if (m_bkBitmap != bmp)
        {
            m_bkBitmap = bmp;
//            m_canvas.drawBitmap(m_bkBitmap, null, null);
            invalidate();
        }
    }
    public void setBkBitmap(Bitmap bmp,int left,int top,int right,int bottom)
    {        
    	imageLeft = left;
    	imageRight = right;
    	imageTop = top;
    	imageBottom = bottom;
        if (m_bkBitmap != bmp)
        {
            m_bkBitmap = bmp;
            invalidate();
        }
    }
    public Bitmap getBkBitmap()
    {
        return m_bkBitmap;
    }
    
    public void setStrokeType(int type)
    {
        switch(type)
        {
        case STROKE_PEN:
            m_curTool = new SketchPadPen(m_penSize, m_strokeColor);
            break;
        case STROKE_ERASER:
            m_curTool = new SketchPadEraser(m_eraserSize);
            break;
        case STROKE_TEXT:
        	setDrawStrokeEnable(false);
            m_curTool = new SketchPadText(m_penSize, m_strokeColor);
            m_curTool.draw(m_canvas);
	   		m_undoStack.push(m_curTool);
	   		 pushText();
            break;
        case STROKE_SHAPE4:
    		m_curTool = new RoundRectuCtl(m_penSize,m_strokeColor);
    		break; 
        case STROKE_SHAPE5:
    		m_curTool = new OvaluCtl(m_penSize,m_strokeColor);
    		break;   
        case STROKE_SHAPE6:
    		m_curTool = new CircleCtl(m_penSize,m_strokeColor);
    		break; 
        case STROKE_PICTURE:
       	   //  invalidate();
        //	 m_undoStack.push(m_curTool);
        	setDrawStrokeEnable(false);
    		 m_curTool = new PictureCtl(m_penSize,m_strokeColor);
    		 m_curTool.draw(m_canvas);
    		 m_undoStack.push(m_curTool);
    		 MyApplication.getInstance().setPic_flag(1);
    		 pushBm();
    		break; 
        }

        m_strokeType = type;
    }

    public void setStrokeSize(int size, int type)
    {
        switch(type)
        {
        case STROKE_PEN://画笔
            m_penSize = size;
            break;
            
        case STROKE_ERASER://橡皮擦
            m_eraserSize = size;
            break;
        }
    }

    public void setStrokeColor(int color)
    {
        m_strokeColor = color;
    }

    public int getStrokeSize()
    {
        return m_penSize;
    }

    public int getStrokeColor()
    {
        return m_strokeColor;
    }
    
    public void clearAllStrokes()
    {
         {
            // Clear the undo stack.
            m_undoStack.clearAll();
           
            
            // Recycle the temporary fore bitmap
            if (null != m_tempForeBitmap)
            {
                m_tempForeBitmap.recycle();
                m_tempForeBitmap = null;
            }
            
            // Create a new fore bitmap and set to canvas.
            createStrokeBitmap(m_canvasWidth, m_canvasHeight);
            
            invalidate();
            m_isDirty = true;
//            m_canClear = false;
        }
    }
    
    
    public Bitmap getCanvasSnapshot()
    {
        setDrawingCacheEnabled(true);
        buildDrawingCache(true);
        Bitmap bmp = getDrawingCache(true);
        
        if (null == bmp)
        {
            android.util.Log.d("leehong2", "getCanvasSnapshot getDrawingCache == null");
        }
       
        return BitmapUtil.duplicateBitmap(bmp);
    }
    public void setCanvasShot(){
    	 setDrawingCacheEnabled(false);
         buildDrawingCache(false);
    }
    public void setCallback(ISketchPadCallback callback)
    {
        m_callback = callback;
    }

    public ISketchPadCallback getCallback()
    {
        return m_callback;
    }

    public void onDeleteFromRedoStack()
    {
        // Do nothing currently.
    }

    public void onDeleteFromUndoStack()
    {
        // Do nothing currently.
    }

    public void redo()
    {
        if (null != m_undoStack)
        {
            m_undoStack.redo();
        }
    }

    public void undo()
    {
        if (null != m_undoStack)
        {
            m_undoStack.undo();
        }
    }

    public boolean canUndo()
    {
        if (null != m_undoStack)
        {
            return m_undoStack.canUndo();
        }
        
        return false;
    }
    
    public boolean canRedo()
    {
        if (null != m_undoStack)
        {
            return m_undoStack.canRedo();
        }
        
        return false;
    }
    public void drawBitmap(Bitmap bitmap,int imageLeft,int imageTop){
    	MyApplication.getInstance().setBitmap(bitmap);
    	MyApplication.getInstance().setBitmapLeft(imageLeft);
    	MyApplication.getInstance().setBitmapTop(imageTop);
    	//MyApplication.getInstance().setTypeShape(23);
    	/*RectF dst = new RectF(imageLeft, imageTop, imageRight, imageBottom);
        Rect  rst = new Rect(0, 0, bitmap.getWidth(),0 bitmap.getHeight());
    	m_canvas.drawBitmap(bitmap, rst, dst, m_bitmapPaint);*/
    	//setTempForeBitmap(bitmap);
    	
    	 m_isTouchUp = false;
    	 setStrokeType(STROKE_PICTURE);
    	 m_undoStack.push(m_curTool);
    	// invalidate();
    	m_curTool.draw(m_canvas);
    	 //setForeBitmap(bitmap);
    	//m_canvas.drawBitmap(bitmap, imageLeft, imageTop,m_bitmapPaint);
    	invalidate();
    	//m_isTouchUp = true;
        m_isDirty = true;
        m_canClear = true;
    }
    public void drawText(String str,int textleft,int textTop,int color){
    	MyApplication.getInstance().setMsg(str);
    	MyApplication.getInstance().setMsgLeft(textleft);
    	MyApplication.getInstance().setMsgTop(textTop);   
    	MyApplication.getInstance().setMsgColor(color);
    	//MyApplication.getInstance().setTypeShape(24);
    //	int  a = m_undoStack.m_stackSize;
    	/*m_isTouchUp = true;
    	setStrokeType(STROKE_PEN);*/
    //	m_curTool.draw(m_canvas);
    	setStrokeType(STROKE_TEXT);
    	//m_curTool.draw(m_canvas);
    	m_isEnableDraw = false;
    	m_undoStack.push(m_curTool);
    	/*m_bitmapPaint.setTextSize(20);
    	m_bitmapPaint.setColor(color);
    	m_canvas.drawText(str, textleft, textTop, m_bitmapPaint);*/
    //	setStrokeType(SketchPadView.STROKE_TEXT);
    	setDrawStrokeEnable(true);
    	m_curTool.draw(m_canvas);
    	invalidate();
    	m_isDirty = true;
        m_canClear = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
    	  yx=(int) event.getX();
		  yy=(int) event.getY();
        if (null != m_callback)
        {
            int action = event.getAction();
            if (MotionEvent.ACTION_DOWN == action)
            {
                m_callback.onTouchDown(this, event);
            }
            else if (MotionEvent.ACTION_UP == action)
            {
                m_callback.onTouchUp(this, event);
            }
        }
        if (m_isEnableDraw)
        {
            m_isTouchUp = false;
            switch(event.getAction())
            {
            case MotionEvent.ACTION_DOWN:
                // This will create a new stroke tool instance with specified stroke type.
                // And the created tool will be added to undo stack.
                setStrokeType(m_strokeType);
                m_curTool.touchDown(event.getX(), event.getY());
                invalidate();
                  m_isDirty = true;
                 m_canClear = true;
                
                
                break;
                
            case MotionEvent.ACTION_MOVE:
            	//if(MyApplication.getInstance().getTypeShape() == 0){
            	if(flag == 0){//判断是不是颜色填充功能 
                m_curTool.touchMove(event.getX(), event.getY());
                // If current stroke type is eraser, draw strokes on bitmap hold by m_canvas.
                if (STROKE_ERASER == m_strokeType)
                {
                    m_curTool.draw(m_canvas);
                }
                invalidate();
                m_isDirty = true;
                m_canClear = true;
            	}
            	//}
                break;
                
            case MotionEvent.ACTION_UP:
            	/*sendService = new SendToServiceToDraw();
            	sendService.sendToReal();*/
            //	if(flag == 0){//判断是不是颜色填充功能 
                m_isTouchUp = true;
               /* if(flag == 1){
                	m_undoStack.push(m_curTool);
                }*/
                if (m_curTool.hasDraw())
                {
                    // Add to undo stack.
                    m_undoStack.push(m_curTool);
                }
                m_curTool.touchUp(event.getX(), event.getY());
                // Draw strokes on bitmap which is hold by m_canvas.
                m_curTool.draw(m_canvas);
                invalidate();
                m_isDirty = true;
                m_canClear = true;  
           // 	}
                if(flag==1){
                	if(progress != null){
                		progress.dismiss();
                		progress = null;
                	}
                	 AlertDialog.Builder ab = new AlertDialog.Builder(context);
					ab.setTitle("提示：");
					ab.setMessage("您确定点击的是封闭性区域并填充吗?");
					ab.setIcon(android.R.drawable.ic_dialog_alert);
					ab.setPositiveButton("是", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							progress = new ProgressDialog(getContext());
	                		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	                		progress.setCancelable(false);
	                		progress.setCanceledOnTouchOutside(false);
	                	progress.setMessage("正在填充图形，请稍候。。。");
	                	progress.show();
	                	Thread thread = new Thread(myRunnable);
	                	thread.start();
						}
					}).setNegativeButton("否", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
					ab.show();
                		
					//flag=0;
                	return false;
				}
                break;
            }
        }
        
        // Here must return true if enable to draw, otherwise the stroke may NOT be drawn.
        return true;
    }
    public Runnable myRunnable = new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			int tcolor = m_foreBitmap.getPixel((int)yx,(int)yy);
			seed_fill((int)yx,(int)yy,tcolor,m_strokeColor);
			Message msg = new Message();
			msg.what = 0;
			myHandler.sendMessage(msg);
		}
	};
	public Handler myHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				progress.dismiss();
				 //m_undoStack.push(m_curTool);
			      int red = (m_strokeColor & 0xff0000) >> 16; 
			      int green = (m_strokeColor & 0x00ff00) >> 8; 
			      int blue = (m_strokeColor & 0x0000ff); 
			      int id = SendToServiceToDraw.getId();
				int[] dataInt0 = {1034,1,30,0,241,id,yx,yy,red,green,blue,0};
//				SendToServiceToDraw sendService = new SendToServiceToDraw();
				MyApplication.getInstance().getSendToService().sFill(dataInt0);	
				invalidate();
			/*	if(m_foreBitmap!=null){
					m_foreBitmap.recycle();
					m_foreBitmap = null;
				}*/
				System.gc();
//				m_undoStack.push(m_curTool);
			}else if(msg.what ==1){
				progress.dismiss();
				invalidate();
			}
			super.handleMessage(msg);
		}
		
	};
    protected void setCanvasSize(int width, int height)
    {
        if (width > 0 && height > 0)
        {
            if (m_canvasWidth != width || m_canvasHeight != height)
            {
                m_canvasWidth = width;
                m_canvasHeight = height;

                createStrokeBitmap(m_canvasWidth, m_canvasHeight);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // Draw background color.
        canvas.drawColor(m_bkColor);
        //canvas.drawColor(m_bkColor, PorterDuff.Mode.SRC_IN);
        // Draw background bitmap.
        if (null != m_bkBitmap)
        {
            RectF dst = new RectF(imageLeft, imageTop, imageRight, imageBottom);
            Rect  rst = new Rect(0, 0, m_bkBitmap.getWidth(), m_bkBitmap.getHeight());
            canvas.drawBitmap(m_bkBitmap, rst, dst, m_bitmapPaint);
        }
        
        if (null != m_foreBitmap)
        {
            canvas.drawBitmap(m_foreBitmap, imageLeft, imageTop, m_bitmapPaint);
        }
        if (null != m_curTool)
        {
            // Do NOT draw current tool stroke real time if stroke type is NOT eraser, because
            // eraser is drawn on bitmap hold by m_canvas.
            if (STROKE_ERASER != m_strokeType)
            {
                // We do NOT draw current tool's stroke to canvas when ACTION_UP event is occurring,
                // because the stroke has been drawn to bitmap hold by m_canvas. But the tool will be 
                // drawn if undo or redo operation is performed.
                if (!m_isTouchUp)
                {
                    m_curTool.draw(canvas);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        
        // If NOT set fore bitmap, call setCanvasSize() method to set canvas size, it will 
        // create a new fore bitmap and set to canvas.
        if (!m_isSetForeBmp)
        {
            setCanvasSize(w, h);
        }
        
        m_canvasWidth = w;
        m_canvasHeight = h;
        
        m_isSetForeBmp = false;
    }

    protected void setTempForeBitmap(Bitmap tempForeBitmap)
    {
        if (null != tempForeBitmap)
        {
            if (null != m_foreBitmap)
            {
                m_foreBitmap.recycle();
            }
            
            m_foreBitmap = BitmapUtil.duplicateBitmap(tempForeBitmap);
            
            if (null != m_foreBitmap && null != m_canvas)
            {
                m_canvas.setBitmap(m_foreBitmap);
                invalidate();
            }
        }
    }

    protected void initialize()
    {
        m_canvas = new Canvas();
        m_bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_undoStack = new SketchPadUndoStack(this, UNDO_SIZE);
        // Set stroke type and create a stroke tool.
       /// setStrokeType(STROKE_PEN);
    }

    protected void createStrokeBitmap(int w, int h)
    {
        m_canvasWidth = w;
        m_canvasHeight = h;
        Bitmap bitmap = Bitmap.createBitmap(m_canvasWidth, m_canvasHeight, Bitmap.Config.ARGB_8888);
        if (null != bitmap)
        {
            m_foreBitmap = bitmap;
            // Set the fore bitmap to m_canvas to be as canvas of strokes.
            m_canvas.setBitmap(m_foreBitmap);
        }
    }
    public static ArrayList<Map<String,Object>> m_undoBm = new ArrayList<Map<String,Object>>();
    public static ArrayList<Map<String,Object>> m_undoText = new ArrayList<Map<String,Object>>();
    public void pushText(){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("msg", MyApplication.getInstance().getMsg());
    	map.put("left", MyApplication.getInstance().getMsgLeft());
    	map.put("top", MyApplication.getInstance().getMsgTop());
    	map.put("color", MyApplication.getInstance().getMsgColor());
    	m_undoText.add(map);
    }
    public void pushBm(){
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("bm", MyApplication.getInstance().getBitmap());
    	map.put("left", MyApplication.getInstance().getBitmapLeft());
    	map.put("top", MyApplication.getInstance().getBitmapTop());
    	m_undoBm.add(map);
    }
    private ArrayList<Bitmap> m_redoBm = new ArrayList<Bitmap>();
    public class SketchPadUndoStack
    {
        private int m_stackSize = 0;
        private SketchPadView m_sketchPad = null;
        private ArrayList<ISketchPadTool> m_undoStack = new ArrayList<ISketchPadTool>();
        private ArrayList<ISketchPadTool> m_redoStack = new ArrayList<ISketchPadTool>();
        private ArrayList<ISketchPadTool> m_removedStack = new ArrayList<ISketchPadTool>();
        
        public SketchPadUndoStack(SketchPadView sketchPad, int stackSize)
        {
            m_sketchPad = sketchPad;
            m_stackSize = stackSize;
        }

        public void push(ISketchPadTool sketchPadTool)
        {
            if (null != sketchPadTool)
            {
                if (m_undoStack.size() == m_stackSize && m_stackSize > 0)
                {
                    ISketchPadTool removedTool = m_undoStack.get(0);
                    m_removedStack.add(removedTool);
                    m_undoStack.remove(0);
                }
                
                m_undoStack.add(sketchPadTool);
            }
        }
        
        public void clearAll()
        {
            m_redoStack.clear();
            m_undoStack.clear();
            m_removedStack.clear();
            m_undoBm.clear();
           // m_redoBm.clear();
            m_undoText.clear();
        }

        public void undo()
        {
        	/*if(pointList.size()>0){
        		progress.setMessage("正在填充图形，请稍候。。。");
            	progress.show();
            	Thread thread = new Thread(pointRunnable);
            	thread.start();
        	}*/
            if (canUndo() && null != m_sketchPad)
            {                
                ISketchPadTool removedTool = m_undoStack.get(m_undoStack.size() - 1);
                m_redoStack.add(removedTool);
                m_undoStack.remove(m_undoStack.size() - 1);
                if (null != m_tempForeBitmap)
                {
                    // Set the temporary fore bitmap to canvas.                    
                    m_sketchPad.setTempForeBitmap(m_sketchPad.m_tempForeBitmap);
                }
                else
                {
                    // Create a new bitmap and set to canvas.
                    m_sketchPad.createStrokeBitmap(m_sketchPad.m_canvasWidth, m_sketchPad.m_canvasHeight);
                }

                Canvas canvas = m_sketchPad.m_canvas;
                
                // First draw the removed tools from undo stack.
                for (ISketchPadTool sketchPadTool : m_removedStack)
                {
                    sketchPadTool.draw(canvas);
                }
                
                for (ISketchPadTool sketchPadTool : m_undoStack)
                {
                    sketchPadTool.draw(canvas);
                    canvas.save();
                    canvas.restore();
                   // MyApplication.getInstance().setPic_flag(1);
                }
                
                m_sketchPad.invalidate();
            }
        }

        public void redo()
        {
            if (canRedo() && null != m_sketchPad)
            {
                ISketchPadTool removedTool = m_redoStack.get(m_redoStack.size() - 1);
                m_undoStack.add(removedTool);
                m_redoStack.remove(m_redoStack.size() - 1);
                
                if (null != m_tempForeBitmap)
                {
                    // Set the temporary fore bitmap to canvas.                    
                    m_sketchPad.setTempForeBitmap(m_sketchPad.m_tempForeBitmap);
                }
                else
                {
                    // Create a new bitmap and set to canvas.
                    m_sketchPad.createStrokeBitmap(m_sketchPad.m_canvasWidth, m_sketchPad.m_canvasHeight);
                }
                
                Canvas canvas = m_sketchPad.m_canvas;
                
                // First draw the removed tools from undo stack.
                for (ISketchPadTool sketchPadTool : m_removedStack)
                {
                    sketchPadTool.draw(canvas);
                }
                
                for (ISketchPadTool sketchPadTool : m_undoStack)
                {
                    sketchPadTool.draw(canvas);
                }
                
                m_sketchPad.invalidate();
            }
        }

        public boolean canUndo()
        {
            return (m_undoStack.size() > 0);
        }

        public boolean canRedo()
        {
            return (m_redoStack.size() > 0);
        }
    }
    public void fillColor(int x,int y,int t_color, int r_color){
    	if(m_foreBitmap.getPixel(x,y) == t_color)
    		m_foreBitmap.setPixel(x,y,r_color);
    		fillColor(x-1,y,t_color,r_color);//左
    		fillColor(x+1,y,t_color,r_color);//右
    		fillColor(x,y+1,t_color,r_color);//下
    		fillColor(x,y-1,t_color,r_color);//上
    	}
   /* private Runnable pointRunnable = new Runnable(){

		public void run() {
			// TODO Auto-generated method stub
			for(int i=0;i<pointList.size();i++){
				int x = (Integer) pointList.get(i).get("x");
				int y = (Integer) pointList.get(i).get("y");
				int tcolor = (Integer) pointList.get(i).get("tcolor");
				int rcolor = (Integer) pointList.get(i).get("mcolor");
				seed_fill(x, y, tcolor, rcolor);
			}
			Message msg = new Message();
			msg.what = 1;
			myHandler.sendMessage(msg);
		}
    	
    };*/
  //队列实现种子递归，用于油漆桶工具   7/28
  	public void seed_fill (int x, int y, int t_color, int r_color){
     		int MAX_COL = MyApplication.getInstance().getScreenw();
  		int MAX_ROW = MyApplication.getInstance().getScreenh();
  		int row_size = MAX_ROW;
  		int col_size = MAX_COL;
  		if (x < 0 || x >= col_size || y < 0 || y >= row_size || m_foreBitmap.getPixel(x,y) == r_color) {
  			return;
  		}   
  		//write of bestar
  		
  		//end of bestar write
  		int queue[][]=new int[MAX_ROW*MAX_COL+1][2];
  		int head = 0, end = 0;
  		int tx, ty;
  		/* Add node to the end of queue. */ 
  		queue[end][0] = x;
  		queue[end][1] = y;
  		end++;
  		while (head < end) {
  			tx = queue[head][0]; 
  			ty = queue[head][1];
  			if (m_foreBitmap.getPixel(tx,ty) == t_color) { 
  				m_foreBitmap.setPixel(tx,ty,r_color);
  			}
  			/* Remove the first element from queue. */ 
  			head++;
  			/* West */ 
  			if (tx-1 >= 0 && m_foreBitmap.getPixel(tx-1,ty) == t_color) {
  				m_foreBitmap.setPixel(tx-1,ty,r_color);
  				queue[end][0] = tx-1;
  				queue[end][1] = ty;
  				end++;
  			}else if(tx-1 >= 0&&m_foreBitmap.getPixel(tx-1,ty)!=t_color){
  				m_foreBitmap.setPixel(tx-1,ty,r_color);
  			}
  			/* East */ 
  			if (tx+1 < col_size && m_foreBitmap.getPixel(tx+1,ty) == t_color) {
  				m_foreBitmap.setPixel(tx+1,ty,r_color);
  				queue[end][0] = tx+1;
  				queue[end][1] = ty;
  				end++;
  			}else if(tx+1 <col_size&&m_foreBitmap.getPixel(tx+1,ty)!=t_color){
  				m_foreBitmap.setPixel(tx+1,ty,r_color);
  			}
  			/* North */ 
  			if (ty-1 >= 0 && m_foreBitmap.getPixel(tx,ty-1) == t_color) {
  				m_foreBitmap.setPixel(tx,ty-1,r_color);
  				queue[end][0] = tx;
  				queue[end][1] = ty-1;
  				end++;
  			}else if(ty-1 >= 0&&m_foreBitmap.getPixel(tx,ty-1)!=t_color){
  				m_foreBitmap.setPixel(tx,ty-1,r_color);
  			}
  			/* South */ 
  			if (ty+1<m_foreBitmap.getHeight() && ty+1 < row_size &&  m_foreBitmap.getPixel(tx,ty+1) == t_color) {
  				m_foreBitmap.setPixel(tx,ty+1,r_color);
  				queue[end][0] = tx;
  				queue[end][1] = ty+1;
  				end++;
  			}else if(ty+1<m_foreBitmap.getHeight() && ty+1<row_size&&m_foreBitmap.getPixel(tx,ty+1)!=t_color){
  				m_foreBitmap.setPixel(tx,ty+1,r_color);
  			}
  		}
  		return; 
  	}          
}
