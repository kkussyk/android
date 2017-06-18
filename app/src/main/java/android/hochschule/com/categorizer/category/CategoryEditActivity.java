package android.hochschule.com.categorizer.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hochschule.com.categorizer.item.ItemClass;
import android.hochschule.com.categorizer.R;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.hochschule.com.categorizer.main.MainActivity.sharedPreferences;

/**
 * Activity dient der Bearbeitung von Kategorien
 */
public class CategoryEditActivity extends AppCompatActivity {

    private CategoryClass category;
    private EditText categoryEditTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //App Theme anpassen
        if (sharedPreferences.getBoolean("nightmode", false)) {
            CategoryEditActivity.this.setTheme(R.style.NightTheme);
        } else {
            CategoryEditActivity.this.setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_category_edit);

        //Farben ändern falls Nightmode
        if (sharedPreferences.getBoolean("nightmode", false)) {
            View headerView = findViewById(R.id.headerGroupEdit);
            headerView.setBackgroundColor(CategoryEditActivity.this.getColor(R.color.colorRipple));
            TextView groupWrapperView = (TextView) findViewById(R.id.titleItemList);
            groupWrapperView.setBackgroundColor(Color.TRANSPARENT);
        }

        //aktuelle Kategorie holen
        category = getIntent().getParcelableExtra("category");
        categoryEditTitle = (EditText) findViewById(R.id.groupTitleEdit);
        categoryEditTitle.setText(category.getName());  //aktuellen Kategorienamen setzen
        categoryEditTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //Filter für den Namen: Max. 20 Zeichen erlaubt

        //Auflisten der Items der aktuellen Kategorie zur Übersicht
        ArrayList<ItemClass> items = category.getItems();
        ArrayList<String> itemNames = new ArrayList<>();
        for (ItemClass item : items) {
            itemNames.add(item.getName());
        }
        ListView listView = (ListView) findViewById(R.id.groupContentView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemNames);
        listView.setAdapter(arrayAdapter);

        //Button zum Speichern
        FloatingActionButton btnSave = (FloatingActionButton) findViewById(R.id.groupSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Titel darf nicht leer sein oder nur aus Leerzeichen bestehen -> Alert Dialog
                if (categoryEditTitle.getText().toString().trim().equals("")) {
                    final AlertDialog alert = new AlertDialog.Builder(CategoryEditActivity.this).create();
                    alert.setTitle(CategoryEditActivity.this.getResources().getString(R.string.dialog_title_empty));
                    alert.setMessage(CategoryEditActivity.this.getResources().getString(R.string.dialog_msg_enter_title));
                    alert.setIcon(R.drawable.ic_dialog_warning);
                    alert.setCancelable(false);
                    alert.setButton(Dialog.BUTTON_POSITIVE, CategoryEditActivity.this.getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            categoryEditTitle.requestFocus();
                        }
                    });
                    alert.show();
                } else {
                    category.setName(categoryEditTitle.getText().toString().trim());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result_category", category);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}