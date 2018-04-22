package com.example.walid.tfg;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import model.Apua;
import model.entities.Usuario;

import static android.app.Activity.RESULT_OK;
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
    public static final String IMAGE_PATH = "http://10.0.2.2:8080/tfg/rest/UserService/getImage/";
    private static final String service = "UserService";
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
        //Initializing views
        buttonChoose = (Button) view.findViewById(R.id.selectUserButton);
        buttonUpload = (Button) view.findViewById(R.id.uploadUserButton);
        deleteUserButton = (Button) view.findViewById(R.id.deleteUserButton);
        imageView = (ImageView) view.findViewById(R.id.editUserImage);
        insertUserImageView = view.findViewById(R.id.insertFotoLayout);
        mProgressView = view.findViewById(R.id.insertFotoProgress);
        showProgress(true);
        Picasso.with(getActivity()).load(IMAGE_PATH + "kkk@kkk.com").centerCrop()
        //.placeholder(R.drawable.unkonwnfoto)
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
                    result = apua.serverAgent.sendImage("kkk@kkk.com", imageFile, service);
                    break;

                    case "del":
                        user = new Usuario();
                        user.setEmail("kkk@kkk.com");
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
