package com.qingfengweb.baoqi.propertyInsurance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.propertyInsurance.ext.Product2Adapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

public class ProductCenter2 extends Activity {

	private ListView listview;
	private List<HashMap<String,Object>> listItems = new ArrayList<HashMap<String,Object>>();
	
	
	//展示图片
			private Integer[] mThumbIds1 = {
					R.drawable.ka1,R.drawable.ka2
					,R.drawable.ka3,R.drawable.ka4
					,R.drawable.ka5,R.drawable.ka6
					,R.drawable.ka7,R.drawable.ka8
					,R.drawable.ka9,R.drawable.ka10
					,R.drawable.ka11,R.drawable.ka12
				};
	
	
	private String[] str1 = {"金万福E款保险卡","全家旺福E款保险卡"
			,"九九祥福E款保险卡","开心天使E款保险卡"
			,"上海家政服务综合保险卡","福佑天年E款保险卡"
			,"守护天使E款保险卡","安享平安E款保险卡"
			,"医疗救援E款保险卡","快乐悠游E款保险卡"
			,"交通意外E款保险卡","畅行无忧E款保险卡"};

	private String[] str2 = {"一次投保，可获得人身、财产双重保障，意外住院津贴可与其他医疗保障同时享有，是您现有保障的有效补充。可作为礼物，送给别人，投保流程简单，全国均可理赔。"
			,"一人投保，全家整年无忧，每个家庭最多可保5名被保险人。同时保障家庭成员人身安全、家庭财产安全，彻底免除后顾之忧。可作为礼物，送给别人，投保流程简单，全国均可理赔。"
			,"保障全面，涵盖意外、疾病、交通意外、紧急救援保障，保额高，是商务人士贴身保障。提供一年期全天候紧急医疗救援服务。您只需要拨打一个24小时服务电话：400-6506-119，接下来的事情让我们来安排！可作为礼物，送给别人，投保流程简单，全国均可理赔。"
			,"专为学生定制，100元保障学生全年意外伤害保险，经济便宜。还提供3万元意外医疗。投保流程简单，全国均可理赔。"
			,"30元保障家政人员意外伤害，经济便宜。保险期限内可致电12333更换家政服务人员。"
			,"专门为老年人定制的老年人保险卡，为老年人日常生活最容易受到的伤害提供保障，涵盖骨折与关节脱位意外、交通意外、一般日常意外等，还提供住院护理津贴和专业医疗救援服务。"
			,"专门为女性定做，除了提供女性日常生活的意外伤害保障，还附加保障女性妇科癌症，是女性的最佳保险选择。"
			,"提供15万元的意外身故/残疾/烧烫伤保险，以及1万元意外医疗保险之外，还提供健康服务附加套餐，可享有意外住院垫付、健康分析评估、特需门诊预约等健康服务。"
			,"提供一年期人身意外综合保障及医疗垫付等紧急医疗救援服务。您只需要拨打一个24小时服务电话：400-6506-119，接下来的事情让我们来安排！投保流程简单，全国均可理赔。"
			,"提供全年航空意外保障，免除在机场急忙购买保险的烦恼。涵盖一整年的意外保障，平时商旅、节假日出行，保障随时随地。可作为礼物，送给别人，投保流程简单，全国均可理赔。"
			,"提供一整年乘坐公共交通工具时的意外伤害身故及意外残疾保障。商旅、节假日出行，保障随时随地。可作为礼物，送给别人，投保流程简单，全国均可理赔。"
			,"可作为车险司机责任险的有效补充，经济实惠。不驾车时，也能享受意外保障。享受全国紧急道路救援服务。投保流程简单，全国均可理赔。"};
	private String[] str3 = {"16-69周岁"
			,"三口之家。（主被保险人年龄16-69周岁）"
			,"16-60周岁"
			,"1-22周岁的学生"
			,"16—69周岁的上海地区家政服务人员。"
			,"80周岁以下的老年人"
			,"18-40周岁的女性"
			,"18-65周岁"
			,"16-69周岁"
			,"18—65周岁经常出游的人士。"
			,"18—65周岁经常出游的人士。"
			,"18—65周岁"};
	private String[] str4 = {"1年","1-12个月任选"};
	private String[] str5 = {"电子保单","纸质保单"};
	private View homebtn;
	private View h_backhomebtn; 
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		setContentView(R.layout.l_productcenter2);
		
		h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(ProductCenter2.this, PropertyInsuranceMainActivity.class);
			  	ProductCenter2.this.startActivity(intent);
			  	ProductCenter2.this.finish();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(ProductCenter2.this, PropertyInsuranceMainActivity.class);
			  	ProductCenter2.this.startActivity(intent);
			  	ProductCenter2.this.finish();
			}
		});
		listview = (ListView)findViewById(R.id.listview);
		
		notifyAdapter();
	}
	
	
	
	public void notifyAdapter(){
		getlistitem();
		Product2Adapter adapter = new Product2Adapter(this, listItems);
		listview.setAdapter(adapter);
	}
	
	
	
	public void getlistitem(){
		listItems.clear();
		
		int i;
		int index = 0;
		int index1 = 0;
		for(i = 0; i<str1.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("text1", str1[i]);
			map.put("text2", str2[i]);
			map.put("text3", str3[i]);
			if(index >= str4.length){
				index = 0;
			}
			map.put("text4", str4[index]);
			index++;
			map.put("text5", str5[0]);
			
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
		 intent.setClass(ProductCenter2.this, PropertyInsuranceMainActivity.class);
		 ProductCenter2.this.startActivity(intent);
		 ProductCenter2.this.finish();
	  }
	  return true;
	}
}
