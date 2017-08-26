package com.example.krishna.letsshield;

/**
 * Created by krishna on 7/3/17.
 */

        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.example.krishna.letsshield.Model.Post;
        import com.example.krishna.letsshield.Model.User;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.HashMap;
        import java.util.Map;

public class NewPostActivity2 extends BaseActivity {

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
    int num,i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final String userId = mFirebaseUser.getUid();

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mNumber[0] = (EditText) findViewById(R.id.number1);
        mNumber[1] = (EditText) findViewById(R.id.number2);
        mNumber[2] = (EditText) findViewById(R.id.number3);
        mNumber[3] = (EditText) findViewById(R.id.number4);
        mNumber[4] = (EditText) findViewById(R.id.number5);
        mSubmitButton = (FloatingActionButton) findViewById(R.id.fab_submit_post);


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();

            }
        });
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
                mNumber[i].setError(NUMBERLENGTH);
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
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

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
                        finish();
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
    // [END write_fan_out]
}
