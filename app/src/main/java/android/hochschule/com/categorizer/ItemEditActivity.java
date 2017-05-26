package android.hochschule.com.categorizer;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
                //******Hier noch Abfragen ob Item Name leer ist -> Dialog als Hinweis einfügen
                item.setName(itemEditTitle.getText().toString());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result_item", item);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}