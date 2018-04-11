package com.example.walid.tfg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.Apua;
import model.entities.Ruta;

public class SearchAnounce extends AppCompatActivity {

    EditText origen = null;
    AsyncTask<Void, Void, List<Ruta> > loadingTask;
    private List<Ruta> rutasList = new ArrayList<>();
    private View mProgressView;
    private View msearchAnounceFormView;
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
                attemptSearchAnounce();
            }
        });

        msearchAnounceFormView = findViewById(R.id.searchAnounceLayout);
        mProgressView = findViewById(R.id.search_anounce_progress);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            msearchAnounceFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            msearchAnounceFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    msearchAnounceFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            msearchAnounceFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void attemptSearchAnounce() {
        final Apua apua = new Apua(this);
        origen.setError(null);

        boolean cancel = false;
        View focusView = null;

        String mOrigen = origen.getText().toString();
        // Check for a valid email address.
        if (TextUtils.isEmpty(mOrigen)) {
            origen.setError(getString(R.string.error_field_required));
            focusView = origen;
            cancel = true;
        } else if (!isOrigenValid(mOrigen)) {
            origen.setError(getString(R.string.error_invalid_origin));
            focusView = origen;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);
            if (loadingTask == null) {
                loadingTask = new LoadingTask(apua,origen.getText().toString());
                loadingTask.execute();
            }
        }
    }
    private boolean isOrigenValid(String mOrigen) {

        return true;
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


    public class LoadingTask extends AsyncTask<Void, Void, List<Ruta>> {
        private Apua apua;
        private String origen;

        public LoadingTask(Apua apua,String origen) {
            this.apua=apua;
            this.origen = origen;
        }

        @Override
        protected List<Ruta> doInBackground(Void... voids) {
            List<Ruta> rutas = null;
            try {
                rutas = apua.serverAgent.getRutasOriginFromServer("kkk@kkk.com", origen);
                Log.d("rustas list size", String.valueOf(rutas.size()));

            } catch (Exception e) {
                Log.d("APPUA", "Error trying to log. ", e);
            }
            return rutas;
        }

        @Override
        protected void onPostExecute(List<Ruta> rutas) {
            loadingTask = null;

            if(rutas.size() > 0) {
                Log.d("rustas list size", String.valueOf(rutas.size()));
                Intent intent = new Intent().setClass(SearchAnounce.this, AnouncesList.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("mylist", (ArrayList<? extends Parcelable>) rutas);
                intent.putExtras(bundle);
                startActivity(intent);
                //showProgress(false);
            }else{
                //showProgress(false);
                Toast.makeText(SearchAnounce.this, "No hay resultados para esta busqueda ", Toast.LENGTH_LONG).show();

            }
            showProgress(false);
        }

    }
}
