package android.hochschule.com.categorizer.category;

import android.hochschule.com.categorizer.item.ItemClass;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Klasse dient der Erstellung der Kategorien.
 */
public class CategoryClass implements Parcelable {

    //Eine Kategorie hat einen Namen und eine Liste von Items
    private String name;
    private ArrayList<ItemClass> items = new ArrayList<>();

    public CategoryClass() {
    }

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

    //###############################################################
    //notwendig für Übertragung von Objekten via Parcel

    private CategoryClass(Parcel In) {
        this.name = In.readString();
        this.items = In.readArrayList(CategoryClass.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeList(getItems());
    }

    public static final Parcelable.Creator<CategoryClass> CREATOR = new Parcelable.Creator<CategoryClass>() {
        @Override
        public CategoryClass createFromParcel(Parcel src) {
            return new CategoryClass(src);
        }

        @Override
        public CategoryClass[] newArray(int size) {
            return new CategoryClass[size];
        }
    };
}