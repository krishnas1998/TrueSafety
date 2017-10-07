package com.example.krishna.letsshield;


import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krishna.letsshield.Model.Location;
import com.example.krishna.letsshield.Model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBarTab;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by krishna on 12/9/17.
 */

public class FirstFragment extends Fragment {
    private  View view = null;
    Button btnShowLocation;
    TextView longtitudeTextView;
    TextView lattitudeTextView;
    private final String TAG = "this";
    private Post post;
    private double latitude ;
    private double longitude;
    private DatabaseReference mPostReference,mDatabase;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;


    Button sendButton;
    // GPSTracker class
    GPSTracker gps;
    BottomBarTab nearby;
    public static int count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_location, container, false);

        }
        nearby = MainActivity.bottomBar.getTabWithId(R.id.location_history);
        longtitudeTextView = (TextView)view.findViewById(R.id.longitudetextview);
        lattitudeTextView = (TextView)view.findViewById(R.id.latitudetextview);
        btnShowLocation = (Button)view. findViewById(R.id.btnShowLocation);
        sendButton = (Button)view.findViewById(R.id.sendButton);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final String userId =mFirebaseUser.getUid();;
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("contact");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(getContext());

                // check if GPS enabled
                if(gps.canGetLocation()){

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    String latitudeString = String.valueOf(latitude);
                    lattitudeTextView.setText(String.valueOf(latitude));
                    longtitudeTextView.setText(String.valueOf(longitude));


                    // \n is for new line
                    Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLocation();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        gps = new GPSTracker(getContext());

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            String latitudeString = String.valueOf(latitude);
            lattitudeTextView.setText(String.valueOf(latitude));
            longtitudeTextView.setText(String.valueOf(longitude));


            // \n is for new line
            Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                post = dataSnapshot.getValue(Post.class);
                //Log.d(TAG,post.Number1 + " " + post.Number2 + " " + post.Number3 +" " + post.Number4 +" " + post.Number5);
                if (post == null)
                {
                    Toast.makeText(getContext(),"Kindly Update Your Information First",Toast.LENGTH_SHORT).show();
                    return;
                }
               // Toast.makeText(getContext(),post.Number1,Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mPostReference.addValueEventListener(postListener);
    }



    public static FirstFragment newInstance()
    {
        FirstFragment f = new FirstFragment();
        return f;
    }
    public void sendLocation() {




       // PendingIntent pi = PendingIntent.getActivity(this, 0,
       //         new Intent(this, AndroidGPSTrackingActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        //https://www.google.com/maps/search/?api=1&query=47.5951518,-122.3316393
        if (post ==null)
        {
            Toast.makeText(getContext(),"Kindly Update Your Information First",Toast.LENGTH_SHORT).show();
            return;
        }

        sms.sendTextMessage(post.Number1, null,"https://www.google.com/maps/search/?api=1&query="+latitude +"," +longitude, null, null);
        nearby.setBadgeCount(++count);

        writeNewPost(mFirebaseUser.getUid(),new Location(latitude,longitude));
    }

    private void writeNewPost(String userId,Location loc) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
       // String key = mDatabase.child("posts").push().getKey();
       // Post post = new Post(userId, numbers);
       // Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/posts/" + key, postValues);
        // childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
        String key = mDatabase.child(userId).child("location").push().getKey();
        mDatabase.child("users").child(userId).child("count").setValue(count);
        mDatabase.child("users").child(userId).child("location").child(key).setValue(loc);
        //mDatabase.child("users").child(userId).child("location").child(key).setValue(latit);


        mDatabase.updateChildren(childUpdates);
    }



}
