package com.example.ComfMP;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Артём
 * Date: 17.01.14
 * Time: 18:22
 * To change this template use File | Settings | File Templates.
 */
public class ControllerPLs {

    private AdapterDB _db;
    Context _context;

    public ControllerPLs(Context _context)
    {
        this._context = _context;
        _db = new AdapterDB(_context);
    }

    // Создание новой таблицы, соответствующей новому плейлисту
    public void CreateInDB(String _playlistName)
    {
        try{
            _db.CreateTable(_playlistName);
            _db.InsertPlaylist("Playlists", _playlistName);
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(_context.getApplicationContext(),
                    "Извините. Не удалось создать плейлист."
                    + "Название не должно содержать цифр.",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    // Получить список плейлистов
    public ArrayList GetPlaylists()
    {
        try{
            _db.CreateTable("Playlists");
        }
        catch (Exception e){
        }
        return _db.Select("Playlists");
    }

    // Открыть плейлист
    public void OpenPlaylist(Context _plActivity, String _namePlaylist)
    {
        Intent intent = new Intent(_plActivity, PlayList.class);
        intent.putExtra("PLAYLIST_NAME", _namePlaylist);
        _plActivity.startActivity(intent);
    }

    // Удаление плейлиста
    public void Delete(String _playlistName)
    {
        _db.DeleteTable(_playlistName);              // Удаление таблицы
        _db.Delete("Playlists", _playlistName);      // Удаление из списка
    }

    // Очистка базы данных
    public void Clear()
    {
        _db.Clear();
    }
}
