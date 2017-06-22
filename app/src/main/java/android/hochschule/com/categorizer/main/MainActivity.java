package android.hochschule.com.categorizer.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.hochschule.com.categorizer.R;
import android.hochschule.com.categorizer.author.AuthorsActivity;
import android.hochschule.com.categorizer.category.CategoryClass;
import android.hochschule.com.categorizer.database.SQLHandlerClass;
import android.hochschule.com.categorizer.expandableListAdapter.MyListAdapterClass;
import android.hochschule.com.categorizer.item.ItemClass;
import android.hochschule.com.categorizer.item.ItemEditActivity;
import android.hochschule.com.categorizer.setting.SettingsActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
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
import java.util.Locale;

import static android.hochschule.com.categorizer.database.DBhelperClass.COL_DESC_ITM;
import static android.hochschule.com.categorizer.database.DBhelperClass.COL_ID_GRP;
import static android.hochschule.com.categorizer.database.DBhelperClass.COL_NAME_GRP;
import static android.hochschule.com.categorizer.database.DBhelperClass.COL_NAME_ITM;

/**
 * Startet die Main Activity mit den Kategorien und Items.
 */
public class MainActivity extends AppCompatActivity {

    //HashMap zur Überprüfung vorhandener Kategorien
    private LinkedHashMap<String, CategoryClass> subjects = new LinkedHashMap<>();
    //Alle Kategorien
    private ArrayList<CategoryClass> categories = new ArrayList<>();
    //Eigener Expandable List View Adapter
    private MyListAdapterClass listAdapter;
    //Aktuelle Group Position notwendig für einige Funktionen
    private int currentGrpPos;
    //Aktuelle Child Position notwendig für einige Funktionen
    private int currentChildPos;
    //Instanz des SQL Handlers
    public SQLHandlerClass sqlHandlerClass;
    //Name der Einstellungsdatei
    public static final String PREFS_NAME = "MyPrefsFile";
    //App Einstellungen
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //App Theme anpassen
        sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        if (sharedPreferences.getBoolean("nightmode", false)) {
            MainActivity.this.setTheme(R.style.NightTheme);
        } else {
            MainActivity.this.setTheme(R.style.AppTheme);
        }

        //Sprache auf englisch stellen, falls die Systemsprache nicht deutsch ist
        Locale current = getResources().getConfiguration().locale;
        Log.e(current.toString(), current.toString());
        if (sharedPreferences.getString("initial", null) == null && !current.toString().equals("de_DE")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("language", true);
            editor.putString("initial", "initial");
            editor.commit();
        }

        if (sharedPreferences.getBoolean("language", false)) {
            SettingsActivity.setLocale("en", getResources());
        } else {
            SettingsActivity.setLocale("default", getResources());
        }

        setContentView(R.layout.activity_main);

