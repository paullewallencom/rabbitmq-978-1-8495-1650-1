package rmqexample;

import java.io.IOException;
import java.io.StringWriter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;

// you must use the google-play-services_lib for this import to work:
// http://developer.android.com/google/play-services/setup.html
// http://developer.android.com/tools/projects/projects-eclipse.html#ReferencingLibraryProject
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;

public class SenderService extends IntentService {
	private final String host = "184.73.245.85";
	private final String username = "guest";
	private final String password = "guest";
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	boolean terminated;

	public SenderService() {
		super("SenderService");
		terminated = false;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("rmqexample", "STARTED");
		try {
			LocationClient locationClient = new LocationClient(this,
					new GooglePlayServicesClient.ConnectionCallbacks() {
						@Override
						public void onConnected(Bundle arg0) {
							// TODO
						}

						@Override
						public void onDisconnected() {
							// TODO
						}
					}, new GooglePlayServicesClient.OnConnectionFailedListener() {
						@Override
						public void onConnectionFailed(ConnectionResult arg0) {
							// TODO
						}
					});
			Connect();
			locationClient.connect();
			while (!terminated) {
				synchronized (this) {
					try {
						long endTime = System.currentTimeMillis() + 1000;
						wait(endTime - System.currentTimeMillis());
						SendCoords(locationClient.getLastLocation());
						Log.d("rmqexample", "TICK");
					} catch (InterruptedException e) {
						// handle interruptions
					}
				}
			}
			Disconnect();
			locationClient.disconnect();
		} catch (IOException e) {
			// TODO handle this
		}
		Log.d("rmqexample", "EXITED");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		terminated = true;
		Log.d("rmqexample", "DESTROY");
		super.onDestroy();
	}

	public void Connect() throws IOException {
		factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setUsername(username);
		factory.setPassword(password);
		connection = factory.newConnection();
		channel = connection.createChannel();
	}

	public void Disconnect() throws IOException {
		channel.close();
		connection.close();
	}

	public void SendCoords(Location loc) {
		try {
			StringWriter swriter = new StringWriter();
			JsonWriter jwriter = new JsonWriter(swriter);
			jwriter.beginObject().name("lat").value(loc.getLatitude())
				.name("lon").value(loc.getLongitude()).endObject();
			jwriter.close();
			String coords = swriter.toString();
			channel.basicPublish("amq.fanout", "", null, coords.getBytes());
		} catch (IOException e) {
			// TODO handle this
		}
	}
}