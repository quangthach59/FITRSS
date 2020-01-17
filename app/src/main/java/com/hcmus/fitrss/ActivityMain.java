package com.hcmus.fitrss;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hcmus.fitrss.adapters.AdapterFeed;
import com.hcmus.fitrss.databinding.ActivityMainBinding;
import com.hcmus.fitrss.decorators.GridSpacingItemDecoration;

public class ActivityMain extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ActivityMainBinding binding;
    private AdapterFeed adapterFeed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @LayoutRes
        int layoutId = R.layout.activity_main;
        setContentView(layoutId);
        binding = DataBindingUtil.setContentView(this, layoutId);
        createComponent();
    }

    private void createComponent() {
        adapterFeed = new AdapterFeed(this);
        binding.rvFeed.setLayoutManager(new LinearLayoutManager(this));
        int column = 1;
        int margin = (getResources().getDimensionPixelSize(R.dimen.margin_8dp));
        RecyclerView.ItemDecoration decoration = new GridSpacingItemDecoration(column, margin, true);
        binding.rvFeed.addItemDecoration(decoration);
        binding.rvFeed.setAdapter(adapterFeed);
        binding.swipeLayout.setOnRefreshListener(this);

        adapterFeed.setOnItemClickedListener(new OnItemClickedListener() {
            @Override
            public void onItemClick(int index) {
                Toast.makeText(ActivityMain.this, Integer.toString(index), Toast.LENGTH_SHORT).show();
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                adapterFeed.fetch();
            }
        });
    }

    @Override
    public void onRefresh() {
        adapterFeed.refresh();
        binding.swipeLayout.setRefreshing(false);

    }
}
