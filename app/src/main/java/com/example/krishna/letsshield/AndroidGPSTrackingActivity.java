package com.example.krishna.letsshield;

/**
 * Created by krishna on 7/2/17.
 */

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krishna.letsshield.Model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AndroidGPSTrackingActivity extends Activity {

    Button btnShowLocation;
    TextView longtitudeTextView;
    TextView lattitudeTextView;
    private final String TAG = "this";
    private Post post;
    private double latitude ;
    private double longitude;
    private DatabaseReference mPostReference;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;

    // GPSTracker class
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        longtitudeTextView = (TextView)findViewById(R.id.longitudetextview);
        lattitudeTextView = (TextView)findViewById(R.id.latitudetextview);
        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final String userId = mFirebaseUser.getUid();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("contact");

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(AndroidGPSTrackingActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    String latitudeString = String.valueOf(latitude);
                    lattitudeTextView.setText(String.valueOf(latitude));
                    longtitudeTextView.setText(String.valueOf(longitude));


                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // create class object
        gps = new GPSTracker(AndroidGPSTrackingActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            String latitudeString = String.valueOf(latitude);
            lattitudeTextView.setText(String.valueOf(latitude));
            longtitudeTextView.setText(String.valueOf(longitude));


            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
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
                Log.d(TAG,post.Number1 + " " + post.Number2 + " " + post.Number3 +" " + post.Number4 +" " + post.Number5);
                Toast.makeText(AndroidGPSTrackingActivity.this,post.Number1,Toast.LENGTH_SHORT).show();
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

    public void sendLocation(View view) {




        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, AndroidGPSTrackingActivity.class), 0);
        SmsManager sms = SmsManager.getDefault();
        //https://www.google.com/maps/search/?api=1&query=47.5951518,-122.3316393

        sms.sendTextMessage("8826013933", null,"https://www.google.com/maps/search/?api=1&query="+latitude +"," +longitude, pi, null);
    }
}