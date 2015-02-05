/**
 * 
 */
package com.qingfengweb.client.data;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

/**
 * @author Ring
 * 
 */
public class BStationLocation {
	private Context context = null;
	private int mcc;
	private int mnc;
	private int lac;
	private int cid;
	private StringBuffer cells = null;

	public BStationLocation(Context context) {
		this.context = context;
	}

	public String getLocation() {
		cells = new StringBuffer();
		TelephonyManager mTelephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		// ����ֵMCC + MNC
		String operator = mTelephonyManager.getNetworkOperator();
		if(operator==null||operator.equals("")){
			return "";
		}
		mcc = Integer.parseInt(operator.substring(0, 3));
		mnc = Integer.parseInt(operator.substring(3));
		String teloperator = mTelephonyManager.getSimOperator();
		mTelephonyManager.getNeighboringCellInfo();
		if (teloperator != null) {
			if (teloperator.equals("46000") || teloperator.equals("46002")) {
				// �й��ƶ�
				// �й��ƶ����й���ͨ��ȡLAC��CID�ķ�ʽ
				GsmCellLocation location = (GsmCellLocation) mTelephonyManager
						.getCellLocation();
				lac = location.getLac();
				cid = location.getCid();
			} else if (teloperator.equals("46001")) {
				// �й���ͨ
				// �й��ƶ����й���ͨ��ȡLAC��CID�ķ�ʽ
				GsmCellLocation location = (GsmCellLocation) mTelephonyManager
						.getCellLocation();
				lac = location.getLac();
				cid = location.getCid();
			} else if (teloperator.equals("46003")) {
				// �й�����
				CdmaCellLocation location = (CdmaCellLocation) mTelephonyManager
						.getCellLocation();
				lac = location.getNetworkId();
				cid = location.getBaseStationId();
			}

		}
		getCells(lac, cid);
		// ��ȡ������վ��Ϣ
		List<NeighboringCellInfo> infos = mTelephonyManager
				.getNeighboringCellInfo();
		if (infos != null && infos.size() > 0) {
			for (NeighboringCellInfo info1 : infos) { // ����������������ѭ��
				getCells(info1.getLac(), info1.getCid());
				if (infos.indexOf(info1) > 1||infos.indexOf(info1) == -1) {
					break;
				}
			}
		}
		if(cells!=null&&cells.length()>0){
			return cells.substring(0, cells.length()-1);
		}
		return "";
	}
	/**
	 * ��ȡJSON��ʽ�Ļ�վ��Ϣ
	 * @param lac
	 *            λ��������
	 * @param cid
	 *            ��վ���
	 * @throws JSONException
	 */
	public void getCells(int cid, int lac) {
		if(cells==null){
			cells= new StringBuffer();
		}
		try {
			Log.i("Bstion", "MCC = " + mcc + "\t MNC = " + mnc + "\t LAC = " + lac
					+ "\t CID = " + cid);
			cells.append(cid);
			cells.append(",");
			cells.append(lac);
			cells.append(",");
			cells.append(mcc);
			cells.append(",");
			cells.append(mnc);
			cells.append(";");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �ж��Ƿ��ȡ����վλ��
	 * @return true ��ȡ����վ�ˣ�falseδ��ȡ����վ
	 */
	
	public String checkBstation(){
		if(cells!=null&&cells.length()>0){
			return cells.substring(0, cells.length()-1);
		}
		return "";
	}
}
