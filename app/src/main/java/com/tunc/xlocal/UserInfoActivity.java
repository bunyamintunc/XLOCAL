package com.tunc.xlocal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.tunc.xlocal.databinding.ActivityMapsBinding;
import com.tunc.xlocal.databinding.ActivityUserInfoBinding;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}