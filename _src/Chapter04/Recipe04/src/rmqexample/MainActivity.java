package rmqexample;

import rmqexample.chapter04_recipe07_2nd.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends Activity {
	Intent serviceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Switch fms = (Switch)findViewById(R.id.followmeSwitch);
		fms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// attention: not "this" of the anonymous listener
					serviceIntent = new Intent(MainActivity.this, SenderService.class);
					startService(serviceIntent);
				} else {
					stopService(serviceIntent);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
