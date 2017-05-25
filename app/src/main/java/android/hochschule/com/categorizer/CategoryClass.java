package android.hochschule.com.categorizer;

import java.util.ArrayList;

/**
 * Created by Thomas on 25.05.2017.
 */

public class CategoryClass {

    private String name;
    private ArrayList<ItemClass> items = new ArrayList<ItemClass>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ItemClass> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemClass> items) {
        this.items = items;
    }
}
