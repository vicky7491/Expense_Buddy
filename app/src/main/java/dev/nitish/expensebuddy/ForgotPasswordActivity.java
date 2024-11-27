package dev.nitish.expensebuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText emailOrPhoneEditText;
    private Button resetPasswordButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailOrPhoneEditText = findViewById(R.id.emailOrPhoneEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailOrPhone = emailOrPhoneEditText.getText().toString().trim();

                if (emailOrPhone.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your email or phone number.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Call the reset password function
                sendPasswordResetRequest(emailOrPhone);
            }
        });
    }

    private void sendPasswordResetRequest(String emailOrPhone) {
        mAuth.sendPasswordResetEmail(emailOrPhone)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
