package com.example.tester.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResultAdapter extends ArrayAdapter<String>{

    Context context;
    String[] elements;
    int[][] res;

    public ResultAdapter(Context context, String[] list, int[][] res) {
        super(context, R.layout.result_element, list);
        this.context = context;
        this.elements = list;
        this.res = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View currView = inflater.inflate(R.layout.result_element, parent, false);
        ((TextView) currView.findViewById(R.id.element_name_res)).setText(elements[position]);
        ((TextView) currView.findViewById(R.id.element_res)).setText(res[0][position] + " correct, " +(res[1][position]-res[0][position]) + " incorrect.");
        return currView;
    }
}
