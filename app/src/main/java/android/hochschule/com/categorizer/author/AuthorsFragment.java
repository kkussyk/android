package android.hochschule.com.categorizer.author;

import android.app.Fragment;
import android.hochschule.com.categorizer.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment dient der Anzeige der Autoren im Fragment innerhalb einer Activity.
 */
public class AuthorsFragment extends Fragment {
    public AuthorsFragment() {
        // Leerer Konstruktor notwendig
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authors, container, false);
    }
}