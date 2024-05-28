package com.example.finalproject.activity.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.CardCategoryAdapter;
import com.example.finalproject.adapter.NewsAdapter;
import com.example.finalproject.adapter.RestaurantAdapter;
import com.example.finalproject.models.News;
import com.example.finalproject.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements NewsAdapter.NewsClickListener{

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsAdapter = new NewsAdapter(new ArrayList<>(), this, this);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewNews);
        LinearLayoutManager layoutManagerNews = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerNews);
        recyclerView.setAdapter(newsAdapter);

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsActivity.this, ProfileMemberActivity.class);
                startActivity(intent);
                finish();
            }
        });
        fetchNewsData();
    }

    private void fetchNewsData() {
        List<News> newsList = createNewsData();
        newsAdapter.setNewsList(newsList);
    }
    private List<News> createNewsData() {
        List<News> newsList = new ArrayList<>();

        // Create news articles and add them to the list
        News news1 = new News("Local Restaurant Introduces New Vegan Menu",
                "Romane Restaurant has recently launched a new vegan menu to cater to the growing demand for plant-based options.",
                "2023-07-14", "John Smith", "news1");
        newsList.add(news1);

        News news2 = new News("Celebrity Chef Opens New Fine Dining Establishment",
                "Renowned celebrity chef Jane Doe unveils her latest upscale restaurant in the heart of the city.",
                "2023-07-13", "Sarah Johnson", "news2");
        newsList.add(news2);

        News news3 = new News("Local Food Truck Festival Returns for 2023",
                "The annual food truck festival is back, bringing a diverse range of cuisines and a lively atmosphere to the city streets.",
                "2023-07-12", "Michael Thompson", "news3");
        newsList.add(news3);

        // Add more news articles as needed

        return newsList;
    }

    @Override
    public void onNewsClick(News news) {
        Intent intent = new Intent(NewsActivity.this, MapRestaurantActivity.class);
    }

    ///đọc file intro ở desktops
}
