package android.hochschule.com.categorizer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Klasse dient als Handler / Steuerung der Datenbank
 */
public class SQLHandlerClass {

    //Notwendige Instanzen
    private DBhelperClass dbhelper;
    private Context context;
    private SQLiteDatabase database;

    public SQLHandlerClass(Context context) {
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
    public void insertItem(String name, String description, long grpID) {
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
    public long insertCategory(String name) {
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
    public Cursor getAllCategories() {
        openReadableDB();
        String[] cols = new String[]{DBhelperClass.COL_ID_GRP, DBhelperClass.COL_NAME_GRP};
        Cursor cursor = database.query(DBhelperClass.TBL_GRP, cols, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        closeDB();
        return cursor;
    }

    /**
     * Alle gespeicherten Items der entsprechenden Kategorie ID in einen Cursor laden.
     */
    public Cursor getAllItemsOfCategory(long grpID) {
        openReadableDB();
        String[] cols = new String[]{DBhelperClass.COL_NAME_ITM, DBhelperClass.COL_DESC_ITM, DBhelperClass.COL_FID_ITM};
        String whereClause = DBhelperClass.COL_FID_ITM + "=?";
        String[] whereArgs = {String.valueOf(grpID)};
        Cursor cursor = database.query(DBhelperClass.TBL_ITM, cols, whereClause, whereArgs, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        closeDB();
        return cursor;
    }

    /**
     * Bisherige Tabellen löschen und neu anlegen.
     */
    public void checkDatabase() {
        openWritableDB();
        database.execSQL("DROP TABLE IF EXISTS " + DBhelperClass.TBL_ITM);
        database.execSQL("DROP TABLE IF EXISTS " + DBhelperClass.TBL_GRP);
        dbhelper.onCreate(database);
        closeDB();
    }
}