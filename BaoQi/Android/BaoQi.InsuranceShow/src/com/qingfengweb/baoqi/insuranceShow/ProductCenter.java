package com.qingfengweb.baoqi.insuranceShow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.insuranceShow.ext.ImageAdapter;
import com.qingfengweb.baoqi.insuranceShow.ext.ProductAdapter;
import com.qingfengweb.baoqi.insuranceShow.ext.SelecClassAdapter;
import com.qingfengweb.baoqi.insuranceShow.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ProductCenter extends Activity {

	private ListView listview;
	private ImageButton btn_select;
	private LinearLayout linear;
	private Button btn_renshou;
	private Button btn_caichan;
	private Button tuan_btn;
	private Button jigou_btn;
	private List<HashMap<String,Object>> listItems = new ArrayList<HashMap<String,Object>>();
	private GridView gridView;
	private int tag = 0;
	
	private int class_baoxian = 0;
	
	//չʾͼƬ
			private Integer[] mThumbIds1 = {

				R.drawable.product_tu1, R.drawable.product_tu2,
				R.drawable.product_tu3,R.drawable.anxinbaoxianzuhejihua_l
				,R.drawable.changjiuhehuzhuyuanfeiyongbuchang_l
				,R.drawable.fujiachangjiuhehu_l,R.drawable.fujiachangjiuhehucanji_l
				,R.drawable.fulubaobao_l,R.drawable.fulumantang_l,R.drawable.fuxingshaoer_l
				,R.drawable.gerenyanglao_l,R.drawable.guoshouhongkang5_l,R.drawable.guoshouhongxi_l
				,R.drawable.guoshouhongxin_l,R.drawable.guoshouhongyu_l
				,R.drawable.guoshoukangheng_l,R.drawable.guoshoukangxin_l,R.drawable.guoshouxiangfu_l
				,R.drawable.hongshounianjin_l,R.drawable.hongxingshaoer_l,R.drawable.hongyoubaozhangjihua_l
				,R.drawable.hongyoubaozhangjihuak_l,R.drawable.hongyunshaoer_l
				,R.drawable.kangningzhongshen_l,R.drawable.ksangningdingqi_l
				,R.drawable.ruyigongmingchujing_l,R.drawable.ruyiguoneilvyou_l
				,R.drawable.ruyiguoneilvyoyiriyou_l,R.drawable.ruyiguoneilvyoyiriyou_l
				,R.drawable.ruyihangkongyiwai_l,R.drawable.ruyijiating_l,R.drawable.ruyiquanjiafu_l
				,R.drawable.ruyisijiache_l,R.drawable.ruyizonghejiaotonggongju_l,R.drawable.ruyizongheyiwai_l
				,R.drawable.shenzhoujinjijiuyuan_l,R.drawable.songheyiniannianjinbaoxian_l
				,R.drawable.xiangtaidingqi_l,R.drawable.xiangtaizhongshen,
				R.drawable.xiangtaizhongshen_l,R.drawable.xiangzhikaf_l,R.drawable.xiangzhikgf_l
				};
	
	
	private String[] str1 = {"���ٸ�»������ȫ����","���ٰ���������ȫ����"
			,"�����ٰ������ǳ��������˺�����","���ٸ��Ӱ���������ǰ�����ش󼲲�����"
			,"���ٸ�������ǻ��ٶ��ش󼲲�����","��������ǻ��ٶ���ȫ����"
			,"�����ɺ����������","����������������������"
			,"���ٸ�»������ȫ����","���ٸ���һ����ȫ����"
			,"���ٸ����ش󼲲�����","���ٸ�»������ȫ����"
			,"���ٸ�»������ȫ����","�����º�̩��ȫ����","���ٸ��ӿ����ش󼲲�����"
			,"���ٰ��α��ռƻ�","���������ٶ���ȫ����","�����º�̩������ȫ����"
			,"���ٸ�»������ȫ����","���ٸ���������ȫ����","���ٸ��ӳ��úǻ������˺�����"
			,"���ٳ��úǻ�סԺ���ò���ҽ�Ʊ���","���ٳ��úǻ������˺����ò���ҽ�Ʊ���"
			,"���ٳ��úǻ������˺��������ҽ�Ʊ���","���ٸ�»�������������"
			,"���ٿ��������ش󼲲�����","���ٿ��������ش󼲲�����","���ӹذ�һ������ҽ�Ʊ���"
			,"���ٹذ�һ����������","���ٸ��Ӷ�������","���ٺ�ʢ��������"
			,"����Ӣ���ٶ���ȫ����","���ٸ��Ӷ�������","������Ů������ȫ����"
			,"������Ů������ȫ����","���ٸ����ٶ���ȫ����","���ٸ��ӿ����ش󼲲�����"
			,"���ٺ迵��ȫ����","���ٸ��Ӻ迵��ǰ�����ش󼲲�����","��������������ȫ����"
			,"���ٺ�ӯ��ȫ����","����ũ��С�������","����ũ��С�������"
			,"���ٿ�ܰ���ڻ�����","���ٰ���һ����ȫ����","����������ȫ����"
			,"���ٸ���������ǰ�����ش󼲲�����","���ٳ��úǻ�סԺ�������ҽ�Ʊ���"
			,"������̩��������","������̩��������","���ٽ��������ȫ����"
			,"���ٽ��������ȫ����","���������ȫ����","����ԣ��Ͷ�����ᱣ��"
			,"���ٺ踻��ȫ����","�����¼���������ȫ����","���ٺ��B��ȫ����"
			,"�����鸣��������","��������һ�������","���ٸ����ش���Ȼ�ֺ������˺�����"
			,"���ٸ������������","���ٺ����ȫ����","�����������������"
			,"���ٺ�ԣ��ȫ����","����������������","���ٺ��������","����������������"
			,"���ٸ��Ӱ�����ǰ�����ش󼲲�����","���ٸ��Ӱ�̩�����˺�����","���ٺ����ȫ����"
			,"���ٸ��Ӱ�����ǰ�����ش󼲲�����","���ٿ����ش󼲲�����","���ٺ�̩��ȫ����"
			,"���ٺ�����ȫ����","���ٺ�����ȫ����","���ٺ�����ȫ����","���ٺ����ٶ���ȫ����"
			,"���ٸ��ӳ��úǻ��м������˺�����","���ٺ����ٶ���ȫ����","���ٺ��������"
			,"���ٺ踣�����ȫ����","����ǧ�������ȫ����","���ٹذ�����Ů�Լ�������"
			,"���ٹذ�����Ů�Լ�������","���ٽ�ɫϦ�����������","����99�踣��ȫ����"};

	private String[] str2 = {"����Ӫ��Ա����","�����н�����"};
	private String[] mThumbIds = {
			"(�ֺ���)","(������)","(Ͷ����)","(�ٶ���)","(������)"
			,"(������)","(������)","(��ȫ��)","(������)","(������)","(������)"
			,"(��������)","(������������)"
		};
	private String[] str4 = {"����ͬ��Ч֮��������ͬ��ֹ��ֹ","��ͬ��Ч֮��������������������ʮ�����������Ч��Ӧ��ֹ","��Ϊ���ꡢ�����ʮ������"};
	private String[] str5 = {"�꽻","��Ϊһ���Խ����ͷ��ڽ������꽻�����֣���Ͷ������Ͷ��ʱѡ��"};
	private View homebtn;
	private View h_backhomebtn; 
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//ȥ���ֻ��ϵı�����
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		setContentView(R.layout.l_productcenter);
		
		h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(ProductCenter.this, InsuranceShowMainActivity.class);
			  	ProductCenter.this.startActivity(intent);
			  	ProductCenter.this.finish();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(ProductCenter.this, InsuranceShowMainActivity.class);
			  	ProductCenter.this.startActivity(intent);
			  	ProductCenter.this.finish();
			}
		});
		
		gridView=(GridView)findViewById(R.id.gridview);
		gridView.setAdapter(new SelecClassAdapter(this));
		
		btn_renshou = (Button)findViewById(R.id.renshou);
		btn_renshou.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn_renshou.setBackgroundResource(R.drawable.product_tag_on);
				btn_caichan.setBackgroundResource(R.drawable.product_tag);
				btn_renshou.setTextColor(Color.WHITE);
				btn_caichan.setTextColor(Color.BLACK);
			}
		});
		btn_caichan = (Button)findViewById(R.id.caichan);
		btn_caichan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn_renshou.setBackgroundResource(R.drawable.product_tag);
				btn_caichan.setBackgroundResource(R.drawable.product_tag_on);
				btn_renshou.setTextColor(Color.BLACK);
				btn_caichan.setTextColor(Color.WHITE);
			}
		});
		
		
		tuan_btn =(Button)findViewById(R.id.tuan_btn);
		tuan_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				class_baoxian = 11;
				notifyAdapter();
				linear.setVisibility(View.GONE);
				tag = 0;
			}
		});
		
		jigou_btn = (Button)findViewById(R.id.jigou_btn);
		jigou_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				class_baoxian = 12;
				notifyAdapter();
				linear.setVisibility(View.GONE);
				tag = 0;
			}
		});
		
		listview = (ListView)findViewById(R.id.listview);
		btn_select = (ImageButton)findViewById(R.id.select_class);
		linear = (LinearLayout)findViewById(R.id.layout1);
		btn_select.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(tag==0){
					linear.setVisibility(View.VISIBLE);
					tag =1;
				}else if(tag == 1){
					linear.setVisibility(View.GONE);
					tag = 0;
				}
			}
		});
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				
				class_baoxian = position;
				notifyAdapter();
				linear.setVisibility(View.GONE);
				tag = 0;
			}
		});
		
		//
		notifyAdapter();
	}
	
	
	
	public void notifyAdapter(){
		getlistitem();
		ProductAdapter adapter = new ProductAdapter(this, listItems);
		listview.setAdapter(adapter);
	}
	
	
	
	public void getlistitem(){
		String[] str3 = {"���ٸ�»������ȫ����"+mThumbIds[class_baoxian]+",����һ����,���������,�ر������,��������������"
				,"���ٸ���һ����ȫ����"+mThumbIds[class_baoxian]+"һ��һ�������������죬������ȡ�����ڸ����������ս����ܺ������䣬���и߶�ϣ���������һ����"
				,"�����º�̩��ȫ����"+mThumbIds[class_baoxian]+"Ϊ���ṩ���汣�Ϻ���ʱ��ϣ���������ͨ���ֺ����ܹ�˾�ľ�Ӫ�ɹ����òƸ�����"
		,"������̩���������ṩ��ʱ��ս�����߶Ȳм����ս�ͻ��Ᵽ�շѣ�����ʵ������"		
		,"�����鸣��������ֻ�轻�ɵͶ�ѣ���ɻ�ø߶�ϣ�Ϊ���ṩ��ʱ��ϼ�����߶Ȳм�����"
		,"���ٿ������ڽ������ϼƻ�Ϊ���ṩ20���ش󼲲����ϼ���ʱ��ϡ�����ȫ��ر��𣬻��ɱ������"
		,"���ٳ��úǻ�סԺ�������ҽ�Ʊ���Ϊ���ṩ���100�յ�סԺ������������618��סԺ�ο����֣����������70����"
		,"���ٺ迵(A��)���ռƻ�Ϊ������12��󲡣����ṩ��ʱ��ϣ����ڷ�����10�꽻�ѿ���20�걣�ϣ��������й�˾�������������,һͨ�绰�����յ���"
		,"���ٰ��α�����ϼƻ����ڱ����ڼ����ṩ����м������������˺���ʵȶ��ر��ϡ�����м����ս��������˱��ս��⸶֮�⣬�����������ڼ�����������Կɻ���������ѣ�������Ϣ��������"
		,"��E����������ռƻ��ʺ������Ρ���ѧ��̽�׷��ѡ��⽻�����ʡ����졢������ѵ����������Ա���ṩ���30��������˺����ϡ�"
		,"��Eȫ�Ҹ����ռƻ�Ϊ���ͼ����ṩ�����˺��������˺�סԺҽ�Ʊ��ϣ�365��ʱ����飬�������ͼ��˵��������"
		,"��E��ͥ���ռƻ�Ϊ���Ϲ��Ҽƻ��������ߵķ򸾼�����Ů�ṩ�����˺���ʡ��м��Լ������˺�סԺҽ�Ʊ��ϣ�365�챣��ʱ����飡"
		};
		listItems.clear();
		
		int i;
		int index = 0;
		int index1 = 0;
		for(i = 0; i<str1.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("text1", str1[i]);
			map.put("text2", mThumbIds[class_baoxian]);
			map.put("text3", str2[0]);
			if(index >= str3.length){
				index = 0;
			}
			map.put("text4", str3[index]);
			index++;
			map.put("text5", str4[0]);
			map.put("text6", str5[0]);
			
			if(index1 >= mThumbIds1.length){
				index1 = 0;
			}
			map.put("image", mThumbIds1[index1]);
			index1++;
			listItems.add(map);
		}
		
		
//		HashMap<String, Object> map1 = new HashMap<String, Object>();
//		map1.put("text1", str1[0]);
//		map1.put("text2", mThumbIds[class_baoxian]);
//		map1.put("text3", str2[0]);
//		map1.put("text4", str3[0]);
//		map1.put("text5", str4[0]);
//		map1.put("text6", str5[0]);
//		listItems.add(map1);
//		HashMap<String, Object> map2 = new HashMap<String, Object>();
//		map2.put("text1", str1[1]);
//		map2.put("text2", mThumbIds[class_baoxian]);
//		map2.put("text3", str2[0]);
//		map2.put("text4", str3[1]);
//		map2.put("text5", str4[1]);
//		map2.put("text6", str5[0]);
//		listItems.add(map2);
//		HashMap<String, Object> map3 = new HashMap<String, Object>();
//		map3.put("text1", str1[2]);
//		map3.put("text2", mThumbIds[class_baoxian]);
//		map3.put("text3", str2[1]);
//		map3.put("text4", str3[2]);
//		map3.put("text5", str4[2]);
//		map3.put("text6", str5[1]);
//		listItems.add(map3);
	}
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
		 Intent intent = new Intent();
		 intent.setClass(ProductCenter.this, InsuranceShowMainActivity.class);
		 ProductCenter.this.startActivity(intent);
		 ProductCenter.this.finish();
	  }
	  return true;
	}
}
