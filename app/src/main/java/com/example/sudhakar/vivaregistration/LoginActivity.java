package com.example.sudhakar.vivaregistration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get Firebase auth instance
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();

        //authenticate user
        auth.signInWithEmailAndPassword("viva@gmail.com", "viva123")
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                    }
                });
        final Spinner dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);
        final Spinner subdynamicSpinner = (Spinner) findViewById(R.id.sub_spinner);
        final Spinner subsubdynamicSpinner = (Spinner) findViewById(R.id.subsub_spinner);
        final Spinner finalsubdynamicSpinner = (Spinner) findViewById(R.id.fisub_spinner);
        final TextView eventtv=(TextView)findViewById(R.id.venue_tv);
        final TextView winnertv=(TextView)findViewById(R.id.winner_tv);

        final String[] items = {"Select Event","solo", "group"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        dynamicSpinner.setAdapter(adapter);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                final String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item
                if (selectedItem.equals("Select Event")) {
                    Toast.makeText(getApplicationContext(), "Select Event", Toast.LENGTH_SHORT).show();
                    eventtv.setText("");
                    return;
                }
                else{
                    myRef.child(selectedItem).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int noe=(int)dataSnapshot.getChildrenCount();
                           // Toast.makeText(getApplicationContext(), "noe"+noe, Toast.LENGTH_LONG).show();

                            String[] mainevents=new String[noe+1];
                            int i=0;
                            mainevents[i]="Select Event"; i++;
                            for(DataSnapshot eventSnapshot:dataSnapshot.getChildren()) {
                                mainevents[i]=eventSnapshot.getKey(); i++;
                                //  Toast.makeText(getApplicationContext(), eventSnapshot.getKey(), Toast.LENGTH_LONG).show();
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, mainevents);
                            subdynamicSpinner.setAdapter(adapter);

                            subdynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                {
                                    final String selectedItem1 = parent.getItemAtPosition(position).toString(); //this is your selected item
                                    if (selectedItem1.equals("Select Event")) {
                                        Toast.makeText(getApplicationContext(), "Select Event", Toast.LENGTH_SHORT).show();
                                        eventtv.setText("");
                                        return;
                                    }
                                    else{
                                        myRef.child(selectedItem).child(selectedItem1).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                int noe=(int)dataSnapshot.getChildrenCount();
                                              //  Toast.makeText(getApplicationContext(), "noe"+noe, Toast.LENGTH_LONG).show();

                                                String[] mainevents=new String[noe+1];
                                                int i=0;
                                                mainevents[i]="Select Event"; i++;
                                                for(DataSnapshot eventSnapshot:dataSnapshot.getChildren()) {
                                                    mainevents[i]=eventSnapshot.getKey(); i++;
                                                    //  Toast.makeText(getApplicationContext(), eventSnapshot.getKey(), Toast.LENGTH_LONG).show();
                                                }
                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, mainevents);
                                                subsubdynamicSpinner.setAdapter(adapter);

                                                subsubdynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                                    {
                                                        final String selectedItem2 = parent.getItemAtPosition(position).toString(); //this is your selected item
                                                        if (selectedItem2.equals("Select Event")) {
                                                            Toast.makeText(getApplicationContext(), "Select Event", Toast.LENGTH_SHORT).show();
                                                            eventtv.setText("");
                                                            return;
                                                        }
                                                        else{
                                                            myRef.child(selectedItem).child(selectedItem1).child(selectedItem2).addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    int noe=(int)dataSnapshot.getChildrenCount();
                                                                  //  Toast.makeText(getApplicationContext(), "noe"+noe, Toast.LENGTH_LONG).show();

                                                                    String[] mainevents=new String[noe+1];
                                                                    int i=0;
                                                                    mainevents[i]="Select Event"; i++;
                                                                    for(DataSnapshot eventSnapshot:dataSnapshot.getChildren()) {
                                                                        mainevents[i]=eventSnapshot.getKey(); i++;
                                                                        //  Toast.makeText(getApplicationContext(), eventSnapshot.getKey(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, mainevents);
                                                                    finalsubdynamicSpinner.setAdapter(adapter);

                                                                    finalsubdynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                                                                        {
                                                                            final String selectedItem3 = parent.getItemAtPosition(position).toString(); //this is your selected item
                                                                            if (selectedItem3.equals("Select Event")) {
                                                                                Toast.makeText(getApplicationContext(), "Select Event", Toast.LENGTH_SHORT).show();
                                                                                eventtv.setText("");
                                                                                return;
                                                                            }
                                                                            else{
                                                                                myRef.child(selectedItem).child(selectedItem1).child(selectedItem2).child(selectedItem3).child("venue").addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        int noe=(int)dataSnapshot.getChildrenCount();
                                                                    //                    Toast.makeText(getApplicationContext(), "noe"+noe, Toast.LENGTH_LONG).show();

                                                                                        eventtv.setText(dataSnapshot.getValue(Venue.class).toString());
                                                                                    }
                                                                                    @Override
                                                                                    public void onCancelled(DatabaseError error) {
                                                                                        // Failed to read value
                                                                                        // Log.w(TAG, "Failed to read value.", error.toException());
                                                                                    }
                                                                                });

                                                                                myRef.child(selectedItem).child(selectedItem1).child(selectedItem2).child(selectedItem3).child("winners").addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                       // int noe=(int)dataSnapshot.getChildrenCount();
                                                                                  //      Toast.makeText(getApplicationContext(), dataSnapshot.toString(), Toast.LENGTH_LONG).show();
                                                                                       if(dataSnapshot.getValue()==null){
                                                                                           winnertv.setText("Winners Yet to Announce");

                                                                                       }
                                                                                        else if(selectedItem.equals("solo")) {
                                                                                           String winners = "First Prize: ";
                                                                                           for (DataSnapshot winner : dataSnapshot.getChildren()) {
                                                                                               if (winner.getKey().equals("first")) {
                                                                                                   winners = winners + winner.getValue(Student.class).toString();
                                                                                               } else {
                                                                                                   winners = winners + "Second Prize: " + winner.getValue(Student.class).toString();
                                                                                               }
                                                                                           }
                                                                                           winnertv.setText(winners);
                                                                                       }
                                                                                       else if(selectedItem.equals("group")){
                                                                                           String winners = "First Prize: ";
                                                                                           for (DataSnapshot winner : dataSnapshot.getChildren()) {
                                                                                               if (winner.getKey().equals("first")) {
                                                                                                   for(DataSnapshot more:winner.getChildren()) {
                                                                                                       winners = winners + more.getValue(Student.class).toString();
                                                                                                   }
                                                                                               } else if(winner.getKey().equals("second")) {
                                                                                                   winners = winners + "Second Prize: ";
                                                                                                   for(DataSnapshot more:winner.getChildren()) {
                                                                                                       winners = winners + more.getValue(Student.class).toString();
                                                                                                   }
                                                                                               }
                                                                                           }
                                                                                           winnertv.setText(winners);
                                                                                       }

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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.loginmenu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_student:
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                return true;
            case R.id.menu_judges:
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), JudgeActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
