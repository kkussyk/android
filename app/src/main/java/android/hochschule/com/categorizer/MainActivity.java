package android.hochschule.com.categorizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

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

        initFloatingActionButton();
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
                Toast.makeText(MainActivity.this, "Einstellungen gedrückt!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mnuAuthors:
                final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_authors));
                alert.setMessage("Kevin Kussyk\nThomas Fahrenholz (32460)");
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

    private void initFloatingActionButton() {
        final FloatingActionMenu famMenu = (FloatingActionMenu) findViewById(R.id.addAction);
        final FloatingActionButton fabAddCat = (FloatingActionButton) findViewById(R.id.addCategory);
        FloatingActionButton fabAddItem = (FloatingActionButton) findViewById(R.id.addItem);

        fabAddCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                famMenu.close(true);
                final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_new_category));
                final EditText input = new EditText(MainActivity.this);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);
                alert.setView(input);
                alert.setCancelable(false);
                alert.setButton(Dialog.BUTTON_POSITIVE, MainActivity.this.getResources().getString(R.string.btn_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!input.getText().toString().equals("")) {
                            createGroup(input.getText().toString());
                        } else {
                            final AlertDialog alert2 = new AlertDialog.Builder(alert.getContext()).create();
                            alert2.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_empty));
                            alert2.setMessage(MainActivity.this.getResources().getString(R.string.dialog_msg_enter_title));
                            alert2.setCancelable(false);
                            alert2.setButton(Dialog.BUTTON_NEGATIVE, MainActivity.this.getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alert2.show();
                        }
                    }
                });
                alert.setButton(Dialog.BUTTON_NEGATIVE, MainActivity.this.getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();


            }
        });
        fabAddItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                famMenu.close(true);

                final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_new_item));
                final EditText input = new EditText(MainActivity.this);
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);
                alert.setView(input);
                alert.setCancelable(false);
                alert.setButton(Dialog.BUTTON_POSITIVE, MainActivity.this.getResources().getString(R.string.btn_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!input.getText().toString().equals("")) {
                            AlertDialog.Builder alertList = new AlertDialog.Builder(MainActivity.this);
                            alertList.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_choose_category));
                            alertList.setCancelable(false);
                            final String[] categoryNames = new String[categories.size()];
                            for (int i = 0; i < categoryNames.length; ++i) {
                                categoryNames[i] = categories.get(i).getName();
                            }
                            alertList.setItems(categoryNames, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    createItem(input.getText().toString(), categoryNames[which], which);
                                }

                            });
                            alertList.show();
                        } else {
                            final AlertDialog alert2 = new AlertDialog.Builder(alert.getContext()).create();
                            alert2.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_empty));
                            alert2.setMessage(MainActivity.this.getResources().getString(R.string.dialog_msg_enter_title));
                            alert2.setCancelable(false);
                            alert2.setButton(Dialog.BUTTON_NEGATIVE, MainActivity.this.getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alert2.show();
                        }
                    }
                });
                alert.setButton(Dialog.BUTTON_NEGATIVE, MainActivity.this.getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
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
        addSubject("Filme", "Dead Pool");
        addSubject("Filme", "Der Hobbit");
        addSubject("Filme", "Dracula");
        addSubject("Filme", "Terminator 2");
        addSubject("Filme", "Dead Pool 2");
        addSubject("Filme", "Der Hobbit - Die Schlacht der fünf Heere");
        addSubject("Filme", "Purpurne Flüsse");
        addSubject("Suppenrezepte", "Pasul");
        addSubject("Suppenrezepte", "Lasagne");
        addSubject("Suppenrezepte", "Nudelauflauf");
        addSubject("Suppenrezepte", "Hamburger");
        addSubject("Nicht vergessen", "Schulden");
        addSubject("Nicht vergessen", "Verliehen");
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

    private void createGroup(String grpHeader) {
        CategoryClass category = subjects.get(grpHeader);
        if (category == null) {
            category = new CategoryClass();
            category.setName(grpHeader);
            subjects.put(grpHeader, category);
            categories.add(category);
            listAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.dialog_msg_category) + " \"" + grpHeader + "\" " + MainActivity.this.getResources().getString(R.string.toast_created), Toast.LENGTH_SHORT).show();
        } else {
            final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_duplicate));
            alert.setMessage(MainActivity.this.getResources().getString(R.string.dialog_msg_category) + " \"" + grpHeader + "\" " + MainActivity.this.getResources().getString(R.string.dialog_msg_already_exists));
            alert.setCancelable(false);
            alert.setButton(Dialog.BUTTON_NEGATIVE, MainActivity.this.getResources().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alert.show();
        }
    }

    private void createItem(String itemName, String grpHeader, int grpPosition) {
        ArrayList<ItemClass> items = categories.get(grpPosition).getItems();
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(new ItemClass(itemName));
        categories.get(grpPosition).setItems(items);
        listAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "\"" + itemName + "\" in " + MainActivity.this.getResources().getString(R.string.dialog_msg_category) + " \"" + grpHeader + "\" " + MainActivity.this.getResources().getString(R.string.toast_created), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ItemClass item = data.getParcelableExtra("result_item");
                    if (currentChildPos == -1) {
                        categories.get(currentGrpPos).getItems().add(item);
                        Toast.makeText(MainActivity.this, "\"" + item.getName() + "\" in " + MainActivity.this.getResources().getString(R.string.dialog_msg_category) + " \"" + categories.get(currentGrpPos).getName() + "\" " + MainActivity.this.getResources().getString(R.string.toast_created), Toast.LENGTH_SHORT).show();
                    } else {
                        categories.get(currentGrpPos).getItems().set(currentChildPos, item);
                        Toast.makeText(MainActivity.this, "\"" + item.getName() + "\" " + MainActivity.this.getResources().getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();
                    }
                    listAdapter.notifyDataSetChanged();
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
    public void setCurrentChildPos(int currentChildPos) {
        this.currentChildPos = currentChildPos;
    }
}