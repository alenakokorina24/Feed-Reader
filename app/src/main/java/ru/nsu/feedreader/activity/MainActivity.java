package ru.nsu.feedreader.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsu.feedreader.R;
import ru.nsu.feedreader.adapter.RecyclerViewAdapter;
import ru.nsu.feedreader.api.ApiClient;
import ru.nsu.feedreader.api.IApi;
import ru.nsu.feedreader.model.Article;
import ru.nsu.feedreader.model.Data;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "599621708f7643cc833cb03914a668b4";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        onLoadingSwipeRefresh();
    }

    public void LoadJson(){
        swipeRefreshLayout.setRefreshing(true);
        IApi iApi = ApiClient.getApiClient().create(IApi.class);


        Call<Data> call = iApi.getNews(Locale.getDefault().getCountry().toLowerCase(), API_KEY);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body().getArticle() != null){
                    if (!articles.isEmpty()){
                        articles.clear();
                    }
                    articles = response.body().getArticle();
                    System.out.println(articles.size());
                    recyclerViewAdapter = new RecyclerViewAdapter(articles, MainActivity.this);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.notifyDataSetChanged();

                    initListener();

                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initListener(){
        recyclerViewAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);

            Article article = articles.get(position);
            intent.putExtra("title", article.getTitle());
            intent.putExtra("image",  article.getUrlToImage());
            intent.putExtra("desc",  article.getDescription());
            startActivity(intent);
        });
    }

    @Override
    public void onRefresh() {
        LoadJson();
    }

    private void onLoadingSwipeRefresh(){
        swipeRefreshLayout.post(this::LoadJson);
    }
}