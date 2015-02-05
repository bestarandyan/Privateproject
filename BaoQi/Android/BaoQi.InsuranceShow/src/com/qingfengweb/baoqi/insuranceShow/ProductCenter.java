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
	
	//展示图片
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
	
	
	private String[] str1 = {"国寿福禄鑫尊两全保险","国寿安欣无忧两全保险"
			,"国国寿安欣无忧长期意外伤害保险","国寿附加安欣无忧提前给付重大疾病保险"
			,"国寿附加绿荫呵护少儿重大疾病保险","国寿绿荫呵护少儿两全保险"
			,"国寿松鹤颐年年金保险","国寿美满人生至尊版年金保险"
			,"国寿福禄宝宝两全保险","国寿福满一生两全保险"
			,"国寿附加重大疾病保险","国寿福禄呈祥两全保险"
			,"国寿福禄金尊两全保险","国寿新鸿泰两全保险","国寿附加康友重大疾病保险"
			,"国寿安鑫保险计划","国寿育才少儿两全保险","国寿新鸿泰金典版两全保险"
			,"国寿福禄尊享两全保险","国寿福瑞人生两全保险","国寿附加长久呵护意外伤害保险"
			,"国寿长久呵护住院费用补偿医疗保险","国寿长久呵护意外伤害费用补偿医疗保险"
			,"国寿长久呵护意外伤害定额给付医疗保险","国寿福禄满堂养老年金保险"
			,"国寿康宁定期重大疾病保险","国寿康宁终身重大疾病保险","附加关爱一生长期医疗保险"
			,"国寿关爱一生终身寿险","国寿附加定期寿险","国寿鸿盛终身寿险"
			,"国寿英才少儿两全保险","国寿附加定期寿险","国寿子女教育两全保险"
			,"国寿子女教育两全保险","国寿福星少儿两全保险","国寿附加康友重大疾病保险"
			,"国寿鸿康两全保险","国寿附加鸿康提前给付重大疾病保险","国寿智力人生两全保险"
			,"国寿鸿盈两全保险","国寿农村小额定期寿险","国寿农村小额定期寿险"
			,"国寿康馨长期护理保险","国寿安享一生两全保险","国寿瑞鑫两全保险"
			,"国寿附加瑞鑫提前给付重大疾病保险","国寿长久呵护住院定额给付医疗保险"
			,"国寿祥泰定期寿险","国寿祥泰终身寿险","国寿金彩明天两全保险"
			,"国寿金彩明天两全保险","国寿瑞丰两全保险","国寿裕丰投资连结保险"
			,"国寿鸿富两全保险","国寿新简易人身两全保险","国寿鸿丰B两全保险"
			,"国寿祥福定期寿险","国寿美满一生年金保险","国寿附加重大自然灾害意外伤害保险"
			,"国寿个人养老年金保险","国寿鸿丰两全保险","国寿美满人生年金保险"
			,"国寿鸿裕两全保险","国寿瑞祥终身寿险","国寿鸿禧年金保险","国寿瑞祥终身寿险"
			,"国寿附加安康提前给付重大疾病保险","国寿附加安泰意外伤害保险","国寿鸿丰两全保险"
			,"国寿附加安康提前给付重大疾病保险","国寿康恒重大疾病保险","国寿鸿泰两全保险"
			,"国寿鸿宇两全保险","国寿鸿鑫两全保险","国寿鸿祥两全保险","国寿鸿星少儿两全保险"
			,"国寿附加长久呵护残疾意外伤害保险","国寿鸿运少儿两全保险","国寿鸿寿年金保险"
			,"国寿鸿福相伴两全保险","国寿千禧理财两全保险","国寿关爱生命女性疾病保险"
			,"国寿关爱生命女性疾病保险","国寿金色夕阳养老年金保险","国寿99鸿福两全保险"};

	private String[] str2 = {"个险营销员渠道","银行中介渠道"};
	private String[] mThumbIds = {
			"(分红险)","(万能险)","(投连险)","(少儿险)","(健康险)"
			,"(养老险)","(保障险)","(两全险)","(意外险)","(附加险)","(连生险)"
			,"(团体寿险)","(机构代理寿险)"
		};
	private String[] str4 = {"本合同生效之日起至合同终止日止","合同生效之日起至被保险人年满七十五周岁的年生效对应日止","分为五年、六年和十年三种"};
	private String[] str5 = {"年交","分为一次性交付和分期交付（年交）两种，由投保人在投保时选择"};
	private View homebtn;
	private View h_backhomebtn; 
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
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
		String[] str3 = {"国寿福禄鑫尊两全保险"+mThumbIds[class_baoxian]+",三年一返还,终身生存金,特别生存金,让您老有所依。"
				,"国寿福满一生两全保险"+mThumbIds[class_baoxian]+"一年一返还，即交即领，多重领取，满期给付基本保险金额，享受红利分配，享有高额保障，保您福满一生。"
				,"国寿新鸿泰两全保险"+mThumbIds[class_baoxian]+"为您提供生存保障和身故保障，您还可以通过分红享受公司的经营成果，让财富升级"
		,"国寿祥泰定期寿险提供身故保险金、身体高度残疾保险金和豁免保险费，助您实现梦想"		
		,"国寿祥福定期寿险只需交纳低额保费，便可获得高额保障，为您提供身故保障及身体高度残疾保障"
		,"国寿康宁定期健康保障计划为您提供20种重大疾病保障及身故保障、满期全额返回本金，还可保单借款"
		,"国寿长久呵护住院定额给付医疗保险为您提供最高100日的住院定额给付，多达618种住院参考病种，最长可续保至70周岁"
		,"国寿鸿康(A款)保险计划为您保障12类大病，并提供身故保障，满期返本，10年交费可享20年保障，还可享有公司红利、保单借款,一通电话，保险到家"
		,"国寿安鑫保险组合计划，在保险期间内提供意外残疾、意外Ⅲ度烧伤和身故等多重保障。意外残疾保险金、意外烧伤保险金赔付之外，生存至保险期间届满受益人仍可获得所交保费（不计利息）返还。"
		,"如E公民出境保险计划适合因旅游、留学、探亲访友、外交、访问、考察、短期培训而出境的人员，提供最高30万的意外伤害保障。"
		,"如E全家福保险计划为您和家人提供意外伤害、意外伤害住院医疗保障，365天时刻相伴，保障您和家人的正常生活！"
		,"如E家庭保险计划为符合国家计划生育政策的夫妇及其子女提供意外伤害身故、残疾以及意外伤害住院医疗保障，365天保障时刻相伴！"
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
