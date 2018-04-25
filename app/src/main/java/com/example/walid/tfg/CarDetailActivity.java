package com.example.walid.tfg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import model.Apua;
import model.entities.Car;
import model.entities.Usuario;

import static Constants.Constants.MyPREFERENCES;
import static Constants.Constants.USUARIO;

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
    private Button insertCarImageButtonFragment;
    public static final String CAR_IMAGE_PATH = "http://10.0.2.2:8080/tfg/rest/CarService/getCarImage/";
    public static final String service = "CarService";
    private Apua apua;
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
        View view = inflater.inflate(R.layout.activity_car_detail, container, false);
        apua = new Apua(getActivity());
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        emailShared = sharedpreferences.getString(USUARIO, null);
        carModelFragment = view.findViewById(R.id.carModelFragment);
        carCategoryFragment = view.findViewById(R.id.carCategoryFragment);
        carColorFragment = view.findViewById(R.id.carColorFragment);
        insertCarImageFragment = view.findViewById(R.id.insertCarImageFragment);
        deleteCarButtonFragment = (Button) view.findViewById(R.id.deleteCarButtonFragment);
        insertCarButtonFragment = (Button) view.findViewById(R.id.insertCarButtonFragment);
        insertCarImageButtonFragment = (Button) view.findViewById(R.id.insertCarImageButtonFragment);
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

                if (loadingTask == null) {
                    showProgress(true);
                    loadingTask = new LoadingTask(apua,emailShared,"del");
                    loadingTask.execute();
                }
            }

        });
        insertCarImageButtonFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent().setClass(
                        getActivity(), CarImageActivity.class);
                startActivity(intent);
            }

        });
        insertCarProgressFragment = view.findViewById(R.id.insertCarProgressFragment);
        insertCarLayoutFragment = view.findViewById(R.id.insertCarLayoutFragment);
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
        showProgress(true);
        //if (loadingTask == null) {
            loadingTask = new LoadingTask(apua,emailShared, "get");
            loadingTask.execute();
        //}
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
        private String action;

        public LoadingTask(Apua apua, String email,String action) {
            this.apua = apua;
            this.email = email;
            this.action =action;
        }

        @Override
        protected Car doInBackground(Void... voids) {
            Car carResult = null;
            Usuario user ;
            try {

                switch(action) {
                    case "get":
                        carResult = apua.serverAgent.getCarFromServer(email);
                        break;
                    case "insert":
                        //carResult = apua.serverAgent.getCarFromServer(email);
                        break;
                    case "del":
                        apua.serverAgent.deleteCar(email);
                        break;
                }
            } catch (Exception e) {
                cancel(true);
                Log.d("UNIVERSITY", "Error trying to operate about car. ", e);
            }
            return carResult;
        }

        @Override
        protected void onPostExecute(Car result) {
            showProgress(false);
            loadingTask = null;
            switch(action) {
                case "get":

                    if (result != null) {
                        //if (!result.getImage().equals("null")) {
                            //Log.d("car image", result.getImage());
                            Picasso.with(getActivity()).load(CAR_IMAGE_PATH + email)
                                    .placeholder(R.drawable.car)
                                    .error(R.drawable.car)
                                    .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .into(insertCarImageFragment, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {
                                            Log.d("UNIVERSITY", "exit trying car. ");
                                            insertCarImageButtonFragment.setVisibility(View.VISIBLE);
                                            insertCarImageButtonFragment.setText("CAMBIAR IMAGEN");;
                                        }

                                        @Override
                                        public void onError() {
                                            Log.d("UNIVERSITY", "Error trying get car. ");
                                            insertCarImageButtonFragment.setVisibility(View.VISIBLE);
                                        }
                                    });
                        //}
                        carModelFragment.setVisibility(View.VISIBLE);
                        carModelFragment.setText("Modelo " + result.getModel());
                        carCategoryFragment.setVisibility(View.VISIBLE);
                        carCategoryFragment.setText("Marca " + result.getCategory());
                        carColorFragment.setVisibility(View.VISIBLE);
                        carColorFragment.setText("Color " + result.getColor());
                        insertCarButtonFragment.setVisibility(View.GONE);
                        deleteCarButtonFragment.setVisibility(View.VISIBLE);

                    }
                    break;
                case "del":
                    Toast.makeText(getActivity(),
                            "the car is deleted correctly",
                            Toast.LENGTH_LONG).show();
                    insertCarImageFragment.setImageResource(R.drawable.car);
                    carModelFragment.setText("");
                    carModelFragment.setVisibility(View.GONE);
                    carCategoryFragment.setText("");
                    carCategoryFragment.setVisibility(View.GONE);
                    carColorFragment.setText("");
                    carColorFragment.setVisibility(View.GONE);
                    insertCarButtonFragment.setVisibility(View.VISIBLE);
                    deleteCarButtonFragment.setVisibility(View.GONE);
                    insertCarImageButtonFragment.setVisibility(View.GONE);
                    break;
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
