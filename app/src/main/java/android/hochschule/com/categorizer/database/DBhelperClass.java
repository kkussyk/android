package android.hochschule.com.categorizer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Klasse dient der Erstellung der SQLite Datenbank
 */
public class DBhelperClass extends SQLiteOpenHelper {

    //Festlegung der Tabellen, Spalten und der Datenbank
    static final String TBL_GRP = "category";
    public static final String COL_ID_GRP = "_id";
    public static final String COL_NAME_GRP = "name";
    static final String TBL_ITM = "item";
    private static final String COL_ID_ITM = "_id";
    public static final String COL_NAME_ITM = "name";
    public static final String COL_DESC_ITM = "description";
    static final String COL_FID_ITM = "fid";    //Fremdschlüsselspalte um Item einer Kategorie zuzuordnen

    private static final String DB_NAME = "CATEGORZIZER.DB";
    private static final int DB_VERSION = 1;

    //Erstellung der create table Anweisungen
    private static final String CREATE_TABLE_GRP = "CREATE TABLE "
            + TBL_GRP + "("
            + COL_ID_GRP + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME_GRP + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_ITM = "CREATE TABLE "
            + TBL_ITM + "("
            + COL_ID_ITM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME_ITM + " TEXT NOT NULL, "
            + COL_DESC_ITM + " TEXT, "
            + COL_FID_ITM + " INTEGER, "
            + " FOREIGN KEY (" + COL_FID_ITM + ") REFERENCES " + TBL_GRP + "(" + COL_ID_GRP + "));";

    DBhelperClass(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tabellen erstellen
        db.execSQL(CREATE_TABLE_GRP);
        db.execSQL(CREATE_TABLE_ITM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Tabellen löschen falls notwendig
        db.execSQL("DROP TABLE IF EXISTS " + TBL_ITM);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_GRP);
        onCreate(db);
    }
}