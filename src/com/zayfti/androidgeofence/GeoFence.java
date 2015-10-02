package com.zayfti.androidgeofence;

public class GeoFence {

	private double lng;
	private double lat;
	private int radius;
	private String identifier;

	public GeoFence(double lng, double lat, int radius,String identifier) {
		this.lng = lng;
		this.lat = lat;
		this.radius = radius;
		this.identifier = identifier;
	}

	public double getX() {
		return lng;
	}

	public double getY() {
		return lat;
	}

	public int getRadius() {
		return radius;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	static double calculateDistance(double longitude1, double latitude1,
			double longitude2, double latitude2) {
		double c = Math.sin(Math.toRadians(latitude1))
				* Math.sin(Math.toRadians(latitude2))
				+ Math.cos(Math.toRadians(latitude1))
				* Math.cos(Math.toRadians(latitude2))
				* Math.cos(Math.toRadians(longitude2)
						- Math.toRadians(longitude1));
		c = c > 0 ? Math.min(1, c) : Math.max(-1, c);
		return 3959 * 1.609 * 1000 * Math.acos(c);
	}

	public static boolean checkInside(GeoFence geoFence, double x, double y) {
		return calculateDistance(geoFence.getX(), geoFence.getY(), x, y) < geoFence
				.getRadius();
	}

}
