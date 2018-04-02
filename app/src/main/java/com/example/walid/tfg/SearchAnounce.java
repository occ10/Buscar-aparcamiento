package com.example.walid.tfg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SearchAnounce extends AppCompatActivity {

    EditText origen = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_anounce);
        getSupportActionBar().setHomeButtonEnabled(true);
        origen = (EditText) findViewById(R.id.userOrigen);

        Button buscarAnuncio = (Button) findViewById(R.id.searchAnounceButton);
        buscarAnuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent().setClass(SearchAnounce.this, AnouncesList.class);
                intent.putExtra("origen", origen.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            /*case R.id.acercaDe:
                //lanzarAcercaDe();
                break;*/
        }
        return true;
    }
}
