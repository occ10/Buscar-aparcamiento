package com.example.walid.tfg;

/**
 * Created by walid on 20/01/2018.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import model.Apua;
import model.entities.Ruta;
import model.entities.Usuario;

/**
 * Created by walid on 14/01/2018.
 */

public class FragmentTabTwo extends Fragment {

    AsyncTask<Void, Void, Boolean >loadingTask;
    TextView origen;
    EditText precio;
    EditText plazas;
    EditText detallesRuta;
    Usuario usuario;
    private View mProgressView;
    private View mInsertFormView;

    public static FragmentTabTwo newInstance() {
        FragmentTabTwo fragment = new FragmentTabTwo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout_two, container, false);
        origen = (TextView) v.findViewById(R.id.origen);
        precio = (EditText) v.findViewById(R.id.precio);
        plazas = (EditText) v.findViewById(R.id.plazas);
        detallesRuta = (EditText) v.findViewById(R.id.detallesRuta);

        Bundle extras = getActivity().getIntent().getExtras();
        usuario = extras.getParcelable("usuario");
        Button publicarRuta = (Button) v.findViewById(R.id.buttonInsertRuta);
        publicarRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptInsertRoute();
            }
        });

        mInsertFormView = (View) v.findViewById(R.id.ruta_form);
        mProgressView = (View) v.findViewById(R.id.insertRoute_progress);
        return v;
    }

    private void attemptInsertRoute() {
        final Apua apua =  new Apua(getActivity());
        // Reset errors.
        origen.setError(null);
        precio.setError(null);
        plazas.setError(null);
        detallesRuta.setError(null);

        boolean cancel = false;
        View focusView = null;

        String mOrigen = origen.getText().toString();
        String mPrecio = precio.getText().toString();
        String mPlazas = plazas.getText().toString();
        String mDetallesRuta = detallesRuta.getText().toString();
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
        if (TextUtils.isEmpty(mPrecio)) {
            precio.setError(getString(R.string.error_field_required));
            focusView = precio;
            cancel = true;
        } else if (!isOrigenValid(mPrecio)) {
            precio.setError(getString(R.string.error_invalid_price));
            focusView = precio;
            cancel = true;
        }

        if (TextUtils.isEmpty(mPlazas)) {
            plazas.setError(getString(R.string.error_field_required));
            focusView = plazas;
            cancel = true;
        } else if (!isOrigenValid(mPlazas)) {
            plazas.setError(getString(R.string.error_invalid_place));
            focusView = plazas;
            cancel = true;
        }

        if (TextUtils.isEmpty(mDetallesRuta)) {
            detallesRuta.setError(getString(R.string.error_field_required));
            focusView = detallesRuta;
            cancel = true;
        } else if (!isOrigenValid(mDetallesRuta)) {
            detallesRuta.setError(getString(R.string.error_invalid_detallesRuta));
            focusView = detallesRuta;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            origen.setText("");
            precio.setText("");
            plazas.setText("");
            detallesRuta.setText("");
            showProgress(true);
            loadingTask = new FragmentTabTwo.LoadingTask(apua, mOrigen, mPrecio, mPlazas, mDetallesRuta, usuario);
            loadingTask.execute();

        }
        //}
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

            mInsertFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mInsertFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mInsertFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mInsertFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    private boolean isOrigenValid(String mOrigen) {

        return true;
    }


    public class LoadingTask extends AsyncTask<Void, Void, Boolean> {
        private Apua apua;
        private String origen;
        private String precio;
        private String plazas;
        private String detallesRuta;

        public LoadingTask(Apua apua,String origen,String precio,String plazas,String detallesRuta, Usuario usuario) {
            this.apua = apua;
            this.origen = origen;
            this.precio = precio;
            this.plazas = plazas;
            this.detallesRuta = detallesRuta;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean succes = false;
            try {
                Log.d("UNIVERSITY", "se llama al servidor........... ");
                succes = apua.serverAgent.insertRoute(origen, precio, plazas,detallesRuta,usuario);

            } catch (Exception e) {
                Log.d("UNIVERSITY", "Error trying to log. ", e);
            }
            return succes;
        }

        @Override
        protected void onPostExecute(Boolean succes) {

            if(succes){

                showProgress(false);
                loadingTask = null;
                Toast.makeText(getActivity(),
                        "Los datos se han insertado correctamente",
                        Toast.LENGTH_LONG).show();
            }
            //parkingsList = parkings;
            //dataAdapter.notifyDataSetChanged();
        }
    }
}