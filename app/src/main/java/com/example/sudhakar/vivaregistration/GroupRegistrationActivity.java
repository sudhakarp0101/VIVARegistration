package com.example.sudhakar.vivaregistration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class GroupRegistrationActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_group_registration);
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
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        };
        final Spinner gdynamicSpinner = (Spinner) findViewById(R.id.group_spinner);
        final String[] gitems = { "1","2", "3","4","5","6","7","8","9","10","11","12"};
        ArrayAdapter<String> gadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gitems);
        gdynamicSpinner.setAdapter(gadapter);

        gdynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                final String mobile=((TextView)findViewById(R.id.mobile_ed)).getText().toString();
                if(TextUtils.isEmpty(mobile)){
                    Toast.makeText(getApplicationContext(), "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                String selectedItem = adapterView.getItemAtPosition(position).toString(); //this is your selected item
                startActivity(new Intent(getApplicationContext(), GrroupActivity.class)
                            .putExtra("mobile",mobile).putExtra("i",selectedItem));
                    finish();
                }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
       // myRef.child(category).child(rollNo).setValue(student);

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
                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
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

