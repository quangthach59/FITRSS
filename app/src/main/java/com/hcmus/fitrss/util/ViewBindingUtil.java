package com.hcmus.fitrss.util;

import androidx.databinding.BindingAdapter;

import com.google.android.material.card.MaterialCardView;

public class ViewBindingUtil {

    @BindingAdapter(value = {"rounded"})
    public static void setRounded(MaterialCardView view, boolean rounded) {
        if (rounded) {
            float width = view.getWidth();
            view.setRadius(width / 2.0f);
        }
    }
}
