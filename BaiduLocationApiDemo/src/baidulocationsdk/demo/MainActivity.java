package baidulocationsdk.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDGeofence;
import com.baidu.location.BDLocationStatusCodes;
import com.baidu.location.GeofenceClient;
import com.baidu.location.GeofenceClient.OnAddBDGeofencesResultListener;
import com.baidu.location.GeofenceClient.OnGeofenceTriggerListener;
import com.baidu.location.GeofenceClient.OnRemoveBDGeofencesResultListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * �ٶȶ�λSDK��ƷDemo������ҳ
 */
public class MainActivity extends Activity implements OnItemClickListener, OnClickListener{
	private ListView mListView;
	private Button mTestLocBtn;
	private MainAdapter mAdapter;
	private SharedPreferences mSharedPreferences;
	private LocationClient mLocClient;
	
	private boolean mLocationInit;
	private boolean mGeofenceInit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLocClient = ((MyApplication)getApplication()).mLocationClient;
		
		mSharedPreferences = getSharedPreferences(Constants.PREF_FILE_NAME, MODE_PRIVATE);
		
		mListView = (ListView) findViewById(R.id.function_list);
		mTestLocBtn = (Button) findViewById(R.id.test_demo);
		mListView.setOnItemClickListener(this);
		mTestLocBtn.setOnClickListener(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		initAdapter();
	}
	