        //zu Beginn alles initialisieren
        initDatabase();
        initFloatingActionButton();
        initExpandableList();
    }

    @Override
    public void onBackPressed() {
        //Activites schließen
        finishAffinity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Wird Activity in den Stopp Modus gesetzt, werden Daten in Datenbank gespeichert
        saveDatabase();
    }

    /**
     * Datenbank initialisieren, Daten laden und Listen Adapter setzen
     */
    private void initDatabase() {
        sqlHandlerClass = new SQLHandlerClass(MainActivity.this);
        listAdapter = new MyListAdapterClass(MainActivity.this, categories);
        loadDatabase();
    }

    /**
     * Daten laden
     */
    private void loadDatabase() {
        Cursor cursorCategories = sqlHandlerClass.getAllCategories();
        Cursor cursorItems;
        if (cursorCategories.moveToFirst()) {
            //Alle Kategorien laden
            while (!cursorCategories.isAfterLast()) {
                String catName = cursorCategories.getString(cursorCategories.getColumnIndex(COL_NAME_GRP));
                long grpID = cursorCategories.getLong(cursorCategories.getColumnIndex(COL_ID_GRP));
                CategoryClass cat = createGroupOnLoad(catName);
                //Cursor für Items in Abhängigkeit der Kategorie erstellen
                cursorItems = sqlHandlerClass.getAllItemsOfCategory(grpID);
                if (cursorItems.moveToFirst()) {
                    //Alle Items der aktuellen Kategorie laden
                    while (!cursorItems.isAfterLast()) {
                        String itemName = cursorItems.getString(cursorItems.getColumnIndex(COL_NAME_ITM));
                        String itemDesc = cursorItems.getString(cursorItems.getColumnIndex(COL_DESC_ITM));
                        createItemOnLoad(itemName, itemDesc, cat);
                        cursorItems.moveToNext();
                    }
                }
                cursorItems.close();
                cursorCategories.moveToNext();
            }
            listAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Datenbank speichern.
     */
    private void saveDatabase() {
        //zunächst prüfen ob es notwendig ist Tabellen zu überschreiben
        sqlHandlerClass.checkDatabase();
        for (CategoryClass cat : categories) {
            long grpID = sqlHandlerClass.insertCategory(cat.getName());
            ArrayList<ItemClass> items = cat.getItems();
            if (items != null) {
                for (ItemClass item : items) {
                    sqlHandlerClass.insertItem(item.getName(), item.getDescription(), grpID);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Menü in der Action Bar erstellen
        getMenuInflater().inflate(R.menu.menu_app_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Menü ID des Action Bar Menüs merken
        int id = item.getItemId();

        switch (id) {
            //Einstellungen aufrufen
            case R.id.mnuSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            //App Autoren anzeigen
            case R.id.mnuAuthors:

//                final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
//                alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_authors));
//                alert.setMessage(MainActivity.this.getResources().getString(R.string.authors));
//                alert.setIcon(R.drawable.ic_dialog_info);
//                alert.setCancelable(false);
//                alert.setButton(Dialog.BUTTON_NEUTRAL, MainActivity.this.getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                alert.show();

                startActivity(new Intent(this, AuthorsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Floating Action Menu erstellen und Listener setzen
     */
    private void initFloatingActionButton() {
        //Menü
        final FloatingActionMenu famMenu = (FloatingActionMenu) findViewById(R.id.addAction);
        //Button für Kategorie hinzufügen
        final FloatingActionButton fabAddCat = (FloatingActionButton) findViewById(R.id.addCategory);
        ////Button für Item hinzufügen
        final FloatingActionButton fabAddItem = (FloatingActionButton) findViewById(R.id.addItem);

        //Farben an Nachtmodus anpassen falls erforderlich
        if (sharedPreferences.getBoolean("nightmode", false)) {
            famMenu.setMenuButtonColorNormal(R.color.colorAccentNight);
            famMenu.setMenuButtonColorPressed(R.color.colorPrimaryNight);
            fabAddCat.setColorNormal(R.color.colorAccentNight);
            fabAddCat.setColorPressed(R.color.colorPrimaryNight);
            fabAddItem.setColorNormal(R.color.colorAccentNight);
            fabAddItem.setColorPressed(R.color.colorPrimaryNight);
        }

        fabAddCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                famMenu.close(true); //Menü schließen falls Button gedrückt wurde
                final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_new_category));
                final EditText input = new EditText(MainActivity.this); //Textfeld im Alert Dialog zur Kategorieerstellung
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});  //Filter für max. 20 Zeichen im Titel
                input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);    //Nur Text, keine Zeilenumbrüche
                alert.setView(input);
                alert.setCancelable(false);
                alert.setButton(Dialog.BUTTON_POSITIVE, MainActivity.this.getResources().getString(R.string.btn_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Prüfen ob Titel nicht leer oder nur Leerzeichen -> Alert Dialog
                        if (!input.getText().toString().trim().equals("")) {
                            createGroup(input.getText().toString().trim());
                        } else {
                            final AlertDialog alert2 = new AlertDialog.Builder(alert.getContext()).create();
                            alert2.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_empty));
                            alert2.setMessage(MainActivity.this.getResources().getString(R.string.dialog_msg_enter_title));
                            alert2.setIcon(R.drawable.ic_dialog_warning);
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
                famMenu.close(true);    //Menü schließen falls Button gedrückt wurde
                //Falls keine Kategorien vorhanden, kann auch kein Item erstellt werden -> Alert Dialog
                if (categories.size() == 0) {
                    final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                    alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_no_categories));
                    alert.setMessage(MainActivity.this.getResources().getString(R.string.dialog_msg_create_category));
                    alert.setIcon(R.drawable.ic_dialog_error);
                    alert.setButton(Dialog.BUTTON_NEUTRAL, MainActivity.this.getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                    return;
                }
                final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_new_item));
                final EditText input = new EditText(MainActivity.this); //Textfeld im Alert Dialog zur Itemerstellung
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});  //Filter für max. 20 Zeichen im Titel
                input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_TEXT);    //Nur Text, keine Zeilenumbrüche
                alert.setView(input);
                alert.setCancelable(false);
                alert.setButton(Dialog.BUTTON_POSITIVE, MainActivity.this.getResources().getString(R.string.btn_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Prüfen ob Titel nicht leer oder nur Leerzeichen -> Alert Dialog
                        if (!input.getText().toString().trim().equals("")) {
                            //Alert Dialog erstellen mit Liste vorhandener Kategorien zur Auswahl für Item
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
                            alert2.setIcon(R.drawable.ic_dialog_warning);
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

    /**
     * Expandable List erstellen und Listener setzen.
     */
    private void initExpandableList() {
        final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expList);
        expandableListView.setAdapter(listAdapter);

        //Farben an Nachtmodus anpassen falls erforderlich
        if (sharedPreferences.getBoolean("nightmode", false)) {
            expandableListView.setDivider(new ColorDrawable(MainActivity.this.getColor(R.color.colorAccentNight)));
            expandableListView.setDividerHeight(2);
        }

        //Ein Klick auf ein Item geht zur Item Bearbeitungsansicht
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

        //Ein langer Klick führt zur Möglichkeit des Löschens von Items und Kategorien
        expandableListView.setOnItemLongClickListener(new ExpandableListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long pos = expandableListView.getExpandableListPosition(position);
                int itemType = ExpandableListView.getPackedPositionType(pos);
                final int groupPosition = ExpandableListView.getPackedPositionGroup(pos);
                final CategoryClass category = categories.get(groupPosition);

                //Prüfen ob es ein Item oder eine Kategorie ist
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    final int childPosition = ExpandableListView.getPackedPositionChild(pos);
                    final String itemName = category.getItems().get(childPosition).getName();
                    final AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                    alert.setTitle(MainActivity.this.getResources().getString(R.string.dialog_title_delete));
                    alert.setMessage("\"" + itemName + "\" " + MainActivity.this.getResources().getString(R.string.dialog_msg_realy_delete));
                    alert.setIcon(R.drawable.ic_dialog_warning);
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
                    alert.setIcon(R.drawable.ic_dialog_warning);
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
                return true;    //true damit nicht danach noch danach der normale Click Listener aufgerufen wird
            }
        });
    }

    /**
     * Neue Kategorie erstellen.
     */
    private void createGroup(String grpHeader) {
        CategoryClass category = subjects.get(grpHeader);
        //Falls Gruppe noch nicht existiert wird sie erstellt, sonst -> Alert Dialog
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
            alert.setIcon(R.drawable.ic_dialog_error);
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

    /**
     * Item erstellen.
     */
    private void createItem(String itemName, String grpHeader, int grpPosition) {
        ArrayList<ItemClass> items = categories.get(grpPosition).getItems();
        //Falls Kategorie noch keine Items besitzt wird zunächst eine Liste erstellt.
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(new ItemClass(itemName));
        categories.get(grpPosition).setItems(items);
        listAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "\"" + itemName + "\" in " + MainActivity.this.getResources().getString(R.string.dialog_msg_category) + " \"" + grpHeader + "\" " + MainActivity.this.getResources().getString(R.string.toast_created), Toast.LENGTH_SHORT).show();
    }

    /**
     * Kategorie innerhalb der Prozedur des Ladens der Datenbank erstellen.
     */
    private CategoryClass createGroupOnLoad(String grpHeader) {
        CategoryClass category = new CategoryClass();
        category.setName(grpHeader);
        subjects.put(grpHeader, category);
        categories.add(category);
        return category;
    }

    /**
     * Item innerhalb der Prozedur des Ladens der Datenbank erstellen.
     */
    private void createItemOnLoad(String itemName, String itemDesc, CategoryClass categoryClass) {
        ArrayList<ItemClass> items = categoryClass.getItems();
        if (items == null) {
            items = new ArrayList<>();
        }
        ItemClass item = new ItemClass(itemName);
        item.setDescription(itemDesc);
        items.add(item);
        categoryClass.setItems(items);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Notwendig für das Empfangen der Ergebnisse der veränderten Daten
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