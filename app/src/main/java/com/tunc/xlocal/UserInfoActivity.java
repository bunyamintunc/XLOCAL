package com.tunc.xlocal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.tunc.xlocal.databinding.ActivityMapsBinding;
import com.tunc.xlocal.databinding.ActivityUserInfoBinding;

import java.util.HashMap;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;
    private Button btnFollow;
    private String userUuid;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private String userName,profilPhotoUrl;
    public UserInfoActivity(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Intent intent = getIntent();
        userUuid = intent.getStringExtra("user_uuid");

        btnFollow = binding.btnFollow;
        btnFollow.setOnClickListener(view1 -> {
            sendFollowRequest();

        });

    }


    public void sendFollowRequest(){

        HashMap<String,Object> followRequestData = new HashMap<>();
        followRequestData.put("request_owner_uuid",auth.getCurrentUser().getUid());
        followRequestData.put("user_name",userName);
        followRequestData.put("profil_photo_url",profilPhotoUrl);
        firebaseFirestore.collection("Users").document(userUuid).collection("FollowRequests").add(followRequestData).addOnSuccessListener(documentReference -> {
             hiddenFollowButton();
        });
    }

    public void getCurrentUser(){
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
             userName = value.getData().get("userName").toString();
             profilPhotoUrl = value.getData().get("profilePhotoDowloadUrl").toString();
        });
    }

    public void hiddenFollowButton(){
        btnFollow.setClickable(false);
        btnFollow.setBackgroundColor(ContextCompat.getColor(this,R.color.teal_200));

    }
}