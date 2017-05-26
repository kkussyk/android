package android.hochschule.com.categorizer;

import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    private LinkedHashMap<String, CategoryClass> subjects = new LinkedHashMap<String, CategoryClass>();
    private ArrayList<CategoryClass> categories = new ArrayList<CategoryClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //starting expandable list
        loadData();
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expList);
        MyAdapterClass listAdapter = new MyAdapterClass(MainActivity.this, categories);
        expandableListView.setAdapter(listAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CategoryClass category = categories.get(groupPosition);
                ItemClass item = category.getItems().get(childPosition);
                Toast.makeText(getBaseContext(), "Kategorie: " + category.getName()
                        + " Item: " + item.getName(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                CategoryClass category = categories.get(groupPosition);
                Toast.makeText(getBaseContext(), "Kategorie: " + category.getName(),
                        Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        //end of expandable list

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
        ItemClass item = new ItemClass();
        item.setName(itemName);
        items.add(item);
        category.setItems(items);
    }
}
