package com.example.ComfMP;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Артём
 * Date: 14.01.14
 * Time: 22:27
 * To change this template use File | Settings | File Templates.
 */
public class PlayLists extends ListActivity {

    private ArrayList _playlists;
    private String _playlistName;
    private ControllerPLs _controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Указываем, что нужно отслеживать долгое нажатие
        getListView().setOnItemLongClickListener(myOnItemLongClickListener);

        _controller = new ControllerPLs(this);
        PrintList();
    }

    // Вывод списка плейлистов на экран
    public void PrintList()
    {
        _playlists = _controller.GetPlaylists();    // Получаем список плейлистов
        _playlists.add(0,"++ Новый плейлист ++");   // Вставляем в начало

        // Привязываем список плейлистов к адаптеру и выводим на активити
        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, _playlists);
        setListAdapter(adapter);
    }

    // Ввод названия плейлиста
    public void InputNamePlaylist()
    {
        AlertDialog.Builder _alert = new AlertDialog.Builder(this);
        _alert.setTitle("Название плейлиста");
        _alert.setMessage("Введите название плейлиста");

        // Создание поле для ввода названия пользователем
        final EditText input = new EditText(this);
        _alert.setView(input);

        _alert.setPositiveButton("Создать", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                // Получили название нового плейлиста
                // Пробуем создать таблицу в базе данных
                // и добавить новый плейдист к списку плейлистов
                _controller.CreateInDB(value);

                // Обновляем список плейлистов
                PrintList();
            }
        });

        _alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        _alert.show();
    }

    // Спрашиваем у пользователя подтверждение на удаление
    public void RequestConfirmation()
    {
        AlertDialog.Builder _alert = new AlertDialog.Builder(this);
        _alert.setMessage("Удалить " + _playlistName + "?");

        _alert.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                _controller.Delete(_playlistName);
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

    // Обработка нажатия на компонент ListActivity
    protected void onListItemClick(android.widget.ListView l, View v, int position, long id)
    {
        // Значение нажатого пункта списка
        _playlistName = (String) getListAdapter().getItem(position);

        if (id == 0){
            InputNamePlaylist();    // Просим пользователя ввести название нового плейлиста
            PrintList();            // Обновляем экран
        }
        else{
            _controller.OpenPlaylist(this, _playlistName);  // Открываем плейлист
        }
    }

    // Обработка длительного нажатия
    AdapterView.OnItemLongClickListener myOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            // Значение нажатого пункта списка
            _playlistName = (String) getListAdapter().getItem(position);

            // Запрашиваем подтверждение на удаление плейлиста
            RequestConfirmation();
            return true;
        }
    };

}
