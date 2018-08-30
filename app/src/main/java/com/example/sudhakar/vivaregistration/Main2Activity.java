package com.example.sudhakar.vivaregistration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class Main2Activity extends AppCompatActivity {
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private Button submit;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //myRef.child("viva").setValue("school by vvit");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //   final SharedPreferences pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(sharedpreferences.getString("event","0"));
        TextView titletv=(TextView)findViewById(R.id.main2title);
        titletv.setText(sharedpreferences.getString("event","0").toUpperCase()+"_"+sharedpreferences.getString("subevent","0").toUpperCase());
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
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        };
      //  final String[] mainevents;
        // Read from the database
        final Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);
        final Spinner subsubdynamicSpinner = (Spinner) findViewById(R.id.subsub_spinner);

        // final String[] mainevents={"Select Event","solo", "group"};

        myRef.child(sharedpreferences.getString("subevent","0")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int noe=(int)dataSnapshot.getChildrenCount();
                //Toast.makeText(getApplicationContext(), "noe"+noe, Toast.LENGTH_LONG).show();

                String[] mainevents=new String[noe+1];
                int i=0;
                mainevents[i]="Select Event"; i++;
                for(DataSnapshot eventSnapshot:dataSnapshot.getChildren()) {
                    mainevents[i]=eventSnapshot.getKey(); i++;
                   // Toast.makeText(getApplicationContext(), eventSnapshot.getKey(), Toast.LENGTH_LONG).show();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, mainevents);
                dynamicSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item
                if (selectedItem.equals("Select Event")) {
                    Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    myRef.child(sharedpreferences.getString("subevent","0")).child(selectedItem).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int noe=(int)dataSnapshot.getChildrenCount();
                            //Toast.makeText(getApplicationContext(), "noe"+noe, Toast.LENGTH_LONG).show();

                            String[] mainevents=new String[noe+1];
                            int i=0;
                            mainevents[i]="Select Event"; i++;
                            for(DataSnapshot eventSnapshot:dataSnapshot.getChildren()) {
                                mainevents[i]=eventSnapshot.getKey().toString(); i++;
                               // Toast.makeText(getApplicationContext(), eventSnapshot.getKey(), Toast.LENGTH_LONG).show();
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, mainevents);
                            subsubdynamicSpinner.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            // Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        submit = (Button) findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String subsubevent=dynamicSpinner.getSelectedItem().toString();
                final String finalsubevent=subsubdynamicSpinner.getSelectedItem().toString();

                if (finalsubevent.equals("Select Event")) {
                    Toast.makeText(getApplicationContext(), "Select Event!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("subsubevent", subsubevent.toLowerCase());
                editor.putString("finalsubevent", finalsubevent.toLowerCase());
                editor.commit();
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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
