package android.hochschule.com.categorizer;

import android.os.Parcel;
import android.os.Parcelable;

class ItemClass implements Parcelable {

    private String name;
    private String description;

    ItemClass(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    private ItemClass(Parcel In) {
        this.name = In.readString();
        this.description = In.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeString(getDescription());
    }

    public static final Parcelable.Creator<ItemClass> CREATOR = new Parcelable.Creator<ItemClass>() {
        @Override
        public ItemClass createFromParcel(Parcel src) {
            return new ItemClass(src);
        }

        @Override
        public ItemClass[] newArray(int size) {
            return new ItemClass[size];
        }
    };
}