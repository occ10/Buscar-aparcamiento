package com.example.walid.tfg;

/**
 * Created by walid on 20/01/2018.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * Created by walid on 14/01/2018.
 */

public class FragmentTabTwo extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout_two, container, false);
        //TextView tv = (TextView) v.findViewById(R.id.text);
        //tv.setText(this.getTag() + " Content 2");
        return v;
    }
}