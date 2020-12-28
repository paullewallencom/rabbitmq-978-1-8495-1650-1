package rmqexample;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.rabbitmq.tools.json.JSONReader;

// just a singleton
public enum MapController {
	INSTANCE;
	private ConcurrentLinkedQueue<MapCmd> cmdList;
	private List<LatLng> allPoints;
	private Polyline polyline;
	private Marker marker;
	
	private MapController() {
		cmdList = new ConcurrentLinkedQueue<MapCmd>();
		allPoints = new LinkedList<LatLng>();
	}
	
	public void ExecuteCmds(GoogleMap mMap) {
		if (polyline == null) {
			polyline = mMap.addPolyline(new PolylineOptions());
			polyline.setColor(0x800000FF);
		}

		while (!cmdList.isEmpty()) {
			MapCmd cmd = cmdList.poll();
			LatLng ll = new LatLng(cmd.latitude, cmd.longitude);
			allPoints.add(ll);
			if (marker == null) {
				marker = mMap.addMarker(new MarkerOptions().position(ll));
			} else {
				marker.setPosition(ll);
			}
			polyline.setPoints(allPoints);
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(cmd.latitude, cmd.longitude), 18, 0, 0)));
		}
	}
	
	public void AddCmdFromJson(String jsonString) {
		JSONReader jsonreader = new JSONReader();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> hash=  (HashMap<String, Object>) jsonreader.read(jsonString);
		
		MapCmd cmd = new MapCmd();
		cmd.latitude = Float.valueOf(hash.get("lat").toString());
		cmd.longitude = Float.valueOf(hash.get("lon").toString());
		cmdList.add(cmd);
	}
}
