package com.example.ComfMP;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
    import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Артём
 * Date: 18.01.14
 * Time: 18:40
 * To change this template use File | Settings | File Templates.
 */
public class Player {

    Context _context;
    private MediaPlayer _mediaPlayer;
    private enum State { NOT_VALID, PLAYING, STOPPED, PAUSED }    // Состояния плеера
    public State state = State.NOT_VALID;                        // Переменная состояний плеера
    private SeekBar _seekBar;
    private final Handler handler = new Handler();
    private View v;

    public ArrayList _playlist;
    public ArrayList _titles;
    public String _currentPlaylist = "";
    public int _currentSong = 0;
    public int _duration = 0;
    public String _path;

    public Player(Context _context)
    {
         this._context = _context;
        _mediaPlayer = new MediaPlayer();
    }

    public boolean GetValid()
    {
        if (state != State.NOT_VALID)
        {
            return true;
        }
        return false;
    }

    // Обработчик на Play и Stop
    public void playAndStop(View v)
    {
        Button _btn = (Button)v.findViewById(R.id.buttonPlayStop);
        if (state == State.PLAYING){
            Pause();
            _btn.setText("PAUSE");
        }
        else if ((state == State.STOPPED) || (state == State.PAUSED)){
            Play();
            _btn.setText("PLAY");
        }
        else {
            PrintMessageNotSelected("Извините. Песня не выбрана");
        }
    }

    // Инициализация скролла
    public void InitSeekBar(SeekBar _seekBar)
    {
        this._seekBar = _seekBar;
        _seekBar.setMax(_mediaPlayer.getDuration());
        _seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                seekChange(v);
                return false;
            }
        });
    }

    public void Save()
    {
        AdapterDB _tmp = new AdapterDB(_context);
        _tmp.SaveState(MyDataBase.TABLE_CURRENT, _currentPlaylist,
                (String)_titles.get(_currentSong - 1), _seekBar.getProgress());
    }

    public void Load()
    {
        AdapterDB _tmp = new AdapterDB(_context);
        _tmp.Load();

        Log.d("MY_LOG", "Мы в  Плеере, Load. Путь = " + _path);

        state = State.PAUSED;
        Prepare();

        if (_duration != 0)
        {
            _mediaPlayer.seekTo(_duration);
        }

    }

    public void SetTitle(TextView _textViev)
    {
        if (_currentSong != 0){
            _textViev.setText((String)_titles.get(_currentSong));
        }
    }

    // Обработчик на перемотку песни
    private void seekChange(View v)
    {
        if(_mediaPlayer.isPlaying()){
            SeekBar sb = (SeekBar)v;
            _mediaPlayer.seekTo(sb.getProgress());
        }
    }

    // Метод, который определяет проигрывать при нажатии кнопки Play или Stop музыку или же остановить
    public void StartPlayProgressUpdater()
    {
        _seekBar.setProgress(_mediaPlayer.getCurrentPosition());

        if (_mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    StartPlayProgressUpdater();
                }
            };
            handler.postDelayed(notification,1000);
        }
        else{
            _mediaPlayer.pause();
        }
    }

    // Подготавливаем плеер
    // Метод mediaPlayer.prepare(); открывает файл и проверяет, можно ли его воспроизвести
    private boolean Prepare() {

        _mediaPlayer.stop();
        _mediaPlayer = new MediaPlayer();

        _mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        state = State.NOT_VALID;
        try {
            _mediaPlayer.setDataSource(_path);
            _mediaPlayer.prepare();
            state = State.STOPPED;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // Воспроизведение
    // Метод mediaPlayer.start(); можно вызывать только после подготовки плеера
    // т.е. после mediaPlayer.prepare();
    private boolean Play()
    {
        try {
            _mediaPlayer.start();               // Запуск плеера
            state = State.PLAYING;              // Установка состояния плеера
            StartPlayProgressUpdater();         // Включение seekBar
        } catch (IllegalStateException e) {
            state = State.NOT_VALID;
            return false;
        }
        return true;
    }

    public boolean Pause()
    {
        try {
            _mediaPlayer.pause();
            state = State.PAUSED;
        } catch (IllegalStateException e) {
            state = State.NOT_VALID;
            return false;
        }
        return true;
    }

    public boolean Next()
    {
        // Проверяем состояние плеера
        if (state == State.NOT_VALID){
            PrintMessageNotSelected("Извините. Песня не выбрана");
            return false;
        }

        // Переключаемся на следующую песню
        if ((_currentSong++) >= _playlist.size() - 1){
            _currentSong = 0;
        }

        // Получаем путь следующей песни
        _path = (String)_playlist.get(_currentSong);

        Prepare();
        Play();

        return true;
    }

    public boolean Previous()
    {
        // Если песня не выбрана, выводим сообщение
        if (state == State.NOT_VALID){
            PrintMessageNotSelected("Извините. Песня не выбрана");
            return false;
        }

        // Переключаемся на предыдущую песню
        if ((_currentSong--) == 0){
            _currentSong = _playlist.size() - 1;
        }

        // Получаем путь предыдущей песни
        _path = (String)_playlist.get(_currentSong);

        Prepare();
        Play();

        return true;
    }

    // Проигрывание песни из плейлиста
    public void PlayFromPlaylist()
    {
        Prepare();
        Play();
    }

    // Вывод сообщения о том, что не выбрана песня для проигрывания
    private void PrintMessageNotSelected(String _message)
    {
        Toast toast = Toast.makeText(_context.getApplicationContext(),
                _message,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // Открыть текущий плейлист
    public void OpenPlaylist()
    {
        if (_currentPlaylist.length() > 0)
        {
            Intent intent = new Intent(_context, PlayList.class);
            intent.putExtra("PLAYLIST_NAME", _currentPlaylist);
            _context.startActivity(intent);
        }
        else
        {
            PrintMessageNotSelected("Плейлист не выбран");
        }
    }

    public void release()
    {
        _mediaPlayer.release();
    }

}
