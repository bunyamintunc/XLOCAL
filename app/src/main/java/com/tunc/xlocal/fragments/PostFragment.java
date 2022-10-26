package com.tunc.xlocal.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tunc.xlocal.R;

public class PostFragment extends Fragment {



    View view;
    ImageView postImageInPostFragment;
    private Button btnPostPostFragment;
    private Uri uri;


    public PostFragment(Uri uri){
      this.uri = uri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.post_fragment,container,false);
        postImageInPostFragment = view.findViewById(R.id.ImageVievPostFragment);
        btnPostPostFragment = view.findViewById(R.id.postButtonInPostFragment);
        postImageInPostFragment.setImageURI(uri);
        btnPostPostFragment.setOnClickListener(view -> {
            Toast.makeText(getContext(), "I am working  ", Toast.LENGTH_SHORT).show();
        });
        return  view;
    }
}
