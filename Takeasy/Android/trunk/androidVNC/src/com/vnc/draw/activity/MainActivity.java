package com.vnc.draw.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import android.androidVNC.AndroidVNCEntry;
import android.androidVNC.R;
import android.androidVNC.VncCanvasActivity;
import android.androidVNC.VncConstants;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.vnc.draw.content.ContentV;
import com.vnc.draw.content.ISketchPadCallback;
import com.vnc.draw.tools.ColorPickerDialog;
import com.vnc.draw.tools.CompassImage;
import com.vnc.draw.tools.SketchPadView;
import com.vnc.draw.utils.BitmapUtil;
import com.vnc.draw.utils.MediaUtil.ImageInfo;
public class MainActivity extends Activity implements View.OnClickListener,
                                                      ISketchPadCallback,View.OnLongClickListener
{
	private int SketchPadPen_flag = 0;//分别依次用来表示顶部的画图功能选择
    private static final int SKETCHPAD_PEN_COLOR = 0x01;
    private static final int SKETCHPAD_BK_COLOR = 0x02;
    private SketchPadView m_sketchPad = null;//画布
    private Button m_penBtn = null;
    private Button m_eraserBtn = null;
    private Button m_clearBtn = null;
    private Button m_undoBtn = null;
    private Button m_redoBtn = null;
    private Button m_saveBtn = null;
    private Button m_loadBtn = null;
    private Button m_pensizeBtn = null;
    private Button m_chooseBtn = null;
    private static final float INIT_IMAGE = 1200l;
    private float imagewh = INIT_IMAGE;
    private LinearLayout size1Layout,size2Layout,size3Layout,size4Layout;//选择画笔大小
    private LinearLayout size21Layout,size22Layout,size23Layout,size24Layout;//选择橡皮擦大小
    private int m_colorSelectorType = SKETCHPAD_PEN_COLOR;
    private LinearLayout sizeLinear,sizeLinear2,softInfoLinear,shapeLinear;
    private int color_flag = 1;//用来判断当前颜色是笔的颜色还是背景的颜色  1代表笔的颜色   2代表背景颜色
    private int dialog_softinfo_flag = 1;//用来标记软件信息框是否显示 1代表没显示  2代表显示
    private Button color1,color2,color3,color4,color5,color6,color7,color8,color9,color10,color11,color12,color13,color14;
    private Button softInfo,keyboardWrite,chooseShape,m_takePickture;//软件信息按钮 键盘输入 选择形状  选择照相机
    private Button colorSelect,penColor,padBgColor;//点击弹出颜色选择器弹出框
    private Button shape1,shape2,shape3,shape4,shape5,shape6,shape7,shape8,shape9,shape10,shape11,shape12
    ,shape13,shape14,shape15,shape16,shape17,shape18,shape19,shape20;
    private Button sBtnArray[]={};
    private Button cancleBtn,addCanvasBtn,brush;//退出按钮   添加画布按钮
    private ColorPickerDialog dialog;//颜色选择器
    private int screenWidth;   
    private int screenHeight;
    private Bitmap m_forePic = null;
    private Bundle bundle = null;
    private Button picBtn,zoomBtn,zoomReduceBtn,cancleImage;//确定图片移动的位置  图片的缩放功能
    private ImageView imageView;
    public  Bitmap initBM =null;
    private float a = 4.0f;
    private float b = 7.0f;
    private float c = 3.0f;
    private  LinearLayout topLinear,typeColor;//顶部工具栏        用来表示画笔的颜色和画板的颜色的层
    private LinearLayout leftLinear;
    private RelativeLayout bestLinear;//最大的布局   
    private int imageBtnFlag = 0 ;//用来标记最右边的确定按钮的功能， 1代表确定图片的位置确定 2代表文字的位置确定
    private int lastPenColor = Color.BLACK;
    private int lastPadColor = Color.WHITE;
    private int lastPenSize = 2;//初始化画笔的大小
    public  EditText et = null;
    public Button openPad;//打开历史画板
    public int save_flag = 0;//用来判断是直接保存画板   还是点击添加新的画布时保存画板
    public String paths = "";
    public int saveOrlookFlag = 0;//用来标记当前操作是保存画板，还是预览画板；0代表保存
    public boolean paint_flag =false;//用来判断是否画过画。
    public boolean cancle_flag = false;//用来判断是否正在进行退出的操作
    public boolean addpad_flag = false;//用来判断是否正在进行添加新画板的操作
    public Button closeInfoBtn ;
    private ProgressDialog progress;
    public SendToServiceToDraw sendService;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sketchpad);
        MyApplication.getInstance().setMainActivity(this);
        ExitApplication.getInstance().context = MainActivity.this;
		ExitApplication.getInstance().addActivity(MainActivity.this);
		ExitApplication.getInstance().addActivity(MyApplication.getInstance().getMainActivity());
        findView();
        initSocket();
        initView();
        initData();
        
    }
  /*  @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	if(hasFocus){
    		int a = m_sketchPad.getWidth();
    		int b = m_sketchPad.getHeight();
    	}
    	super.onWindowFocusChanged(hasFocus);
    }*/
    /**
     * 发送窗口等比命令到服务器
     */
    private void sendToChangeServiceDialog(){
    	DisplayMetrics dm = getResources().getDisplayMetrics();   
        screenWidth = dm.widthPixels;   
        screenHeight = dm.heightPixels;  
        int[] data ={1028,1,22,0,241,screenWidth-57,screenHeight-75};
       /* if((MyApplication.getInstance().getSocket()!=null && !MyApplication.getInstance().getSocket().isClosed())){
        	try {
				MyApplication.getInstance().getSocket().close();
				sendService = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }*/
        if((MyApplication.getInstance().getSocket()!=null )){
        	try{
    			MyApplication.getInstance().getSocket().sendUrgentData(0xFF);
    			}catch(IOException e){
    				try {
    					MyApplication.getInstance().getSocket().close();
    					
    					sendService = new SendToServiceToDraw();//初始化发送给服务器命令的类
    			   	    MyApplication.getInstance().setSocket(SendToServiceToDraw.client);
    			        sendService.sDraw(data);
    			        sendService.setDrawStyle(lastPenSize,lastPenColor);
    			        MyApplication.getInstance().setSendToService(sendService);
    				} catch (IOException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
    			}
        }else{
        	sendService = new SendToServiceToDraw();//初始化发送给服务器命令的类
       	    MyApplication.getInstance().setSocket(SendToServiceToDraw.client);
            sendService.sDraw(data);
            sendService.setDrawStyle(lastPenSize,lastPenColor);
            MyApplication.getInstance().setSendToService(sendService);
        }
    }
    
    /**
     * 发送最小化画板窗口
     */
    private void sendToHideDialog(){
        int[] data ={1035,1,22,0,241};
        MyApplication.getInstance().getSendToService().sDraw(data);
    }
    @Override
    protected void onResume() {
    
    	super.onResume();
    }
    /**
     * 初始化Socket  在4.0.4以上的版本必须加这个才能连接Socket 
     * @author qingfeng.刘星星
     */
    private void initSocket(){
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads().detectDiskWrites().detectNetwork()
        .penaltyLog().build());
     StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
        .build());

    }
    private void initData(){
    	 DisplayMetrics dm = getResources().getDisplayMetrics();   
         screenWidth = dm.widthPixels;   
         screenHeight = dm.heightPixels - 50;   
         MyApplication.getInstance().setTypeShape(1);//初始化形状为曲线
         sendToChangeServiceDialog();//与服务器同步窗口大小
         sClear(false);//清空服务器
       /*  Intent i = this.getIntent();//接收从导入界面传回来的参数
         int bg_flag = i.getIntExtra("bg_flag", 0);
         if(bg_flag == 1){*/
//         	onLoadClick();
//         }
    }
    private void findShapeView(){//初始化多边形控件
    	sBtnArray = new Button[]{shape1,shape2,shape3,shape4,shape5,shape6,shape7,shape8,shape9,
    			shape10,shape11,shape12,shape13,shape14,shape15,shape16,shape17,shape18,shape19,shape20};
    	int idBtnArray[]={R.id.shape1,R.id.shape2,R.id.shape3,R.id.shape4,R.id.shape5,R.id.shape6,
    			R.id.shape7,R.id.shape8,R.id.shape9,R.id.shape10,R.id.shape11,R.id.shape12,
    			R.id.shape13,R.id.shape14,R.id.shape15,R.id.shape16,
    			R.id.shape17,R.id.shape18,R.id.shape19,R.id.shape20};
    	int stroke[] ={SketchPadView.STROKE_PEN,SketchPadView.STROKE_PEN,SketchPadView.STROKE_PEN,SketchPadView.STROKE_SHAPE4
    			,SketchPadView.STROKE_SHAPE5,SketchPadView.STROKE_SHAPE6,SketchPadView.STROKE_PEN,SketchPadView.STROKE_PEN
    			,SketchPadView.STROKE_PEN,SketchPadView.STROKE_PEN,SketchPadView.STROKE_PEN,SketchPadView.STROKE_PEN
    			,SketchPadView.STROKE_PEN,SketchPadView.STROKE_PEN,SketchPadView.STROKE_PEN,SketchPadView.STROKE_PEN
    			,SketchPadView.STROKE_PEN,SketchPadView.STROKE_PEN,SketchPadView.STROKE_PEN,SketchPadView.STROKE_PEN};
    	for(int i=0;i<20;i++){
    		sBtnArray[i] = (Button) findViewById(idBtnArray[i]);
    		sBtnArray[i].setId(i);
    		sBtnArray[i].setTag(stroke[i]);
    		sBtnArray[i].setOnClickListener(new shapeBtnListener());
    	}
    }
    class shapeBtnListener implements OnClickListener{

		public void onClick(View v) {
			MyApplication.getInstance().setTypeShape(v.getId());
			m_sketchPad.setStrokeType((Integer) v.getTag());
			shapeLinear.setVisibility(View.GONE);
		}
    }
    /**
     *  检查文本输入是否保存了所有的文本，如果没有全部保存，当用户进行其他操作时，提醒客户。
     */
    private boolean checkTextInput(){
    	if(et!=null){
    		if(et.getText().toString().trim().length()>0){
    			Toast.makeText(this, "请先处理未保存的文本，再进行其他操作。。。", 10000).show();
    			return false;
    		}else{
    			bestLinear.removeView(et);
    			et = null;
    			return true;
    		}
    	}else{
    		return true;
    	}
    }
    /**
     * 检查照片是否被画入画板
     * @return
     */
    private boolean checkImageState(){
    	if(imageView.isShown()){
    		Toast.makeText(this, "请先处理未保存的照片。。", 5000).show();
    		return false;
    	}else{
    		return true;
    	}
    }
    /**
     * 选择画笔为钢笔
     * @param v
     * @author qingfeng。刘星星
     */
    protected void onPenClick(View v)//选择笔
    {
    	SketchPadView.flag=0;
    	MyApplication.getInstance().setPic_back_flag(0);
        m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
        m_sketchPad.setDrawStrokeEnable(true);
        imageBtnFlag = 0 ;
        MyApplication.getInstance().setTypeShape(1);
        SketchPadPen_flag = 0;
        setBtnBck(0);
    }
    /**
     * 选择键盘输入
     * @param v
     * @author qingfeng.刘星星
     */
    protected void onKeyboardClick(View v){//选择键盘输入
    	SketchPadView.flag=0;
    	MyApplication.getInstance().setPic_back_flag(0);
    	MyApplication.getInstance().setText_back_flag(0);
    	MyApplication.getInstance().setTypeShape(97);
    	setBtnBck(1);
    	imageBtnFlag = 2;
    	SketchPadPen_flag = 1; 
        m_sketchPad.setDrawStrokeEnable(false);
    	}
    /**
     * 选择各种形状的画入
     * @param v
     * @author qingfeng.刘星星
     */
    protected void onChooseShapeClick(View v){//选择形状画笔
    	SketchPadView.flag=0;
    	MyApplication.getInstance().setPic_back_flag(0);
    	MyApplication.getInstance().setTypeShape(0);
    	m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
    	setBtnBck(2);
    	SketchPadPen_flag = 2;
    	m_sketchPad.setDrawStrokeEnable(true);
    }
    /**
     * 选择橡皮擦
     * @param v
     * @author qingfeng.刘星星
     */
    protected void onEraseClick(View v)//选择橡皮擦
    {
    	SketchPadView.flag=0;
    	MyApplication.getInstance().setPic_back_flag(0);
    	MyApplication.getInstance().setTypeShape(96);
        m_sketchPad.setStrokeType(SketchPadView.STROKE_ERASER);
        setBtnBck(3);
        SketchPadPen_flag = 3;
    	m_sketchPad.setDrawStrokeEnable(true);
    }
    /**
     * 拍照函数
     * @param v
     * @author qingfeng.刘星星
     */
  
    private void onTakePictureClick(View v){//拍照函数
    	SketchPadView.flag=0;
    	MyApplication.getInstance().setPic_back_flag(0);
    	MyApplication.getInstance().setTypeShape(98);
    	SketchPadPen_flag =4;
    	m_sketchPad.setDrawStrokeEnable(false);
    	setBtnBck(4);
    	MyApplication.getInstance().setDataByte(null);
    	Intent intent =new Intent(this,CameraPreview.class);
    	startActivityForResult(intent, 20);
    /*	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);*/
    	imageTouchFlag = 0;
    	imagewh = INIT_IMAGE;
    }
    /**
     * 清除画布上的所有东西
     * @param v
     * @author qingfeng.刘星星
     */
    protected void onClearClick()//选择清除画布  不可恢复
    {
    	SketchPadView.flag=0;
    	MyApplication.getInstance().setPic_back_flag(0);
    	m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
    	m_sketchPad.setDrawStrokeEnable(true);
        m_sketchPad.clearAllStrokes();
        m_clearBtn.setEnabled(false);
        m_redoBtn.setEnabled(false);
        m_undoBtn.setEnabled(false);
        paint_flag = false;
         if(imageView.isShown()){
			 imageView.setVisibility(View.GONE);
	    	 picBtn.setVisibility(View.GONE);
	    	 zoomBtn.setVisibility(View.GONE);
	    	 zoomReduceBtn.setVisibility(View.GONE);
	    	 cancleImage.setVisibility(View.GONE);
	    	 m_sketchPad.setDrawStrokeEnable(false);
         }
         if(et!=null){
//        	 MyApplication.getInstance().setText_back_flag(0);
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))   
            .hideSoftInputFromWindow(et.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);   
    		 bestLinear.removeView(et);
    		 et = null;
    		 m_sketchPad.setDrawStrokeEnable(false);
         }
         if(SketchPadPen_flag == 3){
        	 SketchPadPen_flag = 0;
        	 MyApplication.getInstance().setTypeShape(1);
         }
         setBtnBck(SketchPadPen_flag);
         if(SketchPadPen_flag == 4 || SketchPadPen_flag == 5 ){
        	 m_sketchPad.setDrawStrokeEnable(false);
         }
        sClear(false);
    }
    /**
     * 后退函数，一次去掉相对来说最后的笔画
     * @param v
     * @author qingfeng.刘星星
     */
    protected void onUndoClick(View v)//后退
    {
    	if(MyApplication.getInstance().back_pic!=0){
    		MyApplication.getInstance().back_pic = 0;
    	}
    	if(MyApplication.getInstance().back_text!=0){
    		MyApplication.getInstance().back_text = 0;
    	}
    	MyApplication.getInstance().setPic_back_flag(1);
    	MyApplication.getInstance().setText_back_flag(1);
    	SketchPadView.flag=0;
        m_sketchPad.undo();
        m_sketchPad.setDrawStrokeEnable(false);
        m_undoBtn.setEnabled(m_sketchPad.canUndo());
        m_redoBtn.setEnabled(m_sketchPad.canRedo());
    }
    /**
     * 将去掉的最后一笔重新画出来
     * @param v
     * @author qingfeng.刘星星
     */
    protected void onRedoClick(View v)//前进
    {
    	if(MyApplication.getInstance().back_pic!=0){
        	MyApplication.getInstance().back_pic = 0;
        }
    	if(MyApplication.getInstance().back_text!=0){
    		MyApplication.getInstance().back_text = 0;
    	}
    	MyApplication.getInstance().setPic_back_flag(2);
    	MyApplication.getInstance().setText_back_flag(2);
    	SketchPadView.flag=0;
        m_sketchPad.redo();
        m_sketchPad.setDrawStrokeEnable(false);
        m_undoBtn.setEnabled(m_sketchPad.canUndo());
        m_redoBtn.setEnabled(m_sketchPad.canRedo());
    }
    /**
     * 保存画布函数
     * @param v
     * @author qingfeng.刘星星
     */
    protected void onSaveClick(String path)//保存画布
    {
    	SketchPadView.flag=0;
    	MyApplication.getInstance().setPic_back_flag(0);
        String strFilePath = CompassImage.getStrokeFilePath(path);
        Bitmap bmp = m_sketchPad.getCanvasSnapshot();
        if (null != bmp){
            BitmapUtil.saveBitmapToSDCard(bmp, strFilePath);
            Toast.makeText(getApplicationContext(), "保存成功。。。", 5000).show();
            m_sketchPad.setCanvasShot();
            m_sketchPad.invalidate();
        }else{
        	Toast.makeText(getApplicationContext(), "保存失败。。。", 5000).show();
        }
    }
    /**
     * 从特殊文件导入图片成为画布的一部分
     * @param v
     * @author qingfeng.刘星星
     */
    protected void onLoadClick(Intent data)//实现从文件导入  进行修改
    {
    	paint_flag = true;
//    	Intent i = this.getIntent();
        String strFilePath = data.getStringExtra("bgPic");
        Bitmap bmp = BitmapUtil.loadBitmapFromSDCard(strFilePath);
        if (null != bmp){
            m_sketchPad.setForeBitmap(bmp);
//            m_sketchPad.setBkBitmap(bmp);
            MyApplication.getInstance().setBitmap(bmp);
            MyApplication.getInstance().setBitmapLeft(0);
            MyApplication.getInstance().setBitmapTop(0);
            MyApplication.getInstance().setDataByte(SendToServiceToDraw.Bitmap2Bytes(bmp));
            MyApplication.getInstance().setPicId(SendToServiceToDraw.getId());
            MyApplication.getInstance().getSendToService().sDrawPic();
            MyApplication.getInstance().getSendToService().sendToReal();
        }
        m_undoBtn.setEnabled(true);
        m_clearBtn.setEnabled(true);
    }
    /**
     * 选择画笔的大小
     * @param v
     * @author qingfeng.刘星星
     */
    protected void onPenSizeClick(View v)//选择笔的大小
    {
    	sizeLinear.setVisibility(View.VISIBLE);
    }
    /**
     * 选择直接从SD卡读取图片导入画板
     * @param v
     * @author qingfeng.刘星星
     */
    protected void onChoosePictureClick(View v)//选择文件
    {
        Intent intent = new Intent(this, PictureSelectActivity.class);
        startActivityForResult(intent, 0);
    }
    /**
     * 程序暂时跳转再返回的接纳函数
     * @param v
     * @author 系统函数
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) //选择文件后的返回函数
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
             if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
               Toast.makeText(this, "SD卡不存在。。。", 3000).show();
                 return;
             }else{
            	 if(data!=null){
            		 Uri uri = data.getData();
                	 if (uri != null) {
                		 m_forePic = BitmapFactory.decodeFile(uri.getPath());
                	 }
            	 }
            	 if (m_forePic == null) {
            	  Bundle bundle = data.getExtras();
            	  if (bundle != null) {
            		  m_forePic = (Bitmap) bundle.get("data");
            	  } else {
            	   Toast.makeText(MainActivity.this,"获取图片失败。。", Toast.LENGTH_LONG).show();
            	   return;
            	  }
            	 }
            	 if(m_forePic != null){
            			MyApplication.getInstance().setDataByte(Bitmap2Bytes(m_forePic));
		        	   	initBM = m_forePic;
		                float aa = imagewh*(a/b);
		                float bb = imagewh*(c/b);
		                m_forePic = CompassImage.scaleImg(m_forePic,aa,bb);
		           /*     }catch  (OutOfMemoryError err) {
		                }*/
                picHandler.sendEmptyMessage(0);
            	 }

            /*	 if(data!=null && data.getExtras()!=null){
            		 bundle = data.getExtras();
//            		 MyApplication.getInstance().setDataByte(buffer);
            		 showProgress();
                 	Thread thread = new Thread(picRunnable);
                 	thread.start();
            	 }*/
             }
        }
        switch (requestCode) 
        {
        case 0: 
            if (RESULT_OK == resultCode) 
            {
                try
                {
                    Bundle bundle = data.getExtras();
                    ImageInfo imgInfo = PictureSelectActivity.getImageInfoFromBundle(bundle);
                    Uri uri = imgInfo.imageUri;
                    ContentResolver cr = this.getContentResolver();
                    Bitmap bmp = BitmapFactory.decodeStream(cr.openInputStream(uri)); 
                    if (null != bmp)
                    {
                        m_sketchPad.setBkBitmap(bmp);
                    }
                }catch (Exception e)
                {   
                    e.printStackTrace();
                }
            }
            break;
        case 20:
        	if(MyApplication.getInstance().getDataByte()!=null){
        		showProgress();
            	Thread thread = new Thread(picRunnable);
            	thread.start();
        	}
        	break;
        case 100:
        	if(data.getIntExtra("back_type", 0) == 1){
	        	sClear(false);
	        	onClearClick();
	        	onLoadClick(data);
        	}
        	break;
        }
    }
    Runnable picRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
	        	 byte[] buffer = MyApplication.getInstance().getDataByte();
	    			if(buffer != null){
			    		   BitmapFactory.Options opts =  new  BitmapFactory.Options();
			    		   opts.inJustDecodeBounds =  true ;
	    		           BitmapFactory.decodeByteArray(buffer, 0, buffer.length, opts);
	    		           opts.inSampleSize = CompassImage.computeSampleSize(opts, -1 ,800 * 600 ); 
	    		           opts.inJustDecodeBounds =  false ;
	    		           try  {
	    		        	   m_forePic = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, opts);
//	    				 		m_forePic = (Bitmap) bundle.get("data");
//	    				 		MyApplication.getInstance().setDataByte(Bitmap2Bytes(m_forePic));
	    		        	   	initBM = m_forePic;
	    		                float aa = imagewh*(a/b);
	    		                float bb = imagewh*(c/b);
	    		                m_forePic = CompassImage.scaleImg(m_forePic,aa,bb);
	    		              }catch  (OutOfMemoryError err) {
	    		                }
	                    picHandler.sendEmptyMessage(0);
	    			}
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
		}
    	
    };
    public byte[] Bitmap2Bytes(Bitmap bm) {
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    return baos.toByteArray();
  }
    Handler picHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(m_forePic!=null){
				MyApplication.getInstance().setBitmap(m_forePic);
	            MyApplication.getInstance().setPicW(m_forePic.getWidth());
	            MyApplication.getInstance().setPicH(m_forePic.getHeight());
	        	imageView.setVisibility(View.VISIBLE);
	        	picBtn.setVisibility(View.VISIBLE);
	            zoomBtn.setVisibility(View.VISIBLE);
	        	zoomReduceBtn.setVisibility(View.VISIBLE);
	        	cancleImage.setVisibility(View.VISIBLE);
	        	imageBtnFlag = 1;
				setImageViewLayout(imageView,leftLinear.getWidth()+200,topLinear.getHeight()+200,0,0);
				imageView.setImageBitmap(m_forePic); 
	            imageView.setOnTouchListener(movingEventListener);
	            MyApplication.getInstance().setBitmapLeft(200);
	            MyApplication.getInstance().setBitmapTop(200);
	            MyApplication.getInstance().getSendToService().sDrawPic();
	            progress.dismiss();
        	}
			super.handleMessage(msg);
		}
    	
    };
    
    /*
     * 创建并显示进度条
     */
    private void showProgress(){
    	progress = new ProgressDialog(MainActivity.this);
    	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	progress.show();
    	progress.setMessage("正在处理图片，请稍候。。。");
    	progress.setCancelable(false);
    	progress.setCanceledOnTouchOutside(false);
    	
    }
    public void setImageViewLayout(View v,int left,int top,int right,int bottom) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				m_forePic.getWidth(), m_forePic.getHeight());
		params.setMargins(left, top, 0, 0);// 通过自定义坐标来放置你的控件
		v.setLayoutParams(params);
	}
    public void onDestroy(SketchPadView obj)
    {
    	sClear(false);
    	 MyApplication.getInstance().getSendToService().closeSocket();
//    	sendService.closeSocket();
    }
    /**
     * 当点击画板面时触发该函数，开始画图
     * @param v
     * @author qingfeng.刘星星
     */
    public void onTouchDown(SketchPadView obj, MotionEvent event)
    {
    	
    	paint_flag = true;
        m_clearBtn.setEnabled(true);
        m_undoBtn.setEnabled(true);
        sizeLinear.setVisibility(View.GONE);
        sizeLinear2.setVisibility(View.GONE);
        softInfoLinear.setVisibility(View.GONE);
        shapeLinear.setVisibility(View.GONE);
        if(SketchPadPen_flag == 1){
        	if(et != null){
        		if(et.getText().toString().equals("")){
        			bestLinear.removeView(et);
            		et = null;
        		}else{
        		 MyApplication.getInstance().setText_back_flag(0);
        		 int x = et.getLeft()-leftLinear.getWidth()+6;
         		 int y = et.getTop()-topLinear.getHeight()+(et.getHeight()/2+6);
                m_sketchPad.setDrawStrokeEnable(false);
                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE))   
                .hideSoftInputFromWindow(et.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);   
         		// m_sketchPad.drawText(et.getText().toString(), x, y,lastPenColor);
         		MyApplication.getInstance().setMsg(et.getText().toString());
            	MyApplication.getInstance().setMsgLeft(x);
            	MyApplication.getInstance().setMsgTop(y);   
            	MyApplication.getInstance().setMsgColor(lastPenColor);
         		m_sketchPad.setStrokeType(SketchPadView.STROKE_TEXT);
        		 bestLinear.removeView(et);
        		 et = null;
        		}
        		 MyApplication.getInstance().getSendToService().sendToReal();
        	}else{
        		et = new EditText(this);
            	et.setWidth(80);
            	et.setHeight(50);
            	et.setSingleLine(true);
            	et.setBackgroundDrawable(null);
            	bestLinear.addView(et);
            	et.requestFocus();
            	InputMethodManager m = (InputMethodManager)et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);   
            	m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);   
            	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(80,50);
    	     	params.setMargins((int)event.getX()+leftLinear.getWidth(), (int)event.getY()+topLinear.getHeight()-(et.getHeight()/2), 0, 0);// 通过自定义坐标来放置你的控件
    	     	et.setLayoutParams(params);
    	     	et.setVisibility(View.VISIBLE);
    	     	et.addTextChangedListener(twListener);
    	     	MyApplication.getInstance().setTextId(SendToServiceToDraw.getId());
        	}
        	
        }
        dialog_softinfo_flag = 1;
    }
    
    public void onTouchUp(SketchPadView obj, MotionEvent event)
    {
    }
    int etL = 0,etR = 0,etWidth = 10,zengzhi = 10;//键盘输入时的文本框的参数  最左边的点的横坐标和纵坐标      文本宽的宽度 和文本框的每增加一个字的宽度增量
    /**
     *键盘输入的监听，动态改变文本框的宽度，
     *每增加一个字增加一定的宽度保证文本框里面的每个字都能显示
     * @param v
     * @author qingfeng.刘星星
     */
    private String msg = "";
    public TextWatcher twListener = new TextWatcher() {
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			zengzhi = 18*count;
		}
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			if(et!=null){
			 et.setTextColor(lastPenColor);
			 etL = et.getLeft();
    		 etR = et.getTop();
    		 etWidth = et.getWidth();
    		 msg = et.getText().toString();
			}
		}
		public void afterTextChanged(Editable s) {
			m_sketchPad.setDrawStrokeEnable(false);
			if(et!=null){
         	et.setWidth(etWidth+zengzhi);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(etWidth+zengzhi,50);
	     	params.setMargins(etL, etR, 0, 0);// 通过自定义坐标来放置你的控件
	     	et.setLayoutParams(params);
	     	int id = MyApplication.getInstance().getTextId();
	     	if(msg!=null && msg.equals("")){
		     	MyApplication.getInstance().setMsg(et.getText().toString());
		     	int x = et.getLeft()-leftLinear.getWidth()+6;
	    		int y = et.getTop()-topLinear.getHeight()+(et.getHeight()/2+6);
	    		MyApplication.getInstance().setMsgLeft(x);
	         	MyApplication.getInstance().setMsgTop(y);   
		     	 MyApplication.getInstance().getSendToService().sDrawText(1214,id);
	     	}else if(msg!=null && !msg.equals("")){
	     		MyApplication.getInstance().setMsg(et.getText().toString());
		     	int x = et.getLeft()-leftLinear.getWidth()+6;
	    		int y = et.getTop()-topLinear.getHeight()+(et.getHeight()/2+6);
	    		MyApplication.getInstance().setMsgLeft(x);
	         	MyApplication.getInstance().setMsgTop(y);   
		     	 MyApplication.getInstance().getSendToService().sDrawText(1215,id);
	     	}
			}
		}
	};
	
		 public void onClick(View v)
		    {
		        if(v.getId() == R.id.pen)
		        {
		        	if(checkTextInput() && checkImageState())
		             onPenClick(v);
		        }else if(v.getId() == R.id.eraser){
		        	if(checkTextInput() && checkImageState())
		        	 onEraseClick(v);
		        }else if(v.getId() == R.id.clear){
//		        	if(checkTextInput())
		        	 clearBtnDialog();
		        }else if(v.getId() == R.id.undo){
		        	if(checkTextInput() && checkImageState()){
//			        	 m_sketchPad.setDrawStrokeEnable(false);
			        	 onUndoClick(v);
			        	 setBtnBck(6);
			        	  MyApplication.getInstance().getSendToService().sendToUndo();
		        	}
		        }else if(v.getId() == R.id.redo){
		        	if(checkTextInput() && checkImageState()){
//		        	 m_sketchPad.setDrawStrokeEnable(false);
		        	 onRedoClick(v);
		        	 setBtnBck(8);
		        	  MyApplication.getInstance().getSendToService().sendToRedo();
		        	}
		        }else if(v.getId() == R.id.save){//打开保存窗口
		        	if(checkTextInput() && checkImageState()){
		        	setBtnBck(9);
		        	save_flag = 0;
		        	showRootDialog();
		        	}
		        }else if(v.getId() == R.id.open){//打开预览窗口
		        	setBtnBck(13);
		        	saveOrlookFlag = 1;
		        	showListRootDialog("选择预览目录：");
		        }else if(v.getId() == R.id.softInfo){//打开软件信息窗口
		        	setBtnBck(11);
		        	SketchPadView.flag=0;
		        	if(dialog_softinfo_flag == 1){
		        		dialog_softinfo_flag = 2;
		        		softInfoLinear.setVisibility(View.VISIBLE);
		        		sizeLinear.setVisibility(View.GONE);
		        		sizeLinear2.setVisibility(View.GONE);
		        		shapeLinear.setVisibility(View.GONE);
		        		m_sketchPad.setDrawStrokeEnable(false);
		        	}else if(dialog_softinfo_flag == 2){
		        		dialog_softinfo_flag  = 1;
		        		softInfoLinear.setVisibility(View.GONE);
		        		sizeLinear.setVisibility(View.GONE);
		        		sizeLinear2.setVisibility(View.GONE);
		        		shapeLinear.setVisibility(View.GONE);
		        		m_sketchPad.setDrawStrokeEnable(true);
		        	}
		        }else if(v.getId() == R.id.addnewPaint){//添加新的画板并保存之前的画板
		        	save_flag = 1;
		        	setBtnBck(10);
		        	final AlertDialog.Builder ab = new AlertDialog.Builder(this);
					ab.setTitle("提示：");
					ab.setMessage("画板尚未保存，是否保存当前画板？");
					ab.setIcon(android.R.drawable.ic_dialog_alert);
					ab.setPositiveButton("保存", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							if(checkTextInput() && checkImageState()){
								addpad_flag = true;
								showRootDialog();
							}
						}
					}).setNegativeButton("不保存", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							addpad_flag = false;
							onClearClick();
						}
					});
					ab.show();
		        }else if(v.getId() == R.id.keyboardwrite){//选择键盘输入
		        	if(checkTextInput() && checkImageState())
		        	onKeyboardClick(v);
		        }else if(v.getId() == R.id.chooseShape){//选择多边形
		        	if(checkTextInput() && checkImageState())
		        	onChooseShapeClick(v);
		        }else if(v.getId() == R.id.takePicture){//照相
		        	if(checkTextInput() && checkImageState())
		        	onTakePictureClick(v);
		        }else if(v.getId() == R.id.load){
		        	
		        }else if(v.getId() == R.id.penSize){
		        	onPenSizeClick(v);
		        }else if(v.getId() == R.id.choose){
		        	onChoosePictureClick(v);
		        }else if(v.getId() == R.id.zoomBtn){
		        	imagewh+=20;
		        	float aa = imagewh*(a/b);
	                float bb = imagewh*(c/b);
		        	if((imageView.getLeft()+aa)>screenWidth && (imageView.getTop()+bb)>screenHeight 
		        			&& imageView.getLeft()-20>=0 && imageView.getTop()-20>=0){
		        		m_forePic = CompassImage.scaleImg(initBM,aa,bb);
			        	imageView.setImageBitmap(m_forePic); 
			        	 setImageViewLayout(imageView,imageView.getRight()-m_forePic.getWidth(),imageView.getBottom()-m_forePic.getHeight(),
			        			 imageView.getRight(),imageView.getBottom());
		        	}else if((imageView.getLeft()+aa)>screenWidth && imageView.getLeft()-10<=0 ){
		        		imagewh -=20;
		        		return ;
		        	}else if((imageView.getTop()+bb)>screenHeight && imageView.getTop()-10<=0){
		        		imagewh -=20;
		        		return ;
		        	}else if((imageView.getLeft()+aa)>screenWidth && (imageView.getTop()+bb)<=screenHeight 
		        			&& imageView.getLeft()-10>=0){
		        		m_forePic = CompassImage.scaleImg(initBM,aa,bb);
			        	imageView.setImageBitmap(m_forePic); 
			        	 setImageViewLayout(imageView,imageView.getRight()-m_forePic.getWidth(),imageView.getTop(),
			        			 imageView.getRight(),imageView.getTop()+m_forePic.getHeight());
	                   
		        	}else if((imageView.getLeft()-10) <= 0 && (imageView.getBottom()+10)>=screenHeight 
		        			&& imageView.getTop()-10>=0 && (imageView.getRight()+10)<=screenWidth){
		        		m_forePic = CompassImage.scaleImg(initBM,aa,bb);
			        	imageView.setImageBitmap(m_forePic); 
			        	 setImageViewLayout(imageView,imageView.getLeft(),imageView.getBottom()-m_forePic.getHeight(),
			        			 imageView.getLeft()+m_forePic.getWidth(),imageView.getBottom());
	                
		        	}else if((imageView.getLeft()-10) <= 0 && (imageView.getBottom()+10)<=screenHeight 
		        			&& imageView.getTop()-10<=0 && (imageView.getRight()+10)<=screenWidth){
		        		m_forePic = CompassImage.scaleImg(initBM,aa,bb);
			        	imageView.setImageBitmap(m_forePic); 
			        	 setImageViewLayout(imageView,imageView.getLeft(),imageView.getTop(),
			        			 imageView.getLeft()+m_forePic.getWidth(),imageView.getTop()+m_forePic.getHeight());
	                  
		        	}else if((imageView.getLeft()-10)<=0 && (imageView.getTop()+bb)<=screenHeight 
		        			&& imageView.getRight()+10<=screenWidth){
		        		m_forePic = CompassImage.scaleImg(initBM,aa,bb);
			        	imageView.setImageBitmap(m_forePic); 
			        	 setImageViewLayout(imageView,imageView.getLeft(),imageView.getTop(),
			        			 imageView.getLeft()+m_forePic.getWidth(),imageView.getTop()+m_forePic.getHeight());
	                   
		        	}else if((imageView.getLeft()-10) > 0 && (imageView.getBottom()+10)<screenHeight 
		        			&& imageView.getTop()-10<=0 && (imageView.getRight()+10)<=screenWidth){
		        		m_forePic = CompassImage.scaleImg(initBM,aa,bb);
			        	imageView.setImageBitmap(m_forePic); 
			        	 setImageViewLayout(imageView,imageView.getLeft(),imageView.getTop(),
			        			 imageView.getLeft()+m_forePic.getWidth(),imageView.getTop()+m_forePic.getHeight());
	                  
		        	}else if((imageView.getLeft()-10) > 0 && (imageView.getBottom()+10)>=screenHeight 
		        			&& imageView.getTop()-10 > 0 && (imageView.getRight()+10)<=screenWidth){
		        		m_forePic = CompassImage.scaleImg(initBM,aa,bb);
			        	imageView.setImageBitmap(m_forePic); 
			        	 setImageViewLayout(imageView,imageView.getLeft(),imageView.getBottom()-m_forePic.getHeight(),
			        			 imageView.getLeft()+m_forePic.getWidth(),imageView.getBottom());
	                   
		        	}else if((imageView.getLeft()-10) > 0 && (imageView.getBottom()+10)<screenHeight 
		        			&& imageView.getTop()-10 > 0 && (imageView.getRight()+10)<screenWidth){
			        	m_forePic = CompassImage.scaleImg(initBM,aa,bb);
			        	imageView.setImageBitmap(m_forePic); 
			        	setImageViewLayout(imageView,imageView.getLeft(),imageView.getTop(),
			        			 imageView.getLeft()+m_forePic.getWidth(),imageView.getTop()+m_forePic.getHeight());
		        	}
		        	 	MyApplication.getInstance().setPicW(m_forePic.getWidth());
	                    MyApplication.getInstance().setPicH(m_forePic.getHeight());
	                    MyApplication.getInstance().setBitmapLeft(imageView.getLeft()-leftLinear.getWidth());
	    	            MyApplication.getInstance().setBitmapTop(imageView.getTop()-topLinear.getHeight());
	                     MyApplication.getInstance().getSendToService().sUpdatePic();
		        }else if(v.getId() == R.id.reducezoomBtn){
		        	imagewh-=20;
		        	/*if((imagewh+imageView.getRight())>screenWidth || imagewh+imageView.getBottom()>screenHeight){
		        		return;
		        	}else{*/
		        	if(imagewh>40){
			        	float aa = imagewh*(a/b);
		                float bb = imagewh*(c/b);
			        	m_forePic = CompassImage.scaleImg(initBM,aa,bb);
			        	imageView.setImageBitmap(m_forePic); 
//			        	imageView.layout(imageView.getLeft(), imageView.getTop(), 
//			        			imageView.getLeft()+m_forePic.getWidth(), imageView.getTop()+imageView.getHeight());
	                    /*imageView.setVisibility(View.VISIBLE);
	                    imageView.setOnTouchListener(movingEventListener);  */
			        	setImageViewLayout(imageView,imageView.getLeft(),imageView.getTop(),
			        			 imageView.getLeft()+m_forePic.getWidth(),imageView.getTop()+m_forePic.getHeight());
	                    MyApplication.getInstance().setPicW(m_forePic.getWidth());
	                    MyApplication.getInstance().setPicH(m_forePic.getHeight());
	                     MyApplication.getInstance().getSendToService().sUpdatePic();
		        	}else{
		        		imagewh+=20;
		        		Toast.makeText(MainActivity.this, "已经缩到最小了", 5000).show();
		        	}
		        }else if(v.getId() == R.id.imageBtn){
		        	 if(imageBtnFlag == 1){
		        		 AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
							ab.setTitle("提示：");
							ab.setMessage("确定保存图片到当前位置吗?");
							ab.setIcon(android.R.drawable.ic_dialog_alert);
							ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								
								public void onClick(DialogInterface dialog, int which) {
									MyApplication.getInstance().setBitmap(m_forePic);
					        	    MyApplication.getInstance().setBitmapLeft(imageView.getLeft()-leftLinear.getWidth());
				        	        MyApplication.getInstance().setBitmapTop(imageView.getTop()-topLinear.getHeight());
					        	    MyApplication.getInstance().setPic_back_flag(0);
					        	    m_sketchPad.setDrawStrokeEnable(false);
					        	    m_clearBtn.setEnabled(true);
					                m_undoBtn.setEnabled(true);
					        		m_sketchPad.setStrokeType(SketchPadView.STROKE_PICTURE);
					        		 imageView.setVisibility(View.GONE);
						        	 imageTouchFlag = 1;
						        	 picBtn.setVisibility(View.GONE);
						        	 zoomBtn.setVisibility(View.GONE);
						        	 zoomReduceBtn.setVisibility(View.GONE);
						        	 cancleImage.setVisibility(View.GONE);
//						        	 imagewh = INIT_IMAGE;
						        	 paint_flag = true;
						        	 
						        	  MyApplication.getInstance().getSendToService().sendToReal();
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
								
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									return;
								}
							});
							ab.show();
		        		
		        	 }else if(imageBtnFlag == 2){
		        	 }
		        }else if(v.getId() == R.id.cancleImage){
		        	AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
					ab.setTitle("提示：");
					ab.setMessage("确定删除该照片吗?");
					ab.setIcon(android.R.drawable.ic_dialog_alert);
					ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							 imageView.setVisibility(View.GONE);
				        	 picBtn.setVisibility(View.GONE);
				        	 zoomBtn.setVisibility(View.GONE);
				        	 zoomReduceBtn.setVisibility(View.GONE);
				        	 cancleImage.setVisibility(View.GONE);
				        	 imagewh = INIT_IMAGE;
				        	  MyApplication.getInstance().getSendToService().canclePic();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							return;
						}
					});
					ab.show();
		        	 
		        }else if(v.getId() == R.id.cancel){
		        	if(checkTextInput() && checkImageState()){
		        		setBtnBck(12);
			        	SketchPadView.flag=0;
			        		cancleBtnDialog();
		        	}
		        }else if(v.getId() == R.id.brush){
		        	if(checkTextInput() && checkImageState()){
		        /*	 MyApplication.getInstance().getSendToService() = new SendToServiceToDraw();
		        	 MyApplication.getInstance().getSendToService().drawBrush();*/
		        	SketchPadView.flag=1;
		        	setBtnBck(5);
		        	m_sketchPad.setStrokeType(SketchPadView.STROKE_PEN);
		        	m_sketchPad.setDrawStrokeEnable(true);
		        	SketchPadPen_flag = 5;
		        	 DisplayMetrics dm = getResources().getDisplayMetrics();   
		             screenWidth = dm.widthPixels;   
		             screenHeight = dm.heightPixels;   
		             MyApplication.getInstance().setTypeShape(99);
		             MyApplication.getInstance().setScreenw(screenWidth-leftLinear.getWidth());
		             MyApplication.getInstance().setScreenh(screenHeight-topLinear.getHeight());
		             MyApplication.getInstance().setTopLinearHeight(topLinear.getHeight());
		             MyApplication.getInstance().setLeftLinearWidth(leftLinear.getWidth());
		             
		        	}
		        }else if(v.getId() == R.id.closeDialog){
		        	
		        }else if(v.getId() == R.id.size1){
		        	MyApplication.getInstance().setPaintsize(ContentV.SIZE_1);
		        	lastPenSize = ContentV.SIZE_1;
		        	sizeLinear.setVisibility(View.GONE);
		        	m_sketchPad.setStrokeSize(ContentV.SIZE_1, 1);
		        	 MyApplication.getInstance().getSendToService().setDrawStyle(ContentV.SIZE_1,lastPenColor);
		        }else if(v.getId() == R.id.size2){
		        	MyApplication.getInstance().setPaintsize(ContentV.SIZE_2);
		        	lastPenSize = ContentV.SIZE_2;
		        	 sizeLinear.setVisibility(View.GONE);
		        	 m_sketchPad.setStrokeSize(ContentV.SIZE_2, 1);
		        	  MyApplication.getInstance().getSendToService().setDrawStyle(ContentV.SIZE_2,lastPenColor);
		        }else if(v.getId() == R.id.size3){
		        	MyApplication.getInstance().setPaintsize(ContentV.SIZE_3);
		        	lastPenSize = ContentV.SIZE_3;
		        	 sizeLinear.setVisibility(View.GONE);
		        	 m_sketchPad.setStrokeSize(ContentV.SIZE_3, 1);
		        	  MyApplication.getInstance().getSendToService().setDrawStyle(ContentV.SIZE_3,lastPenColor);
		        }else if(v.getId() == R.id.size4){
		        	MyApplication.getInstance().setPaintsize(ContentV.SIZE_4);
		        	lastPenSize = ContentV.SIZE_4;
		        	 sizeLinear.setVisibility(View.GONE);
		        	 m_sketchPad.setStrokeSize(ContentV.SIZE_4, 1);
		        	  MyApplication.getInstance().getSendToService().setDrawStyle(ContentV.SIZE_4,lastPenColor);
		        }else if(v.getId() == R.id.size21){
		        	sizeLinear2.setVisibility(View.GONE);
		        	m_sketchPad.setStrokeSize(ContentV.SIZE_1, 2);
		        }else if(v.getId() == R.id.size22){
		        	 sizeLinear2.setVisibility(View.GONE);
		        	 m_sketchPad.setStrokeSize(ContentV.SIZE_2, 2);
		        }else if(v.getId() == R.id.size23){
		        	 sizeLinear2.setVisibility(View.GONE);
		        	 m_sketchPad.setStrokeSize(ContentV.SIZE_3, 2);
		        }else if(v.getId() == R.id.size24){
		        	 sizeLinear2.setVisibility(View.GONE);
		        	 m_sketchPad.setStrokeSize(ContentV.SIZE_4, 2);
		        }else if(v.getId() == R.id.typeColor){   
		        	if(color_flag == 1){
		        		color_flag = 2;
		        		initTypeColorLinear(R.layout.padcolor);
		        		padBgColor.setBackgroundColor(lastPadColor);
		        		penColor.setBackgroundColor(lastPenColor);
		        	}else if(color_flag == 2){   
		        		color_flag = 1;
		        		initTypeColorLinear(R.layout.pencolor);
		        		padBgColor.setBackgroundColor(lastPadColor);
		        		penColor.setBackgroundColor(lastPenColor);
		        	}
		        }else if(v.getId() == R.id.color1){
		        	int color = Color.rgb(0, 0, 0);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        }else if(v.getId() == R.id.color2){
		        	int color = Color.rgb(255,255,255);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        	
		        }else if(v.getId() == R.id.color3){
		        	int color = Color.rgb(205,205,205);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        	
		        }else if(v.getId() == R.id.color4){
		        	int color = Color.rgb(138,138,138);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        	
		        }else if(v.getId() == R.id.color5){
		        	int color = Color.rgb(11,142,14);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        	
		        }else if(v.getId() == R.id.color6){
		        	int color = Color.rgb(45,202,14);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        	
		        }else if(v.getId() == R.id.color7){
		        	int color = Color.rgb(0,246,255);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        	
		        }else if(v.getId() == R.id.color8){
		        	int color = Color.rgb(255,0,0);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(Color.rgb(255,0,0));
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        	
		        }else if(v.getId() == R.id.color9){
		        	int color = Color.rgb(162,0,255);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        }else if(v.getId() == R.id.color10){
		        	int color = Color.rgb(255,0,252);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        }else if(v.getId() == R.id.color11){
		        	int color = Color.rgb(255,144,0);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        }else if(v.getId() == R.id.color12){
		        	int color = Color.rgb(255,228,0);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        }else if(v.getId() == R.id.color13){
		        	int color = Color.rgb(0,54,255);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        	
		        }else if(v.getId() == R.id.color14){
		        	int color = Color.rgb(0,144,255);
		        	if(color_flag == 1){
		        		lastPenColor = color;
		        		m_sketchPad.setStrokeColor(color);
		        		penColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
		        	}else if(color_flag == 2){
		        		lastPadColor = color;
		        		m_sketchPad.setBkColor(color);
		        		padBgColor.setBackgroundColor(color);
		        		 MyApplication.getInstance().getSendToService().setBgColor(color);
		        	}
		        }else if(v.getId() == R.id.color16){
		        	dialog = new ColorPickerDialog(this, Color.BLUE, new ColorPickerDialog.OnColorChangedListener() {
						public void colorChanged(int color) {
							if(color_flag == 1){
								lastPenColor = color;
				        		m_sketchPad.setStrokeColor(color);
				        		penColor.setBackgroundColor(color);
				        		 MyApplication.getInstance().getSendToService().setDrawStyle(lastPenSize,color);
				        	}else if(color_flag == 2){
				        		lastPadColor = color;
				        		m_sketchPad.setBkColor(color);
				        		padBgColor.setBackgroundColor(color);
				        		 MyApplication.getInstance().getSendToService().setBgColor(color);
				        	}
						}
					});
		        	dialog.show();
		        }	
		        }
		 private int imageTouchFlag = 0;//用来标记图片的状态  0代表还在拖动当中 没有确定位置   2代表已经点击确认 确定了位置
		 private OnTouchListener movingEventListener = new OnTouchListener() {
			    int lastX, lastY;  
			    public boolean onTouch(View v, MotionEvent event) {
			    	
			    	 if(imageTouchFlag == 0){
			        switch (event.getAction()) {   
			        case MotionEvent.ACTION_DOWN:   
			            lastX = (int) event.getRawX();   
			            lastY = (int) event.getRawY(); 
			            
			            break;   
			        case MotionEvent.ACTION_MOVE:   
			            int dx = (int) event.getRawX() - lastX;   
			            int dy = (int) event.getRawY() - lastY;   
			            int left = v.getLeft() + dx;   
			            int top = v.getTop() + dy;   
			            int right = v.getRight() + dx;   
			            int bottom = v.getBottom() + dy;   
			            // 设置不能出界   
			            if (left < 0) {   
			                left = 0;   
			                right = left + v.getWidth();   
			            }   
			            if (right > screenWidth) {   
			                right = screenWidth;   
			                left = right - v.getWidth();   
			            }   
			            if (top < 0) {   
			                top = 0;   
			                bottom = top + v.getHeight();   
			            }   
			            if (bottom > screenHeight) {   
			                bottom = screenHeight;   
			                top = bottom - v.getHeight();   
			            }   
			            v.layout(left, top, right, bottom);   
			            v.postInvalidate();
			            v.refreshDrawableState();
			            lastX = (int) event.getRawX();   
			            lastY = (int) event.getRawY();  
//			            for(int i=0;i<300;i+=10){
			            	MyApplication.getInstance().setBitmapLeft(left-leftLinear.getWidth());
				            MyApplication.getInstance().setBitmapTop(top-topLinear.getHeight());
				             MyApplication.getInstance().getSendToService().sUpdatePic();
//			            }
			            
			            break;   
			        case MotionEvent.ACTION_UP:   
			        	picBtn.setVisibility(View.VISIBLE);
			        	zoomBtn.setVisibility(View.VISIBLE);
			        	zoomReduceBtn.setVisibility(View.VISIBLE);
			        	cancleImage.setVisibility(View.VISIBLE);
			            break;   
			        }   
			    	 }
			        return true; 
			    
			    }   
			};   
			private Button btnArray[]={};//用来装按钮
		    private int btnOnIdArray[]={R.drawable.tool1_on,R.drawable.tool2_on,
		    		R.drawable.tool3_on,R.drawable.tool4_on,R.drawable.tool5_on,R.drawable.tool13_on,
		    		R.drawable.tool6_on,R.drawable.tool12_on,R.drawable.tool7_on,R.drawable.tool8_on,
					R.drawable.tool9_on,R.drawable.tool10_on,R.drawable.tool11_on,R.drawable.tool14_on
		    		};//用来装btnArray数组对应的按钮选中时的背景颜色
		    private int btnIdArray[]={R.drawable.tool1,R.drawable.tool2,
		    		R.drawable.tool3,R.drawable.tool4,R.drawable.tool5,R.drawable.tool13,
		    		R.drawable.tool6,R.drawable.tool12,R.drawable.tool7,R.drawable.tool8,
					R.drawable.tool9,R.drawable.tool10,R.drawable.tool11,R.drawable.tool14};//用来装btnArray数组对应的按钮选中时的背景颜色
		    private void setBtnBck(int a){
		    	for(int i = 0;i<btnArray.length;i++){
		    		if(a == i){
		    			btnArray[i].setBackgroundResource(btnOnIdArray[i]);
		    			if(i == 0){
		    				MyApplication.getInstance().setPaintsize(ContentV.SIZE_1);
		    		    	lastPenSize = ContentV.SIZE_1;
		    		    	 m_sketchPad.setStrokeSize(ContentV.SIZE_1, 1);
		    		    	 MyApplication.getInstance().getSendToService().setDrawStyle(ContentV.SIZE_1,lastPenColor);
		    				  sizeLinear2.setVisibility(View.GONE);
			  			      softInfoLinear.setVisibility(View.GONE);
			  			      shapeLinear.setVisibility(View.GONE);
			  			      dialog_softinfo_flag = 1;
			  			    m_sketchPad.setDrawStrokeEnable(true);
		    			}else if(i == 1){
					        sizeLinear.setVisibility(View.GONE);
					        softInfoLinear.setVisibility(View.GONE);
					        sizeLinear2.setVisibility(View.GONE);
					        shapeLinear.setVisibility(View.GONE);
					        m_sketchPad.setDrawStrokeEnable(false);
					        dialog_softinfo_flag = 1;
		    			}else if(i == 2){
		    				sizeLinear.setVisibility(View.GONE);
					        softInfoLinear.setVisibility(View.GONE);
					        sizeLinear2.setVisibility(View.GONE);
					        dialog_softinfo_flag = 1;
					        m_sketchPad.setDrawStrokeEnable(true);
		    			}else if(i == 3){
		    				sizeLinear.setVisibility(View.GONE);
					        softInfoLinear.setVisibility(View.GONE);
					        shapeLinear.setVisibility(View.GONE);
					        dialog_softinfo_flag = 1;
					        m_sketchPad.setDrawStrokeEnable(true);
		    			}else if(i == 5){
						  	sizeLinear2.setVisibility(View.GONE);
					        sizeLinear.setVisibility(View.GONE);
					        softInfoLinear.setVisibility(View.GONE);
					        shapeLinear.setVisibility(View.GONE);
					        dialog_softinfo_flag = 1;
					        m_sketchPad.setDrawStrokeEnable(true);
		    			}/*else{
		    				  	sizeLinear.setVisibility(View.GONE);
		    			        sizeLinear2.setVisibility(View.GONE);
		    			        softInfoLinear.setVisibility(View.GONE);
		    			        shapeLinear.setVisibility(View.GONE);
		    			        dialog_softinfo_flag = 1;
		    			}*/
		    		}else{
		    			btnArray[i].setBackgroundResource(btnIdArray[i]);
		    		}
		    		
		    	}
		    }
		    /**
		     * 保存图片的方法
		     * @author 刘星星
		     */
		    public void showRootDialog() {// 保存图片弹出框
				LayoutInflater layout = (LayoutInflater) this
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				View v = layout.inflate(R.layout.alert_dialog, null);
				RadioButton rb1 = (RadioButton) v.findViewById(R.id.rb1);
				rb1.setText("选择已有目录");
				RadioButton rb2 = (RadioButton) v.findViewById(R.id.rb2);
				rb2.setText("创建新目录");
				RadioButton rb3 = (RadioButton) v.findViewById(R.id.rb3);
				final AlertDialog	alertDialog = new AlertDialog.Builder(this).setView(v)
						.setCancelable(false).create();
				alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
				alertDialog.setTitle("选择：");
				alertDialog.setOnKeyListener(new OnKeyListener() {

					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							alertDialog.dismiss();
						}
						return false;
					}
				});
				alertDialog.show();
				rb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {// 选择已有目录
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						saveOrlookFlag = 0;
						alertDialog.dismiss();
						showListRootDialog("选择保存目录：");
					}
				});
				rb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {// 创建新目录
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						alertDialog.dismiss();
						showNewRootDialog();
					}

				});
				rb3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						alertDialog.dismiss();
					}
				});
			}
		    /**
		     * 创建新的目录进行保存
		     * @author 刘星星
		     */
		    public void showNewRootDialog() {// 保存图片弹出框
				LayoutInflater layout = (LayoutInflater) this
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				View v = layout.inflate(R.layout.new_root, null);
				final EditText et = (EditText) v.findViewById(R.id.etRoot);
				final Button btn = (Button) v.findViewById(R.id.btnRoot);
				final AlertDialog	alertDialog = new AlertDialog.Builder(this).setView(v)
						.setCancelable(false).create();
				alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
				alertDialog.setTitle("输入目录名称：");
				alertDialog.setOnKeyListener(new OnKeyListener() {
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							alertDialog.dismiss();
						}
						return false;
					}
				});
				alertDialog.show();
				btn.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						if(!et.getText().toString().equals("")){
							CompassImage.createFilePath(et.getText().toString());
							if(save_flag == 0){
								onSaveClick(et.getText().toString());
								if(cancle_flag == true){
									sClear(true);
								}
							}else if(save_flag == 1){
								onSaveClick(et.getText().toString());
								onClearClick();
								setBtnBck(10);
							}
						}else{
							Toast.makeText(getApplicationContext(), "目录名字不能为空，创建失败。。", 5000).show();
						}
						alertDialog.dismiss();
					}
				});
			}
		    /**
		     * 向服务器发送命令清空画板
		     * @param type 退出画板参数
		     */
		    private void sClear(boolean type){
		    	int[] data ={1029,1,14,0,241};
		         MyApplication.getInstance().getSendToService().sDraw(data);
		        if(type){
		        	if(MyApplication.getInstance().getParcelable()!=null){
			        	Intent i = new Intent(this,VncCanvasActivity.class);
			        	i.putExtra(VncConstants.CONNECTION,MyApplication.getInstance().getParcelable());
			        	startActivity(i);
					    finish();
		        	}else{
		        		Intent i = new Intent(this,AndroidVNCEntry.class);
			        	startActivity(i);
					    finish();
		        	}
		        }
		        
		    }
		    /**
		     * 选择已有目录进行文件保存
		     * @param msg 弹出款的提示消息。。。
		     * @author qingfeng.刘星星
		     */
		    public void showListRootDialog(String msg) {// 保存图片弹出框
				LayoutInflater layout = (LayoutInflater) this
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				View v = layout.inflate(R.layout.selectcreated, null);
				final ListView listView = (ListView) v.findViewById(R.id.listView);
				final ArrayList<HashMap<String,String>> rootList = new ArrayList<HashMap<String,String>>();
				String root = Environment.getExternalStorageDirectory() + "/DCIM/Takeasy";
				File rootfile = new File(root);
				if(!rootfile.exists()){
					rootfile.mkdirs();
				}
				if(rootfile.listFiles().length == 0){
					Toast.makeText(getApplicationContext(), "暂无目录，请先创建。。", 5000).show();
				}else{
					File[] file = rootfile.listFiles();
					for (int i = 0; i < file.length; i++) {
						if(file[i].isDirectory()){
							HashMap<String,String> map = new HashMap<String, String>();
							map.put("path", file[i].getAbsolutePath());
							rootList.add(map);
						}
					}
					final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(v)
							.setCancelable(false).create();
					alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
					alertDialog.setTitle(msg);
					alertDialog.setOnKeyListener(new OnKeyListener() {
						public boolean onKey(DialogInterface dialog, int keyCode,
								KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								alertDialog.dismiss();
								
							}
							return false;
						}
					});
					alertDialog.show();
					SimpleAdapter adapter = new SimpleAdapter(this, rootList, R.layout.item_root,
							new String[]{"path"}, new int[]{R.id.tv});
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							alertDialog.dismiss();
							paths = rootList.get(arg2).get("path");
							if(saveOrlookFlag == 0){
								File file = new File(paths);
								onSaveClick(file.getName());
								if(cancle_flag == true){
									sClear(true);
								}
								if(addpad_flag){
									onClearClick();
								}
								cancle_flag = false;
								addpad_flag = false;
							}else if(saveOrlookFlag == 1){
								Intent i = new Intent(MainActivity.this,OpenPadActivity.class);
								i.putExtra("paths", paths);
					        	startActivityForResult(i, 100);
					        	//setResult(100, i);
							}else if(saveOrlookFlag == 2){
								File file = new File(paths);
								onSaveClick(file.getName());
								sClear(true);
							}
						}
					});
				}
			}
			private void findView(){
		    	 findShapeView();//初始化多边形控件
		    	 topLinear = (LinearLayout) findViewById(R.id.topLinear);
		         leftLinear = (LinearLayout) findViewById(R.id.leftLinear);
		    	 m_sketchPad = (SketchPadView)this.findViewById(R.id.sketchpad);
		         m_penBtn = (Button)this.findViewById(R.id.pen);
		         m_eraserBtn = (Button)this.findViewById(R.id.eraser);
		         m_clearBtn = (Button)this.findViewById(R.id.clear);
		         m_undoBtn = (Button)this.findViewById(R.id.undo);
		         m_redoBtn = (Button)this.findViewById(R.id.redo);
		         m_saveBtn = (Button)this.findViewById(R.id.save);
		         m_loadBtn = (Button)this.findViewById(R.id.load);
		         brush = (Button) findViewById(R.id.brush);
		         m_pensizeBtn = (Button)this.findViewById(R.id.penSize);
		         m_chooseBtn = (Button)this.findViewById(R.id.choose);
		         m_takePickture = (Button)this.findViewById(R.id.takePicture);
		         keyboardWrite = (Button)this.findViewById(R.id.keyboardwrite);
		         chooseShape = (Button)this.findViewById(R.id.chooseShape);
		         softInfo = (Button) findViewById(R.id.softInfo);
		         closeInfoBtn = (Button) findViewById(R.id.closeDialog);
		         sizeLinear = (LinearLayout) findViewById(R.id.sizeDiv);
		         softInfoLinear = (LinearLayout) findViewById(R.id.dialogSoftInfo);
		         shapeLinear = (LinearLayout) findViewById(R.id.dialogShape);
		         size1Layout = (LinearLayout) findViewById(R.id.size1);
		         size2Layout = (LinearLayout) findViewById(R.id.size2);
		         size3Layout = (LinearLayout) findViewById(R.id.size3);
		         size4Layout = (LinearLayout) findViewById(R.id.size4);
		         sizeLinear2 = (LinearLayout) findViewById(R.id.sizeDiv2);
		         size21Layout = (LinearLayout) findViewById(R.id.size21);
		         size22Layout = (LinearLayout) findViewById(R.id.size22);
		         size23Layout = (LinearLayout) findViewById(R.id.size23);
		         size24Layout = (LinearLayout) findViewById(R.id.size24);
		         typeColor = (LinearLayout) findViewById(R.id.typeColor);
		         color1 = (Button) findViewById(R.id.color1);
		         color2 = (Button) findViewById(R.id.color2);
		         color3 = (Button) findViewById(R.id.color3);
		         color4 = (Button) findViewById(R.id.color4);
		         color5 = (Button) findViewById(R.id.color5);
		         color6 = (Button) findViewById(R.id.color6);
		         color7 = (Button) findViewById(R.id.color7);
		         color8 = (Button) findViewById(R.id.color8);
		         color9 = (Button) findViewById(R.id.color9);
		         color10 = (Button) findViewById(R.id.color10);
		         color11 = (Button) findViewById(R.id.color11);
		         color12 = (Button) findViewById(R.id.color12);
		         color13 = (Button) findViewById(R.id.color13);
		         color14 = (Button) findViewById(R.id.color14);
		         colorSelect = (Button) findViewById(R.id.color16);
		         picBtn = (Button) findViewById(R.id.imageBtn);
		         imageView = (ImageView) findViewById(R.id.image);
		         imageView.setVisibility(View.GONE);
		         zoomBtn = (Button) findViewById(R.id.zoomBtn);
		         zoomReduceBtn =  (Button) findViewById(R.id.reducezoomBtn);
		         cancleBtn = (Button) findViewById(R.id.cancel);
		         cancleImage = (Button) findViewById(R.id.cancleImage);
		         bestLinear = (RelativeLayout) findViewById(R.id.bestLayout);
		         addCanvasBtn = (Button) findViewById(R.id.addnewPaint);
		         openPad = (Button) findViewById(R.id.open);
		    }
		    private void initView(){
		    	 btnArray=new Button[]{m_penBtn,keyboardWrite,chooseShape,m_eraserBtn,m_takePickture,brush,
		    			 m_undoBtn,m_clearBtn,m_redoBtn,m_saveBtn,addCanvasBtn,softInfo,cancleBtn,openPad};
		         m_penBtn.setOnClickListener(this);
		         m_eraserBtn.setOnClickListener(this);
		         m_penBtn.setOnLongClickListener(this);
		         m_eraserBtn.setOnLongClickListener(this);
		         chooseShape.setOnLongClickListener(this);
		         m_clearBtn.setOnClickListener(this);
		         addCanvasBtn.setOnClickListener(this);
		         m_undoBtn.setOnClickListener(this);
		         m_redoBtn.setOnClickListener(this);
		         m_saveBtn.setOnClickListener(this);
		         m_loadBtn.setOnClickListener(this);
		         m_pensizeBtn.setOnClickListener(this);
		         m_chooseBtn.setOnClickListener(this);
		         m_sketchPad.setCallback(this);
		         softInfo.setOnClickListener(this);
		         m_takePickture.setOnClickListener(this);
		        // m_penBtn.setEnabled(false);
		         m_redoBtn.setEnabled(false);
		         m_undoBtn.setEnabled(false);
		         closeInfoBtn.setOnClickListener(this);
		         //m_saveBtn.setEnabled(false);
		         size1Layout.setOnClickListener(this);
		         size2Layout.setOnClickListener(this);
		         size3Layout.setOnClickListener(this);
		         size4Layout.setOnClickListener(this);
		         size21Layout.setOnClickListener(this);
		         size22Layout.setOnClickListener(this);
		         size23Layout.setOnClickListener(this);
		         size24Layout.setOnClickListener(this);
		         color1.setOnClickListener(this);
		         color2.setOnClickListener(this);
		         color3.setOnClickListener(this);
		         color4.setOnClickListener(this);
		         color5.setOnClickListener(this);
		         color6.setOnClickListener(this);
		         color7.setOnClickListener(this);
		         color8.setOnClickListener(this);
		         color9.setOnClickListener(this);
		         color10.setOnClickListener(this);
		         color11.setOnClickListener(this);
		         color12.setOnClickListener(this);
		         color13.setOnClickListener(this);
		         color14.setOnClickListener(this);
		         typeColor.setOnClickListener(this);
		         keyboardWrite.setOnClickListener(this);
		         chooseShape.setOnClickListener(this);
		         colorSelect.setOnClickListener(this);
		         picBtn.setOnClickListener(this);
		         zoomBtn.setOnClickListener(this);
		         zoomReduceBtn.setOnClickListener(this);
		         cancleBtn.setOnClickListener(this);
		         cancleImage.setOnClickListener(this);
		         initTypeColorLinear(R.layout.pencolor);//给颜色切换区域初始化控件
		         brush.setOnClickListener(this);
		         openPad.setOnClickListener(this);
		         
		         
		    }
		    
		    /**
		     * 初始化颜色却换区域的控件
		     * @param v
		     * @author qingfeng.刘星星
		     */
		    private void initTypeColorLinear(int id){
		    	typeColor.removeAllViews();
		    	LayoutInflater layout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		    	RelativeLayout r = (RelativeLayout) layout.inflate(id, null);
		    	penColor = (Button) r.findViewById(R.id.PenColor);
		    	padBgColor = (Button) r.findViewById(R.id.PadBgColor);
		    	typeColor.addView(r);
		    }
		    /**
		     * 清空画板函数
		     * @param v
		     * @author qingfeng.刘星星
		     */
		    private void clearBtnDialog(){
		    			setBtnBck(7);
						AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
						ab.setTitle("提示：");
						ab.setMessage("当前画板尚未保存，确定清空?");
						ab.setIcon(android.R.drawable.ic_dialog_alert);
						ab.setPositiveButton("是", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								 onClearClick();
					        	 
							}
						}).setNegativeButton("否", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								return;
							}
						});
						ab.show();
		    }
		    /**
		     * 退出程序执行函数
		     * @param v
		     * @author qingfeng.刘星星
		     */
		    private void cancleBtnDialog(){
	        	AlertDialog.Builder ab = new AlertDialog.Builder(this);
				ab.setTitle("提示：");
				ab.setMessage("退出画板并切换到远程桌面?");
				ab.setIcon(android.R.drawable.ic_dialog_alert);
				ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					if(paint_flag){
						AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
						ab.setTitle("提示：");
						ab.setMessage("是否保存当前画?");
						ab.setIcon(android.R.drawable.ic_dialog_alert);
						ab.setPositiveButton("是", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								showRootDialog();
								cancle_flag = true;
								save_flag = 0;
							}
						}).setNegativeButton("否", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								sClear(true);
							}
						});
						ab.show();
					}else{
						sClear(true);
			        }
