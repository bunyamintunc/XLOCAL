package com.tunc.xlocal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.tunc.xlocal.databinding.ActivityMapsBinding;
import com.tunc.xlocal.databinding.ActivityUserInfoBinding;
import com.tunc.xlocal.model.User;

import java.util.HashMap;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;
    private Button btnFollow;
    private String userUuid;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private String userName,profilPhotoUrl;
    private TextView textCountOfJoin,textCountOfComment,textCountOfComplaint;
    private User user;
    private ImageView userProfile;
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

        textCountOfComment = binding.textComment;
        textCountOfJoin = binding.textViewJoin;


        Intent intent = getIntent();
        userUuid = intent.getStringExtra("user_uuid");

        getUserInfo();


        btnFollow = binding.btnFollow;
        btnFollow.setOnClickListener(view1 -> {
            sendFollowRequest();

        });

        isThereRequestFollow();
        isAFollowers();




    }

    public void sendFollowRequest(){

        HashMap<String,Object> followRequestData = new HashMap<>();
        followRequestData.put("request_owner_uuid",auth.getCurrentUser().getUid());
        followRequestData.put("user_name",userName);
        followRequestData.put("profil_photo_url",profilPhotoUrl);
        firebaseFirestore.collection("Users").document(userUuid).collection("FollowRequests").add(followRequestData).addOnSuccessListener(documentReference -> {
             //hiddenFollowButton();
        });
    }

    public void getCurrentUser(){
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
             userName = value.getData().get("userName").toString();
             profilPhotoUrl = value.getData().get("profilePhotoDowloadUrl").toString();
             textCountOfJoin.setText(value.getData().get("countOfJoin").toString());
             textCountOfComment.setText(value.getData().get("countOfComment").toString());

        });
    }

    public void hiddenFollowButton(){
        btnFollow.setClickable(false);
        btnFollow.setBackgroundColor(ContextCompat.getColor(this,R.color.teal_200));

    }

    public void unFollowButton(){
        btnFollow.setBackgroundColor(ContextCompat.getColor(this,R.color.teal_700));
    }

    public void deleteFollowButton(){
         btnFollow.setVisibility(View.GONE);
    }

     // kullanicinin bu kişiye istek atıp atmadığına bakiyoruz
    public void isThereRequestFollow(){
        firebaseFirestore.collection("Users").document(userUuid).collection("FollowRequests").addSnapshotListener((value, error) -> {
            if (value.isEmpty()){
                Toast.makeText(this,"I am working",Toast.LENGTH_LONG).show();
            }else{
                for ( DocumentSnapshot document : value.getDocuments()){
                    if (document.get("request_owner_uuid").equals(auth.getCurrentUser().getUid())){
                        hiddenFollowButton();
                    }
                }
            }
        });
    }

    public void isAFollowers(){
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Followers").addSnapshotListener((value, error) -> {
           if (value.isEmpty()){

           }else{
               for(DocumentSnapshot document: value.getDocuments()){
                     if(document.get("user_uuid").equals(userUuid)){
                         unFollowButton();
                     }
               }
           }

        });
    }

    public void getUserInfo(){

        firebaseFirestore.collection("Users").document(userUuid).addSnapshotListener((value, error) -> {

            Picasso.get().load(value.getData().get("profilePhotoDowloadUrl").toString()).into(binding.imageView);
            binding.textViewUserName.setText(value.getData().get("userName").toString());
            binding.textComment.setText(value.getData().get("countOfComment").toString());
            binding.textConfirm.setText(value.getData().get("countOfConfirm").toString());
            binding.textViewJoin.setText(value.getData().get("countOfJoin").toString());
            binding.textViewLike.setText(value.getData().get("countOfLike").toString());
            binding.textFriendList.setText(value.getData().get("countOfFollowers").toString());
        });
    }


}