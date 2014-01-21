package com.example.ComfMP;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Артём
 * Date: 14.01.14
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private ArrayList values;

    public MySimpleArrayAdapter(Context context, ArrayList values) {
        super(context, R.layout.playlists, values);
        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.playlists, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values.get(position).toString());


        // Изменение иконки для Windows и iPhone
        String s = values.get(position).toString();
        if (s.startsWith("Создать новый плейлист")) {
            imageView.setImageResource(NO_SELECTION);
        } else if(s.startsWith("Windows7")) {
            imageView.setImageResource(R.drawable.button_play);
        }
        else{
            imageView.setImageResource(R.drawable.button_pause);
        }

        return rowView;
    }
}
