package com.cst2335.project01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
/**
 *
 */
public class SoccerDB extends SQLiteOpenHelper {
    protected final static  String DATABASE_NAME = "SoccerDB";
    protected final static int VERSION_NUM = 3;
    public final static String TABLE_NAME = "FAV_DETAILS";
    public final static String TITLE_COL = "TITLE_NAME";
    public final static String DATE_COL = "GAME_DATE";
    public final static String URL_COL = "GAME_URL";
    public final static String IMG_COL = "IMG_URL";
    public final static String DES_COL ="DES_DETAILS";
    public final static String COL_ID = "_id";


    public SoccerDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);

    }

    /**
     * create a soccerDB with 6 columns:id, TITLE_NAME,GAME_DATE,GAME_URL, IMG_URL AND game description
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_COL + " text,"
                + DATE_COL + " text,"
                + IMG_COL + "  text,"
                + URL_COL + " text,"
                + DES_COL + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
}
