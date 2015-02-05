package com.qingfengweb.piaoguanjia.orderSystem.request;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class PlayerServlet extends SimpleServlet {
	public static final String SUFFIXINTERFACE_USER = "/interface/order/player";// 游玩人接口
	// ---------------------------------------------------------------------
	public static final String ACTION_ADDPLAYER = "addPlayer";// 添加游玩人
	public static final String ACTION_UPDATEPLAYER = "updatePlayer";// 更新游玩人
	public static final String ACTION_PLAYERLIST = "playerList";// 游玩人列表
	public static final String ACTION_DELETEPLAYER = "deletePlayer";// 删除游玩人

	/**
	 * 新增游玩人
	 */

	public static String actionAddplayer() {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_ADDPLAYER);
		UploadData uploaddata = new UploadData(SERVER_ADDRESS
				+ SUFFIXINTERFACE_USER, params);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 游玩人列表
	 * 
	 * isUpOrDown 向上或向下刷新 1向上，2向下
	 */

	public static String actionPlayerList(int isUpOrDown, int limit,
			String lastUpdateTime) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_PLAYERLIST);
		params.add(new BasicNameValuePair("isUpOrDown", isUpOrDown+""));
		params.add(new BasicNameValuePair("limit", limit+""));
		params.add(new BasicNameValuePair("lastUpdateTime", lastUpdateTime));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS
				+ SUFFIXINTERFACE_USER, params);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 删除游玩人
	 */

	public static String actionDeletePlayer(String playerid) {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_ADDPLAYER);
		params.add(new BasicNameValuePair("playerid", playerid));
		UploadData uploaddata = new UploadData(SERVER_ADDRESS
				+ SUFFIXINTERFACE_USER, params);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

	/**
	 * 更新游玩人
	 */

	public static String actionUpdatePlayer() {
		String msgString = "";
		List<NameValuePair> params = getParams(ACTION_UPDATEPLAYER);
		UploadData uploaddata = new UploadData(SERVER_ADDRESS
				+ SUFFIXINTERFACE_USER, params);
		uploaddata.Post();
		msgString = uploaddata.getReponse();
		if (params != null) {
			params.clear();
		}
		return msgString;
	}

}
