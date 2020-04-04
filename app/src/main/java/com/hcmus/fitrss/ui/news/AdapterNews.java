package com.hcmus.fitrss.ui.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.fitrss.AppConfig;
import com.hcmus.fitrss.OnItemClickedListener;
import com.hcmus.fitrss.UtilsTextView;
import com.hcmus.fitrss.UtilsTime;
import com.hcmus.fitrss.databinding.FeedItemBinding;
import com.hcmus.fitrss.model.FeedItem;

import java.util.ArrayList;

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.NewsViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private OnItemClickedListener listener;
    private ArrayList<FeedItem> items = new ArrayList<>();

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        FeedItemBinding binding = FeedItemBinding.inflate(inflater, parent, false);
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final @NonNull NewsViewHolder holder, final int position) {
        holder.binding.view.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClick(position);
        });
        final FeedItem item = items.get(position);
        holder.binding.setItem(item);
        UtilsTextView.setText(holder.binding.publicTime, UtilsTime.toFormat(item.getPublicDate(), AppConfig.DATE_TIME_FORMAT_SHORT));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        FeedItemBinding binding;

        public NewsViewHolder(FeedItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public AdapterNews(Context context) {
        this.context = context;
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.listener = onItemClickedListener;
    }

    public void update(ArrayList<FeedItem> updatedList) {
        DiffUtil.Callback callback = new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return items.size();
            }

            @Override
            public int getNewListSize() {
                return updatedList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return true;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return false;
            }
        };

        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        this.items.clear();
        this.items.addAll(updatedList);
        diffResult.dispatchUpdatesTo(this);
    }
}
