package android.hochschule.com.categorizer;

import java.util.ArrayList;

class CategoryClass {

    private String name;
    private ArrayList<ItemClass> items = new ArrayList<ItemClass>();

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    ArrayList<ItemClass> getItems() {
        return items;
    }

    void setItems(ArrayList<ItemClass> items) {
        this.items = items;
    }
}