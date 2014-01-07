package com.example.ComfMP;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;           //
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;            //
import android.view.View;                   //
import android.widget.Button;               //
import android.widget.SeekBar;              //
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MyActivityComfMP extends Activity {

    private Button buttonPlayStop;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;

    private Uri uri;    // Создаем uri, для проигрывания музыки

    private final Handler handler = new Handler();        // android OS

    static final private int CODE_PLAER_ACTIVITY = 1;          // Код активити, чтобы знать откуда пришли данные
    static final private int CODE_LISTSONGS_ACTIVITY = 2;

    private enum State { NOT_VALID, PLAYING, STOPPED, PAUSED }    // Состояния плеера
    private State state = State.NOT_VALID;                        // Переменная состояний плеера

    public Plaer _plaer;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        //initViews();
        mediaPlayer = new MediaPlayer();

    }

    // тут мы определяем кнопку и скролл, а также нашу песню с ресурсов в MediaPlayer.
    private void initViews() {
        buttonPlayStop = (Button) findViewById(R.id.buttonPlayStop);
        mediaPlayer = MediaPlayer.create(this, R.raw.vhod);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seekChange(v);
                return false;
            }
        });
    }



    // Обработка нажатия с возвратом данных (проверка)
    public void onClickBtnCheckReturn(View v){
        Intent intent = new Intent(this, Plaer.class);
        startActivityForResult(intent, CODE_PLAER_ACTIVITY);  // второй параметр - номер экрана

    }

    // Обработка нажатия на кнопку "плейлист"
    public void onClickShowListSongs(View v)
    {
        Intent _intent = new Intent(this, ListSongs.class);
        startActivityForResult(_intent, CODE_LISTSONGS_ACTIVITY);
    }

    // Прием данных со второй активити (с плейлтста)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView returnText = (TextView) findViewById(R.id.textViewReturnText); // для проверки

        // если данные поступили
        if (requestCode == CODE_PLAER_ACTIVITY) {
            // если данные не пустые и пришли из plaer активити
            if (resultCode == RESULT_OK) {
                // извлекаем данные (из ключа)
                String myReturnData = data.getStringExtra("KEY_PLAER");
                returnText.setText(myReturnData);
            }else {
                returnText.setText(""); // стираем текст
            }
        }

        // Обработка данных пришедших с плейлиста
        if (requestCode == CODE_LISTSONGS_ACTIVITY)
            // если данные пришли из ListSongs активити
            if (resultCode == RESULT_OK){

                // здесь мы получаем данные
                Intent intent = getIntent();
                long id = data.getLongExtra("KEY_LISTSONGS", -1);


                // Теперь надо указать проигрываелю, что ему воспроизводить
                LinkToSong(id);
                mediaPlayer = new MediaPlayer();
                prepare();
                Play();

                returnText.setText("Пришли данные из ListSongs активити");
            }
        else{
                returnText.setText("Мы были в ListSongs активити");
            }
        }

    private void LinkToSong(long id){
        if (id != -1) {
            uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
            ContentResolver contentResolver = getContentResolver();
            Cursor _cursor = contentResolver.query(uri, null, null, null, null);
            _cursor.moveToFirst();    // Переход к первому элементу (элемент будет всего один)

            // Заполняем инф-ю о песне
            TextView albumView = (TextView) findViewById(R.id.textViewReturnText);
            int _albumColumn = _cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            String _album = _cursor.getString(_albumColumn);
            albumView.setText(_album);

            _cursor.close();
        }
        else
        {
            //создаем и отображаем текстовое уведомление
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Проблема с передачей информации о песне!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    // Подготавливаем плеер
    // Метод mediaPlayer.prepare(); открывает файл и проверяет, можно ли его воспроизвести
    private boolean prepare() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        state = State.NOT_VALID;
        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            mediaPlayer.prepare();
            state = State.STOPPED;

            TextView text = (TextView) findViewById(R.id.textViewPrepear);
            text.setText("PPEPARE: stopped");

        } catch (Exception e) {
            TextView text = (TextView) findViewById(R.id.textViewPrepear);
            text.setText("PPEPARE: not valid");
            return false;
        }
        return true;
    }

    // Воспроизведение
    // Метод mediaPlayer.start(); можно вызывать только после подготовки плеера
    // т.е. после mediaPlayer.prepare();
    private boolean Play(){
        try {
            mediaPlayer.start();
            state = State.PLAYING;

            TextView text = (TextView) findViewById(R.id.textViewPlay);
            text.setText("PLAY");

        } catch (IllegalStateException e) {
            state = State.NOT_VALID;

            TextView text = (TextView) findViewById(R.id.textViewPlay);
            text.setText("PLAY: not valid");

            return false;
        }
        return true;
    }

    // Остановка проигрывания
    // После метода mediaPlayer.stop(); для того, чтобы вновь запустить плеер
    // нужно опять его подготовить - mediaPlayer.prepare();
    private boolean stop() {
        try {
            mediaPlayer.stop();
            state = State.STOPPED;

            TextView textStop = (TextView) findViewById(R.id.textViewStop);
            textStop.setText("STOP");


        } catch (IllegalStateException e) {
            state = State.NOT_VALID;

            TextView textStop = (TextView) findViewById(R.id.textViewStop);
            textStop.setText("STOP: not valid");

            return false;
        }
        return true;
    }

    private boolean pause() {
        try {
            mediaPlayer.pause();
            state = State.PAUSED;

            TextView text = (TextView) findViewById(R.id.textViewPause);
            text.setText("PAUSE");

        } catch (IllegalStateException e) {
            state = State.NOT_VALID;

            TextView text = (TextView) findViewById(R.id.textViewPause);
            text.setText("PAUSE: not valid");

            return false;
        }
        return true;
    }

    private void release() {
        mediaPlayer.release();
    }

    // обработчик на перемотку песни.
    private void seekChange(View v){
        if(mediaPlayer.isPlaying()){
            SeekBar sb = (SeekBar)v;
            mediaPlayer.seekTo(sb.getProgress());
        }
    }

    // обработчик на Play и Stop
    public void playAndStop(View v){

        if (state == State.PLAYING)
            pause();
        else if (state == State.STOPPED || state == State.PAUSED)
            Play();

        /*
        if (buttonPlayStop.getText() == getString(R.string.play_str)) {
            buttonPlayStop.setText(getString(R.string.pause_str));
            try{
                mediaPlayer.start();
                startPlayProgressUpdater();
            }catch (IllegalStateException e) {
                mediaPlayer.pause();
            }
        }else {
            buttonPlayStop.setText(getString(R.string.play_str));
            mediaPlayer.pause();
        }
        */
    }

    // метод, который определяет проигрывать при нажатии кнопки Play или Stop музыку или же остановить
    public void startPlayProgressUpdater() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();
                }
            };
            handler.postDelayed(notification,1000);
        }else{
            mediaPlayer.pause();
            buttonPlayStop.setText(getString(R.string.play_str));
            seekBar.setProgress(0);
        }
    }

    // при закрытии activity сначала вызывается onStop()
    @Override
    protected void onStop() {
        release();
        super.onStop();
    }

}
