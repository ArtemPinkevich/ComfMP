package com.example.ComfMP;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ListSongs extends ListActivity {

    ControllerListSongs _controller;
    private String _playlistName;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.main);

        // Получаем название плейлиста
        Intent intent = getIntent();
        _playlistName = intent.getStringExtra("PLAYLIST_NAME");

        _controller = new ControllerListSongs(this, _playlistName);

        PrintListSongs();
    }

    // Вывод всех песен на экран
    public void PrintListSongs()
    {
        // Устанавливаем  доступ к списку аудиофайлов
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        // Создаем адаптер для связывания с курсором
        ListAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item,     // Говорит о том, что на одной строке будут два текста (песня, автор)
                cursor,
                new String[] {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST},
                new int[] {android.R.id.text1, android.R.id.text2});

        // Связываем адаптер с ListActivity
        setListAdapter(adapter);
    }

    // Обработка нажатия на список ListActivity
    protected void onListItemClick(android.widget.ListView l, View v, int position, long id)
    {
        // Значение нажатого пункта списка
        Cursor _cursor = (Cursor) getListAdapter().getItem(position);
        String _nameSong = _cursor.getString(2);
        String _puthSong = _cursor.getString(1);

        // Добавляем песню в базу (в плейлист)
        _controller.AddSongInDb(_nameSong, _puthSong);

        // Вывод сообщения о выбранном элементе
        Toast.makeText(getApplicationContext(),
                "Добавлена " + _nameSong,
                Toast.LENGTH_SHORT).show();

        //_cursor.close();
    }

}
