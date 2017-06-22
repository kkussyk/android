package android.hochschule.com.categorizer.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hochschule.com.categorizer.R;
import android.hochschule.com.categorizer.main.MainActivity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

import static android.hochschule.com.categorizer.main.MainActivity.sharedPreferences;

/**
 * Activity dient der Einstellungsmöglichkeit der App.
 */
public class SettingsActivity extends AppCompatActivity {

    ConstraintLayout settings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Switch nightmodeSwitch, languageSwitch;

        //App Theme anpassen
        if (sharedPreferences.getBoolean("nightmode", false)) {
            SettingsActivity.this.setTheme(R.style.NightTheme);
        } else {
            SettingsActivity.this.setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_settings);

        settings = (ConstraintLayout) findViewById(R.id.settings);
        nightmodeSwitch = (Switch) findViewById(R.id.switch1);
        languageSwitch = (Switch) findViewById(R.id.switch2);

        nightmodeSwitch.setChecked(sharedPreferences.getBoolean("nightmode", false));
        languageSwitch.setChecked(sharedPreferences.getBoolean("language", false));

        nightmodeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SettingsActivity.this.setTheme(R.style.NightTheme);
                    Toast.makeText(getApplicationContext(), SettingsActivity.this.getResources().getString(R.string.toast_switch_theme_night), Toast.LENGTH_SHORT).show();
                } else {
                    SettingsActivity.this.setTheme(R.style.AppTheme);
                    Toast.makeText(getApplicationContext(), SettingsActivity.this.getResources().getString(R.string.toast_switch_theme_light), Toast.LENGTH_SHORT).show();
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("nightmode", isChecked);
                editor.commit();
                refresh();
            }
        });

        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setLocale("en", getResources());
                    refresh();
                } else {
                    setLocale("default", getResources());
                    refresh();
                }
                Toast.makeText(getApplicationContext(), SettingsActivity.this.getResources().getString(R.string.toast_switch_language), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("language", isChecked);
                editor.commit();
            }
        });
    }

    /**
     * Sprache ändern.
     */
    public static void setLocale(String lang, Resources res) {
        Locale myLocale = new Locale(lang);
        //Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);
    }

    /**
     * Main Activity refreshen.
     */
    public void refresh() {
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }

}