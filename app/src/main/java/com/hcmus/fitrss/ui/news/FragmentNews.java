package com.hcmus.fitrss.ui.news;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hcmus.fitrss.GridSpacingItemDecoration;
import com.hcmus.fitrss.R;
import com.hcmus.fitrss.databinding.FragmentNewsBinding;

public class FragmentNews extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private AppCompatActivity activity;
    private FragmentNewsBinding binding;
    private AdapterNews adapterNews;
    private NewsViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            this.activity = ((AppCompatActivity) context);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }

    public NavController getNavController() {
        return NavHostFragment.findNavController(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipeLayout.setOnRefreshListener(this);
        viewModel = new ViewModelProvider(activity).get(NewsViewModel.class);

        int column = 1, margin = getResources().getDimensionPixelSize(R.dimen.margin_8dp);
        binding.rvNews.addItemDecoration(new GridSpacingItemDecoration(column, margin, true));

        adapterNews = new AdapterNews(activity);
        adapterNews.setOnItemClickedListener(index -> {
            viewModel.select(index);
            getNavController().navigate(R.id.action_view_detail);});

        viewModel.getModel().observe(getViewLifecycleOwner(), feedItems -> adapterNews.update(feedItems));
        viewModel.fetch();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.rvNews.setAdapter(adapterNews);
    }

    @Override
    public void onRefresh() {
        binding.swipeLayout.postDelayed(() -> {
            viewModel.fetch();
            binding.swipeLayout.setRefreshing(false);
        }, 500);
    }
}
