package dev.nitish.expensebuddy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash_screen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    //Variables
    Animation greetAnim, logoAnim, smileyAnim;
    ImageView greet_img, smiley_img, logo_img ;
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Used for remove the navigation bar from the splash screen
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);


        //Animations
        greetAnim = AnimationUtils.loadAnimation(this, R.anim.greet_animation);
        logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        smileyAnim = AnimationUtils.loadAnimation(this, R.anim.smiley_animation);

        //Hooks
        greet_img = findViewById(R.id.greet);
        smiley_img = findViewById(R.id.smiley);
        logo_img = findViewById(R.id.logo);
        slogan = findViewById(R.id.slogan);

        greet_img.setAnimation(greetAnim);
        smiley_img.setAnimation(smileyAnim);
        logo_img.setAnimation(logoAnim);
        slogan.setAnimation(logoAnim);


        new Handler() .postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash_screen.this, Loginpage.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}