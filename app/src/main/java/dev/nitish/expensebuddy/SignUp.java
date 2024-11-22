package dev.nitish.expensebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity {

    //Variables
    TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword;
    Button regBtn, CallLoginpage;

    FirebaseDatabase rootNode;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This line hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_up);


        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        //Hooks to all xml elements in activity sign_up.xml
        regName = findViewById(R.id.regName);
        regUsername = findViewById(R.id.regUsername);
        regEmail = findViewById(R.id.regEmail);
        regPhoneNo = findViewById(R.id.regPhoneNo);
        regPassword = findViewById(R.id.regPassword);
        regBtn = findViewById(R.id.regBtn);
        CallLoginpage = findViewById(R.id.CallLoginpage);

        CallLoginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,Loginpage.class);
                startActivity(intent);
            }
        });

        //save Data in firebase on button click
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerUser(view);
//                rootNode = FirebaseDatabase.getInstance();
//                reference = rootNode.getReference("users");

                //Get all values in String
//                String name = regName.getEditText().getText().toString();
//                String username = regUsername.getEditText().getText().toString();
//                String email = regEmail.getEditText().getText().toString();
//                String phoneNo = regPhoneNo.getEditText().getText().toString();
//                String password = regPassword.getEditText().getText().toString();
//                UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo, password);
//                reference.child(username).setValue(helperClass);

            }
        });
    }//Register Button method end


    private Boolean validateName() {
        String value = regName.getEditText().getText().toString();

        if (value.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateusername() {
        String value = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (value.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (value.length() >= 15) {
            regUsername.setError("Username too long");
            return false;
        } else if (!value.matches(noWhiteSpace)) {
            regUsername.setError("White spaces are not allowed");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateemail() {
        String value = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (value.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!value.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            return true;
        }
    }

    private Boolean validatephoneNo() {
        String value = regPhoneNo.getEditText().getText().toString();

        if (value.isEmpty()) {
            regPhoneNo.setError("Field cannot be empty");
            return false;
        } else {
            regPhoneNo.setError(null);
            return true;
        }
    }

    private Boolean validatepassword() {
        String value = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";
        if (value.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!value.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            return true;
        }
    }


    public void registerUser(View view) {

        if (!validateName() | !validatepassword() | !validatephoneNo() | !validateemail() | !validateusername()) {
            return;
        }

        //Get all values in String
        String name = regName.getEditText().getText().toString();
        String username = regUsername.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phoneNo = regPhoneNo.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();



        UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo, password);


        reference.child(phoneNo).setValue(helperClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Navigate to dashboard on successful registration
                Toast.makeText(SignUp.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this, Dashboard.class); // Replace with your dashboard activity
                startActivity(intent);
                finish(); // Close the current activity
            } else {
                Toast.makeText(SignUp.this, "Registration failed! Try again.", Toast.LENGTH_SHORT).show();
            }
        });;

    }
}


