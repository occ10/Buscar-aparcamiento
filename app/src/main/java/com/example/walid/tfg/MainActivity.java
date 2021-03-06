package com.example.walid.tfg;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {
    private FragmentTabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(),
                android.R.id.tabcontent);
        Resources res = getResources();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("tab3");
        tab1.setIndicator("Anuncios", null);
        tab2.setIndicator("Publicar Anuncio", null);
        tab3.setIndicator("Buscar Parking", null);
        tabHost.addTab(tab1, FragmentTabOne.class, null);
        tabHost.addTab(tab2, FragmentTabTwo.class, null);
        tabHost.addTab(tab3, FragmentTabTree.class, null);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        tabHost = null;
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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menuSearch:
                intent = new Intent().setClass(
                        MainActivity.this, SearchAnounce.class);
                startActivity(intent);
                break;
            case R.id.closeSesion:
                intent = new Intent().setClass(
                        MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.searchUser:
                intent = new Intent().setClass(
                        MainActivity.this, SearchUserActivity.class);
                startActivity(intent);
                break;
            case R.id.editPerfil:
                intent = new Intent().setClass(
                        MainActivity.this, EditPerfilActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
