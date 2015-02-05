package com.qingfengweb.common.utils;

public class LongitudeAndLatitude {
	private static final double PI = 3.14159265;
	private static final double EARTH_RADIUS = 6378137;
	private static final double RAD = Math.PI / 180.0;
	/**
	 * 根据经纬度获得最大的最小的经纬度
	 * 
	 * @param lon
	 * @param lat
	 * @param raidus
	 * @return
	 */
	public static double[] getAround(double lon, double lat, int raidus) {

		/*final double PI = 3.14159265;
		final double EARTH_RADIUS = 6378137;
		final double RAD = Math.PI / 180.0;*/

		Double latitude = lat; // 纬度
		Double longitude = lon; // 精度

		Double degree = (24901 * 1609) / 360.0;
		double raidusMile = raidus; // 半径

		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		Double minLat = latitude - radiusLat;
		Double maxLat = latitude + radiusLat;

		Double mpdLng = degree * Math.cos(latitude * (PI / 180));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		Double minLng = longitude - radiusLng;
		Double maxLng = longitude + radiusLng;
		// System.out.println("["+minLat+","+minLng+","+maxLat+","+maxLng+"]");
		return new double[] { minLng, maxLng, minLat, maxLat };
	}

	/**
	 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double getDistance(double lng1, double lat1, double lng2,
			double lat2) {
		double radLat1 = lat1 * RAD;
		double radLat2 = lat2 * RAD;
		double a = radLat1 - radLat2;
		double b = (lng1 - lng2) * RAD;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
}
