package android.hochschule.com.categorizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

public class ItemEditActivity extends AppCompatActivity {

    private ItemClass item;
    private EditText itemEditTitle;
    private EditText itemDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        item = getIntent().getParcelableExtra("item");
        itemEditTitle = (EditText) findViewById(R.id.itemTitleEdit);
        itemEditTitle.setText(item.getName());
        itemEditTitle.setFilters(new InputFilter[] {new InputFilter.LengthFilter(20)});
        itemDescription = (EditText) findViewById(R.id.itemDescription);
        itemDescription.setText(item.getDescription());

        FloatingActionButton btnSave = (FloatingActionButton) findViewById(R.id.itemSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemEditTitle.getText().toString().equals("")) {
                    final AlertDialog alert = new AlertDialog.Builder(ItemEditActivity.this).create();
                    alert.setTitle(ItemEditActivity.this.getResources().getString(R.string.dialog_title_empty));
                    alert.setMessage(ItemEditActivity.this.getResources().getString(R.string.dialog_msg_enter_title));
                    alert.setCancelable(false);
                    alert.setButton(Dialog.BUTTON_POSITIVE, ItemEditActivity.this.getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            itemEditTitle.requestFocus();
                        }
                    });
                    alert.show();
                } else {
                    item.setName(itemEditTitle.getText().toString());
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