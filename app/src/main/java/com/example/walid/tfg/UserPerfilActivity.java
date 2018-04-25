package com.example.walid.tfg;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import model.Apua;
import model.entities.Comment;
import model.entities.Ruta;
import model.entities.Usuario;

import static Constants.Constants.IMAGE_PATH;

public class UserPerfilActivity extends AppCompatActivity {
    private Boolean QuickFactsExpanded = true;
    private Usuario user = null;
    private AsyncTask<Void, Void, Void > loadingTask;
    private Apua apua;
    private LinearLayout firstLayoutPerfil;
    private TextView firstCommentPerfil;
    private TextView comentInfoTextPerfil;
    private ImageButton downButtonPerfil;
    private LinearLayout secondLayoutPerfil;
    private TextView secondCommentPerfil;
    private LinearLayout layoutContainerComments;
    private TextView link_comments;
    private TextView nameFirstCommentPerfil;
    private TextView nameSecondCommentPerfil;
    private TextView numComents;
    private TextView numTravels;
    private ImageView userPerfilImage;
    private List<Comment> commentsList = new ArrayList<>();
    private List<Ruta> rutasList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_perfil);
        getSupportActionBar().setHomeButtonEnabled(true);
        Bundle extras = getIntent().getExtras();
        user = extras.getParcelable("usuario");
        Log.d("user email: ",user.getEmail());

        TextView userPerfilName = (TextView) findViewById(R.id.userPerfilName);
        TextView userPerfilFulname = (TextView) findViewById(R.id.userPerfilFulname);
        TextView userPerfilAge = (TextView) findViewById(R.id.userPerfilAge);
        TextView userTelPerfil = (TextView) findViewById(R.id.userTelPerfil);
        TextView userEmailPerfil = (TextView) findViewById(R.id.userEmailPerfil);
        firstLayoutPerfil = (LinearLayout) findViewById(R.id.firstLayoutPerfil);
        firstCommentPerfil = (TextView) findViewById(R.id.firstCommentPerfil);
        comentInfoTextPerfil = (TextView) findViewById(R.id.comentInfoTextPerfil);
        secondLayoutPerfil = (LinearLayout) findViewById(R.id.secondLayoutPerfil);
        secondCommentPerfil = (TextView) findViewById(R.id.secondCommentPerfil);
        layoutContainerComments = (LinearLayout) findViewById(R.id.layoutContainerComments);
        link_comments = (TextView) findViewById(R.id.link_comments);
        nameFirstCommentPerfil = (TextView) findViewById(R.id.nameFirstCommentPerfil);
        nameSecondCommentPerfil = (TextView) findViewById(R.id.nameSecondCommentPerfil);
        numComents = (TextView) findViewById(R.id.numComents);
        numTravels = (TextView) findViewById(R.id.numTravels);
        userPerfilImage = (ImageView) findViewById(R.id.userPerfilImage);

        userPerfilName.setText(user.getNombre());
        userPerfilFulname.setText(user.getApellido());
        userPerfilAge.setText(String.valueOf(user.getEdad()) + "años");
        userTelPerfil.setText("Tel: " + user.getTelefono());
        userEmailPerfil.setText("Correo: " + user.getEmail());
        Picasso.with(UserPerfilActivity.this).load(IMAGE_PATH + user.getEmail()).centerCrop()
        //.placeholder(R.drawable.unkonwnfoto)
        .error(R.drawable.unkonwnfoto)
        .resize(800,800)
        .into(userPerfilImage, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }
            @Override
            public void onError() {
            }
        });

        apua =  new Apua(this);
        if (loadingTask == null) {
            loadingTask = new LoadingTask(apua, user.getEmail());
            loadingTask.execute();
        }

        downButtonPerfil = (ImageButton) findViewById(R.id.downButtonPerfil);
        downButtonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(QuickFactsExpanded) {
                    QuickFactsExpanded = false;
                    layoutContainerComments.setVisibility(View.VISIBLE);
                    downButtonPerfil.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    nameFirstCommentPerfil.setText(commentsList.get(0).getUser().getNombre() + " ");
                    firstCommentPerfil.setText(commentsList.get(0).getComment());
                    if(commentsList.size() > 1){
                        secondLayoutPerfil.setVisibility(View.VISIBLE);
                        nameSecondCommentPerfil.setText(commentsList.get(1).getUser().getNombre() + " ");
                        secondCommentPerfil.setText(commentsList.get(1).getComment());
                    }
                    if(commentsList.size() > 2){
                        link_comments.setVisibility(View.VISIBLE);
                        link_comments.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent().setClass(
                                        UserPerfilActivity.this, CommentActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelableArrayList("commentsList", (ArrayList<? extends Parcelable>) commentsList);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }

                        });
                    }
                }else{
                    QuickFactsExpanded = true;
                    layoutContainerComments.setVisibility(View.GONE);
                    downButtonPerfil.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            }
        });
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
                        UserPerfilActivity.this, SearchAnounce.class);
                startActivity(intent);
                break;
            case R.id.closeSesion:
                intent = new Intent().setClass(
                        UserPerfilActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.searchUser:
                intent = new Intent().setClass(
                        UserPerfilActivity.this, SearchUserActivity.class);
                startActivity(intent);
                break;
            case R.id.editPerfil:
                intent = new Intent().setClass(
                        UserPerfilActivity.this, EditPerfilActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    public class LoadingTask extends AsyncTask<Void, Void, Void> {
        private Apua apua;
        private String email;

        public LoadingTask(Apua apua, String email) {
            this.apua = apua;
            this.email = email;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<Comment> comments = null;
            try {
                commentsList = apua.serverAgent.getCommentsFromServer(email);
                rutasList = apua.serverAgent.getUserRutasFromServer(email);
                Log.d("rustas list size", String.valueOf(commentsList.size()));

            } catch (Exception e) {
                Log.d("APPUA", "Error trying to log. ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            loadingTask = null;
            numTravels.setText(String.valueOf(rutasList.size()) + " Viajes publicados");
            numComents.setText(String.valueOf(commentsList.size()) + " Comentarios Recibidos");
            if (commentsList.size() > 0) {
                comentInfoTextPerfil.setVisibility(View.GONE);
                downButtonPerfil.setVisibility(View.VISIBLE);

            }
            //showProgress(false);
        }
    }
}
