package com.sagr.newsreader.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sagr.newsreader.R;
import com.sagr.newsreader.activities.WebViewActivity;
import com.sagr.newsreader.models.NewsItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<NewsItem> news = new ArrayList<>();
    private Context context;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        holder.txtTitle.setText(news.get(holder.getAdapterPosition()).getTitle());
        holder.txtDescription.setText(news.get(holder.getAdapterPosition()).getDescription());
        holder.txtDate.setText(news.get(holder.getAdapterPosition()).getDate());
        Log.d("TAG", "onBindViewHolder: " + news.get(holder.getAdapterPosition()).getDate());
        Log.d("ADAPTER", "onBindViewHolder: " + news.get(holder.getAdapterPosition()) + news.get(holder.getAdapterPosition()).getImageUrl());
        Glide.with(context)
                .asBitmap()
                .load(
                        news.get(holder.getAdapterPosition()).getImageUrl()
                )
                .into(holder.imageView);

        holder.parent.setOnClickListener(v -> {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", news.get(holder.getAdapterPosition()).getLink());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void setNews(ArrayList<NewsItem> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle, txtDescription, txtDate;
        private CardView parent;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtContent);
            txtDate = itemView.findViewById(R.id.txtDate);
            parent = itemView.findViewById(R.id.parent);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}