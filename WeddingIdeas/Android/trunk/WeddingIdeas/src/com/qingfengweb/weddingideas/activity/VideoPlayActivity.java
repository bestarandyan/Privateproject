/**
 * 
 */
package com.qingfengweb.weddingideas.activity;

import com.qingfengweb.weddingideas.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * @author 刘星星
 * @createDate 2013、1、2
 * 视频播放页面
 *
 */
@TargetApi(Build.VERSION_CODES.ECLAIR)
public class VideoPlayActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_playvideo);
		findViewById(R.id.fullBtn).setOnClickListener(this);
		videoPlay();
	}
	
	/**
	 * 播放视频
	 */
	public void videoPlay(){
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		final VideoView videoHolder = (VideoView) findViewById(R.id.videoView);
		//if you want the controls to appear
		videoHolder.setMediaController(new MediaController(this));
//		Uri video = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.video_weddingideas); //do not add any extension
//		//if your file is named sherif.mp4 and placed in /raw
//		//use R.raw.sherif
//		videoHolder.setVideoURI(video);
		 String mFileNameArg = "/mnt/sdcard/IWAgent/Video/1419332153727.mp4";
		 videoHolder.setVideoPath(mFileNameArg);
		videoHolder.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				videoHolder.start();
			}
		});
		videoHolder.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				videoHolder.clearAnimation();
				videoHolder.destroyDrawingCache();
				System.gc();
				finish();
				overridePendingTransition(R.anim.anim_scale_exit, R.anim.anim_scale_back);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.fullBtn){
			finish();
			overridePendingTransition(R.anim.anim_scale_exit, R.anim.anim_scale_back);
		}
		
	}
	
}
