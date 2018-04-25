package com.example.walid.tfg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import model.Apua;

import static android.content.ContentValues.TAG;
import static Constants.Constants.MyPREFERENCES;
import static Constants.Constants.USUARIO;

public class CarImageActivity extends AppCompatActivity implements View.OnClickListener{
    AsyncTask<Void, Void, Boolean> loadingTask;
    private static int RESULT_LOAD_IMAGE = 1;
    private Uri filePath;
    private Button selectCarImageButton;
    private Button uploadCarImageButton;
    private ImageView editCarImageUpload;
    private View insertCarImageProgress;
    private View insertCarImageLayout;
    private static final String service = "CarService";
    private SharedPreferences sharedpreferences;
    private String emailShared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_image);
        getSupportActionBar().setHomeButtonEnabled(true);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        emailShared = sharedpreferences.getString(USUARIO, null);
        selectCarImageButton = (Button) findViewById(R.id.selectCarImageButton);
        uploadCarImageButton = (Button) findViewById(R.id.uploadCarImageButton);
        editCarImageUpload = (ImageView) findViewById(R.id.editCarImageUpload);
        insertCarImageProgress = findViewById(R.id.insertCarImageProgress);
        insertCarImageLayout = findViewById(R.id.insertCarImageLayout);
        selectCarImageButton.setOnClickListener(this);
        uploadCarImageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Apua apua = new Apua(CarImageActivity.this);
        String imageFile = "";
        if (v == selectCarImageButton) {
            showFileChooser();
        }
        else if (v == uploadCarImageButton) {
            imageFile = getPathFromURI(filePath);
            if (loadingTask == null) {
                showProgress(true);
                loadingTask = new LoadingTask(apua,imageFile);
                loadingTask.execute();
            }
        }
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
                        CarImageActivity.this, SearchAnounce.class);
                startActivity(intent);
                break;
            case R.id.closeSesion:
                intent = new Intent().setClass(
                        CarImageActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.editPerfil:
                intent = new Intent().setClass(
                        CarImageActivity.this, EditPerfilActivity.class);
                startActivity(intent);
                break;
            case R.id.searchUser:
                intent = new Intent().setClass(
                        CarImageActivity.this, SearchUserActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK){
            filePath  = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                editCarImageUpload.setImageBitmap(bitmap);
                uploadCarImageButton.setVisibility(View.VISIBLE);
                Log.d(TAG, "onActivityResult: image uri: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getPathFromURI( Uri contentUri) {
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null
                , MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        Log.d("path to image", path);
        cursor.close();

        return path;
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

            insertCarImageLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            insertCarImageLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    insertCarImageLayout.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            insertCarImageProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            insertCarImageProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    insertCarImageProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            insertCarImageProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            insertCarImageLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    public class LoadingTask extends AsyncTask<Void, Void, Boolean> {
        private Apua apua;
        private String imageFile;

        public LoadingTask(Apua apua, String imageFile) {
            this.apua = apua;
            this.imageFile = imageFile;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = false;
            try {
                result = apua.serverAgent.sendImage(emailShared, imageFile, service);

            } catch (Exception e) {
                cancel(true);
                Log.d("UNIVERSITY", "Error trying to delete image file. ", e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            loadingTask = null;
            if(result){
                Log.d("UNIVERSITY", "the car is inserted correctly");
                Toast.makeText(CarImageActivity.this,
                        "the image is inserted correctly",
                        Toast.LENGTH_LONG).show();
                finish();
            }else{
                Toast.makeText(CarImageActivity.this,
                        "ther is un error, try again",
                        Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onCancelled() {
            showProgress(false);
            loadingTask = null;
            Toast.makeText(CarImageActivity.this,
                    "try mas later, an error has occured in the server",
                    Toast.LENGTH_LONG).show();

        }
    }
}
