package com.example.walid.tfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Apua;
import model.entities.Usuario;

import static Constants.Constants.*;

public class SearchUserActivity extends AppCompatActivity implements View.OnClickListener{

    private Button searchUserName;
    private Button searchUserOrigin;
    private Button searchUserPhone;
    private AutoCompleteTextView searchUserNameInput;
    private AutoCompleteTextView searchUserOriginInput;
    private AutoCompleteTextView searchUserPhoneInput;
    AsyncTask<String, Void, List<Usuario>> loadingTask;
    private SharedPreferences sharedpreferences;
    private String emailShared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        getSupportActionBar().setHomeButtonEnabled(true);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        emailShared = sharedpreferences.getString(USUARIO, null);
        searchUserName = findViewById(R.id.searchUserName);
        searchUserOrigin = findViewById(R.id.searchUserOrigin);
        searchUserPhone = findViewById(R.id.searchUserPhone);

        searchUserNameInput = (AutoCompleteTextView) findViewById(R.id.searchUserNameInput);
        searchUserOriginInput = (AutoCompleteTextView) findViewById(R.id.searchUserOriginInput);
        searchUserPhoneInput = (AutoCompleteTextView) findViewById(R.id.searchUserPhoneInput);

        searchUserName.setOnClickListener(this);
        searchUserOrigin.setOnClickListener(this);
        searchUserPhone.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.searchUser);
        item.setVisible(false);
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
                        SearchUserActivity.this, SearchAnounce.class);
                startActivity(intent);
                break;
            case R.id.closeSesion:
                intent = new Intent().setClass(
                        SearchUserActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.editPerfil:
                intent = new Intent().setClass(
                        SearchUserActivity.this, EditPerfilActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        final Apua apua = new Apua(this);
        String name = "";
        String origin = "";
        String phone = "";

        searchUserNameInput.setError(null);
        searchUserOriginInput.setError(null);
        searchUserPhoneInput.setError(null);
        if (v == searchUserName) {
            name =  searchUserNameInput.getText().toString();
            if (TextUtils.isEmpty(name)) {
                searchUserNameInput.setError(getString(R.string.error_field_required));
                searchUserNameInput.requestFocus();
            }else
            {
                if (loadingTask == null) {
                    //showProgress(true);
                    loadingTask = new LoadingTask(apua,name);
                    loadingTask.execute(USUARIO_NAME_PATH);
                }
            }
        }
        else if (v == searchUserOrigin) {
            origin =  searchUserOriginInput.getText().toString();
            if (TextUtils.isEmpty(origin)) {
                searchUserOriginInput.setError(getString(R.string.error_field_required));
                searchUserOriginInput.requestFocus();
            }else{
                if (loadingTask == null) {
                    //showProgress(true);
                    loadingTask = new LoadingTask(apua,origin);
                    loadingTask.execute(USUARIO_ORIGIN_PATH);
                }
            }
        }else {
            phone = searchUserPhoneInput.getText().toString();

            if (TextUtils.isEmpty(phone)) {
                searchUserPhoneInput.setError(getString(R.string.error_field_required));
                searchUserPhoneInput.requestFocus();
            } else if (!isPhoneValid(phone)) {
                searchUserPhoneInput.setError(getString(R.string.error_invalid_phone));
                searchUserPhoneInput.requestFocus();
            } else {

                if (loadingTask == null) {
                    //showProgress(true);
                    loadingTask = new LoadingTask(apua, phone);
                    loadingTask.execute(USUARIO_PHONE_PATH);
                }
            }
        }
    }
    private boolean isPhoneValid(String phone) {
        String regx = "[67]{1}\\d{8}";
        Pattern pat = Pattern.compile(regx);
        Matcher mat = pat.matcher(phone);
        return mat.matches() && phone.length() == 9;

    }

    public class LoadingTask extends AsyncTask<String, Void, List<Usuario>> {
        private Apua apua;
        private String filter;

        public LoadingTask(Apua apua, String filter) {
            this.apua = apua;
            this.filter = filter;
        }

        @Override
        protected List<Usuario> doInBackground(String... strings) {
            boolean result = false;
            List<Usuario> users = null;
            try {
                users = apua.serverAgent.getUserByFilter(strings[0] + emailShared + "/" + filter);

            } catch (Exception e) {
                cancel(true);
                Log.d("UNIVERSITY", "Error trying to get user.", e);
            }
            return users;
        }

        @Override
        protected void onPostExecute(List<Usuario> users) {
            //showProgress(false);
            loadingTask = null;
            if(users.size() > 0){
                if(users.size() > 1){
                    Intent intent = new Intent().setClass(
                            SearchUserActivity.this, UsersListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("users", (ArrayList<? extends Parcelable>) users);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent().setClass(
                            SearchUserActivity.this, UserPerfilActivity.class);
                    intent.putExtra("usuario", users.get(0));
                    startActivity(intent);
                }

            }else{
                Toast.makeText(SearchUserActivity.this,
                        "No result for this search",
                        Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onCancelled() {
            loadingTask = null;
            Toast.makeText(SearchUserActivity.this,
                    "try mas later, an error has occured in the server",
                    Toast.LENGTH_LONG).show();

        }
    }
}
