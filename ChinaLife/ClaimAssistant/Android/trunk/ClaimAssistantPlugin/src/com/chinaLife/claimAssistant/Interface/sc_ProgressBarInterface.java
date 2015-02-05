package com.chinaLife.claimAssistant.Interface;

import com.chinaLife.claimAssistant.activity.Sc_MyApplication;

public interface sc_ProgressBarInterface {
	/**
	 * 
	 * @param legendid图例编号
	 *            更新进度条
	 */

	public void updateProgressBar(String legendid_, int position);
	
	/**
	 * 
	 * @param legendid图例编号
	 *            关闭进度条
	 */

	public void closeProgressBar(String legendid_);
	
	
	/**
	 * 
	 * @param legendid图例编号
	 *            显示进度条
	 */

	public void startProgressBar(String legendid_, int length);
	
	/**
	 * 
	 * @param 
	 */

	public void showInputDialog(String password);
	
	/**
	 * 
	 * @param 
	 */

	/**
	 * 点击照片弹出的对话框
	 * 
	 * @param index
	 *            调用系统功能的返回参数
	 * @param type
	 *            标识应该显示的对话框功能参数
	 * @author 刘星星
	 */
	public void showQianDaoDialog(int type, final String file);
}
