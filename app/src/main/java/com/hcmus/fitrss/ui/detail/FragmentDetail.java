package com.hcmus.fitrss.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hcmus.fitrss.R;
import com.hcmus.fitrss.UtilsTextView;
import com.hcmus.fitrss.databinding.FragmentDetailBinding;
import com.hcmus.fitrss.model.FeedItem;
import com.hcmus.fitrss.ui.news.NewsViewModel;

public class FragmentDetail extends BottomSheetDialogFragment {
    private AppCompatActivity activity;
    private FragmentDetailBinding binding;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(activity).get(NewsViewModel.class);

        viewModel.getIndexModel().observe(getViewLifecycleOwner(), integer -> {
            FeedItem item = viewModel.getSelectedItem();
            if (item != null){
                UtilsTextView.setText(binding.description, item.getDescription());
            }
        });
    }
}
