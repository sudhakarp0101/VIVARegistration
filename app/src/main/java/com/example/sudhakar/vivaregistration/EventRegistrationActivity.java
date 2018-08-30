package com.example.sudhakar.vivaregistration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.sudhakar.vivaregistration.MainActivity.MyPREFERENCES;

public class EventRegistrationActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private Button submit;
    FirebaseDatabase database;
    DatabaseReference myRef;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_registration);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        TextView eventText=(TextView)findViewById(R.id.eventtitle);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(!sharedpreferences.getString("event","0").equals("solo")) {
            startActivity(new Intent(getApplicationContext(), GroupRegistrationActivity.class));
            finish();
        }
       // final SharedPreferences pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String category=sharedpreferences.getString("event", "0")+"_"
                +sharedpreferences.getString("subevent", "0")+"_"
                +sharedpreferences.getString("subsubevent", "0")+"_"
                +sharedpreferences.getString("finalsubevent", "0");
        eventText.setText(category.toUpperCase());
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(EventRegistrationActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        final Spinner bdynamicSpinner = (Spinner) findViewById(R.id.branch_spinner);
        final String[] bitems = {"Select Branch","CE", "CSE", "ECE","EEE","IT","ME","MCA","M.Tech","Other"};
        ArrayAdapter<String> badapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bitems);
        bdynamicSpinner.setAdapter(badapter);

        final Spinner ydynamicSpinner = (Spinner) findViewById(R.id.year_spinner);
        final String[] yitems = {"Select Year","I Year", "II Year", "III Year","IV Year"};
        ArrayAdapter<String> yadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yitems);
        ydynamicSpinner.setAdapter(yadapter);

        submit = (Button) findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String branch=bdynamicSpinner.getSelectedItem().toString();
                final String year=ydynamicSpinner.getSelectedItem().toString();
                final String rollNo=((TextView)findViewById(R.id.rollno_ed)).getText().toString();
                final String name=((TextView)findViewById(R.id.name_ed)).getText().toString();
                final String college=((TextView)findViewById(R.id.college_ed)).getText().toString();
                final String mobile=((TextView)findViewById(R.id.mobile_ed)).getText().toString();

                if (branch.equals("Select Branch") ) {
                    Toast.makeText(getApplicationContext(), "Select Branch!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (year.equals("Select Year")) {
                    Toast.makeText(getApplicationContext(), "Select Year!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(rollNo)){
                    Toast.makeText(getApplicationContext(), "Enter Roll Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(college)){
                    Toast.makeText(getApplicationContext(), "Enter College", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(mobile)){
                    Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                Student student=new Student(branch,year,rollNo,name,college,mobile);
                myRef.child(category).child(rollNo).setValue(student);
                myRef.child(category).child(rollNo).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot!=null){
                            Toast.makeText(getApplicationContext(), rollNo+" Stored", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                startActivity(new Intent(getApplicationContext(), EventRegistrationActivity.class));
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.maps,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                signOut();
                return true;
            case R.id.menu_ce:
                startActivity(new Intent(EventRegistrationActivity.this, Main2Activity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