//					 MyApplication.getInstance().getSendToService().closeSocket();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});
				ab.show();
		    }
		    @Override
		    public boolean onCreateOptionsMenu(Menu menu) {
		    	getMenuInflater().inflate(R.menu.draw_main_menu, menu);
		    	return super.onCreateOptionsMenu(menu);
		    }
		    Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					sendToHideDialog();
				}
			};
			@Override
			public boolean onOptionsItemSelected(MenuItem item) {
				if(item.getItemId() == R.id.control){//切换到远程桌面
//					 MyApplication.getInstance().getSendToService().closeSocket();
					/*Intent i = new Intent(this,VncCanvasActivity.class);
		        	i.putExtra(VncConstants.CONNECTION,MyApplication.getInstance().getParcelable());
		        	startActivity(i);
				    finish();*/
					Thread thread = new Thread(runnable);
					thread.start();
				    Intent intent2=new Intent();
		            intent2.setClass(this, VncCanvasActivity.class);
		            intent2.putExtra(VncConstants.CONNECTION,MyApplication.getInstance().getParcelable());
		            intent2.putExtra("activity_flag", 1);
		            int version = Integer.valueOf(android.os.Build.VERSION.SDK);
		            if(version  >= 5) {     
		               overridePendingTransition(R.anim.stepin, R.anim.stepout); 
		              //overridePendingTransition(android.R.anim.decelerate_interpolator,android.R.anim.decelerate_interpolator);    
		                 //overridePendingTransition(android.R.anim.overshoot_interpolator,android.R.anim.linear_interpolator);  
		            }    
//		            startActivity(intent2);
		            startActivityForResult(intent2, 22);
//		            finish();
				}else if(item.getItemId() == R.id.chuangkou){//服务器窗口自适应
					 MyApplication.getInstance().getSendToService().closeSocket();
					 sendToChangeServiceDialog();//与服务器同步窗口大小
					sendToChangeServiceDialog();
				}else if(item.getItemId() == R.id.menu_exit){//退出应用程序
//					 MyApplication.getInstance().getSendToService().closeSocket();
					ExitApplication.getInstance().showExitDialog(MainActivity.this);
				}/*else if(item.getItemId() == R.id.jopToList){
//					 MyApplication.getInstance().getSendToService().closeSocket();
					sClear(false);
					 MyApplication.getInstance().getSendToService().closeSocket();
					Intent i = new Intent(this,AndroidVNCEntry.class);
		        	startActivity(i);
				    finish();
				}*/
				return super.onOptionsItemSelected(item);
			}
			
		/*	 *//**
			 * 退出系统时的提示函数 @author 刘星星
			 *//*
			public void showExitDialog(){
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
				  			.setTitle("提示")
							.setMessage("您确定要退出系统吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int which) {
											int[] data ={1029,1,14,0,241};
											MyApplication.getInstance().getSendToService().sDraw(data);
											MyApplication.getInstance().getSendToService().closeSocket();
											((Activity) context).finish();
											onTerminate();
										}
									}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int which) {
											return ;
										}
									});
				  AlertDialog alert =((Builder) callDailog).create();
				  alert.show();
			}*/
			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
//					cancleBtnDialog();
//					sClear(true);
					return true;
				}
				return super.onKeyDown(keyCode, event);
			}
			@Override
			public boolean onLongClick(View v) {
				if(v == m_penBtn){
					  sizeLinear.setVisibility(View.VISIBLE);
	  			      sizeLinear2.setVisibility(View.GONE);
	  			      softInfoLinear.setVisibility(View.GONE);
	  			      shapeLinear.setVisibility(View.GONE);
	  			      dialog_softinfo_flag = 1;
				}else if(v == m_eraserBtn){
					sizeLinear2.setVisibility(View.VISIBLE);
			        sizeLinear.setVisibility(View.GONE);
			        softInfoLinear.setVisibility(View.GONE);
			        shapeLinear.setVisibility(View.GONE);
			        dialog_softinfo_flag = 1;
				}else if(v == chooseShape){
					shapeLinear.setVisibility(View.VISIBLE);
			        sizeLinear.setVisibility(View.GONE);
			        softInfoLinear.setVisibility(View.GONE);
			        sizeLinear2.setVisibility(View.GONE);
			        dialog_softinfo_flag = 1;
				}
				return false;
			}
    
}