package com.example.finalproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.models.News;
import com.example.finalproject.models.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> newsList;
    private NewsClickListener newsClickListener;

    private Context context;

    public interface NewsClickListener {
        void onNewsClick(News news);
    }
    public NewsAdapter(List<News> newsList, Context context, NewsClickListener newsClickListener) {
        this.newsList = newsList;
        this.context = context;
        this.newsClickListener = newsClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.textTitle.setText(news.getTitle());
        holder.textContent.setText(news.getContent());
        holder.textTime.setText(news.getTime());
        holder.textAuthor.setText(news.getAuthor());

        String imageNews = news.getImageUrl();
        String imageUrl = "android.resource://com.example.finalproject/drawable/" + imageNews;
        // Load and display the image using Picasso
        Picasso.get().load(Uri.parse(imageUrl)).into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsClickListener.onNewsClick(news);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView textContent;
        private TextView textTime;
        private TextView textAuthor;
        private ImageView imageView;
        private CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textContent = itemView.findViewById(R.id.textContent);
            textTime = itemView.findViewById(R.id.textTime);
            textAuthor = itemView.findViewById(R.id.textAuthor);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}

