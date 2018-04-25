package com.example.walid.tfg;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import model.Apua;
import model.entities.Usuario;

import static Constants.Constants.MyPREFERENCES;
import static Constants.Constants.USUARIO;

public class EditUserDataActivity extends Fragment {

    private AutoCompleteTextView nombre;
    private AutoCompleteTextView apellido;
    private AutoCompleteTextView mEmailView;
    private EditText telefono;
    private EditText edad;
    private EditText descripcion;
    private ProgressBar mProgressBar;
    private View mEditFormView;
    private Apua apua;
    AsyncTask<Void, Integer, Usuario> loadingTaskEdit;
    private Usuario user;
    private static final int MAX_PROGRESS = 100;
    private SharedPreferences sharedpreferences;
    private String emailShared;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_user_data, container, false);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        emailShared = sharedpreferences.getString(USUARIO, null);
        nombre = (AutoCompleteTextView) view.findViewById(R.id.editName);
        apellido = (AutoCompleteTextView) view.findViewById(R.id.editFullName);
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.editEmail);
        //populateAutoComplete();
        telefono = (EditText) view.findViewById(R.id.editPhone);
        edad = (EditText) view.findViewById(R.id.editAge);
        descripcion = (EditText) view.findViewById(R.id.editDescription);
        apua =  new Apua(getActivity());
        user = new Usuario();
        user.setEmail(emailShared);
        if (loadingTaskEdit == null) {
            loadingTaskEdit = new LoadingTask(apua,user, "get");
            loadingTaskEdit.execute();
        }
        Button mEmailSignInButton = (Button) view.findViewById(R.id.editButton);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptEditPerfil();
            }
        });

        mEditFormView = view.findViewById(R.id.editPerfilform);
        mProgressBar = view.findViewById(R.id.editPprogress);
        mProgressBar.setMax(MAX_PROGRESS);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void attemptEditPerfil() {
        Usuario editUser = new Usuario();
        // Reset errors.
        mEmailView.setError(null);
        nombre.setError(null);
        apellido.setError(null);
        edad.setError(null);
        descripcion.setError(null);
        telefono.setError(null);

        // Store values at the time of the edit attempt.
        String email = mEmailView.getText().toString();
        String name = nombre.getText().toString();
        String fullName = apellido.getText().toString();
        String age = edad.getText().toString();
        String description = descripcion.getText().toString();
        String mobilPhone = telefono.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(mobilPhone)) {
            telefono.setError(getString(R.string.error_field_required));
            focusView = telefono;
            cancel = true;
        }

        else if ( !isMobileValid(mobilPhone)) {
            telefono.setError(getString(R.string.error_invalid_mobile));
            focusView = telefono;
            cancel = true;
        }

        if (TextUtils.isEmpty(age)) {
            edad.setError(getString(R.string.error_field_required));
            focusView = edad;
            cancel = true;
        }

        else if ( !isAgeValid(age)) {
            edad.setError(getString(R.string.error_invalid_age));
            focusView = edad;
            cancel = true;
        }

        if (TextUtils.isEmpty(description)) {
            descripcion.setError(getString(R.string.error_field_required));
            focusView = descripcion;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            nombre.setError(getString(R.string.error_field_required));
            focusView = nombre;
            cancel = true;
        }
        if (TextUtils.isEmpty(fullName)) {
            apellido.setError(getString(R.string.error_field_required));
            focusView = nombre;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            if (loadingTaskEdit == null) {
                editUser.setNombre(name);
                editUser.setApellido(fullName);
                editUser.setEmail(email);
                editUser.setTelefono(mobilPhone);
                editUser.setEdad(Integer.valueOf(age));
                editUser.setDescripcion(description);
                loadingTaskEdit = new LoadingTask(apua,editUser, "update");
                loadingTaskEdit.execute();
            }

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isMobileValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.length() >= 9;
    }
    private boolean isAgeValid(String edad) {
        //TODO: Replace this with your own logic
        return Integer.parseInt(edad) > 18;
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

            mEditFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mEditFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mEditFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mEditFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class LoadingTask extends AsyncTask<Void, Integer, Usuario> {
        private Apua apua;
        private Usuario user;
        private String action;


        public LoadingTask(Apua apua, Usuario user, String action) {
            this.apua = apua;
            this.user = user;
            this.action = action;
        }


        @Override
        protected void onPreExecute() {
            switch(action) {
                case "update":
                    mProgressBar.setProgress(0);
                    break;
            }
            super.onPreExecute();

        }

        @Override
        protected Usuario doInBackground(Void... voids) {
            Usuario usuario = null;
            try {
                switch(action) {
                    case "get":
                        usuario = apua.serverAgent.getUser(user.getEmail());
                    break;
                    case "update":
                        publishProgress(10);
                        publishProgress(20);
                        publishProgress(30);
                        publishProgress(40);
                        publishProgress(50);
                        publishProgress(60);
                        publishProgress(70);
                        publishProgress(80);
                        publishProgress(90);
                        publishProgress(100);
                        boolean succes = apua.serverAgent.updateUser(user);
                        if(succes)
                        usuario = user;
                    break;
                }

            } catch (Exception e) {
                cancel(true);
                Log.d("APPUA", "Error trying to edit user. ", e);
            }
            return usuario;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            switch(action) {
                case "update":
                    mProgressBar.setProgress(values[0]);
                    break;
            }

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Usuario user) {
            loadingTaskEdit = null;
            showProgress(false);
            if(user != null) {
                nombre.setText(user.getNombre());
                apellido.setText(user.getApellido());
                mEmailView.setText(user.getEmail());
                telefono.setText(user.getTelefono());
                edad.setText(String.valueOf(user.getEdad()));
                descripcion.setText(user.getDescripcion());
                switch(action) {
                    case "update":
                        Log.d("APPUA", "Los datos se han actualizado correctamente");
                        Toast.makeText(getActivity(), "Los datos se han actualizado correctamente ", Toast.LENGTH_SHORT).show();
                        break;
                }
                //Log.d("APPUA", "Los datos se han actualizado correctamente");
            }else
                Log.d("APPUA", "Error en el servidor intentalo mas tarde");
        }

        @Override
        protected void onCancelled(Usuario user) {
            super.onCancelled(user);
        }
    }
}
