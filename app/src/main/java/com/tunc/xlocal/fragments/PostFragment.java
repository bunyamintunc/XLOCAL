package com.tunc.xlocal.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.tunc.xlocal.MapsActivity;
import com.tunc.xlocal.R;
import com.tunc.xlocal.databinding.PostFragmentBinding;
import com.tunc.xlocal.model.Post;

public class PostFragment extends Fragment {

     //Post fragment icin degiskenler
    private View view;
    private Marker marker;
    private Button btnClose;
    private PostFragmentBinding binding;
    private ImageView postImageView;
    private Post post;
    private TextView description, countLike, countComment, countJoin, countConfirm;
    private MapsActivity mapsActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PostFragmentBinding.inflate(inflater,container,false);
        view = binding.getRoot();

        //view uzerindeki elemanlar bulunuyor.
        insertDefaultValues();

        return  view;
    }

    public PostFragment(Marker marker){
        this.marker = marker;
        this.post = (Post) marker.getTag();
    }

    public PostFragment(){

    }

    public void insertDefaultValues(){

        //postFragmenti maps fragmentten ayırmak icin yolunu bilmem gerekiyor
        mapsActivity = (MapsActivity) getActivity();

        postImageView = binding.imageViewPostFragment;
        description = binding.textMarkerDescription;
        countComment = binding.textCommnet;
        countConfirm = binding.textConfirm;;
        countJoin = binding.textJoin;
        countLike = binding.textLike;
        btnClose = binding.btnClose;
        Picasso.get().load(post.postImageDownloadUrl).into(postImageView);
        btnClose.setOnClickListener(view -> {
            mapsActivity.removePostFragmentInMapsFragment();
        });

        description.setText(post.description);
        countComment.setText(String.valueOf(post.countOfComment));
        countConfirm.setText(String.valueOf(post.countOfConfirm));
        countJoin.setText(String.valueOf(post.countOfJoin));
        countLike.setText(String.valueOf(post.countOfLike));


    }
}
