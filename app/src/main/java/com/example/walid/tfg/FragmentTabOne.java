package com.example.walid.tfg;

/**
 * Created by walid on 20/01/2018.
 */

import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

public class FragmentTabOne extends Fragment {
    ListView   listView ;
    private TextView selection;
    private static final String[] items = {"En", "un", "lugar", "de",
            "la", "Mancha", "de", "cuyo", "nombre", "no", "quiero",
            "acordarme", "no", "ha", "mucho", "tiempo", "que",
            "vivía", "un", "hidalgo", "de", "los", "de", "lanza",
            "en", "astillero", "adarga", "antigua", "rocín", "flaco",
            "y", "galgo", "corredor"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout_one, container, false);
        listView = (ListView ) v.findViewById(R.id.list);

        TextView textview = (TextView ) v.findViewById(R.id.empty);
        // Obtener el ListView
        FrameLayout frameLayout =
                v.findViewById(R.id.frame);
        if (items.length != 0) {
            listView.setVisibility(View.VISIBLE);
            frameLayout.getChildAt(1).setVisibility(View.INVISIBLE);
        }
        else {
            listView.setVisibility(View.INVISIBLE);
            frameLayout.getChildAt(1).setVisibility(View.VISIBLE);
            textview.setText(this.getTag() + " Content 1");

        }


        ListView listView = (ListView)frameLayout.getChildAt(0);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        items);


        listView.setAdapter(arrayAdapter);
        //listView.setOnItemSelectedListener(getActivity());
        //selection = (TextView) v.findViewById(R.id.elemento_seleccionado);
        /*TextView tv = (TextView) v.findViewById(R.id.text);
        tv.setText(this.getTag() + " Content 1");*/
        return v;
    }




    @Override
    public void onStart() {
        super.onStart();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //selection.setText(items[position]);
                //Toast.makeText(getActivity(), data.get(position).get("Cancion"), Toast.LENGTH_SHORT).show();
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( data.get(position).get("urls")));
                //startActivity(browserIntent);
            }
        });
    }
}