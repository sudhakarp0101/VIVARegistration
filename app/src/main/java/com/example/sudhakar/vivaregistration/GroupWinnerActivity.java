package com.example.sudhakar.vivaregistration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.sudhakar.vivaregistration.MainActivity.MyPREFERENCES;

public class GroupWinnerActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_group_winner);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        TextView eventText=(TextView)findViewById(R.id.eventtitle);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
// final SharedPreferences pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String category=sharedpreferences.getString("event", "0")+"_"
                +sharedpreferences.getString("subevent", "0")+"_"
                +sharedpreferences.getString("subsubevent", "0")+"_"
                +sharedpreferences.getString("finalsubevent", "0");
        eventText.setText((sharedpreferences.getString("event", "0")+" "
                +sharedpreferences.getString("subevent", "0")+" "
                +sharedpreferences.getString("subsubevent", "0")+" "
                +sharedpreferences.getString("finalsubevent", "0")+" Event Winners"));
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
                    startActivity(new Intent(GroupWinnerActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        //final String[] fruits = {"Apple", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango", "Pear"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getApplicationContext(), R.layout.spinner_item);
        myRef.child(category).addValueEventListener(new ValueEventListener() {
            int i=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot rollNo:dataSnapshot.getChildren()){
                    adapter.add(rollNo.getKey());
                    //Toast.makeText(getApplicationContext(), rollNo.getKey(), Toast.LENGTH_SHORT).show();
                }
                //Creating the instance of ArrayAdapter containing list of fruit names

                //Getting the instance of AutoCompleteTextView

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final AutoCompleteTextView actvf = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewFirst);
        final AutoCompleteTextView actvs = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewSecond);

        actvf.setThreshold(1);//will start working from first character
        actvf.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actvf.setTextColor(Color.RED);

        actvs.setThreshold(1);//will start working from first character
        actvs.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actvs.setTextColor(Color.RED);

        final ArrayList<Student> first=new ArrayList<Student>();
        final ArrayList<Student> second=new ArrayList<Student>();

        submit = (Button) findViewById(R.id.btn_submit);
        final TextView winnerstv=(TextView)findViewById(R.id.winners_tv);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child(category).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       // Toast.makeText(getApplicationContext(), dataSnapshot.toString(), Toast.LENGTH_SHORT).show();

                        for(DataSnapshot mobileNo:dataSnapshot.getChildren()) {
                            if (actvf.getText().toString().equals(mobileNo.getKey())) {
                                for(DataSnapshot rollNo:mobileNo.getChildren()) {
                                    Student st = rollNo.getValue(Student.class);
                                    first.add(st);
                                }
                            }
                            else if(actvs.getText().toString().equals(mobileNo.getKey())){
                                for(DataSnapshot rollNo:mobileNo.getChildren()) {
                                    Student st = rollNo.getValue(Student.class);
                                    second.add(st);
                                }}
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(first.size()>0) {

                    for (int i = 0; i < first.size(); i++){
                        myRef.child(sharedpreferences.getString("event", "0")).
                                child(sharedpreferences.getString("subevent", "0")).
                                child(sharedpreferences.getString("subsubevent", "0")).
                                child(sharedpreferences.getString("finalsubevent", "0")).
                                child("winners").child("first").child(Integer.toString(i)).setValue(first.get(i));
                }
                    for (int i = 0; i < second.size(); i++) {

                        myRef.child(sharedpreferences.getString("event", "0")).
                                child(sharedpreferences.getString("subevent", "0")).
                                child(sharedpreferences.getString("subsubevent", "0")).
                                child(sharedpreferences.getString("finalsubevent", "0")).
                                child("winners").child("second").child(Integer.toString(i)).setValue(second.get(i));
                    }
                    Toast.makeText(getApplicationContext(), "Winners List Updated", Toast.LENGTH_SHORT).show();
//                    winnerstv.setText(first.get(0).toString()+"\n"+first.get(1).toString());
                    // startActivity(new Intent(getApplicationContext(), WinnerSelectionActivity.class));
                    //finish();
                }
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
                startActivity(new Intent(GroupWinnerActivity.this, WinnerSelectionActivity.class));
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
