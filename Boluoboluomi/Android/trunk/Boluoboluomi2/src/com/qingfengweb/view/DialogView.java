package com.qingfengweb.view;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.qingfengweb.id.biluomiV2.R;

public class DialogView extends Dialog{

	public DialogView(Context context) {
		super(context);
		LayoutInflater l = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		LinearLayout dialog = (LinearLayout) l.inflate(R.layout.dialog_share, null);
		
	}
	

}
