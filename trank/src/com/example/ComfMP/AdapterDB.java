package com.example.ComfMP;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Артём
 * Date: 17.01.14
 * Time: 18:11
 * To change this template use File | Settings | File Templates.
 */
public class AdapterDB {

    private MyDataBase _db;
    private SQLiteDatabase _sqdb;
    private Player _player = MyActivityComfMP._player;

    public AdapterDB(Context context)
    {
        _db = new MyDataBase(context);
        _sqdb = _db.getWritableDatabase();

        _player = MyActivityComfMP._player;
    }

    public void CreateTable(String _tableName)
    {
        _sqdb.execSQL("CREATE TABLE "
                + _tableName + " (" + MyDataBase.UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MyDataBase.NAME_COL + " VARCHAR(255),"
                + "PATH" + " VARCHAR(255));");
    }

    public ArrayList Select(String _tableName)
    {
        ArrayList _list  = new ArrayList();

        // Создание курсора. Он будет указывать на полученные данные из базы данных
        Cursor cursor = _sqdb.query(_tableName,
                new String[] {MyDataBase.UID, MyDataBase.NAME_COL },
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        // Выборка данных
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(MyDataBase.NAME_COL));
            _list.add(name);

            Log.d("MY_LOG_SELECT: ", name);
        }
        cursor.close();

