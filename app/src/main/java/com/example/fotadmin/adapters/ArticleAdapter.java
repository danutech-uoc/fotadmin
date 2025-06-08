package com.example.fotadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fotadmin.R;
import com.example.fotadmin.models.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    public interface OnArticleActionListener {
        void onReadMoreClicked(Article article);
        void onEditClicked(Article article);
        void onDeleteClicked(Article article);
    }

    private Context context;
    private List<Article> articleList;
    private OnArticleActionListener listener;

    // Updated constructor to include listener
    public ArticleAdapter(Context context, List<Article> articleList, OnArticleActionListener listener) {
        this.context = context;
        this.articleList = articleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.article_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articleList.get(position);

        holder.articleTitle.setText(article.getTitle());
        holder.articleSummary.setText(article.getSummary());
        holder.articleDate.setText(article.getDate());

        Glide.with(context)
                .load(article.getImageUrl())
                .placeholder(R.drawable.ic_image_placeholder)
                .into(holder.articleImage);

        // Setup click listeners for actions
        holder.readMore.setOnClickListener(v -> {
            if (listener != null) listener.onReadMoreClicked(article);
        });

        holder.editBtn.setOnClickListener(v -> {
            if (listener != null) listener.onEditClicked(article);
        });

        holder.deleteBtn.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClicked(article);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView articleTitle, articleSummary, articleDate, readMore;
        ImageView articleImage, editBtn, deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            articleTitle = itemView.findViewById(R.id.articleTitle);
            articleSummary = itemView.findViewById(R.id.articleSummary);
            articleDate = itemView.findViewById(R.id.articleDate);
            articleImage = itemView.findViewById(R.id.articleImage);
            readMore = itemView.findViewById(R.id.readMore);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
