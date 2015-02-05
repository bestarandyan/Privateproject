/**
 * 
 */
package com.qingfengweb.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 刘星星
 * @createDate 2013/9/4
 * 存放本地静态数据，不会存入数据库， 在程序退出之前这里面的数据一值存在，在程序退出的时候释放这里面的数据
 */
public class LocalStaticData {
	public static List<String> isSelectedStore = new ArrayList<String>();//装载已经选过的城市门店
}
