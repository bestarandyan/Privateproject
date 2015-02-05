/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhihuigu.sosoOffice.AccurateSearchActivity;
import com.zhihuigu.sosoOffice.CollectManagerActivity;
import com.zhihuigu.sosoOffice.DemandManagerActivity;
import com.zhihuigu.sosoOffice.LinkmanActivity;
import com.zhihuigu.sosoOffice.MainFirstTab;
import com.zhihuigu.sosoOffice.MainTabActivity;
import com.zhihuigu.sosoOffice.PotentialDemandActivity;
import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.RecommendManagerForAgency;
import com.zhihuigu.sosoOffice.RecommendManagerForOwner;
import com.zhihuigu.sosoOffice.RoomListActivity;
import com.zhihuigu.sosoOffice.StationInLetterActivity;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.MyApplication;

/**
 * @author ������
 * @createDate 2012/1/7
 * �����湦�ܿؼ���������
 *
 */
public class MainGridViewAdapter extends BaseAdapter {

	private Activity mContext;
	private List<HashMap<String, Object>> bitmaplist;
	LayoutInflater layoutInflater;
	public MainGridViewAdapter(Activity context,List<HashMap<String, Object>> bitmaplist) {
		this.mContext = context;
		this.bitmaplist = bitmaplist;
		layoutInflater = LayoutInflater.from(context);
	}
	public int getCount() {
		return bitmaplist.size();
	}
	public Object getItem(int position) {

		return bitmaplist.get(position);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// ����һ��ImageView,��ʾ��GridView��
		ViewHolder holder = null;
		if (convertView == null) {
//			imageView = new ImageView(mContext);
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.item_maingv, null);
			holder.image = (ImageView) convertView.findViewById(R.id.mainImage);
			holder.tv = (TextView) convertView.findViewById(R.id.mainText);
			holder.btn = (Button) convertView.findViewById(R.id.mainCircle);
			int width = Integer.parseInt(mContext.getResources().getString(R.string.main_gv_size));
			LinearLayout.LayoutParams param= new LinearLayout.LayoutParams(width, width);
			holder.image.setLayoutParams(param);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(MyApplication.getInstance(mContext).getRoleid() == Constant.TYPE_CLIENT){
			if(position == 1){
				holder.btn.setVisibility(View.GONE);
			}
		}
		holder.setView(bitmaplist.get(position));
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = null;
				switch(MyApplication.getInstance(mContext).getRoleid()){
				case Constant.TYPE_AGENCY://�н����ҳ���ܲ˵��¼�
					if(position!=1){
						if((MyApplication.getInstance().getSosouserinfo(mContext)==null
								||MyApplication.getInstance().getSosouserinfo(mContext).getUserID()==null)){
							break;
						}
					}
					if(position == 0){//���͹���
						/*intent = new Intent(mContext, RecommendManagerForAgency.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
						MyApplication.getInstance().setRecommendAgencyBackBtnVisibility(true);
						MyApplication.getInstance().setSearchBack(false);
						MainTabActivity.mTabHost.setCurrentTab(2);
					}else if(position == 1){//����д��¥
						intent = new Intent(mContext, AccurateSearchActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						MyApplication.getInstance().setSearchRoomBackVisibility(true);
						MyApplication.getInstance().setSearchBack(true);
					}else if(position == 2){//�ղع���
						/*intent = new Intent(mContext, CollectManagerActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
						MyApplication.getInstance().setCollectBackBtnVisibility(true);
						MainTabActivity.mTabHost.setCurrentTab(3);
					}else if(position == 3){//վ����
						/*intent = new Intent(mContext, StationInLetterActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
						MyApplication.getInstance().setStationInLetterBackBtnVisibility(true);
						MainTabActivity.mTabHost.setCurrentTab(1);
					}else if(position == 4){//��ϵ��
						intent = new Intent(mContext, LinkmanActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("haveMenu", true);
					}
					break;
				case Constant.TYPE_CLIENT://�ͻ���ҳ�Ĺ��ܲ˵��¼�����
					if(position!=0){
						if((MyApplication.getInstance().getSosouserinfo(mContext)==null
								||MyApplication.getInstance().getSosouserinfo(mContext).getUserID()==null)){
							break;
						}
					}
					if(position == 0){
						/*intent = new Intent(mContext, AccurateSearchActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
						MyApplication.getInstance().setSearchRoomBackVisibility(true);
						MainTabActivity.mTabHost.setCurrentTab(2);
					}else if(position == 1){
						intent = new Intent(mContext, DemandManagerActivity.class).
			              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					}else if(position == 2){
						/*intent = new Intent(mContext, CollectManagerActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
						MyApplication.getInstance().setCollectRoom(true);
						MyApplication.getInstance().setCollectBackBtnVisibility(true);
						MainTabActivity.mTabHost.setCurrentTab(3);//���õ�Ȼ��ǩҳ�������ղع���
					}else if(position == 3){
						/*intent = new Intent(mContext, StationInLetterActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
						MyApplication.getInstance().setStationInLetterBackBtnVisibility(true);
						MainTabActivity.mTabHost.setCurrentTab(1);
					}else if(position == 4){
						intent = new Intent(mContext, LinkmanActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("haveMenu", true);
					}
				break;
				case Constant.TYPE_PROPRIETOR://ҵ����ҳ�Ĺ��ܲ˵��¼�����
					if((MyApplication.getInstance().getSosouserinfo(mContext)==null
					||MyApplication.getInstance().getSosouserinfo(mContext).getUserID()==null)){
						break;
					}
					if(position == 0){
						intent = new Intent(mContext, RoomListActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("tag", 1);
					}else if(position == 1){
						/*intent = new Intent(mContext, RecommendManagerForOwner.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
						MyApplication.getInstance().setRecommendOwnerBackBtnVisibility(true);
						MyApplication.getInstance().setPotentialDemandBack(false);
						MainTabActivity.mTabHost.setCurrentTab(2);
					}else if(position == 2){
						/*intent = new Intent(mContext, StationInLetterActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
						MyApplication.getInstance().setStationInLetterBackBtnVisibility(true);
						MainTabActivity.mTabHost.setCurrentTab(1);//���õ�Ȼ��ǩҳ������վ����
					}else if(position == 3){
						intent = new Intent(mContext, LinkmanActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("activityFlag", 0);
						intent.putExtra("haveMenu", true);
					}else if(position == 4){
						/*intent = new Intent(mContext, PotentialDemandActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
						MyApplication.getInstance().setClientDemandBackBtnVisiblity(true);
						MyApplication.getInstance().setPotentialDemandBack(false);
						MainTabActivity.mTabHost.setCurrentTab(3);
					}else if(position == 5){
						intent = new Intent(mContext, PotentialDemandActivity.class).
					              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						MyApplication.getInstance().setPotentialDemandBack(true);
					}
				break;
				}
				if(intent!=null){
					//��һ��Activityת����һ��View
					Window w = MainFirstTab.group.getLocalActivityManager()
							.startActivity("activityid",intent);
				    View view = w.getDecorView();
				    //��View��Ӵ�ActivityGroup��
				    MainFirstTab.group.setContentView(view);
				}
			}
		});
		return convertView;
	}
	public long getItemId(int position) {
		return position;
	}
	class ViewHolder{
		ImageView image;
		TextView tv;
		Button btn;
		private void setView(HashMap<String, Object> map){
		/*	BitmapDrawable bitmapDrawable = (BitmapDrawable)image.getDrawable();   
	        //���ͼƬ��δ���գ���ǿ�ƻ��ո�ͼƬ    
	        if(bitmapDrawable !=null && !bitmapDrawable.getBitmap().isRecycled()){   
	            bitmapDrawable.getBitmap().recycle();   
	        }   */
			image.setClickable(false);
			image.setEnabled(false);
			btn.setClickable(false);
			btn.setEnabled(false);
			image.setImageResource((Integer) map.get("mainImage"));
			tv.setText(map.get("mainTv").toString());
			if((Integer)map.get("mainBtn") == 0){
				btn.setVisibility(View.GONE);
			}else if((Integer)map.get("mainBtn") > 0){
//				btn.setVisibility(View.GONE);
				btn.setText(map.get("mainBtn").toString());
			}
			
			
		}
	}
}
