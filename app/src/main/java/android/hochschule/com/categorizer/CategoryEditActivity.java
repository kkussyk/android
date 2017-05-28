package android.hochschule.com.categorizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class CategoryEditActivity extends AppCompatActivity {

    private CategoryClass category;
    private EditText categoryEditTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);

        category = getIntent().getParcelableExtra("category");
        categoryEditTitle = (EditText) findViewById(R.id.groupTitleEdit);
        categoryEditTitle.setText(category.getName());
        ArrayList<ItemClass> items = category.getItems();
        ArrayList<String> itemNames = new ArrayList<>();
        for (ItemClass item : items) {
            itemNames.add(item.getName());
        }
        ListView listView = (ListView) findViewById(R.id.groupContentView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemNames);
        listView.setAdapter(arrayAdapter);

        FloatingActionButton btnSave = (FloatingActionButton) findViewById(R.id.groupSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryEditTitle.getText().toString().equals("")) {
                    final AlertDialog alert = new AlertDialog.Builder(CategoryEditActivity.this).create();
                    alert.setTitle(CategoryEditActivity.this.getResources().getString(R.string.dialog_title_empty));
                    alert.setMessage(CategoryEditActivity.this.getResources().getString(R.string.dialog_msg_enter_title));
                    alert.setCancelable(false);
                    alert.setButton(Dialog.BUTTON_POSITIVE, CategoryEditActivity.this.getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            categoryEditTitle.requestFocus();
                        }
                    });
                    alert.show();
                } else {
                    category.setName(categoryEditTitle.getText().toString());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result_category", category);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
}
