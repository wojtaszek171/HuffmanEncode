package com.example.pawel.huffman;

import android.content.Context;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.pawel.huffman.R;
import com.example.pawel.huffman.StaticticsFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by pawel on 02.02.2018.
 */

public class StatisticsAdapter extends ArrayAdapter<String>{
    private final Context context;
    private final HashMap<String,HashMap> items;
    private String[] mKeys;

    public StatisticsAdapter(Context context, HashMap<String,HashMap> items) {
        super(context, R.layout.char_item_row);
        this.context = context;
        this.items = items;
        mKeys = items.keySet().toArray(new String[items.size()]);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.char_item_row, parent, false);
        TextView currentChar = rowView.findViewById(R.id.currentChar);
        TextView ascii = rowView.findViewById(R.id.ascii);
        TextView interval = rowView.findViewById(R.id.interval);
        TextView count = rowView.findViewById(R.id.count);
        TextView code = rowView.findViewById(R.id.code);
        currentChar.setText(mKeys[position].toString());
        ascii.setText((items.get(mKeys[position])).get("ascii").toString());
        interval.setText(String.format("%.6f",Double.parseDouble((items.get(mKeys[position])).get("interval").toString())));
        count.setText((items.get(mKeys[position])).get("count").toString());
        code.setText((items.get(mKeys[position])).get("code").toString());


        return rowView;
    }

    @Override
    public int getCount() {
        return items.size();
    }



    @Override
    public long getItemId(int position) {
        return position;
    }
}
