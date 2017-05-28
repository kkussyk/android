package android.hochschule.com.categorizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private int currentGrpPos;
    private int currentChildPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initExpandableList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mnuSettings:
                //Toast ersetzen durch Einstellungen Activity Aufruf
                Toast.makeText(this, "Einstellungen gedrückt!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mnuAuthors:
                final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_authors));
                alert.setMessage("Kevin Kussyk\nThomas Schrul");
                alert.setCancelable(false);
                alert.setButton(Dialog.BUTTON_NEUTRAL, MainActivity.this.getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initExpandableList() {
        loadData();
        final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expList);
        listAdapter = new MyAdapterClass(MainActivity.this, categories);
        expandableListView.setAdapter(listAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                currentGrpPos = groupPosition;
                currentChildPos = childPosition;
                CategoryClass category = categories.get(groupPosition);
                ItemClass item = category.getItems().get(childPosition);
                Intent intent = new Intent(MainActivity.this, ItemEditActivity.class);
                intent.putExtra("item", item);
                startActivityForResult(intent, 0);
                return false;
            }
        });

        expandableListView.setOnItemLongClickListener(new ExpandableListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long pos = expandableListView.getExpandableListPosition(position);
                int itemType = ExpandableListView.getPackedPositionType(pos);
                final int groupPosition = ExpandableListView.getPackedPositionGroup(pos);
                final CategoryClass category = categories.get(groupPosition);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    final int childPosition = ExpandableListView.getPackedPositionChild(pos);
                    final String itemName = category.getItems().get(childPosition).getName();
                    final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                    alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_delete));
                    alert.setMessage("\"" + itemName + "\" " + MainActivity.this.getResources().getString(R.string.dialog_msg_realy_delete));
                    alert.setCancelable(false);
                    alert.setButton(Dialog.BUTTON_POSITIVE, MainActivity.this.getResources().getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<ItemClass> items = categories.get(groupPosition).getItems();
                            items.remove(childPosition);
                            listAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "\"" + itemName + "\" " + MainActivity.this.getResources().getString(R.string.toast_deleted), Toast.LENGTH_SHORT).show();
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
                    final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                    alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_delete));
                    alert.setMessage(MainActivity.this.getResources().getString(R.string.dialog_msg_category) + " \"" + category.getName() + "\" " + MainActivity.this.getResources().getString(R.string.dialog_msg_realy_delete));
                    alert.setCancelable(false);
                    alert.setButton(Dialog.BUTTON_POSITIVE, MainActivity.this.getResources().getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String categoryName = category.getName();
                            categories.remove(category);
                            listAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "\"" + categoryName + "\" " + MainActivity.this.getResources().getString(R.string.toast_deleted), Toast.LENGTH_SHORT).show();
                        }
                    });
                    alert.setButton(Dialog.BUTTON_NEGATIVE, MainActivity.this.getResources().getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
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
                    categories.get(currentGrpPos).getItems().set(currentChildPos, item);
                    listAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "\"" + item.getName() + "\" " + MainActivity.this.getResources().getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    CategoryClass category = data.getParcelableExtra("result_category");
                    categories.set(currentGrpPos, category);
                    listAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "\"" + category.getName() + "\" " + MainActivity.this.getResources().getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void setCurrentGrpPos(int currentGrpPos) {
        this.currentGrpPos = currentGrpPos;
    }
}