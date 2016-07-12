package com.chatt.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.chatt.R;
import com.chatt.custom.CustomFragmentNew;
import com.chatt.model.BroadcastItem;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mithramedia on 09/06/16.
 */
public class BroadcastList extends CustomFragmentNew {
    @Override
    public void onStop() {
        Log.d("STATE","OFFLINE");
        setUserOffline();

        super.onStop();
    }

    ArrayList<BroadcastItem> braodcastLists;
    ArrayList<BroadcastItem> offlinelist;
    private BroadcastList vpub;
    int mpIndex;
    int seek1,seek2,seek3;
    View v;
    AlertDialog levelDialog;
    final String className= "UserLanguage";

    protected R5Stream subscribe;
    protected R5Stream publish;
    protected int cameraOrientation;
    public ListView list2;
    public int i;
public String currentStreamName;
    public static boolean swapped = false;

    public boolean isFetchingStatus = false;

    protected String getStream1() {
        if (!swapped) return ParseUser.getCurrentUser().getUsername().toString();
        else return ParseUser.getCurrentUser().getUsername().toString();
    }

    protected Camera cam;
    final CharSequence[] items = {" English "," Spanish "," French "," Hindi ", "German", "Chinese", "Arabic" ,"Russian"};
    Context context; MediaPlayer mp;

    String flag = "english";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

          v = inflater.inflate(R.layout.broadcast_list, null);

        getLiveUser();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getLiveUser();
            }
        }, 0, 10000);//put here time 1000 milliseconds=1 second


     //   userlist();
       // getLiveUser();
        setTouchNClick(v.findViewById(R.id.tab1));
      //  setTouchNClick(v.findViewById(R.id.tab2));
     setTouchNClick(v.findViewById(R.id.btnNewChat1));
        setTouchNClick(v.findViewById(R.id.toggle));



        return v;
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.tab1)
        {
            getView().findViewById(R.id.tab2).setEnabled(true);
            v.setEnabled(false);
        }
