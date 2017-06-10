package android.hochschule.com.categorizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static android.hochschule.com.categorizer.DBhelperClass.COL_DESC_ITM;
import static android.hochschule.com.categorizer.DBhelperClass.COL_FID_ITM;
import static android.hochschule.com.categorizer.DBhelperClass.COL_ID_GRP;
import static android.hochschule.com.categorizer.DBhelperClass.COL_NAME_GRP;
import static android.hochschule.com.categorizer.DBhelperClass.COL_NAME_ITM;
import static android.hochschule.com.categorizer.DBhelperClass.TBL_GRP;
import static android.hochschule.com.categorizer.DBhelperClass.TBL_ITM;

/**
 * Klasse dient als Handler / Steuerung der Datenbank
 */
class SQLHandlerClass {

    //Notwendige Instanzen
    private DBhelperClass dbhelper;
    private Context context;
    private SQLiteDatabase database;

    SQLHandlerClass(Context context) {
        this.context = context;
    }

    /**
     * Öffnet beschreibare Datenbank.
     */
    private SQLHandlerClass openWritableDB() throws SQLException {
        dbhelper = new DBhelperClass(this.context);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    /**
     * Öffnet lesbare Datenbank.
     */
    private SQLHandlerClass openReadableDB() throws SQLException {
        dbhelper = new DBhelperClass(this.context);
        database = dbhelper.getReadableDatabase();
        return this;
    }

    /**
     * Schließt Datenbank.
     */
    private void closeDB() {
        dbhelper.close();
    }

    /**
     * Items der Datenbank hinzufügen.
     */
    void insertItem(String name, String description, long grpID) {
        openWritableDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbhelper.COL_NAME_ITM, name);
        contentValues.put(dbhelper.COL_DESC_ITM, description);
        contentValues.put(dbhelper.COL_FID_ITM, grpID);
        database.insert(dbhelper.TBL_ITM, null, contentValues);
        closeDB();
    }

    /**
     * Kategorien der Datenbank hinzufügen. ID wird als Fremdschlüssel für die Items benötigt.
     */
    long insertCategory(String name) {
        openWritableDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbhelper.COL_NAME_GRP, name);
        long id = database.insert(dbhelper.TBL_GRP, null, contentValues);
        closeDB();
        return id;
    }

    /**
     * Alle gespeicherten Kategorien in einen Cursor laden.
     */
    Cursor getAllCategories() {
        openReadableDB();
        String[] cols = new String[]{COL_ID_GRP, COL_NAME_GRP};
        Cursor cursor = database.query(TBL_GRP, cols, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        closeDB();
        return cursor;
    }

    /**
     * Alle gespeicherten Items der entsprechenden Kategorie ID in einen Cursor laden.
     */
    Cursor getAllItemsOfCategory(long grpID) {
        openReadableDB();
        String[] cols = new String[]{COL_NAME_ITM, COL_DESC_ITM, COL_FID_ITM};
        String whereClause = COL_FID_ITM + "=?";
        String[] whereArgs = {String.valueOf(grpID)};
        Cursor cursor = database.query(TBL_ITM, cols, whereClause, whereArgs, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        closeDB();
        return cursor;
    }

    /**
     * Bisherige Tabellen löschen und neu anlegen.
     */
    void checkDatabase() {
        openWritableDB();
        database.execSQL("DROP TABLE IF EXISTS " + TBL_ITM);
        database.execSQL("DROP TABLE IF EXISTS " + TBL_GRP);
        dbhelper.onCreate(database);
        closeDB();
    }
}