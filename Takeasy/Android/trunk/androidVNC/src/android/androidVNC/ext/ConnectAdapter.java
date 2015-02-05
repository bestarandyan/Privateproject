package android.androidVNC.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vnc.draw.activity.MainActivity;

import android.androidVNC.AndroidVNCEntry;
import android.androidVNC.ConnectionBean;
import android.androidVNC.InterActivity;
import android.androidVNC.MostRecentBean;
import android.androidVNC.MyApplication;
import android.androidVNC.R;
import android.androidVNC.VncCanvasActivity;
import android.androidVNC.VncConstants;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ConnectAdapter extends BaseAdapter {
	private AndroidVNCEntry context; // �н���������
	private List<Map<String, Object>> listItems; // listview�е�������
	private ArrayList<ConnectionBean>  mObj=null;
	private LayoutInflater listContainer;
	public ImageView image; // ͼ��
	public TextView text1; // ��ַ�ı�1
	public TextView text2; // ��ַ�İ�2
	public Button btn1; // �޸İ�ť
	public Button btn2; // ���Ӱ�ť
	public Button btn3; // ɾ����ť
	public Button btn4;//��ͼ��ť
	public String password;
	public int port;
	public String nickname;
	public String address;
	public final int UPDATE = 1;
	private int mPonsitonBtn=0;
	private ConnectionBean selected;
	/**
	 * ���캯��ConnectAdapter
	 */

	public ConnectAdapter() {

	}

	public ConnectAdapter(AndroidVNCEntry context, List<Map<String, Object>> listItems,ArrayList<ConnectionBean> connections , ArrayList<ConnectionBean> connected ) {
		mObj=connected;
		this.context = context;
		listContainer = LayoutInflater.from(context);
		this.listItems = listItems;

	}

	public int getCount() {

		return listItems.size();
	}

	public Object getItem(int position) {
		
		return listItems.get(position);

	}

	public long getItemId(int position) {

		return position;

	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		convertView = listContainer.inflate(R.layout.main_ui_item, null);
		// ��ÿؼ�����
		image = (ImageView) convertView.findViewById(R.id.main_ui_image);
		text1 = (TextView) convertView.findViewById(R.id.main_ui_text1);
		text2 = (TextView) convertView.findViewById(R.id.main_ui_text2);
		btn1 = (Button) convertView.findViewById(R.id.main_ui_btn1);
		btn2 = (Button) convertView.findViewById(R.id.main_ui_btn2);
		btn3 = (Button) convertView.findViewById(R.id.main_ui_btn3);
		btn4 = (Button) convertView.findViewById(R.id.main_ui_btn4);
		nickname = (String) listItems.get(position).get("nickname");
		text1.setText(nickname); // ��map��keyֵΪbigadress��valueֵ������ַ�ı�1
		address = (String) listItems.get(position).get("address");
		text2.setText(address); // ��map��keyֵΪsmalladress��valueֵ������ַ�ı�2
		password = (String) listItems.get(position).get("password");
		port = (Integer) listItems.get(position).get("port");
		//��ɾ�������ӣ��޸İ�ť���ñ�ǣ�
		btn1.setTag(position);
		btn2.setTag(position);
		btn3.setTag(position);
		// ���޸İ�ť��Ӽ����¼�
		btn1.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//��ȡ��ǰ�������ť��λ�ñ��
				mPonsitonBtn=Integer.parseInt(v.getTag().toString());
				Map<String, Object> map=listItems.get(mPonsitonBtn);
				//Bundle���������󶨣��û��������룬��ַ���˿ڣ��Ϳؼ�λ�õ�����
				Bundle bundle = new Bundle();
				bundle.putString("nickname", map.get("nickname").toString());
				bundle.putString("address", map.get("address").toString());
				bundle.putString("password", map.get("password").toString());
				bundle.putInt("port", Integer.parseInt(map.get("port").toString()));
				bundle.putInt("position", mPonsitonBtn);
				bundle.putString("colormodle", map.get("colormodle").toString());
				Intent intent = new Intent();
				intent.putExtra("bundle_item", bundle);
				intent.putExtra("flag_update", UPDATE);
				intent.setClass(context, InterActivity.class); // ��ת���޸�ҳ���Activity
				context.startActivity(intent);
				context.finish();				
			}
		});

		
		// �����Ӱ�ť��Ӽ����¼�
		btn2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {		
				 ArrayList<ConnectionBean> map=	mObj;
				mPonsitonBtn=Integer.parseInt(v.getTag().toString());
				Parcelable  parcelable = mObj.get(mPonsitonBtn).Gen_getValues();
				com.vnc.draw.activity.MyApplication.getInstance().setParcelable(parcelable);
				com.vnc.draw.activity.MyApplication.padIp = (String) listItems.get(position).get("address");
				Intent intent = new Intent();
				intent.putExtra(VncConstants.CONNECTION,parcelable);
				intent.putExtra("activity_flag", 0);
				intent.setClass(context, VncCanvasActivity.class); // ��ת���޸�ҳ���Activity
				
				context.startActivity(intent);
				context.finish();
			}
		});
		
		// ��ɾ����ť��Ӽ����¼�
				btn3.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						mPonsitonBtn=Integer.parseInt(v.getTag().toString());
						saveAndWriteRecent();
						context.notificationAdapter(mPonsitonBtn);	
					}
				});
		//����ͼ��ť��Ӽ����¼�
		btn4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				com.vnc.draw.activity.MyApplication.padIp = (String) listItems.get(position).get("address");
				Parcelable  parcelable = mObj.get(position).Gen_getValues();
				com.vnc.draw.activity.MyApplication.getInstance().setParcelable(parcelable);
				Intent i = new Intent(context,MainActivity.class);
				context.startActivity(i);
				context.finish();
			}
		});
		
		return convertView;
	}
	
	public void saveAndWriteRecent()
	{
		SQLiteDatabase db = MyApplication.getInstance().getDatabase().getWritableDatabase();
		db.beginTransaction();
		try
		{
			selected = MyApplication.getInstance().getConnections().get(mPonsitonBtn);
			selected.deletedata(db);
			MostRecentBean mostRecent = getMostRecent(db);
			if (mostRecent == null)
			{
				mostRecent = new MostRecentBean();
				mostRecent.setConnectionId(selected.get_Id());
				mostRecent.Gen_insert(db);
			}
			else
			{
				//mostRecent.setConnectionId(AndroidVNCEntry.connections.get(position).get_Id());
				mostRecent.Gen_update(db);
			}
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}
	}
	static MostRecentBean getMostRecent(SQLiteDatabase db)
	{
		ArrayList<MostRecentBean> recents = new ArrayList<MostRecentBean>(1);
		MostRecentBean.getAll(db, MostRecentBean.GEN_TABLE_NAME, recents, MostRecentBean.GEN_NEW);
		if (recents.size() == 0)
			return null;
		return recents.get(0);
	}

}
