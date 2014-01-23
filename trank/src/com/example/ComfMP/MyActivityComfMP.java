package com.example.ComfMP;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;


public class MyActivityComfMP extends Activity {

    static Player _player;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        Log.d("MY_LOG", "Мы в главной активити, onCreate ");

        _player = new Player(this);
        _player.Load();
    }

    // обработчик на Play и Stop
    public void playAndStop(View v){
        _player.playAndStop(v);
    }

    public void onClickNext(View v){
        _player.Next();
    }

    public void onClickPrev(View v){
        _player.Previous();
    }

    // Обработка нажатия на кнопку "плейлисты"
    public void onClickBtnPlaylists(View v){
        Intent intent = new Intent(this, PlayLists.class);
        startActivity(intent);
    }

    // Обработка нажатия на кнопку "плейлисты"
    public void onClickBtnPlaylist(View v){
        _player.OpenPlaylist();
    }

    @Override
    public void onResume(){
        super.onResume();
        _player.InitSeekBar((SeekBar)findViewById(R.id.seekBar));
        /*
        try{
            _player.SetTitle((TextView)findViewById(R.id.textViewTitle));
        }
        catch (Exception e)
        {
            Log.d("MY_LOG_TITLE", "Нет названия песни");
        }    */
    }

    // При закрытии activity сначала вызывается onStop()
    @Override
    protected void onStop() {
        super.onStop();
    }

    // При уничтожение активити
    @Override
    protected void onDestroy(){

        if (_player.GetValid())
        {
            _player.Save();
            Log.d("MY_LOG", "СОХРАНИЛИСЬ!!");
        }
        else
        {
            Log.d("MY_LOG", "Не сохранились!!");
        }

        Log.d("MY_LOG", "Мы в onDestroy");

        super.onDestroy();
    }

}
