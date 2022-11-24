package com.tunc.xlocal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tunc.xlocal.adapter.CommentAdapter;
import com.tunc.xlocal.adapter.FriendsAdapter;
import com.tunc.xlocal.databinding.ActivityFriendsBinding;
import com.tunc.xlocal.model.FollowRequest;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<FollowRequest> friendList;
    private View view;
    private ActivityFriendsBinding binding;
    private FriendsAdapter friendsAdapter;

    public FriendsActivity(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        friendList = new ArrayList<>();
        getFriendList();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.friendRecylerView.setLayoutManager(new LinearLayoutManager(this));
        friendsAdapter = new FriendsAdapter(friendList,this);
        binding.friendRecylerView.setAdapter(friendsAdapter);

    }

    public  void getFriendList(){
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Followers").addSnapshotListener((value, error) -> {
            if (value.isEmpty()){

            }else{

                for (DocumentSnapshot document : value.getDocuments()){
                    FollowRequest friend = new FollowRequest();
                    friend.userUuid = document.get("user_uuid").toString();
                    friend.userName = document.get("user_name").toString();
                    friend.photoUrl = document.get("profil_photo_url").toString();
                    friendList.add(friend);
                }
            }
        });
    }

    public void startChatWithFriends(FollowRequest followRequest){
        Intent goToChatWithFriend = new Intent( this,ChatActivity.class);
        goToChatWithFriend.putExtra("user",followRequest);
        startActivity(goToChatWithFriend);
    }
}