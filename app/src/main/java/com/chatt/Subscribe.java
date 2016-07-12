package com.chatt;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.chatt.custom.CustomFragment;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.event.R5ConnectionEvent;
import com.red5pro.streaming.event.R5ConnectionListener;
import com.red5pro.streaming.event.R5RemoteCallContainer;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;
import com.red5pro.streaming.view.R5VideoView;

import org.json.JSONArray;

/**
 * Created by mithramedia on 09/06/16.
 */
public class Subscribe extends CustomFragment implements R5ConnectionListener{

    Thread listThread;
    private Subscribe spub;

    boolean hasPublished = false;
    protected R5Stream subscribe;
    protected R5Stream publish;
    protected int cameraOrientation;
    protected Camera cam;
    public static boolean swapped = false;
    View view;
    Context mContext;
    String stream2;

    protected String getStream1(){
        if(!swapped) return ParseUser.getCurrentUser().getUsername().toString();
        else return ParseUser.getCurrentUser().getUsername().toString();
    }
    protected String getStream2(){
        if(!swapped) return stream2;
        else return stream2;
    }
    public Subscribe(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {


        Bundle bundle = this.getArguments();
        if (bundle != null) {
              stream2 = bundle.getString("userid");
            Log.d("foki **:",stream2);
        }

        View v = inflater.inflate(R.layout.two_way,container,false);
        Resources res = getResources();

        if(publish == null){

            subscribe = getNewStream(0);

            R5VideoView r5videoview = (R5VideoView) v.findViewById(R.id.video2);
            r5videoview.attachStream(subscribe);
            r5videoview.showDebugView(res.getBoolean(R.bool.debugView));

            subscribe.client = this;
            subscribe.setListener(this);


            publish = getNewStream(1);
            R5VideoView r5PublishView = (R5VideoView) v.findViewById(R.id.video);
            r5PublishView.attachStream(publish);
            r5PublishView.showDebugView(res.getBoolean(R.bool.debugView));

            publish.client = this;
            publish.setListener(this);
            subscribe.play(getStream2());

//            Button b1 = (Button) v.findViewById(R.id.push);
//            b1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    publish.publish(getStream1(), R5Stream.RecordType.Live);
//
//                    cam.startPreview();
//                }
//            });

            publish.publish(getStream1(), R5Stream.RecordType.Live);

            cam.startPreview();
//
        }

        return v;
    }

    @Override
    public void onConnectionEvent(R5ConnectionEvent r5ConnectionEvent) {
        if (r5ConnectionEvent == R5ConnectionEvent.CONNECTED) {
            if (!hasPublished) {
                listThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.interrupted() && publish != null) {
                            try {
                                Thread.sleep(4000);
                                publish.connection.call(new R5RemoteCallContainer("streams.getLiveStreams", "R5GetLiveStreams", null));
                            } catch (Exception e) {
                                System.out.println("failed to get new streams");
                            }
                        }
                    }
                });
                listThread.start();
                hasPublished = true;
            } else {
                System.out.println("Subscribed - two way active");
            }
        }
    }
    public void R5GetLiveStreams(String streams){
        System.out.println("Got the streams: "+streams);

       // parse string as JSON
        JSONArray names;
        try {
            names = new JSONArray(streams);
        } catch (Exception e) {
            System.out.println("Failed to parse streams to JSONArray");
            return;
        }

        //Look for the other stream, subscribe when available
        for(int i  = 0; i < names.length(); i++){
            try {
                if(getStream2().equals(names.getString(i))){
                    subscribe.play(getStream2());
                    listThread.interrupt();
                    return;
                }
            } catch (Exception e){
                System.out.println("Item at index " + i + " cannot be retrieved as a String");
            }
        }
    }



    protected R5Stream getNewStream(int type){


        Resources res = getResources();

        R5Configuration config = new R5Configuration(R5StreamProtocol.RTSP,res.getString(R.string.domain), res.getInteger(R.integer.port), res.getString(R.string.context), 0.5f);
        R5Connection connection = new R5Connection(config);

        R5Stream stream = new R5Stream(connection);
        stream.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

        if(type == 1) { //publishing

            cam = openFrontFacingCameraGingerbread();
            cam.setDisplayOrientation((cameraOrientation + 180)%360);

            R5Camera camera  = new R5Camera(cam, 320, 240);
            camera.setBitrate(res.getInteger(R.integer.bitrate));
            camera.setOrientation(cameraOrientation);
            R5Microphone mic = new R5Microphone();

            stream.attachMic(mic);
            stream.attachCamera(camera);
        }

        return stream;

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
    protected void applyDeviceRotation(){
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        cameraOrientation += degrees;

        cameraOrientation = cameraOrientation%360;
    }

    @Override
    public void onStop(){

        super.onStop();

        if(publish != null){
            ParseUser user =    ParseUser.getCurrentUser();
            user.put("sttaus","offline");
//		user.saveEventually();
            try {
                user.save();
            } catch (ParseException e) {
                //Toast.makeText(getApplicationContext(), "Unable To save", Toast.LENGTH_LONG).show();
                //e.printStackTrace();
            }
            publish.stop();}

        if(subscribe != null)
            subscribe.stop();

        publish = subscribe = null;

        if(cam != null) {
            cam.stopPreview();
            cam.release();
        }

    }
}
