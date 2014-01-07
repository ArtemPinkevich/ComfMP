package com.example.ComfMP;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;


/**
 * Created with IntelliJ IDEA.
 * User: Артём
 * Date: 04.01.14
 * Time: 18:48
 * To change this template use File | Settings | File Templates.
 */
public class Plaer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);
    }

    public void Print(View v)
    {
        //создаем и отображаем текстовое уведомление
        Toast toast = Toast.makeText(getApplicationContext(),
                "Моё сообщение!",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void onClickBtnReturn(View v)
    {
        Intent answerIntent = new Intent();
        answerIntent.putExtra("KEY_PLAER", "Возвращенный текст ");

        setResult(RESULT_OK, answerIntent);
        finish();   // Закрываем активити
    }
}
