package com.example.fotadmin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fotadmin.R;
import com.example.fotadmin.adapters.ArticleAdapter;
import com.example.fotadmin.models.Article;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewsCategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ArticleAdapter adapter;
    private ArrayList<Article> articles = new ArrayList<>();

    private String categoryKey;

    public NewsCategoryFragment() {
        // Required empty constructor
    }

    public static NewsCategoryFragment newInstance(String categoryKey) {
        NewsCategoryFragment fragment = new NewsCategoryFragment();
        Bundle args = new Bundle();
        args.putString("categoryKey", categoryKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.newsRecyclerView);
        progressBar = view.findViewById(R.id.loadingProgressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ArticleAdapter(getContext(), articles, new ArticleAdapter.OnArticleActionListener() {
            @Override
            public void onReadMoreClicked(Article article) {
                // TODO: Implement action (e.g. open detail screen)
            }

            @Override
            public void onEditClicked(Article article) {
                // TODO: Implement edit action
            }

            @Override
            public void onDeleteClicked(Article article) {
                // TODO: Implement delete action
            }
        });

        recyclerView.setAdapter(adapter);

        if (getArguments() != null) {
            categoryKey = getArguments().getString("categoryKey");
        }

        loadArticles();
    }

    private void loadArticles() {
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("articles")
                .child(categoryKey);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                articles.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Article article = child.getValue(Article.class);
                    if (article != null) {
                        article.setId(child.getKey());
                        articles.add(article);
                    }
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                // Handle error here if needed
            }
        });
    }
}
