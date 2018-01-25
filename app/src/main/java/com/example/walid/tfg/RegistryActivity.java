package com.example.walid.tfg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

public class RegistryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private String mAuthTask = null;
    private SharedPreferences prefs;
    // UI references.
    private AutoCompleteTextView nombre;
    private AutoCompleteTextView apellido;
    private AutoCompleteTextView mEmailView;
    private EditText telefono;
    private EditText mPasswordView;
    private EditText edad;
    private EditText descripcion;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        // Set up the login form.
        nombre = (AutoCompleteTextView) findViewById(R.id.nombre);
        apellido = (AutoCompleteTextView) findViewById(R.id.apellido);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email2);
        populateAutoComplete();

        telefono = (EditText) findViewById(R.id.telefono);
        mPasswordView = (EditText) findViewById(R.id.password2);
        edad = (EditText) findViewById(R.id.edad);
        descripcion = (EditText) findViewById(R.id.descripcion);
       /* mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });*/

        Button mEmailSignInButton = (Button) findViewById(R.id.button2);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.registry_form);
        mProgressView = findViewById(R.id.registry_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        nombre.setError(null);
        apellido.setError(null);
        edad.setError(null);
        descripcion.setError(null);
        telefono.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String nombre2 = nombre.getText().toString();
        String apellido2 = apellido.getText().toString();
        String edad2 = edad.getText().toString();
        String descripcion2 = descripcion.getText().toString();
        String telefono2 = telefono.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        else if ( !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(telefono2)) {
            telefono.setError(getString(R.string.error_field_required));
            focusView = telefono;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        else if ( !isMobileValid(telefono2)) {
            telefono.setError(getString(R.string.error_invalid_mobile));
            focusView = telefono;
            cancel = true;
        }

        if (TextUtils.isEmpty(edad2)) {
            edad.setError(getString(R.string.error_field_required));
            focusView = edad;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        else if ( !isAgeValid(edad2)) {
            edad.setError(getString(R.string.error_invalid_age));
            focusView = edad;
            cancel = true;
        }

        if (TextUtils.isEmpty(descripcion2)) {
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
        // Check for a valid name address.
        if (TextUtils.isEmpty(nombre2)) {
            nombre.setError(getString(R.string.error_field_required));
            focusView = nombre;
            cancel = true;
        }
        if (TextUtils.isEmpty(apellido2)) {
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
            Intent myIntent = new Intent(RegistryActivity.this, MainActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            RegistryActivity.this.startActivity(myIntent);
            /*mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/

           /* String stringUrl = "http://moodbooster.esy.es/moodbooster/public/api/usuario/login";
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d("Emai", mEmailView.getText().toString());
                new UserLoginTask().execute(stringUrl,mEmailView.getText().toString(),mPasswordView.getText().toString());

            } else {
                //textView.setText("No network connection available.");
                Log.d("MyApp", "no hay conexion");
            }*/
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
        return phone.length() > 6;
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), RegistryActivity.ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(RegistryActivity.ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegistryActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private class UserLoginTask  extends AsyncTask<String,Void, String> {

        Context context;
        public UserLoginTask() {


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            String pal="";
            //  ArrayList<Historial> lista_historial = new ArrayList<>();

            //Intent myIntent = new Intent(MainActivity.this, MainActivity2.class);
            // startActivity(myIntent);
            try {
                JSONObject rootObject = new JSONObject(result);
                //"data":"Login correcto"
                pal=rootObject.getString("data");
                Log.d("EL usuario es ",pal);
                if(pal.equalsIgnoreCase("Login correcto")){
                    Log.d("EL usuario es ","ha entrado");
                    prefs = getSharedPreferences("prefName", MODE_PRIVATE);
                    SharedPreferences preferencias=getSharedPreferences("datos",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferencias.edit();
                    editor.putString("mail", mEmailView.getText().toString());
                    editor.putString("password", mPasswordView.getText().toString());
                    editor.commit();
                    finish();

                    Intent myIntent = new Intent(RegistryActivity.this, MainActivity.class);
                    startActivity(myIntent);
                }else{


             /*   Context context = getApplicationContext();
                CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();*/
                }
                /// Log.d("MyApp", "Data" + pal);
            } catch (JSONException e) {
            }

        }

        @Override
        protected String doInBackground(String... params) {


            try {
                return downloadUrl(params[0],params[1],params[2]);
            } catch (IOException e) {
                return e.getMessage();
                // return "Unable to retrieve web page. URL may be invalid.";
            }


        }


        // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
        private String downloadUrl(String myurl,String email,String password) throws IOException {

            final String DEBUG_TAG = "HttpExample";
            InputStream is = null;
            OutputStream out=null;
            String data = "";
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // conn.setReadTimeout(10000 /* milliseconds */);
                //conn.setConnectTimeout(15000 /* milliseconds */);
                //conn.setRequestMethod("GET");
                //conn.setDoInput(true);

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);


                out = new BufferedOutputStream(conn.getOutputStream());
                // Starts the query

                HashMap<String, String> postDataParams=new HashMap<>();
                //Log.d("Emailxxxxx", email);
                postDataParams.put("email", email);
                postDataParams.put("password", password);



                String pal=getPostDataString(postDataParams);
                out.write(pal.getBytes());

                out.flush();
                out.close();
                //InputStream in = new BufferedInputStream(conn.getInputStream());
                int response = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response is: " + response);

                is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                Log.d("Mydata", data);
                br.close();
                // conn.disconnect();

            } finally {
                if (is != null ) {
                    is.close();
                }
           /* if (out != null ) {
                out.close();
            }*/

            }

            return data;
        }


        // Reads an InputStream and converts it to a String.
        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(entry.getKey());
                result.append("=");
                //Log.d("Emailyyyyy",entry.getValue());
                result.append(entry.getValue());
            }


            return result.toString();
        }



    }
}
