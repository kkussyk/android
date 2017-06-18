package android.hochschule.com.categorizer.author;

import android.hochschule.com.categorizer.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.hochschule.com.categorizer.main.MainActivity.sharedPreferences;

/**
 * Activity dient der Anzeige der Autoren.
 */
public class AuthorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //App Theme anpassen
        if (sharedPreferences.getBoolean("nightmode", false)) {
            AuthorsActivity.this.setTheme(R.style.NightTheme);
        } else {
            AuthorsActivity.this.setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_authors);
    }
}