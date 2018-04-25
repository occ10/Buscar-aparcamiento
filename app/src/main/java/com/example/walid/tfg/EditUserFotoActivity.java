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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import model.Apua;
import model.entities.Usuario;

import static Constants.Constants.*;
import static android.content.ContentValues.TAG;


public class EditUserFotoActivity extends Fragment implements View.OnClickListener {
    AsyncTask<Void, Void, Boolean> loadingTask;
    private static int RESULT_LOAD_IMAGE = 1;
    private Button buttonChoose;
    private Button buttonUpload;
    private Button deleteUserButton;
    private ImageView imageView;
    private EditText editText;
    private Uri filePath;
    private View mProgressView;
    private View insertUserImageView;
    private static final String service = "UserService";
    private SharedPreferences sharedpreferences;
    private String emailShared;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_user_foto, container, false);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        emailShared = sharedpreferences.getString(USUARIO, null);
        buttonChoose = (Button) view.findViewById(R.id.selectUserButton);
        buttonUpload = (Button) view.findViewById(R.id.uploadUserButton);
        deleteUserButton = (Button) view.findViewById(R.id.deleteUserButton);
        imageView = (ImageView) view.findViewById(R.id.editUserImage);
        insertUserImageView = view.findViewById(R.id.insertFotoLayout);
        mProgressView = view.findViewById(R.id.insertFotoProgress);
        showProgress(true);
        Picasso.with(getActivity()).load(IMAGE_PATH + emailShared).centerCrop()
        .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
        .error(R.drawable.unkonwnfoto)
        .resize(800,1200)
        .into(imageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                showProgress(false);
                buttonChoose.setVisibility(View.GONE);
                buttonUpload.setVisibility(View.GONE);
                deleteUserButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                showProgress(false);
            }
        });
        //Setting clicklistener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        deleteUserButton.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View v) {
        final Apua apua = new Apua(getActivity());
        String imageFile = "";
        if (v == buttonChoose) {
            showFileChooser();
        }
        else if (v == buttonUpload) {
            imageFile = getPathFromURI(filePath);
            if (loadingTask == null) {
                showProgress(true);
                loadingTask = new LoadingTask(apua,imageFile,"insert");
                loadingTask.execute();
            }
        }else{
            //imageFile = getPathFromURI(filePath);
            if (loadingTask == null) {
                showProgress(true);
                loadingTask = new LoadingTask(apua,imageFile,"del");
                loadingTask.execute();
            }
        }
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                buttonUpload.setVisibility(View.VISIBLE);
                Log.d(TAG, "onActivityResult: image uri: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getPathFromURI( Uri contentUri) {
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null
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

            insertUserImageView.setVisibility(show ? View.GONE : View.VISIBLE);
            insertUserImageView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    insertUserImageView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            insertUserImageView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class LoadingTask extends AsyncTask<Void, Void, Boolean> {
        private Apua apua;
        private String imageFile;
        private String action;

        public LoadingTask(Apua apua, String imageFile, String action) {
            this.apua = apua;
            this.imageFile = imageFile;
            this.action = action;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = false;
            Usuario user ;
            try {
                switch(action) {
                    case "insert":
                    result = apua.serverAgent.sendImage(emailShared, imageFile, service);
                    break;

                    case "del":
                        user = new Usuario();
                        user.setEmail(emailShared);
                        result = apua.serverAgent.deleteImage(user);
                        break;

                }

            } catch (Exception e) {
                Log.d("UNIVERSITY", "Error trying to send image file. ", e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            showProgress(false);
            loadingTask = null;
            if(result){
                switch(action) {
                    case "insert":
                        buttonChoose.setVisibility(View.GONE);
                        buttonUpload.setVisibility(View.GONE);
                        deleteUserButton.setVisibility(View.VISIBLE);
                        break;
                    case "del":
                        imageView.setImageResource(R.drawable.unkonwnfoto);
                        buttonChoose.setVisibility(View.VISIBLE);
                        deleteUserButton.setVisibility(View.GONE);
                        break;

                }
            }
        }
    }
}
