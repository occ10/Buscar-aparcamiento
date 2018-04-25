package com.example.walid.tfg;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import model.entities.Usuario;
import static Constants.Constants.*;

public class UsersListActivity extends AppCompatActivity {

    private List<Usuario> users =new ArrayList<>();
    private UsersAdaptador usersAdaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        getSupportActionBar().setHomeButtonEnabled(true);
        Bundle extras = getIntent().getExtras();
        users =  extras.getParcelableArrayList("users");
        Log.d("numero usuraios", String.valueOf(users.size()));
        RecyclerView rv = (RecyclerView) findViewById(R.id.usersList);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        usersAdaptador = new UsersAdaptador();
        rv.setAdapter(usersAdaptador);
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
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menuSearch:
                intent = new Intent().setClass(
                        UsersListActivity.this, SearchAnounce.class);
                startActivity(intent);
                break;
            case R.id.closeSesion:
                intent = new Intent().setClass(
                        UsersListActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.searchUser:
                intent = new Intent().setClass(
                        UsersListActivity.this, SearchUserActivity.class);
                startActivity(intent);
                break;
            case R.id.editPerfil:
                intent = new Intent().setClass(
                        UsersListActivity.this, EditPerfilActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    class UsersAdaptador extends RecyclerView.Adapter<FilaHolder> {

        @Override
        public FilaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return(new FilaHolder(getLayoutInflater()
                    .inflate(R.layout.user_item, parent, false)));
        }

        @Override
        public void onBindViewHolder(final FilaHolder holder, int position) {
            holder.user = users.get(position);
            holder.bindModel(users.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent().setClass(
                            UsersListActivity.this, UserPerfilActivity.class);
                    intent.putExtra("usuario", holder.user);
                    startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
            return(users.size());
        }

    }
    class FilaHolder extends RecyclerView.ViewHolder {
        public Usuario user;
        public final View mView ;
        public final TextView listNameUser;
        public final TextView listFullNameUser;
        public final TextView listAgeUser;
        public final TextView listPhoneUser;
        public final TextView listEmailUser;
        public final ImageView userImageList;

        FilaHolder(View fila) {
            super(fila);
            mView = fila;
            listNameUser = (TextView) fila.findViewById(R.id.listNameUser);
            listFullNameUser = (TextView) fila.findViewById(R.id.listFullNameUser);
            listAgeUser = (TextView) fila.findViewById(R.id.listAgeUser);
            listPhoneUser = (TextView) fila.findViewById(R.id.listPhoneUser);
            listEmailUser = (TextView) fila.findViewById(R.id.listEmailUser);
            userImageList = (ImageView)fila.findViewById(R.id.userImageList);

        }
        private void bindModel(Usuario user) {
            listNameUser.setText(user.getNombre());
            listFullNameUser.setText(user.getApellido());
            listAgeUser.setText("Edad: " + String.valueOf(user.getEdad()));
            listPhoneUser.setText("Tel: " + user.getTelefono());
            listEmailUser.setText("Email: " + user.getEmail());
            Picasso.with(UsersListActivity.this).load(IMAGE_PATH + user.getEmail())
                    .error(R.drawable.unkonwnfoto)
                    //.resize(800,800)
                    .into(userImageList, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }
                        @Override
                        public void onError() {
                        }
                    });

        }
    }
}
