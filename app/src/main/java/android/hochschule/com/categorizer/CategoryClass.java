package android.hochschule.com.categorizer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

class CategoryClass implements Parcelable {

    private String name;
    private ArrayList<ItemClass> items = new ArrayList<>();

    CategoryClass() {
    }

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