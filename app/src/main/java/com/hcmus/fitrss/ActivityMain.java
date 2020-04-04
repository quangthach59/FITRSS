package com.hcmus.fitrss;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

public class ActivityMain extends AppCompatActivity {
    //private ActivityMainBinding binding;
    //private NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding =
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        //navController =
        Navigation.findNavController(this, R.id.nav_host);
    }
}
