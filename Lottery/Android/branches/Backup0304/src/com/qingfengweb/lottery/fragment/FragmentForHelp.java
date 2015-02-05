/**
 * 
 */
package com.qingfengweb.lottery.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.activity.AmendMyInfoActivity;
import com.qingfengweb.lottery.activity.ChargeHistoryActivity;
import com.qingfengweb.lottery.activity.ChargeMoneyActivity;
import com.qingfengweb.lottery.activity.LoginActivity;
import com.qingfengweb.lottery.activity.MainActivity;
import com.qingfengweb.lottery.adapter.MyCaiPiaoListViewAdapter;
import com.qingfengweb.lottery.bean.OrderBean;
import com.qingfengweb.lottery.data.DBHelper;
import com.qingfengweb.lottery.data.JsonData;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.util.DeviceTool;
import com.qingfengweb.lottery.util.NetworkCheck;
import com.qingfengweb.lottery.view.XListView;
import com.qingfengweb.lottery.view.XListView.IXListViewListener;

/**
 * @author 刘星星
 * @createDate 2013/11/22
 *
 */
@SuppressLint("HandlerLeak")
public class FragmentForHelp  extends Fragment implements OnTouchListener,IXListViewListener,OnClickListener{
	private static final String ARG_TEXT = "com.qingfengweb.lottery.fragment";
	 View view = null;
	 public static FragmentForHelp newInstance(String text) {
		  FragmentForHelp f = new FragmentForHelp();
	      Bundle args = new Bundle();
	      args.putString(ARG_TEXT, text);
	      f.setArguments(args);

      return f;
  }
	 String msg = "开奖时间："+
"\n每天8:58-22:28，每10分钟开奖一次，每天82期"+
"\n玩法规则："+
"\n1、三个号码组合为一注进行单式投注，每个投注号码为1-6共六个自然数中的任意一"+
"\n个，每注金额2元。"+
"\n2、快3游戏根据号码组合共分为“和值”、“三同号”、“二同号”、“三不同号”、“二不同号”、"+
"\n“三连号通选”投注方式，具体规定如下："+
"\n（1）和值投注：是指对三个号码的和值进行投注，包括“和值4”至“和值17”投注。"+
"\n（2）三同号投注：是指对三个相同的号码进行投注，具体分为："+
"\n三同号通选：是指对所有相同的三个号码（111、222、…、666）进行投注；"+
"\n三同号单选：是指对所有相同的三个号码（111、222、…、666）中任意选择一组号码进行投注。"+
"\n（3）二同号投注：是指对两个指定的相同号码进行投注，具体分为："+
"\n二同号复选：是指对三个号码中两个指定的相同号码和一个任意号码进行投注；"+
"\n二同号单选：是指对三个号码中两个指定的相同号码和一个指定的不同号码进行投注。"+
"\n（4）三不同号投注：是指对三个各不相同的号码进行投注。"+
"\n（5）二不同号投注：是指对3个号码中2 个指定的不同号码和1个任意号码进行投注。"+
"\n（6）三连号通选投注：是指对所有3个相连的号码（仅限123、234、345、456）进行投注。"+
"\n奖项设置： "+
"\n快3游戏按当期销售额的59%、13%和28%分别计提彩票奖金、彩票发行费和彩票公益金。彩票奖金分为当期奖金和调节基金，其中，58%为当期奖金，1%为调节基金。"+
"\n快3游戏按不同单式投注方式设奖，均为固定奖。奖金规定如下："+
"\n（一）	和值投注"+
"\n1．和值4：单注奖金固定为80元；"+
"\n2．和值5：单注奖金固定为40元；"+
"\n3．和值6：单注奖金固定为25元；"+
"\n4．和值7：单注奖金固定为16元；"+
"\n5．和值8：单注奖金固定为12元；"+
"\n6．和值9：单注奖金固定为10元；"+
"\n7．和值10：单注奖金固定为9元；"+
"\n8．和值11：单注奖金固定为9元；"+
"\n9．和值12：单注奖金固定为10元；"+
"\n10．和值13：单注奖金固定为12元；"+
"\n11．和值14：单注奖金固定为16元；"+
"\n12．和值15：单注奖金固定为25元；"+
"\n13．和值16：单注奖金固定为40元；"+
"\n14．和值17：单注奖金固定为80元；"+
"\n（二）三同号投注"+
"\n    1．三同号通选：单注奖金固定为40元；"+
"\n2．三同号单选：单注奖金固定为240元；"+
"\n（三）二同号投注"+
"\n    1．二同号复选：单注奖金固定为15元；"+
"\n2．二同号单选：单注奖金固定为80元；"+
"\n（四）三不同号投注"+
"\n    三不同号：单注奖金固定为40元；"+
"\n（五）二不同号投注"+
"\n              二不同号：单注奖金固定为80元；"+
"\n（六）三连号通选投注"+
"\n     三连号通选：单注奖金固定为10元。";
	 @Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return view;
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			view = LayoutInflater.from(getActivity()).inflate(R.layout.f_help, null);
			TextView  tv = (TextView) view.findViewById(R.id.infoText);
			tv.setText(msg);
		}
	 @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		 System.out.println("onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onStart() {
		System.out.println("onStart");
		super.onStart();
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onClick(View v) {
	}
	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
		
	}
}
