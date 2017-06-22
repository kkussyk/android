package android.hochschule.com.categorizer.item;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hochschule.com.categorizer.R;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import static android.hochschule.com.categorizer.main.MainActivity.sharedPreferences;

/**
 * Activity dient der Bearbeitung von Items
 */
public class ItemEditActivity extends AppCompatActivity {

    private ItemClass item;
    private EditText itemEditTitle;
    private EditText itemDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //App Theme anpassen
        if (sharedPreferences.getBoolean("nightmode", false)) {
            ItemEditActivity.this.setTheme(R.style.NightTheme);
        } else {
            ItemEditActivity.this.setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_item_edit);

        //aktuelles Item holen
        item = getIntent().getParcelableExtra("item");
        itemEditTitle = (EditText) findViewById(R.id.itemTitleEdit);
        itemEditTitle.setText(item.getName());  //aktuellen Item Namen setzen
        itemEditTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //Filter für den Namen: Max. 20 Zeichen erlaubt
        itemDescription = (EditText) findViewById(R.id.itemDescription);
        itemDescription.setText(item.getDescription()); //Item Beschreibung setzen

        //Farben ändern falls Nightmode
        if (sharedPreferences.getBoolean("nightmode", false)) {
            View headerView = findViewById(R.id.headerItemEdit);
            headerView.setBackgroundColor(ItemEditActivity.this.getColor(R.color.colorRipple));
            itemEditTitle.setTextColor(ItemEditActivity.this.getColor(R.color.colorItemTitleEditModeNight));
        }

        //Button zum Speichern
        FloatingActionButton btnSave = (FloatingActionButton) findViewById(R.id.itemSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Titel darf nicht leer sein oder nur aus Leerzeichen bestehen -> Alert Dialog
                if (itemEditTitle.getText().toString().trim().equals("")) {
                    final AlertDialog alert = new AlertDialog.Builder(ItemEditActivity.this).create();
                    alert.setTitle(ItemEditActivity.this.getResources().getString(R.string.dialog_title_empty));
                    alert.setMessage(ItemEditActivity.this.getResources().getString(R.string.dialog_msg_enter_title));
                    alert.setIcon(R.drawable.ic_dialog_warning);
                    alert.setCancelable(false);
                    alert.setButton(Dialog.BUTTON_POSITIVE, ItemEditActivity.this.getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            itemEditTitle.requestFocus();
                        }
                    });
                    alert.show();
                } else {
                    item.setName(itemEditTitle.getText().toString().trim());
                    item.setDescription(itemDescription.getText().toString());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result_item", item);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}