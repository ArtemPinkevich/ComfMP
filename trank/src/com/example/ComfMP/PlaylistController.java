package com.example.ComfMP;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Артём
 * Date: 16.01.14
 * Time: 17:48
 * To change this template use File | Settings | File Templates.
 */
public class PlaylistController  {

    private AdapterDB _db;
    private Context _context;
    private String _playlistName;
    private Player _player;

    public PlaylistController(Context _context, String _playlistName)
    {
        this._player = MyActivityComfMP._player;    // Связаваемся с плеером
        this._context = _context;                   // Устанавливаем активити Playlist
        _db = new AdapterDB(_context);              // Инициализируем адаптер для связи с БД
        this._playlistName = _playlistName;         // Устанавливаем название плейлиста
    }

    // Обработка нажатия на песню в плейлисте
    public void Play(String _songName)
    {
        _db.SetParameter(_playlistName, _songName);     // Установка параметров плеера
        _player.PlayFromPlaylist();                     // Запуск проигрывания песни
    }

    // Получить список песен для данного плейлиста
    public ArrayList GetListSongs()
    {
        return _db.Select(_playlistName);
    }

    // Показать список всех песен для добавления в плейлист
    public void AddSong()
    {
        Intent intent = new Intent(_context, ListSongs.class);
        intent.putExtra("PLAYLIST_NAME", _playlistName);
        _context.startActivity(intent);
    }

    // Удалить песню из плейлиста
    public void Delete(String _songName)
    {
        _db.Delete(_playlistName, _songName);
    }

}
