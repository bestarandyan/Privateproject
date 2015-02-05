package com.boluomi.children.view;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.boluomi.children.R;

public class DialogView extends Dialog{

	public DialogView(Context context) {
		super(context);
		LayoutInflater l = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		LinearLayout dialog = (LinearLayout) l.inflate(R.layout.dialog_share, null);
		
	}
	

}
