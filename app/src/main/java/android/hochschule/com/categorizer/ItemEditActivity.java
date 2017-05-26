package android.hochschule.com.categorizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemEditActivity extends AppCompatActivity {

    private ItemClass item;
    private EditText itemEditTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        item = getIntent().getParcelableExtra("item");
        itemEditTitle = (EditText) findViewById(R.id.itemTitleEdit);
        itemEditTitle.setText(item.getName());

        FloatingActionButton btnSave = (FloatingActionButton) findViewById(R.id.itemSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemEditTitle.getText().toString().equals("")){
                    final AlertDialog alert = new AlertDialog.Builder(ItemEditActivity.this).create();
                    alert.setTitle(ItemEditActivity.this.getResources().getString(R.string.dialog_title_empty));
                    alert.setMessage("Bitte einen Titel eingeben!");
                    alert.setCancelable(false);
                    alert.setButton(Dialog.BUTTON_POSITIVE, ItemEditActivity.this.getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            itemEditTitle.requestFocus();
                        }
                    });
                    alert.show();
                    return;
                }
                else {
                    item.setName(itemEditTitle.getText().toString());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result_item", item);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}