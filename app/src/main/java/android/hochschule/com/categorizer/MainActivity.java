package android.hochschule.com.categorizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    private LinkedHashMap<String, CategoryClass> subjects = new LinkedHashMap<>();
    private ArrayList<CategoryClass> categories = new ArrayList<>();
    private MyAdapterClass listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initExpandableList();
    }

    private void initExpandableList() {
        loadData();
        final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expList);
        listAdapter = new MyAdapterClass(MainActivity.this, categories);
        expandableListView.setAdapter(listAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CategoryClass category = categories.get(groupPosition);
                ItemClass item = category.getItems().get(childPosition);
                Intent intent = new Intent(MainActivity.this, ItemEditActivity.class);
                intent.putExtra("item", item);
                startActivityForResult(intent, 0);
                return false;
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                CategoryClass category = categories.get(groupPosition);
                Toast.makeText(MainActivity.this, "Kategorie: " + category.getName(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnItemLongClickListener(new ExpandableListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long pos = expandableListView.getExpandableListPosition(position);
                int itemType = ExpandableListView.getPackedPositionType(pos);
                final int groupPosition = ExpandableListView.getPackedPositionGroup(pos);
                CategoryClass category = categories.get(groupPosition);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    final int childPosition = ExpandableListView.getPackedPositionChild(pos);
                    final String itemName = category.getItems().get(childPosition).getName();
                    final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                    alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_delete));
                    alert.setMessage("\"" + itemName + "\" wirklich löschen?");
                    alert.setCancelable(false);
                    alert.setButton(Dialog.BUTTON_POSITIVE, MainActivity.this.getResources().getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<ItemClass> items = categories.get(groupPosition).getItems();
                            items.remove(childPosition);
                            listAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "\"" + itemName + "\" gelöscht", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alert.setButton(Dialog.BUTTON_NEGATIVE, MainActivity.this.getResources().getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.show();

                } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    Toast.makeText(MainActivity.this, "Lange gedrückt auf Gruppe: " + category.getName(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void loadData() {
        addSubject("Filme", "Harry Potter 1");
        addSubject("Filme", "Fluch der Karibik 5");
        addSubject("Filme", "Terminator 1");
        addSubject("Suppenrezepte", "Pasul");
        addSubject("Suppenrezepte", "Hochzeitssupppppppppppppppppppppppppe");
    }

    private void addSubject(String grpHeader, String itemName) {
        CategoryClass category = subjects.get(grpHeader);
        if (category == null) {
            category = new CategoryClass();
            category.setName(grpHeader);
            subjects.put(grpHeader, category);
            categories.add(category);
        }
        ArrayList<ItemClass> items = category.getItems();
        ItemClass item = new ItemClass(itemName);
        items.add(item);
        category.setItems(items);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ItemClass item = data.getParcelableExtra("result_item");
                    //hier noch das geänderte Item permanent speichern
                    listAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "\""+item.getName()+"\" erfolgreich gespeichert.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}