package com.qingfengweb.piaoguanjia.orderSystem;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class MainTab01Fragment extends Fragment {

	@ViewInject(R.id.relbtn1)
	private RelativeLayout relbtn1;
	@ViewInject(R.id.relbtn2)
	private RelativeLayout relbtn2;
	@ViewInject(R.id.relbtn3)
	private RelativeLayout relbtn3;
	@ViewInject(R.id.relbtn4)
	private RelativeLayout relbtn4;
	@ViewInject(R.id.relbtn5)
	private RelativeLayout relbtn5;
	@ViewInject(R.id.relbtn6)
	private RelativeLayout relbtn6;
	@ViewInject(R.id.relbtn7)
	private RelativeLayout relbtn7;
	@ViewInject(R.id.relbtn8)
	private RelativeLayout relbtn8;

	private boolean touchFlg = false;// false 都未点击，true 正在点击

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_home, container, false);
		ViewUtils.inject(this, view);
		return view;
	}

	@OnTouch({ R.id.relbtn1, R.id.relbtn2, R.id.relbtn3, R.id.relbtn4,
			R.id.relbtn5, R.id.relbtn6, R.id.relbtn7, R.id.relbtn8 })
	public void homeiconTouch(final View v, MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN && !touchFlg) {
			touchFlg = true;
			Animation clickAnimation = AnimationUtils.loadAnimation(
					getActivity(), R.anim.anim_button_in);
			clickAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					sendIntent(v);
				}
			});
			v.startAnimation(clickAnimation);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
		}
	}

	private void sendIntent(View v) {
		if (v.getId() == R.id.relbtn1 || v.getId() == R.id.relbtn2
				|| v.getId() == R.id.relbtn3 || v.getId() == R.id.relbtn5
				|| v.getId() == R.id.relbtn6 || v.getId() == R.id.relbtn7
				|| v.getId() == R.id.relbtn8) {
			try {
				int tag = Integer.parseInt(v.getTag().toString());
				Intent i = new Intent(getActivity(), NewProductActivity.class);
				i.putExtra("type", tag);
				startActivity(i);
			} catch (Exception e) {
			}
		} else if (v.getId() == R.id.relbtn4) {
			
			Intent i = new Intent(getActivity(), OwerActivity.class);
			startActivity(i);
		}
		touchFlg = false;
	}

}
