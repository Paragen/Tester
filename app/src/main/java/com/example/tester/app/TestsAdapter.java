package com.example.tester.app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TestsAdapter extends ArrayAdapter<String>{

    Context context;
    String[][] elements;
    public TestsAdapter(Context context, String[][] list) {
        super(context, R.layout.list_element, list[0]);
        this.context = context;
        this.elements = list;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View currView = inflater.inflate(R.layout.list_element, parent, false);
        ((TextView) currView.findViewById(R.id.element_category)).setText(elements[1][position]);
        ((TextView) currView.findViewById(R.id.element_name)).setText(elements[0][position]);
        return currView;
    }
}