	//��ʼ��������List
	private void initAdapter() {
		String[] mainTitle = this.getResources().getStringArray(R.array.main_title);
		String[] secondTitle = this.getResources().getStringArray(R.array.second_title);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(5);
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(MainAdapter.PARAM_MAIN_TITLE, mainTitle[i]);
			map.put(MainAdapter.PARAM_SECOND_TITLE, secondTitle[i]);
			map.put(MainAdapter.PARAM_SETTING_TITLE, getCurrentSetting(i));
			list.add(map);
		}
		mAdapter = new MainAdapter(this, list);
		mListView.setAdapter(mAdapter);
	}
	
	//���������Ƿ��������
	private String getCurrentSetting(int index) {
		int setting = 0;
		switch (index) {
		case 0:
			setting = mSharedPreferences.getInt(Constants.BASE_LOCATION_FUNCTION, 0);
			break;
		case 1:
			setting = mSharedPreferences.getInt(Constants.GEOCODING_LOCATION_FUNCTION, 0);
			break;
		case 2:
			setting = mSharedPreferences.getInt(Constants.GEOFENCE_LOCATION_FUNCTION, 0);
			break;
		case 3:
			setting = mSharedPreferences.getInt(Constants.OTHRER_LOCATION_FUNCTION, 0);
			break;
		case 4:
			setting = Integer.MAX_VALUE;
			break;

		default:
			break;
		}
		
		return setting == 0 ? "δ����" : (setting == 1 ? "�������" : "");
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i ;
		switch (arg2) {
		case 0:
			//������λ����
			i = new Intent(MainActivity.this, BaseLocationActivity.class);
			startActivity(i);
			break;
		case 1:
			//��������빦��
			i = new Intent(MainActivity.this, GeocodeingActivity.class);
			startActivity(i);
			break;
		case 2:
			//����Χ������
			i = new Intent(MainActivity.this, GeofenceActivity.class);
			startActivity(i);
			break;
		case 3:
			//��������
			i = new Intent(MainActivity.this, OtherActivity.class);
			startActivity(i);
			break;
		case 4:
			//��������
			i = new Intent(MainActivity.this, QuestActivity.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}

	
	private LocationMode mLocationMode;
	private boolean mLocationSequency;
	private int mScanSpan;
	private boolean mIsNeedAddress;
	private String mCoordType;
	private boolean mIsNeedDirection;
	
	private int mGeofenceType;
	
	private GeofenceClient mGeofenceClient;
	private String mSavingFenceName;
	private String mSavingFenceLongitude;
	private String mSavingFenceLaitude;
	private String mSavingFenceRadius;
	private String mSavingFenceCoordType;
	private Long mSavingFenceExpirationTime;
	
	private String mHightAccuracyLongitude;
	private String mHightAccuracyLaitude;
	private String mHightAccuracyRadius;
	private String mHightAccuracyCoordType;
	
	@Override
	public void onClick(View v) {
		getLocationParams();
		setLocationOption();
		//��ʼ��λ
		if (mLocationInit) {
			mLocClient.start();
		} else {
			Toast.makeText(this, "�����ö�λ��صĲ���", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!mLocationSequency && mLocClient.isStarted()) {
			//��������λ
			mLocClient.requestLocation();
		} 
		
		if (mGeofenceInit) {
			//����Χ��ʵ��
			if (mGeofenceType == 0) {
				//�߾���
				//4����������Ҫλ�����ѵĵ�����꣬���庬������Ϊ��γ�ȣ����ȣ����뷶Χ������ϵ����(gcj02,gps,bd09,bd09ll)
				((MyApplication)getApplication()).mNotifyLister.SetNotifyLocation(Double.valueOf(mHightAccuracyLaitude),
						Double.valueOf(mHightAccuracyLongitude), Float.valueOf(mHightAccuracyRadius), 
						mHightAccuracyCoordType);
				mLocClient.registerNotify(((MyApplication)getApplication()).mNotifyLister);
			} else if (mGeofenceType == 1) {
				//�͹���
				mGeofenceClient = ((MyApplication)getApplication()).mGeofenceClient;
				mGeofenceClient.registerGeofenceTriggerListener(new GeofenceTriggerListener());
				addGeofenceBySaving();
			}
		}
		
		Intent i = new Intent(MainActivity.this, LocationResultActivity.class);
		startActivity(i);
	}
	
	//���һ���͹���Χ��
	private void addGeofenceBySaving() {
		BDGeofence fence = new BDGeofence.Builder().setGeofenceId(mSavingFenceName).
				setCircularRegion(Double.valueOf(mSavingFenceLongitude), Double.valueOf(mSavingFenceLaitude), Integer.valueOf(mSavingFenceRadius)).
				setExpirationDruation(mSavingFenceExpirationTime).
				setCoordType(mSavingFenceCoordType).
				build();
		
		mGeofenceClient.addBDGeofence(fence, new AddGeofenceListener());
	}
	
	//��ȡ��λ��������
	private void getLocationParams() {
		//��λ����
		int locationMode = mSharedPreferences.getInt(Constants.LOCATION_MODE, 0);
		switch (locationMode) {
		case R.id.location_mode_height_accuracy:
			mLocationMode = LocationMode.Hight_Accuracy;
			break;
		case R.id.location_mode_saving_battery:
			mLocationMode = LocationMode.Battery_Saving;
			break;
		case R.id.location_mode_device_sensor:
			mLocationMode = LocationMode.Device_Sensors;
			break;
		default:
			break;
		}
		
		//��λģʽ�����ʱ��
		int locationSequence = mSharedPreferences.getInt(Constants.LOCATION_SEQUENCE, 0);
		switch (locationSequence) {
		case R.id.continuous_location:
			mLocationSequency = true;
			mScanSpan = mSharedPreferences.getInt(Constants.LOCATION_SCAN_TIME, 1000);
			break;
		case R.id.single_location:
			mLocationSequency = false;
			break;

		default:
			break;
		}
		
		//��ַ��Ϣ
		int geocoding = mSharedPreferences.getInt(Constants.GEOCODING_TYPE, 0);
		switch (geocoding) {
		case R.id.use_geocode:
			mIsNeedAddress = false;
			break;
		case R.id.no_use_geocode:
			mIsNeedAddress = true;
			break;

		default:
			break;
		}
		
		//��λ��������
		int coord = mSharedPreferences.getInt(Constants.LOCATION_COORD_TYPE_FOR_OTHER, 1);
		switch (coord) {
		case R.id.location_coord_type_gcj:
			mCoordType = "gcj02";
			break;
		case R.id.location_coord_type_bd09ll:
			mCoordType = "bd09ll";
			break;
		case R.id.location_coord_type_bd09:
			mCoordType = "bd09";
			break;

		default:
			break;
		}
		
		//�Ƿ���Ҫ����
		int direction = mSharedPreferences.getInt(Constants.LOCATION_DIRECTION, 1);
		switch (direction) {
		case R.id.is_need_direction_yes:
			mIsNeedDirection = true;
			break;
		case R.id.is_need_direction_no:
			mIsNeedDirection = false;
			break;

		default:
			break;
		}
		
		
		try {
			//����Χ��ѡ��
			mGeofenceType = mSharedPreferences.getInt(Constants.GEOFENCE_TYPE, R.id.saving_battery);
			switch (mGeofenceType) {
			case R.id.hight_accuracy:
				//�߾���
				mHightAccuracyLongitude = mSharedPreferences.getString(Constants.GEOFENCE_LONGITUDEE, "116.30677");
				mHightAccuracyLaitude = mSharedPreferences.getString(Constants.GEOFENCE_LATITUDE, "40.04173");
				mHightAccuracyRadius = mSharedPreferences.getString(Constants.GEOFENCE_RADIUS, "3000");
				mHightAccuracyCoordType = mSharedPreferences.getString(Constants.LOCATION_COORD_TYPE, "gps");
				mGeofenceInit = true;
				break;
			case R.id.saving_battery:
				//�͹���
				mSavingFenceName = mSharedPreferences.getString(Constants.BATTERYSAVE_GEOFENCE_NAME, "fence_name");
				mSavingFenceLongitude = mSharedPreferences.getString(Constants.BATTERYSAVE_GEOFENCE_LONGITUDEE, "116.30677");
				mSavingFenceLaitude = mSharedPreferences.getString(Constants.BATTERYSAVE_GEOFENCE_LATITUDE, "40.04173");
				mSavingFenceRadius = mSharedPreferences.getString(Constants.BATTERYSAVE_GEOFENCE_NAME, "1");
				mSavingFenceCoordType = mSharedPreferences.getString(Constants.BATTERYSAVE_GEOFENCE_COORD_TYPE, BDGeofence.COORD_TYPE_BD09LL);
				mSavingFenceExpirationTime = mSharedPreferences.getLong(Constants.BATTERYSAVE_GEOFENCE_VALITATE_TIME, 10L * (3600 * 1000));
				mGeofenceInit = true;
				break;
			default:
				break;
			}
		} catch (Exception e) {
			mGeofenceInit = false;
		}
	}
	
	//����Option
	private void setLocationOption() {
		try {
			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(mLocationMode);
			option.setCoorType(mCoordType);
			option.setScanSpan(mScanSpan);
			option.setNeedDeviceDirect(mIsNeedDirection);
			option.setIsNeedAddress(mIsNeedAddress);
			mLocClient.setLocOption(option);
			mLocationInit = true;
		} catch (Exception e) {
			e.printStackTrace();
			mLocationInit = false;
		}
	}
	
	
	/**
	 * ʵ�����Χ��������
	 *
	 */
	public class AddGeofenceListener implements OnAddBDGeofencesResultListener {

		@Override
		public void onAddBDGeofencesResult(int statusCode, String geofenceId) {
			try {
				if (statusCode == BDLocationStatusCodes.SUCCESS) {
					//������ʵ�ִ���Χ���ɹ��Ĺ����߼�
					
					Toast.makeText(MainActivity.this, "Χ��" + geofenceId + "��ӳɹ�", Toast.LENGTH_SHORT).show();
					
					if (mGeofenceClient != null) {
						//����ӵ���Χ���ɹ��󣬿�������Χ�����񣬶Ա��δ����ɹ����ѽ���ĵ���Χ��������ʵʱ������
						mGeofenceClient.start();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ʵ��ɾ��Χ��������
	 *
	 */
	public class RemoveGeofenceListener implements OnRemoveBDGeofencesResultListener {

		@Override
		public void onRemoveBDGeofencesByRequestIdsResult(int statusCode, String[] geofenceIds) {
			if (statusCode == BDLocationStatusCodes.SUCCESS) {
				//������ʵ��ɾ��Χ���ɹ��Ĺ����߼�

				Toast.makeText(MainActivity.this, "Χ��ɾ���ɹ�", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	/**
	 * ʵ�ֽ���Χ��������
	 *
	 */
	public class GeofenceTriggerListener implements OnGeofenceTriggerListener {

		@Override
		public void onGeofenceTrigger(String geofenceId) {
			//������ʵ�ֽ���Χ���Ĺ����߼�
			try {
				((Vibrator)MainActivity.this.getApplication().getSystemService(Service.VIBRATOR_SERVICE)).vibrate(3000);
				Toast.makeText(MainActivity.this, "�ѽ���Χ��" + geofenceId, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				
			}
		}
	}
	
}
