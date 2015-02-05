/**
 * 
 */
package com.qingfengweb.weddingideas.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * @author qingfeng
 *
 */
public class MyVideoView  extends VideoView  {
	public MyVideoView(Context context) {
		  super(context);
		}
		public MyVideoView(Context context, AttributeSet attrs) {
		  super(context, attrs);
		}

		public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
		  super(context, attrs, defStyle);
		}

		//重點在此，override這個 function 才可以正常滿版!9 t: M+ ]& J/ ^5 E1 A
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
		{
			int width = getDefaultSize(480, widthMeasureSpec);
			        int height = getDefaultSize(320, heightMeasureSpec);
			        /**//*if (mVideoWidth > 0 && mVideoHeight > 0) {
			            if ( mVideoWidth * height  > width * mVideoHeight ) {
			                //Log.i("@@@", "image too tall, correcting");
			                height = width * mVideoHeight / mVideoWidth;
			            } else if ( mVideoWidth * height  < width * mVideoHeight ) {
			                //Log.i("@@@", "image too wide, correcting");
		               width = height * mVideoWidth / mVideoHeight;
			            } else {
			                //Log.i("@@@", "aspect ratio is correct: " +
			                        //width+"/"+height+"="+
			                        //mVideoWidth+"/"+mVideoHeight);
			            }
			        }*/
			       //Log.i("@@@@@@@@@@", "setting size: " + width + 'x' + height);
			       setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
		}
}
