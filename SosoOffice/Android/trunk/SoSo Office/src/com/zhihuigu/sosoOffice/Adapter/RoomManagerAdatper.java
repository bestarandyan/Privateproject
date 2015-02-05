/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.RoomManagerActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author ������
 * @createDate 2013/1/18
 * ��Դ�������������
 *
 */
public class RoomManagerAdatper  extends BaseAdapter implements OnClickListener{
	Context context;
	ArrayList<HashMap<String,Object>> list;
	ArrayList<HashMap<String,Object>> layerList = new ArrayList<HashMap<String,Object>>();
	ListView listView;
	GridView gv;
//	private int view_state = 1;//�����ж�¥��������ʾ����ʽ   1����Ϊ��ʾ���з�����Ϣ   2����ֻ��ʾ¥��
	public RoomManagerAdatper(Context context,
			ArrayList<HashMap<String,Object>> list,ListView listView,GridView gv) {
		this.context = context;
		this.list = list ;
		this.listView = listView;
		this.gv = gv;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_roommanager, null);
				holder.layerNumber = (TextView) convertView.findViewById(R.id.layerNumber);
				holder.gv = (GridView) convertView.findViewById(R.id.roomGridview);
				holder.layerLayout = (LinearLayout) convertView.findViewById(R.id.layerLayout);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
			int itemSize = ((ArrayList<HashMap<String, Object>>)list.get(position).get("list")).size();//��ȡ���������
			int height = Integer.parseInt(context.getString(R.string.room_list_gv_height));//ÿһ������ռ�õĸ߶�
			int number = Integer.parseInt(context.getString(R.string.room_number));//ÿһ����ʾ�ķ�����
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT,(itemSize%number)>0?((itemSize/number)*height+height):(itemSize/number)*height);
			holder.gv.setLayoutParams(param);
			holder.gv.setNumColumns(number);
			holder.setViewContent(position);
		
		return convertView;
	}
	class ViewHolder{
		LinearLayout layerLayout;//��ʾ¥��Ĳ���
		TextView layerNumber;//��ʾ¥������ֵĿؼ�
		GridView gv;//��ʾ����Ŀؼ�
		public void setViewContent(int position){
				HashMap<String,Object> map = list.get(position);
				layerNumber.setText(map.get("layer").toString());
				layerLayout.setOnClickListener(RoomManagerAdatper.this);
				ArrayList<HashMap<String,Object>> list1 = (ArrayList<HashMap<String, Object>>) map.get("list");
				gv.setAdapter(new RoomManagerGridViewAdapter(context, list1));
				gv.setCacheColorHint(0);
				gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
			
		}
	}
	/**
	 * ��ȡ¥������
	 * @author ������
	 * @createDate 2013/1/24
	 */
	private void getLayerListData(){
		for(int i=0;i<list.size();i++){
			HashMap<String,Object> map = new HashMap<String, Object>();
			map.put("layer", list.get(i).get("layer").toString());
			layerList.add(map);
		}
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.layerLayout){
//			layerList.clear();
//			getLayerListData();
//			listView.setVisibility(View.GONE);
////			gv.setAdapter( new RoomLayerAdapter(context, layerList));
//			gv.setCacheColorHint(0);
//			gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
//			RoomManagerActivity.list_type = 2;
		}
	}
	
}
