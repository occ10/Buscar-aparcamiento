package com.example.walid.tfg;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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

import model.Apua;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class EditUserFotoActivity extends Fragment implements View.OnClickListener {
    AsyncTask<Void, Void, Void> loadingTask;
    private static int RESULT_LOAD_IMAGE = 1;
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView imageView;
    private EditText editText;
    //Uri to store the image uri
    private Uri filePath;
    //private static final String UPLOAD_URL = "http://10.0.2.2:8080/tfg/rest/UserService/saveFile";
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
        imageView = (ImageView) view.findViewById(R.id.editUserImage);

        //Setting clicklistener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        return view;
    }
    @Override
    public void onClick(View v) {
        String imageFile = "";
        if (v == buttonChoose) {
            showFileChooser();
        }
        if (v == buttonUpload) {
            final Apua apua = new Apua(getActivity());
            imageFile = getPathFromURI(filePath);
            if (loadingTask == null) {
                loadingTask = new LoadingTask(apua,imageFile);
                loadingTask.execute();
            }
        }
        Toast.makeText(getActivity(), "se ha hecho click en el button", Toast.LENGTH_SHORT).show();
        // Start the Intent
        //startActivityFromFragment(galleryIntent,RESULT_LOAD_IMG);
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

    public class LoadingTask extends AsyncTask<Void, Void, Void> {
        private Apua apua;
        private String imageFile;
        public LoadingTask(Apua apua, String imageFile) {
            this.apua = apua;
            this.imageFile = imageFile;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            boolean result;
            try {
                result =  apua.serverAgent.sendImage("kkk@kkk.com", imageFile);

            } catch (Exception e) {
                Log.d("UNIVERSITY", "Error trying to send image file. ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            loadingTask = null;
        }
    }
}
