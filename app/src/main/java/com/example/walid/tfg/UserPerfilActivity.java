package com.example.walid.tfg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserPerfilActivity extends AppCompatActivity {
    Boolean QuickFactsExpanded = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_perfil);
        Log.d("Activity :","UserPerfilActivity");
        getSupportActionBar().setHomeButtonEnabled(true);

        final ImageButton showDetalle = (ImageButton) findViewById(R.id.downButtonPerfil);

        showDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TextView routeDetal = (TextView) findViewById(R.id.routeDetail);

                if(QuickFactsExpanded) {
                    QuickFactsExpanded = false;
                    //routeDetal.setVisibility(View.VISIBLE);
                    showDetalle.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }else{
                    QuickFactsExpanded = true;
                    //routeDetal.setVisibility(View.GONE);
                    showDetalle.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
        // this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_buscar:
                Intent intent = new Intent().setClass(
                        UserPerfilActivity.this, SearchAnounce.class);
                startActivity(intent);
                break;
            /*case R.id.acercaDe:
                //lanzarAcercaDe();
                break;*/
        }
        return true;
    }
}
