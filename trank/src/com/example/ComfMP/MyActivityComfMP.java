package com.example.ComfMP;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;


public class MyActivityComfMP extends Activity {

    static Player _player;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        _player = new Player(this);
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
        //_player.SetTitle((TextView)findViewById(R.id.textViewTitle));
    }

    // При закрытии activity сначала вызывается onStop()
    @Override
    protected void onStop() {
        super.onStop();
        // _player.Pause();
        // _player.release();
        //release();
    }

}
