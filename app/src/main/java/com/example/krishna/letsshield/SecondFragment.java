package com.example.krishna.letsshield;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.krishna.letsshield.Model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by krishna on 12/9/17.
 */

public class SecondFragment extends Fragment {
    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";
    private static final String NUMBERLENGTH = "Not a valid Number";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;

    private EditText[] mNumber = new EditText[5];
    private FloatingActionButton mSubmitButton;
    private Button pickContact;
    static final int PICK_CONTACT=1;
    static final int PERMISSION_REQUEST_CONTACT = 99;

    int num;
    static int i = 0;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_new_post, container, false);

        }
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final String userId = mFirebaseUser.getUid();


        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mNumber[0] = (EditText) view.findViewById(R.id.number1);
        mNumber[1] = (EditText) view.findViewById(R.id.number2);
        mNumber[2] = (EditText) view.findViewById(R.id.number3);
        mNumber[3] = (EditText) view.findViewById(R.id.number4);
        mNumber[4] = (EditText) view.findViewById(R.id.number5);
        pickContact = (Button)view.findViewById(R.id.pickContactButton);
        mSubmitButton = (FloatingActionButton) view.findViewById(R.id.fab_submit_post);


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();

            }
        });
        pickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForContactPermission();
            }
        });
        return view;

    }

    private void submitPost() {
        final String[] number = new String[5];
        number[0] = mNumber[0].getText().toString();
        number[1] = mNumber[1].getText().toString();
        number[2] = mNumber[2].getText().toString();
        number[3] = mNumber[3].getText().toString();
        number[4] = mNumber[4].getText().toString();

        /*try {

            for( i = 0; i<5; i++)
            num = Integer.parseInt(number[i]);
            Log.i("",num+" is a number");
        } catch (NumberFormatException e) {
            mNumber[i].setError("Not a Number");
            Log.i("",number[i]+" is not a number");
            return;
        }*/


        for(i=0; i<5;i++)
        {
            if (TextUtils.isEmpty(number[i])) {
                mNumber[i].setError(REQUIRED);
                return;
            }
            if(number[i].length()!=10)
            {
                mNumber[i].setError(NUMBERLENGTH);
                return;
            }

        }

        // Title is required
       /* if (TextUtils.isEmpty(number[0])) {
            mNumber[0].setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(number[1])) {
            mNumber[1].setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(number[2])){
         mNumber[2].setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(number[3])){
            mNumber[3].setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(number[4])){
            mNumber[4].setError(REQUIRED);
            return;
        }*/


        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(getContext(), "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = mFirebaseUser.getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                       /* User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(NewPostActivity2.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                        */   // writeNewPost(userId, user.username, title, body);
                        writeNewPost(userId,number);
                        // }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();

                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }
    private void setEditingEnabled(boolean enabled) {
        mNumber[0].setEnabled(enabled);
        mNumber[1].setEnabled(enabled);
        mNumber[2].setEnabled(enabled);
        mNumber[3].setEnabled(enabled);
        mNumber[4].setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String numbers[]) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, numbers);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/posts/" + key, postValues);
        // childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
        mDatabase.child("users").child(userId).child("contact").setValue(post);


        mDatabase.updateChildren(childUpdates);
    }

    public void pickContact() {

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);




    }

    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                pickContact();
            }
        }
        else{
            pickContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickContact();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(getContext(),"No permission for contacts",Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        String cNumber=null;

        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c = getContext().getContentResolver().query(contactData, null, null, null, null);
                    // c.moveToFirst();

                    if (c.moveToFirst()) {


                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContext().getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                            cNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:"+cNumber);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                    }
                }
                break;
        }
        //  Toast.makeText(this,"No : "+cNumber,Toast.LENGTH_SHORT).show();
        cNumber = cNumber.trim().replaceAll("\\s+","");
        if (cNumber.length() == 10) {

        } else if (cNumber.length() > 10) {
            cNumber= cNumber.substring(cNumber.length() - 10);
        } else {
            // whatever is appropriate in this case
            throw new IllegalArgumentException("word has less than 3 characters!");
        }
        mNumber[i].setText(cNumber);
        i++;

    }

    public static SecondFragment newInstance()
    {
        SecondFragment f = new SecondFragment();
        return f;
    }
}
