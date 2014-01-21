package com.example.ComfMP;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Артём
 * Date: 15.01.14
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
public class PlayList extends ListActivity {

    private ArrayList _playlist;
    private String _playlistName;
    private String _songName;
    private PlaylistController _controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Указываем, что нужно отслеживать долгое нажатие
        getListView().setOnItemLongClickListener(myOnItemLongClickListener);

        // Получаем название плейлиста
        Intent intent = getIntent();
        _playlistName = intent.getStringExtra("PLAYLIST_NAME");

        _controller = new PlaylistController(this, _playlistName);

        PrintList();
    }

    // Вывод на экран содержимого плейлиста
    public void PrintList()
    {
        // Просим у контроллера вернуть список песен
        _playlist = _controller.GetListSongs();
        _playlist.add(0,"++ Добавить песню ++");

        // Прикрепляем к адаптеру список песен
        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, _playlist);
        setListAdapter(adapter);
    }

    // Обработка нажатия на список ListActivity
    protected void onListItemClick(android.widget.ListView l, View v, int position, long id) {

        // Значение нажатого пункта списка
        String _songName = (String) getListAdapter().getItem(position);

        // Если нажали на "Добавить песню в плейлист"
        if (id == 0){
            _controller.AddSong();
        }
        else{
            _controller.Play(_songName);
        }
    }

    // Обработка длительного нажатия
    OnItemLongClickListener  myOnItemLongClickListener = new OnItemLongClickListener () {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                long id) {

            // Значение нажатого пункта списка
            _songName = (String) getListAdapter().getItem(position);

            RequestConfirmation();   // Спрашиваем у пользователя подтверждение на удаление
            return true;
        }

    };

    // Спрашиваем у пользователя подтверждение на удаление
    public void RequestConfirmation()
    {
        AlertDialog.Builder _alert = new AlertDialog.Builder(this);
        _alert.setMessage("Удалить " + _songName + "?");

        _alert.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                _controller.Delete(_songName);
                PrintList();    // Обновляем список плейлистов
            }
        });

        _alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        _alert.show();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        PrintList();
    }

}
