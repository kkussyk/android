package android.hochschule.com.categorizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private Switch nightmodeSwitch, languageSwitch;
    public static boolean nightmode = false;
    private boolean language = false;
    ConstraintLayout settings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings = (ConstraintLayout) findViewById(R.id.settings);
        nightmodeSwitch = (Switch) findViewById(R.id.switch1);
        languageSwitch = (Switch) findViewById(R.id.switch2);

        final SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        nightmode = sharedPreferences.getBoolean("nightmode", false);
        nightmodeSwitch.setChecked(nightmode);
        language = sharedPreferences.getBoolean("language", false);
        languageSwitch.setChecked(language);

        nightmodeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settings.setBackgroundColor(Color.RED);
                    //Toast.makeText(getApplicationContext(), "Switch on!", Toast.LENGTH_LONG).show();
                } else {
                    settings.setBackgroundColor(Color.GREEN);
                    //Toast.makeText(getApplicationContext(), "Switch off!", Toast.LENGTH_LONG).show();
                }


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("nightmode", isChecked);
                editor.commit();
            }
        });

        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setLocale("en");
                }else{
                    setLocale("default");
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("language", isChecked);
                editor.commit();
            }
        });
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);

    }

    protected void onResume(){
        super.onResume();

        if(nightmode){
            settings.setBackgroundColor(Color.RED);
        }else{
            settings.setBackgroundColor(Color.GREEN);
        }
    }
}
