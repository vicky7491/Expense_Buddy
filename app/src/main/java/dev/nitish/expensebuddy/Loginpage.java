package dev.nitish.expensebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Loginpage extends AppCompatActivity {

    Button callSignUp, login_btn;
    TextInputLayout regPhoneNo, regPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This line hides the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loginpage);

        // HOOKS
        regPhoneNo = findViewById(R.id.regPhoneNo);
        regPassword = findViewById(R.id.regPassword);
        callSignUp = findViewById(R.id.callSignUp);
        login_btn = findViewById(R.id.login_btn);

        callSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(Loginpage.this, SignUp.class);
            startActivity(intent);
        });

        login_btn.setOnClickListener(v -> loginUser(v));
    }

    public void openForgotPassword(View view) {
        Intent intent = new Intent(Loginpage.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private Boolean validatePhoneNo() {
        String value = regPhoneNo.getEditText().getText().toString();
        if (value.isEmpty()) {
            regPhoneNo.setError("Field cannot be empty");
            return false;
        } else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = regPassword.getEditText().getText().toString();
        if (value.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View view) {
        // Validate Login info
        if (!validatePhoneNo() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private void isUser() {
        String userEnteredPhoneNo = regPhoneNo.getEditText().getText().toString().trim();
        String userEnteredPassword = regPassword.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("phoneNo").equalTo(userEnteredPhoneNo);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Loop through matching results (ideally just one result due to unique usernames)
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        regPhoneNo.setError(null);
                        regPhoneNo.setErrorEnabled(false);

                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                        if (passwordFromDB != null && passwordFromDB.equals(userEnteredPassword)) {
                            regPhoneNo.setError(null);
                            regPhoneNo.setErrorEnabled(false);

                            // Retrieve user details
                            String nameFromDB = userSnapshot.child("name").getValue(String.class);
                            String usernameFromDB = userSnapshot.child("username").getValue(String.class);
                            String phoneNoFromDB = userSnapshot.child("phoneNo").getValue(String.class);
                            String emailFromDB = userSnapshot.child("email").getValue(String.class);

                            // Start the Dashboard activity and pass the user data
                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            intent.putExtra("name", nameFromDB);
                            intent.putExtra("username", usernameFromDB);
                            intent.putExtra("email", emailFromDB);
                            intent.putExtra("phoneNo", phoneNoFromDB);
                            intent.putExtra("password", passwordFromDB);

                            startActivity(intent);
                            finish();
                        } else {
                            // Password does not match
                            regPassword.setError("Wrong Password");
                            regPassword.requestFocus();
                        }
                    }
                } else {
                    regPhoneNo.setError("No such User Exist");
                    regPhoneNo.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
