package com.chatt;

import android.content.res.Resources;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import com.chatt.custom.CustomActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;
import com.red5pro.streaming.view.R5VideoView;
//import android.graphics.Camera;

/**
 * The Class NewChat is an Activity class that shows the screen for creating a
 * new Chat. The current implementation simply shows the layout and you can
 * apply your logic for each button click and for other view components.
 */
public class NewChat extends CustomActivity {
	protected R5Stream subscribe;
	protected R5Stream publish;
	protected int cameraOrientation;

	public static boolean swapped = false;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	protected String getStream1() {
		if (!swapped) return ParseUser.getCurrentUser().getUsername().toString();
		else return ParseUser.getCurrentUser().getUsername().toString();
	}

	protected String getStream2() {
		if (!swapped) return getString(R.string.stream2);
		else return getString(R.string.stream1);
	}

	protected Camera cam;

	/* (non-Javadoc)
	 * @see com.chatt.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_chat);
		Resources res = getResources();

		//Create the configuration from the values.xml

		R5Configuration config = new R5Configuration(R5StreamProtocol.RTSP, res.getString(R.string.domain), res.getInteger(R.integer.port), res.getString(R.string.context), 0.5f);
		R5Connection connection = new R5Connection(config);

		//setup a new stream using the connection
		publish = new R5Stream(connection);

		//show all logging
		publish.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

		//attach a camera video source
		cam = openFrontFacingCameraGingerbread();
		cam.setDisplayOrientation(90);

		R5Camera camera = new R5Camera(cam, 320, 240);
		camera.setBitrate(res.getInteger(R.integer.bitrate));
		camera.setOrientation(cameraOrientation);


		//attach a microphone
		R5Microphone mic = new R5Microphone();

		publish.attachMic(mic);

		R5VideoView r5VideoView = (R5VideoView) this.findViewById(R.id.video2);
		//r5VideoView.attachStream(publish);
		r5VideoView.showDebugView(res.getBoolean(R.bool.debugView));

		//publish.attachCamera(camera);

		publish.publish(getStream1(), R5Stream.RecordType.Live);

		 ParseUser user =    ParseUser.getCurrentUser();
		user.put("sttaus","online");
//		user.saveEventually();
		try {
			user.save();
		} catch (ParseException e) {
			Toast.makeText(getApplicationContext(), "Unable To save", Toast.LENGTH_LONG).show();
			//e.printStackTrace();
		}
		cam.startPreview();

//		setTouchNClick(R.id.btnAdd);
//		setTouchNClick(R.id.btnProject);
//		setTouchNClick(R.id.btnCamera);
//		setTouchNClick(R.id.btnSend);

		getActionBar().setLogo(R.drawable.icon_trans);
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	/* (non-Javadoc)
	 * @see com.chatt.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.btnSend) {
			finish();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.newchat, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * @see com.newsfeeder.custom.CustomActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}

	protected Camera openFrontFacingCameraGingerbread() {
		int cameraCount = 0;
		Camera cam = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();
		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					cam = Camera.open(camIdx);
					cameraOrientation = cameraInfo.orientation;
					applyDeviceRotation();
				} catch (RuntimeException e) {
					Log.e("R5 Test Activity", "Camera failed to open: " + e.getLocalizedMessage());
				}
			}
		}

		return cam;
	}

	protected void applyDeviceRotation() {
		int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				break;
			case Surface.ROTATION_90:
				degrees = 90;
				break;
			case Surface.ROTATION_180:
				degrees = 180;
				break;
			case Surface.ROTATION_270:
				degrees = 270;
				break;
		}

		cameraOrientation += degrees;

		cameraOrientation = cameraOrientation % 360;
	}

	public void onStop() {

		super.onStop();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"NewChat Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.chatt/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);

		if (publish != null) {
			ParseUser user =    ParseUser.getCurrentUser();
			user.put("sttaus","offline");
//		user.saveEventually();
			try {
				user.save();
			} catch (ParseException e) {
				Toast.makeText(getApplicationContext(), "Unable To save", Toast.LENGTH_LONG).show();
				//e.printStackTrace();
			}
			publish.stop();
		}
		if (subscribe != null)
			subscribe.stop();

		publish = subscribe = null;

		if (cam != null) {
			cam.stopPreview();
			cam.release();
		}

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.disconnect();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		client.connect();
//		Action viewAction = Action.newAction(
//				Action.TYPE_VIEW, // TODO: choose an action type.
//				"NewChat Page", // TODO: Define a title for the content shown.
//				// TODO: If you have web page content that matches this app activity's content,
//				// make sure this auto-generated web page URL is correct.
//				// Otherwise, set the URL to null.
//				Uri.parse("http://host/path"),
//				// TODO: Make sure this auto-generated app URL is correct.
//				Uri.parse("android-app://com.chatt/http/host/path")
//		);
//		AppIndex.AppIndexApi.start(client, viewAction);
	}
}
