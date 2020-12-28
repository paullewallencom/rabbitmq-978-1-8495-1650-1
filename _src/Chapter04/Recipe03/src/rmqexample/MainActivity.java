package rmqexample;

import java.io.IOException;

import rmqexample.Chapter04_Recipe07.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MainActivity extends Activity {
	final int UPDATE_INTERVAL = 1000; //milliseconds
  private GoogleMap mMap;
  private RabbitmqHandler rabbitmqHandler;
  private Handler updateHandler;

  public class UpdateMap implements Runnable {
  	GoogleMap mMap;
  	public UpdateMap(GoogleMap mMap_) {
  		mMap = mMap_;
		}
    @Override
    public void run() {
    	// periodically update the map here, in the main thread
    	MapController.INSTANCE.ExecuteCmds(mMap);
    	// rethrow the event
    	updateHandler.postDelayed(this, UPDATE_INTERVAL);
    }
  };
  UpdateMap updateMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment)).getMap();
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		
		updateMap = new UpdateMap(mMap);
		updateHandler = new Handler();
		updateHandler.postDelayed(updateMap, UPDATE_INTERVAL);
		
		rabbitmqHandler = new RabbitmqHandler();
		AsyncTask<Void, Void, Void> aconnect = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					rabbitmqHandler.Connect();
				} catch (IOException e) {
					// TODO handle this
				}
				return null;
			}			
		};
		aconnect.execute((Void)null);
	}

	@Override
	protected void onDestroy() {
		updateHandler.removeCallbacks(updateMap);
		AsyncTask<Void, Void, Void> aconnect = new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					rabbitmqHandler.Disconnect();
				} catch (IOException e) {
					// TODO handle this
				}
				return null;
			}			
		};
		aconnect.execute((Void)null);
		super.onDestroy();
	}
		
}
