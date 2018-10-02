package com.example.user.retrofitandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    List<String> Heronames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.mListView);
        //getMarvel();
        getMarvel2();

    }


//    private void getMarvel() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Api.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
//                .build();
//
//        Api api = retrofit.create(Api.class);
//
//        Call<List<Marvel>> call = api.getMarvel();
//
//        call.enqueue(new Callback<List<Marvel>>() {
//            @Override
//            public void onResponse(Call<List<Marvel>> call, Response<List<Marvel>> response) {
//                List<Marvel> heroList = response.body();
//
//                //Creating an String array for the ListView
//                String[] heroes = new String[heroList.size()];
//
//                //looping through all the heroes and inserting the names inside the string array
//                for (int i = 0; i < heroList.size(); i++) {
//                    heroes[i] = heroList.get(i).getName();
//                }
//
//
//                //displaying the string array into listview
//                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, heroes));
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Marvel>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void getMarvel2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);


        api.getMarvel2().flatMap(new Function<List<Marvel>, ObservableSource<Marvel>>() {
            @Override
            public ObservableSource<Marvel> apply(List<Marvel> marvels) throws Exception {
                return Observable.fromIterable(marvels);
            }
        }).flatMap(new Function<Marvel, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Marvel marvel) throws Exception {
                return Observable.just(marvel.getName());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

                Toast.makeText(getApplicationContext(), "Started loading Data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(String s) {
                Heronames.add(s);

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                Toast.makeText(getApplicationContext(), "Completed loading Data", Toast.LENGTH_SHORT).show();
                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, Heronames));

            }
        });


    }
}
