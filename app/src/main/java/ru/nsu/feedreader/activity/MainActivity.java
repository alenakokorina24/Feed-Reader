package ru.nsu.feedreader.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nsu.feedreader.R;
import ru.nsu.feedreader.adapter.RecyclerViewAdapter;
import ru.nsu.feedreader.api.ApiClient;
import ru.nsu.feedreader.api.IApi;
import ru.nsu.feedreader.model.Article;
import ru.nsu.feedreader.model.Data;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = <api key>;
    private static final int PAGE_SIZE = 4;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;
    private boolean isLoading = false;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        loadJson();
        initAdapter();
        initScrollListener();
    }

    public void loadJson(){
        IApi iApi = ApiClient.getApiClient().create(IApi.class);
        Call<Data> call = iApi.getNews("bitcoin", PAGE_SIZE, page, API_KEY);

        call.enqueue(new Callback<Data>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body().getArticle() != null){
                    List<Article> newArticles = response.body().getArticle();
                    articles.addAll(newArticles);
                    page += 1;
                    recyclerViewAdapter.notifyDataSetChanged();

                    initListener();

                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                // ...
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

    private void initAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(articles, MainActivity.this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == articles.size() - 1) {
                        loadJson();
                        isLoading = true;
                    }
                }
            }
        });
    }
}