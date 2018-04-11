package com.example.walid.tfg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.entities.Comment;

public class CommentActivity extends AppCompatActivity {
    private List<Comment> commentsList =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Bundle extras = getIntent().getExtras();
        commentsList =  extras.getParcelableArrayList("commentsList");
        Log.d("numero comentarios", String.valueOf(commentsList.size()));
    }
}
