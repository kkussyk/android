package android.hochschule.com.categorizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.hochschule.com.categorizer.MainActivity.sharedPreferences;

public class Authors extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(sharedPreferences.getBoolean("nightmode", false)){
            Authors.this.setTheme(R.style.NightTheme);
        }else{
            Authors.this.setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_authors);
    }
}
