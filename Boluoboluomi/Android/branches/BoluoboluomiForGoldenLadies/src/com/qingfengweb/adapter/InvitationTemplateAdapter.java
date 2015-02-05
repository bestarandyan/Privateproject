package com.qingfengweb.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.id.blm_goldenLadies.R;

public class InvitationTemplateAdapter extends BaseAdapter{
	public Context context;
	public List<Map<String,Object>> list;
	public InvitationTemplateAdapter(Context context,List<Map<String,Object>> list) {
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_invitationtemplate, null);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.textView = (TextView) convertView.findViewById(R.id.title);
//			int width = MyApplication.getInstance(context).getWidthPixels()/4-10;
//			int height = width;
//			LinearLayout.LayoutParams param= new LinearLayout.LayoutParams(width, height);
//			holder.image.setLayoutParams(param);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object> map = list.get(position);
		String name = map.get("name").toString();
		String thumb = map.get("thumb").toString();
		holder.textView.setText(name);
		if(thumb!=null && thumb.length()>0){
			String[] fileStrings = thumb.trim().split("/");
			String nameString = fileStrings[fileStrings.length-1];
			File file = new File(ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.INTEGRAL_IMG_URL_THUMB+nameString);
			if(file.exists()){
				Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
				holder.image.setImageBitmap(bitmap);
			}else{
				RequestServerFromHttp.downImageForUrl(context, holder.image, R.drawable.photolist_defimg, thumb, ConstantsValues.INVITATION_IMG_URL_THUMB, false);
			}
		}
		return convertView;
	}
	class ViewHolder{
		ImageView image;
		TextView textView;
	}
}
