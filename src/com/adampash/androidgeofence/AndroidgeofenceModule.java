package com.adampash.androidgeofence;

import java.util.ArrayList;
import java.util.HashMap;

import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.adampash.androidgeofence.GeoFence;
import com.google.gson.Gson;

@Kroll.module(name = "Androidgeofence", id = "com.adampash.androidgeofence")
public class AndroidgeofenceModule extends KrollModule {

	// Standard Debugging variables

	private static final String TAG = "AndroidgeofenceModule";

	private KrollFunction callback = null;

	protected static ArrayList<GeoFence> mGeofenceList;
	private String lastEnteredSite = null;
	Gson gson = new Gson();

	// You can define constants with @Kroll.constant, for example:

	// @Kroll.constant public static final String EXTERNAL_NAME = value;

	public AndroidgeofenceModule() {

		super();

	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {

		Log.d(TAG, "inside onAppCreate");

		// put module init code that needs to run when the application is

		// created

	}

	@Kroll.method
	public void startMonitoringForRegions(final String regions,
			KrollFunction _callback)

	throws JSONException {

		JSONArray jsonarray = new JSONArray(regions);

		mGeofenceList = new ArrayList<GeoFence>();
		for (int i = 0; i < jsonarray.length(); i++) {

			JSONObject region = jsonarray.getJSONObject(i);

			JSONObject center = region.getJSONObject("center");

			Double lat = center.getDouble("latitude");

			Double lng = center.getDouble("longitude");

			int radius = region.getInt("radius");

			String identifier = region.getString("identifier");

			System.out.println(lat);

			System.out.println(lng);

			System.out.println(radius);

			createGeofences(lng, lat, radius, identifier

			);

		}

		HashMap<String, String> event = new HashMap<String, String>();
		event.put("regions", gson.toJson(mGeofenceList));
		fireEvent("monitorregions", event);

	}

	@Kroll.method
	public void stopMonitoringAllRegions() {
		// TODO Auto-generated method stub

		HashMap<String, String> event = new HashMap<String, String>();
		event.put("regions", gson.toJson(mGeofenceList));
		fireEvent("removeregions", event);

		mGeofenceList.clear();
		mGeofenceList = null;
	}

	@Kroll.method
	public void checkGeofences(double lng, double lat) {
		System.out.println("Inside check geofences");

		if (mGeofenceList != null) {
			for (GeoFence fence : mGeofenceList) {
				String fenceJSON = gson.toJson(fence);
				System.out.println("Checking fence: " + fence.getIdentifier());
				HashMap<String, String> event = new HashMap<String, String>();
				if (GeoFence.checkInside(fence, lng, lat)) {
					if (lastEnteredSite == null) {
						System.out.printf("You are entering %s",
								fence.getIdentifier());
						event.put("regions", fenceJSON);
						fireEvent("enterregions", event);
						lastEnteredSite = fence.getIdentifier();
					} else {
						System.out.printf("You are still checked into %s",
								fence.toString());
						event.put("regions", fenceJSON);
						fireEvent("inregions", event);
					}

				} else if (lastEnteredSite != null
						&& lastEnteredSite.equals(fence.getIdentifier())) {
					System.out.printf("You are exiting %s",
							fence.getIdentifier());
					event.put("regions", fenceJSON);
					fireEvent("exitregions", event);

				} else {
					System.out.printf("You are not in %s",
							fence.getIdentifier());
				}

			}
		}

	}

	private void createGeofences(Double lng, Double lat, int radius,
			String identifier) {

		// TODO Auto-generated method stub

		GeoFence geoFence = new GeoFence(lng, lat, radius, identifier);

		mGeofenceList.add(geoFence);

	}

}
