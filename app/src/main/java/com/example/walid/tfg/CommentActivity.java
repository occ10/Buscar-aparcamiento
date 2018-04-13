package com.example.walid.tfg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.entities.Comment;

public class CommentActivity extends AppCompatActivity {
    private List<Comment> commentsList =new ArrayList<>();
    private CommentsAdaptador commentsAdaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getSupportActionBar().setHomeButtonEnabled(true);
        Bundle extras = getIntent().getExtras();
        commentsList =  extras.getParcelableArrayList("commentsList");
        Log.d("numero comentarios", String.valueOf(commentsList.size()));
        RecyclerView rv = (RecyclerView) findViewById(R.id.commentsList);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        commentsAdaptador = new CommentsAdaptador();
        rv.setAdapter(commentsAdaptador);


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
                        CommentActivity.this, SearchAnounce.class);
                startActivity(intent);
                break;
            case R.id.closeSesion:
                intent = new Intent().setClass(
                        CommentActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.editPerfil:
                intent = new Intent().setClass(
                        CommentActivity.this, EditPerfilActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    class CommentsAdaptador extends RecyclerView.Adapter<FilaHolder> {

        @Override
        public FilaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return(new FilaHolder(getLayoutInflater()
                    .inflate(R.layout.comments_list, parent, false)));
        }

        @Override
        public void onBindViewHolder(final FilaHolder holder, int position) {
           // holder.ruta = commentsList.get(position);
            holder.bindModel(commentsList.get(position));
        }
        @Override
        public int getItemCount() {
            return(commentsList.size());
        }

    }
    static class FilaHolder extends RecyclerView.ViewHolder {
        public Comment comment;
        public final View mView ;
        public final TextView comentsContents;
        public final ImageView icono;

        FilaHolder(View fila) {
            super(fila);
            mView = fila;
            comentsContents = (TextView) fila.findViewById(R.id.comentsContents);
            icono = (ImageView)fila.findViewById(R.id.commentIcon);

        }
        private void bindModel(Comment comment) {
            comentsContents.setText(comment.getComment());
            icono.setImageResource(R.drawable.unkonwnfoto);

        }
    }
}
