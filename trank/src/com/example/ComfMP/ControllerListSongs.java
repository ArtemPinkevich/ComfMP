package com.example.ComfMP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Артём
 * Date: 16.01.14
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */
public class ControllerListSongs{

    private AdapterDB _db;
    private Context _context;
    private String _playlistName;

    public ControllerListSongs(Context _context, String _playlistName)
    {
        this._context = _context;
        _db = new AdapterDB(_context);

        this._playlistName = _playlistName;
    }

    public void AddSongInDb(String _nameSong, String _path)
    {
        _db.InsertSong(_playlistName, _nameSong, _path);
    }

}
