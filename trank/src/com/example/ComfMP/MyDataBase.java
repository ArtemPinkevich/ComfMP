package com.example.ComfMP;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Артём
 * Date: 08.01.14
 * Time: 2:42
 * To change this template use File | Settings | File Templates.
 */
public class MyDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "songs_database.db";
    private static final String TABLE_NAME = "Playlists";
    public static final String UID = "_id";
    public static final String NAME_COL = "name";
    private static final int DATABASE_VERSION = 1;    // Номер версии базы данны.
                                                // Как только программа заметила обновление номера базы,
                                                // она запускает метод onUpgrade()

    // Запрос на создание таблицы плейлистов
    private static final String CREATE_TABLE_PLAYLISTS = "CREATE TABLE "
                + TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COL + " VARCHAR(255));";

    private static final String DELETE_TABLE_PLAYLISTS = "DROP TABLE IF EXISTS " + TABLE_NAME;

    // Конструктор
    public MyDataBase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создаем таблицу, в которой будут храниться названия плейлистов
        db.execSQL(CREATE_TABLE_PLAYLISTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w("LOG_TAG", "Обновление базы данных с версии " + oldVersion
                + " до версии " + newVersion + ", которое удалит все старые данные");

        db.execSQL(DELETE_TABLE_PLAYLISTS);     // Удаляем предыдущую таблицу при апгрейде
        onCreate(db);                           // Создаём новый экземпляр таблицы
    }

}
