package com.example.ComfMP;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class ListSongs extends ListActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.main);
        GetPlayList();

    }
    /*
    // Обработка нажатия на список ListActivity
    protected void onListItemClick(android.widget.ListView l, View v,
                                   int position, long id) {

        // Вывод сообщения о выбранном элементе
        Toast.makeText(
                getApplicationContext(),
                "Вы выбрали "
                        + l.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();

    }     */


    // Обработка нажатия на список ListActivity
    protected void onListItemClick(android.widget.ListView l, View v,
                                   int position, long id) {

        Intent answerIntent = new Intent();
        answerIntent.putExtra("KEY_LISTSONGS", id);

        setResult(RESULT_OK, answerIntent);
        finish();   // Закрываем активити


        //Intent startIntent = new Intent(this, PlayActivity.class);
       // startIntent.putExtra("id", id);
       // startActivity(startIntent);
    }



    public void GetPlayList()
    {
        // Устанавливаем  доступ к списку аудиофайлов
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        // Создаем адаптер для связывания с курсором
        ListAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item,
                cursor,
                new String[] {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST},
                new int[] {android.R.id.text1, android.R.id.text2});

        // Связываем адаптер с ListActivity
        setListAdapter(adapter);
    }


}