//        else if (v.getId() == R.id.tab2)
//        {
//            getView().findViewById(R.id.tab1).setEnabled(true);
//            v.setEnabled(false);
//        }
//        else if (v.getId() == R.id.btnNewChat) {
//            startActivity(new Intent(getActivity(), ChatView.class));
//        }
        else if (v.getId() == R.id.btnNewChat1){

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Select Language");
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {


                    switch(item)
                    {
                        case 0:

                            Log.d("English","1");
                            // Your code when first option seletced

                            flag = "english";
                            break;

                        case 1:
                            // Your code when 2nd  option seletced
                            Log.d("Spanish","2");
                            flag = "spanish";
                            break;
                        case 2:
                            // Your code when 3rd option seletced
                            Log.d("French","3");
                            flag = "french";
                            break;


                        case 3:
                            // Your code when 4th  option seletced
                            flag = "hindi";
                            break;

                        case 4:
                            flag = "german";
                            break;

                        case 5:
                            flag = "chinese";
                            break;

                        case 6:
                            flag = "arabic";
                            break;

                        case 7:
                            flag = "russian";
                            break;
                        default:
                            flag = "english";
                            break;


                    }
                    changelanguage(flag);

                    levelDialog.dismiss();
                }
            });
            levelDialog = builder.create();
            levelDialog.show();



          //  MediaPlayer mp1 = MediaPlayer.create(getActivity().getBaseContext(),R.raw.french);
           // mp1.start();
            //ParseUser user = ParseUser.getCurrentUser();
            //user.put("sttaus","online");
            //Toast.makeText(getActivity().getBaseContext(),"Hello",Toast.LENGTH_LONG).show();

            //startActivity(new Intent(getActivity(), NewChat.class));
        }

        else if(v.getId() == R.id.toggle){
            ToggleButton togglebutton = (ToggleButton) v.findViewById(R.id.toggle);

            if(togglebutton.isChecked()){
                ToggleStatusLive();

//                ParseUser user =    ParseUser.getCurrentUser();
//                user.setACL(new ParseACL(user));
//                user.put("sttaus","online");
//                user.saveEventually();
//                try {
//                    user.save();
//                } catch (ParseException e) {
//
//                    //e.printStackTrace();
//                }
                Resources res = getResources();
                R5Configuration config = new R5Configuration(R5StreamProtocol.RTSP, res.getString(R.string.domain), res.getInteger(R.integer.port), res.getString(R.string.context), 0.5f);
                R5Connection connection = new R5Connection(config);

                //setup a new stream using the connection
                publish = new R5Stream(connection);

                //show all logging
                publish.setLogLevel(R5Stream.LOG_LEVEL_DEBUG);

                //attach a camera video source
                cam = openFrontFacingCameraGingerbread();
                cam.setDisplayOrientation(90);

//        R5Camera camera = new R5Camera(cam, 320, 240);
//        camera.setBitrate(res.getInteger(R.integer.bitrate));
//        camera.setOrientation(cameraOrientation);


                //attach a microphone
                R5Microphone mic = new R5Microphone();

                publish.attachMic(mic);

//        R5VideoView r5VideoView = (R5VideoView) this.findViewById(R.id.video2);
//        //r5VideoView.attachStream(publish);
//        r5VideoView.showDebugView(res.getBoolean(R.bool.debugView));

                //publish.attachCamera(camera);

                publish.publish(getStream1(), R5Stream.RecordType.Live);

                cam.startPreview();
                //startActivity(new Intent(getActivity(), NewChat.class));
            }
            else{
                if (publish != null) {
                    setUserOffline();
//                    ParseUser user =    ParseUser.getCurrentUser();
//                    user.put("sttaus","offline");
//		            user.saveEventually();
//                    try {
//                        user.save();
//                    } catch (ParseException e) {
//
//                        //e.printStackTrace();
//                    }
                    publish.stop();
                }

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
           // stream.attachCamera(camera);
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
                    //applyDeviceRotation();
                } catch (RuntimeException e) {
                    Log.e("R5 Test Activity", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }

    void ToggleStatusLive(){
        final String currentuserid = ParseUser.getCurrentUser().getObjectId();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(className);
        query.whereEqualTo("userid",currentuserid);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size()>0){
                        ParseObject obj = objects.get(0);
                        String userstatus = obj.get("status").toString();
                        if(userstatus.equals("online")){
                            obj.put("status","online");
                            obj.saveInBackground();
                        }
                        else{
                            obj.put("status","online");
                            obj.saveInBackground();
                        }
                    }
                    else{
                        ParseObject obj1 = new ParseObject(className);
                        obj1.put("userid",currentuserid);
                        obj1.put("status","online");
                        obj1.put("language",flag);
                        obj1.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                            }
                        });
                    }
                }
            }
        });
    }

    void setUserOffline(){
        final String currentuserid = ParseUser.getCurrentUser().getObjectId();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(className);
        query.whereEqualTo("userid",currentuserid);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size()>0){
                        ParseObject obj = objects.get(0);
                        String userstatus = obj.get("status").toString();
                        if(userstatus.equals("online")){
                            obj.put("status","offline");
                            obj.saveInBackground();
                        }
                        else{
                            obj.put("status","offline");
                            obj.saveInBackground();
                        }
                    }
                    else{
                        ParseObject obj1 = new ParseObject(className);
                        obj1.put("userid",currentuserid);
                        obj1.put("status","offline");
                        obj1.put("language",flag);
                        obj1.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                            }
                        });
                    }
                }
            }
        });
    }


    void changelanguage(String lang){

        final String currentuserid = ParseUser.getCurrentUser().getObjectId();
        final String lang1 = lang;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(className);
        query.whereEqualTo("userid",currentuserid);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                        if(objects.size()>0){
                            ParseObject objs = objects.get(0);
                            objs.put("language",lang1);
                            objs.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    Log.d("CHANGED LANG",lang1);
                                }
                            });
                        }
                    else{
                            ParseObject obj2 = new ParseObject(className);
                            obj2.put("userid",currentuserid);
                           // obj2.put("status","online");
                            obj2.put("language",lang1);
                            obj2.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    Log.d("CHANGED LANG",lang1);
                                }
                            });
                        }
                }
            }
        });
    }


    private void getLiveUser() {
        if (isFetchingStatus ) {
            return;
        } else {

                isFetchingStatus = true;
            final String currentuserid = ParseUser.getCurrentUser().getObjectId();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserLanguage");
            query.whereEqualTo("status", "online");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    i = objects.size();
                    Log.d("I ki value", String.valueOf(i));
                    if (e == null) {

                        if (objects.size() > 0) {
                            braodcastLists = new ArrayList<BroadcastItem>();
                            for (final ParseObject obj : objects) {
                                String userid = obj.get("userid").toString();



                                Log.d("userid", userid);
                                final String lang = obj.get("language").toString();

                                Log.d("I ki value", String.valueOf(i));
                                ParseQuery<ParseUser> fetchuser = ParseUser.getQuery();
                                fetchuser.whereEqualTo("objectId", userid);
                                fetchuser.findInBackground(new FindCallback<ParseUser>() {
                                    @Override
                                    public void done(List<ParseUser> objects, ParseException e) {
                                        if (e == null) {


                                            if (objects.size() > 0) {
                                                ParseUser obj_fetch = (ParseUser) objects.get(0);// ParseObject.create("_User");
                                                String _uname = obj_fetch.get("username").toString();//"Yuvraj"; //obj_fetch.get("username").toString();
                                                String title = obj_fetch.get("username").toString();
                                                String icon = lang;
                                                obj_fetch.get("country").toString();
                                                String third = obj_fetch.get("Third").toString();

                                                boolean online = true;
                                                DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                                Date date = obj_fetch.getUpdatedAt();
                                                String dateF = df.format(date).toString();
                                                BroadcastItem user = new BroadcastItem(_uname, title, icon, online, dateF, third, lang);
                                                Log.d("FINAL LOG", user.toString());

                                                if(!obj_fetch.getObjectId().equals(currentuserid))
                                                braodcastLists.add(user);

                                                i--;
                                                if (i == 0) {
                                                    userlist3();
                                                    isFetchingStatus = false;
                                                    return;
                                                }


                                            } else {
                                                Log.d("No Value", String.valueOf(i));
                                            }
                                        }
                                    }
                                });


                            }
                        } else {
                            isFetchingStatus = false;
                            //Toast.makeText(getActivity().getBaseContext(), "No Users Online", Toast.LENGTH_LONG).show();
                            userlist3();

                        }
                    }
                }
            });
        }
    }


    private void userlist()
    {

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("sttaus","online");
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().toString());
//        query.whereEqualTo("username",ParseUser.getCurrentUser());
        //query.whereExists("sttaus");
        query.findInBackground(new FindCallback<ParseUser>() {
            //public ArrayList<BroadcastItem> braodcastLists1;

            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null)
                {




                    for(ParseUser parseUser:parseUsers){

                          String name = parseUser.get("username").toString();
                          String title = parseUser.get("username").toString();
                        String icon = parseUser.get("country").toString();
                        String third = parseUser.get("Third").toString();
                        String language = parseUser.get("language").toString();
                        Log.d("Country",icon);
                          //int icon = 1;
                          boolean online;
                        String status = parseUser.get("sttaus").toString();

                        if (status.equals("online")){online = true;}else{online = false;}
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        Date date =  parseUser.getUpdatedAt();
                        String dateF = df.format(date).toString();
                        BroadcastItem user = new BroadcastItem(name,title,icon,online,dateF,third,language);

                        braodcastLists.add(user);

                    }
                    final ListView list = (ListView) v.findViewById(R.id.list);

                    list.setAdapter(new BroadcastAdapter());
                   // final MediaPlayer mp;
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                           // Toast.makeText(getActivity().getBaseContext(),position,Toast.LENGTH_LONG).show();
//                            Subscribe sub = new Subscribe();
//                            Bundle bundle = new Bundle();
//                            bundle.putString("userid",braodcastLists.get(position).getName() );
//
//                            sub.setArguments(bundle);

                          //    MediaPlayer mp =  MediaPlayer.create(getActivity().getBaseContext(),R.raw.french);
                          //  mp.start();
//                                switch (position){
//                                    case 0:
//                                        mp = MediaPlayer.create(getActivity().getBaseContext(),R.raw.french);
//                                        mp.start();
//                                        break;
//
//                                    case 1:
//                                        mp = MediaPlayer.create(getActivity().getBaseContext(),R.raw.spanish);
//                                        mp.start();
//                                        break;
//                                }




//                                getFragmentManager().beginTransaction()
//                                    .replace(R.id.content_frame,sub )
//
//                                    .addToBackStack("Live Video").commit();

                        }
                    });


                }
                else{


                }
            }
        });

    }

    private void userlist2()
    {




        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
       // query1.whereEqualTo("sttaus","offline");
        query1.whereEqualTo("active","1");
        query1.whereNotEqualTo("username", ParseUser.getCurrentUser().toString());

        //query.whereExists("sttaus");
        query1.findInBackground(new FindCallback<ParseUser>() {
            //public ArrayList<BroadcastItem> braodcastLists1;

            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null)
                {


                    offlinelist = new ArrayList<BroadcastItem>();


                    for(ParseUser parseUser:parseUsers){



                        String name = parseUser.get("username").toString();
                        String title = parseUser.get("subname").toString();
                        final String icon = parseUser.get("country").toString();
                        String third = parseUser.get("Third").toString();
                        String language = parseUser.get("language").toString();
                        Log.d("Country",language);
                       // int icon = 1;
                        boolean online;
                        String status = parseUser.get("sttaus").toString();

                        if (status.equals("online")){online = true;}else{online = false;}
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        Date date =  parseUser.getUpdatedAt();
                        String dateF = df.format(date).toString();
                        BroadcastItem user = new BroadcastItem(name,title,icon,online,dateF,third,language);


                        offlinelist.add(user);



                    }
                    final ListView list2 = (ListView) v.findViewById(R.id.list2);

                    list2.setAdapter(new BroadcastAdapter2());


                    list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            for (int j = 0; j < parent.getChildCount(); j++) {

                                if(j == position){
                                    view.setBackgroundResource(R.color.main_color_green);

                                     Log.d("Language",flag);


                                    Toast.makeText(getActivity(), flag, Toast.LENGTH_SHORT).show();



                                }else{

                                    parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                                }
                            }

                            switch (position){
                                case 0:

                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("UserLanguage");


// Retrieve the object by id
                                    query.getInBackground("8Vdi4XSovf", new GetCallback<ParseObject>() {
                                        public void done(ParseObject gameScore, ParseException e) {
                                            if (e == null) {
                                                // Now let's update it with some new data. In this case, only cheatMode and score
                                                // will get sent to the Parse Cloud. playerName hasn't changed.
                                                gameScore.put("language", flag);
                                                gameScore.put("status", "online");
                                                gameScore.saveInBackground();
                                            }
                                        }
                                    });
                                    //parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                                    if(mp != null){
                                        mp.pause();
                                        int seek = mp.getCurrentPosition();
                                        if(mpIndex == 0)
                                            seek1=seek;
                                        if(mpIndex == 1)
                                            seek2=seek;
                                        if(mpIndex == 2)
                                            seek3=seek;

                                                    mp.stop();
                                    }
                                    mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.spanish);
                                    mpIndex = 0;
                                    mp.seekTo(seek1+10000);
                                    mp.start();
                                    mp.setVolume(1.0f,1.0f);
//                                    if(mp.isPlaying()){
//                                        mp.pause();
//                                    }

                                    break;
                                case 1:
                                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("UserLanguage");

// Retrieve the object by id
                                    query1.getInBackground("bA1qkDcenp", new GetCallback<ParseObject>() {
                                        public void done(ParseObject gameScore, ParseException e) {
                                            if (e == null) {
                                                // Now let's update it with some new data. In this case, only cheatMode and score
                                                // will get sent to the Parse Cloud. playerName hasn't changed.
                                                gameScore.put("language", flag);
                                                gameScore.put("status", "online");
                                                gameScore.saveInBackground();
                                            }
                                        }
                                    });
                                    if(mp != null){
                                        int seek = mp.getCurrentPosition();
                                        if(mpIndex == 0)
                                            seek1=seek;
                                        if(mpIndex == 1)
                                            seek2=seek;
                                        if(mpIndex == 2)
                                            seek3=seek;
                                        mp.stop();
                                    }
                                    mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.english);
                                    mpIndex = 1;
                                    mp.seekTo(seek2+10000);
                                    mp.start();
                                    mp.setVolume(1.0f,1.0f);
//                                    if(mp.isPlaying()){
//                                        mp.pause();
//                                    }
                                    break;
                                case 2:
                                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("UserLanguage");

// Retrieve the object by id
                                    query2.getInBackground("PLiDEIAGhc", new GetCallback<ParseObject>() {
                                        public void done(ParseObject gameScore, ParseException e) {
                                            if (e == null) {
                                                // Now let's update it with some new data. In this case, only cheatMode and score
                                                // will get sent to the Parse Cloud. playerName hasn't changed.
                                                gameScore.put("language", flag);
                                                gameScore.put("status", "online");
                                                gameScore.saveInBackground();
                                            }
                                        }
                                    });
                                    if(mp != null){
                                        int seek = mp.getCurrentPosition();
                                        if(mpIndex == 0)
                                            seek1=seek;
                                        if(mpIndex == 1)
                                            seek2=seek;
                                        if(mpIndex == 2)
                                            seek3=seek;
                                        mp.stop();
                                    }
                                    mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.french);
                                    mpIndex = 2;
                                    mp.seekTo(seek3+10000);
                                    mp.start();
                                    mp.setVolume(1.0f,1.0f);
