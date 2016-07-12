package com.chatt;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mithramedia on 08/06/16.
 */
public class VideoPublish extends Fragment{

    public VideoPublish(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish_example, container, false);
        System.out.println("fragment added");
//        Resources res = getResources();
//
//        //Create the configuration from the values.xml
//        R5Configuration config = new R5Configuration(R5StreamProtocol.RTSP,res.getString(R.string.domain), res.getInteger(R.integer.port), res.getString(R.string.context), 0.5f);
//        R5Connection connection = new R5Connection(config);
//
//        //setup a new stream using the connection
//        publish = new R5Stream(connection);
//
//        //show all logging
//        publish.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);
//
//        //attach a camera video source
//        cam = openFrontFacingCameraGingerbread();
//       // cam.setDisplayOrientation(90);
//
//        R5Camera camera  = new R5Camera(cam, 320, 240);
//        camera.setBitrate(res.getInteger(R.integer.bitrate));
//        camera.setOrientation(cameraOrientation);
//
//
//
//        //attach a microphone
//        R5Microphone mic = new R5Microphone();
//
//        publish.attachMic(mic);
//
//        R5VideoView r5VideoView =(R5VideoView) view.findViewById(R.id.video2);
//        r5VideoView.attachStream(publish);
//        r5VideoView.showDebugView(res.getBoolean(R.bool.debugView));
//
//        publish.attachCamera(camera);
//
//        publish.publish(getStream1(), R5Stream.RecordType.Live);
//
//        cam.startPreview();

        return view;
    }
}
