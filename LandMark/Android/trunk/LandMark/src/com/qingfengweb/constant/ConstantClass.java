package com.qingfengweb.constant;

import com.qingfengweb.activity.ShoppingGuideActivity;
import com.qingfengweb.activity.SpecialGoodsActivity;
import com.qingfengweb.activity.FoodsActivity;
import com.qingfengweb.activity.CuXiaoActivity;
import com.qingfengweb.activity.MemberAreaActivity;
import com.qingfengweb.activity.R;


/**
 * Created by qingfeng on 13-5-23.
 */
public class ConstantClass {
    public static final String HTTP_CONNECT = "http://api-m.landmark-sh.com";
    public static final String DATABASE_NAME = "zhiDiDatabase";
    public static final String[] TabTag 		= {"tab1", "tab2", "tab3", "tab4", "tab5"};
    public static final  int tabImg[] = {R.drawable.tabitem_tab1_img_sel,R.drawable.tabitem_tab2_img_sel,
            R.drawable.tabitem_tab3_img_sel,R.drawable.tabitem_tab4_img_sel,R.drawable.tabitem_tab5_img_sel};
    public static final Class<?> mActivity[] ={
            CuXiaoActivity.class,
            SpecialGoodsActivity.class,
            ShoppingGuideActivity.class,
            FoodsActivity.class,
            MemberAreaActivity.class
    };
    
    public static final int NUMBER = 3;//购物指南中的品牌罗列的没行显示数据条数
    
    public static final String IMG_URL_STRING = "zhidisquare/";//程序的图片存储路径
    public static final String HOME_URL = "http://m.landmark-sh.com/?client=app";//首页的连接地址
//    public static final String HOME_URL = "http://222.73.173.53:8004/?client=app";//首页的连接地址
}