//                                    if(mp.isPlaying()){
//                                        mp.pause();
//                                    }
                                    break;

                            }


                                // change the background color of the selected element
                                //view.setBackgroundResource(R.color.main_color_green);


                                //parent.getChildAt(j).setSoundEffectsEnabled(true);

                          //  }

                            //Subscribe sub = new Subscribe();
                            //Bundle bundle = new Bundle();
                            //bundle.putString("userid",offlinelist.get(position).getName() );
                            //list2.setEnabled(false);
                            //sub.setArguments(bundle);
                           // list2.setEnabled(false);

//                            getFragmentManager().beginTransaction()
//                                    .replace(R.id.content_frame,sub )
//
//                                    .addToBackStack("Live Video").commit();

                        }
                    });


                }
                else{


                }
            }
        });

    }

    private void userlist3()
    {




        if((braodcastLists != null) && (braodcastLists.size() > 0)) {

            list2 = (ListView) v.findViewById(R.id.list2);

            list2.setAdapter(new BroadcastAdapter2());


            list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    BroadcastItem obj = (BroadcastItem) list2.getAdapter().getItem(position);
                    // String value =
                    // Log.d("MyLog", "Value is: "+value);
                    String name = // how code to get name value.
                            currentStreamName = obj.getName();


                    for (int j = 0; j < parent.getChildCount(); j++) {

                        if (j == position) {
                            view.setBackgroundResource(R.color.main_color_green);

                            Log.d("Language", flag);


                            Toast.makeText(getActivity(), flag, Toast.LENGTH_SHORT).show();


                        } else {

                            parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                    if (subscribe != null) {
                        subscribe.stop();
                    }
                    subscribe = getNewStream(0);
                    subscribe.play(currentStreamName);


                }
            });


        }else{


            if(list2 != null){


                list2.invalidateViews();
                currentStreamName = "";
                if (subscribe != null) {
                    subscribe.stop();
                }
            }
        }

    }


    private class BroadcastAdapter extends BaseAdapter{

        @Override
        public int getCount() {

            return braodcastLists.size();

        }

        @Override
        public BroadcastItem getItem(int position) {
            return braodcastLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int pos, View v, ViewGroup parent) {
            if (v == null)
                v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.chat_item, null);

            BroadcastItem c = getItem(pos);
            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            lbl.setText(c.getName());

//            lbl = (TextView) v.findViewById(R.id.lbl2);
//            lbl.setText(c.getDate());

            lbl = (TextView) v.findViewById(R.id.lbl3);
            lbl.setText(c.getTitle());

            lbl = (TextView)v.findViewById(R.id.lbl4);
            lbl.setText(c.getThird());
            ImageView img = (ImageView) v.findViewById(R.id.img1);
            //img.setImageResource(R.drawable.user2);
            String imagename = c.getIcon().toString();
            //img.setImageResource(c.getIcon());
            // String imagename = ParseUser.getCurrentUser().get("avtar").toString();
            if(imagename.equals("peru"))
            {

                    img.setImageResource(R.drawable.peru);


            }
            else if(imagename.equals("america"))
            {
                img.setImageResource(R.drawable.american);
            }
            else if(imagename.equals("france"))
            {
                img.setImageResource(R.drawable.france);
            }
            else {
                img.setImageResource(R.drawable.user1);
            }
            //img.setImageResource(R.drawable.user1);



            img = (ImageView) v.findViewById(R.id.online);
            img.setVisibility(c.isOnline() ? View.VISIBLE : View.INVISIBLE);
            return v;
        }
    }
    private class BroadcastAdapter2 extends BaseAdapter{

        @Override
        public int getCount() {

            if(braodcastLists.size() == 0){
                return 0;
            }
            else{
                return braodcastLists.size();

            }

        }

        @Override
        public BroadcastItem getItem(int position) {
            return braodcastLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int pos, View v, ViewGroup parent) {
            if (v == null)
                v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.chat_item, null);

            BroadcastItem c = getItem(pos);
            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            lbl.setText(c.getName());
            if(currentStreamName!=null)
           if(currentStreamName.equals(c.getName())){
//                v.setSelected(true);
               v.setBackgroundResource(R.color.main_color_green);
            }

//            lbl = (TextView) v.findViewById(R.id.lbl2);
//            lbl.setText(c.getDate());

            lbl = (TextView) v.findViewById(R.id.lbl3);
            lbl.setText(c.getTitle());

            lbl = (TextView)v.findViewById(R.id.lbl4);
            lbl.setText(c.getThird());

            ImageView img = (ImageView) v.findViewById(R.id.img1);
            //img.setImageResource(R.drawable.user2);
            String imagename = c.getIcon().toString();

            String lang = c.getLanguage();

            //img.setImageResource(c.getIcon());

           // String imagename = ParseUser.getCurrentUser().get("avtar").toString();
            //    final CharSequence[] items = {" English "," Spanish "," French "," Hindi ", "German", "Chinese", "Arabic" ,"Russian"};

            if(lang.equals("english") )
            {
                img.setImageResource(R.drawable.american);
            }
            else if(lang.equals("spanish"))
            {
                img.setImageResource(R.drawable.spanish);
            }
            else if(lang.equals("french"))
            {
                img.setImageResource(R.drawable.france);
            }
            else if(lang.equals("hindi")) {
                img.setImageResource(R.drawable.hindi);
            } else if(lang.equals("chinese")) {
                img.setImageResource(R.drawable.chinese);
            } else if(lang.equals("arabic")) {
                img.setImageResource(R.drawable.arabic);
            }else if(lang.equals("german")) {
                img.setImageResource(R.drawable.german);
            }
            else if(lang.equals("russian")) {
                img.setImageResource(R.drawable.russia);
            }else {
                img.setImageResource(R.drawable.american);


            }







            img = (ImageView) v.findViewById(R.id.online);
            img.setVisibility(c.isOnline() ? View.VISIBLE : View.INVISIBLE);
            return v;
        }
        private int selectedItem;

        public void setSelectedItem(int position) {
            selectedItem = position;
        }
    }
}