        return _list;
    }

    public void InsertPlaylist(String _tableName, String _playlistName)
    {
        // Создание новой строки со значениями для вставки.
        ContentValues newValues = new ContentValues();
        // Задайте значения для каждой строки.
        newValues.put(MyDataBase.NAME_COL, _playlistName);
        // [ ... Повторить для каждого столбца ... ]
        // Вставка строки базу данных.
        _sqdb.insert(_tableName, null, newValues);

    }

    // Сохраняем состояние
    public void SaveState(String _tableName, String _playlistName, String _songName, int _duration)
    {
        DeleteAll(_tableName);

        // Создание новой строки со значениями для вставки.
        ContentValues newValues = new ContentValues();
        // Задайте значения для каждой строки.
        newValues.put(MyDataBase.PLAYLIST_CURRENT, _playlistName);
        newValues.put(MyDataBase.NAME_COL, _songName);
        newValues.put(MyDataBase.DURATION, _duration);
        // [ ... Повторить для каждого столбца ... ]
        // Вставка строки базу данных.
        _sqdb.insert(_tableName, null, newValues);

    }

    public void Load()
    {

        Log.d("MY_LOG", "Мы в Адаптере, load");

        ArrayList _list  = new ArrayList();

        // Создание курсора. Он будет указывать на полученные данные из базы данных
        Cursor cursor = _sqdb.query(MyDataBase.TABLE_CURRENT,
                new String[] {MyDataBase.UID, MyDataBase.PLAYLIST_CURRENT, MyDataBase.NAME_COL,
                        MyDataBase.SONG_CURRENT, MyDataBase.DURATION },
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );

        //cursor.moveToFirst();

       // Log.d("MY_LOG", "Мы в Adapter  LOAD: Название плейлиста = " + cursor.getString(cursor.getColumnIndex(MyDataBase.PLAYLIST_CURRENT)));
        //Log.d("MY_LOG", "Мы в Adapter  LOAD: Название песни = " + cursor.getString(cursor.getColumnIndex(MyDataBase.NAME_COL)));

        String _playlistName = " ";
        String _songName = " ";

        while (cursor.moveToNext()) {

            Log.d("MY_LOG", "Мы в Adapter  Название плейлиста: " + cursor.getString(cursor.getColumnIndex(MyDataBase.PLAYLIST_CURRENT)));
            Log.d("MY_LOG", "Мы в Adapter  Номер песни: " + cursor.getInt(cursor.getColumnIndex(MyDataBase.SONG_CURRENT)));
            Log.d("MY_LOG", "Мы в Adapter  Дурашион: " + cursor.getInt(cursor.getColumnIndex(MyDataBase.DURATION)));
            Log.d("MY_LOG", "Мы в Adapter  Название песни: " + cursor.getString(cursor.getColumnIndex(MyDataBase.NAME_COL)));


            _playlistName = cursor.getString(cursor.getColumnIndex(MyDataBase.PLAYLIST_CURRENT));
            _songName = cursor.getString(cursor.getColumnIndex(MyDataBase.NAME_COL));

            _player._currentPlaylist = _playlistName;
            _player._currentSong = cursor.getInt(cursor.getColumnIndex(MyDataBase.SONG_CURRENT));
            _player._duration = cursor.getInt(cursor.getColumnIndex(MyDataBase.DURATION));

            //_player._playlist = Select(_playlistName);
        }

        /*

         */
        cursor.close();

        if (_playlistName != " ")
        {
            SetParameter(_playlistName, _songName);
        }
    }

    public void InsertSong(String _tableName, String _nameSong, String _path)
    {
        // Создание новой строки со значениями для вставки.
        ContentValues _newValues = new ContentValues();

        // Задаем значение для каждого столбца.
        _newValues.put(MyDataBase.NAME_COL, _nameSong);
        _newValues.put("PATH", _path);

        // Вставка строки базу данных.
        _sqdb.insert(_tableName, null, _newValues);
    }

    public void DeleteAll(String _tableName)
    {
        _sqdb.delete(_tableName, null, null);
    }

    public void Delete(String _tableName, String _nameSong)
    {
        _sqdb.delete(_tableName, MyDataBase.NAME_COL + " = '" + _nameSong + "'", null);
    }

    public void DeleteTable(String _tableName)
    {
        _sqdb.execSQL("DROP TABLE IF EXISTS " + _tableName);
    }

    public void SetParameter(String _tableName, String _nameSong)
    {
        _player = MyActivityComfMP._player;
        ArrayList _listPath  = new ArrayList();
        ArrayList _listTitles  = new ArrayList();

        // Создание курсора. Он будет указывать на полученные данные из базы данных
        Cursor cursor = _sqdb.query(_tableName, new String[] {
                MyDataBase.UID, MyDataBase.NAME_COL, "PATH" },
                null,null, null, null, null);

        while (cursor.moveToNext())
        {
            String _path = cursor.getString(cursor.getColumnIndex("PATH"));
            String _title = cursor.getString(cursor.getColumnIndex(MyDataBase.NAME_COL));

            Log.d("MY_LOG", "Мы в SetParameter  Название песни: " + _title);

            // Если нашлась песня по названию, устанавливаем её текущей в плеере
            if (_nameSong.equals(cursor.getString(cursor.getColumnIndex(MyDataBase.NAME_COL))))
            {
                _player._currentSong = cursor.getInt(cursor.getColumnIndex("_id"));
                _player._path = _path;
            }

            _listTitles.add(_title);
            _listPath.add(_path);   // Формируем список песен (путей), чтобы задать в плеере как текущий
        }
        cursor.close();

        _player._currentPlaylist = _tableName;
        _player._titles = _listTitles;
        _player._playlist = _listPath;  // Устанавливаем полученный список текущим в плеере
    }

    public void Clear()
    {
        Log.d("MY_LOG", "Мы в Adapter  Clear ");

        // Подготовка запроса для получения названий всех таблиц
        String query = "SELECT name FROM sqlite_master "
                + "WHERE type='table'";

        // Выполнение запроса
        Cursor cursor2 = _sqdb.rawQuery(query, null);

        // перебор названий таблиц
        while (cursor2.moveToNext()) {
            String name = cursor2.getString(cursor2.getColumnIndex("name"));
            Log.d("MY_LOG", "Таблица: " + name);

            if(!name.equals("sqlite_sequence"))     // Эту таблицу нельзя удалять
                DeleteTable(name);
        }
        cursor2.close();
    }
}
