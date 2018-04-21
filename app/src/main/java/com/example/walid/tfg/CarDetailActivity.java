package com.example.walid.tfg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import model.Apua;
import model.entities.Car;

import static com.example.walid.tfg.EditUserFotoActivity.IMAGE_PATH;

public class CarDetailActivity extends Fragment {

    private Button insertCarButtonFragment;
    private Button deleteCarButtonFragment;
    AsyncTask<Void, Void, Car> loadingTask;
    private View insertCarProgressFragment;
    private View insertCarLayoutFragment;
    private TextView carModelFragment;
    private TextView carCategoryFragment;
    private TextView carColorFragment;
    private ImageView insertCarImageFragment;
    public static final String CAR_IMAGE_PATH = "http://10.0.2.2:8080/tfg/rest/CarService/getCarImage/";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_car_detail, container, false);
        final Apua apua = new Apua(getActivity());
        carModelFragment = view.findViewById(R.id.carModelFragment);
        carCategoryFragment = view.findViewById(R.id.carCategoryFragment);
        carColorFragment = view.findViewById(R.id.carColorFragment);
        insertCarImageFragment = view.findViewById(R.id.insertCarImageFragment);
        deleteCarButtonFragment = (Button) view.findViewById(R.id.deleteCarButtonFragment);
        insertCarButtonFragment = (Button) view.findViewById(R.id.insertCarButtonFragment);
        insertCarButtonFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent().setClass(
                        getActivity(), InsertUserCarActivity.class);
                startActivity(intent);
            }

        });
        deleteCarButtonFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });
        insertCarProgressFragment = view.findViewById(R.id.insertCarProgressFragment);
        insertCarLayoutFragment = view.findViewById(R.id.insertCarLayoutFragment);
        showProgress(true);
        if (loadingTask == null) {
            //showProgress(true);
            loadingTask = new LoadingTask(apua,"kkk@kkk.com");
            loadingTask.execute();
        }

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        //getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onStop(){
        super.onStop();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            insertCarLayoutFragment.setVisibility(show ? View.GONE : View.VISIBLE);
            insertCarLayoutFragment.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    insertCarLayoutFragment.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            insertCarProgressFragment.setVisibility(show ? View.VISIBLE : View.GONE);
            insertCarProgressFragment.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    insertCarProgressFragment.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            insertCarProgressFragment.setVisibility(show ? View.VISIBLE : View.GONE);
            insertCarLayoutFragment.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class LoadingTask extends AsyncTask<Void, Void, Car> {
        private Apua apua;
        private String email;

        public LoadingTask(Apua apua, String email) {
            this.apua = apua;
            this.email = email;
        }

        @Override
        protected Car doInBackground(Void... voids) {
            Car carResult = null;
            try {

                carResult = apua.serverAgent.getCarFromServer(email);
            } catch (Exception e) {
                cancel(true);
                Log.d("UNIVERSITY", "Error trying to get car. ", e);
            }
            return carResult;
        }

        @Override
        protected void onPostExecute(Car result) {
            showProgress(false);
            loadingTask = null;
            if(result != null){

                if(result.getImage() != null){
                    Log.d("car image", result.getImage());
                    Picasso.with(getActivity()).load(CAR_IMAGE_PATH + "kkk@kkk.com")
                            .placeholder(R.drawable.car)
                            .error(R.drawable.car)
                            //.resize(500,500)
                            .into(insertCarImageFragment, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
                                    //showProgress(false);
                                }

                                @Override
                                public void onError() {
                                    Log.d("UNIVERSITY", "no se ha podido cargar la imagen");
                                    //showProgress(false);
                                }
                            });
                }
                carModelFragment.setVisibility(View.VISIBLE);
                carModelFragment.setText("Modelo " + result.getModel());
                carCategoryFragment.setVisibility(View.VISIBLE);
                carCategoryFragment.setText("Marca " + result.getCategory());
                carColorFragment.setVisibility(View.VISIBLE);
                carColorFragment.setText("Color " + result.getColor());
                insertCarButtonFragment.setVisibility(View.GONE);
                deleteCarButtonFragment.setVisibility(View.VISIBLE);

            }

        }
        @Override
        protected void onCancelled() {
            showProgress(false);
            loadingTask = null;
            Toast.makeText(getActivity(),
                    "try mas later, an error has occured in the server",
                    Toast.LENGTH_LONG).show();

        }
    }
}
