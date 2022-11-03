package com.tunc.xlocal.fragments;

import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tunc.xlocal.R;
import com.tunc.xlocal.adapter.CommentAdapter;
import com.tunc.xlocal.databinding.CommentFragmentBinding;
import com.tunc.xlocal.databinding.PostFragmentBinding;
import com.tunc.xlocal.model.Comment;
import com.tunc.xlocal.model.User;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;


public class CommentFragment extends Fragment {

    private View view;
    private CommentFragmentBinding binding;
    private CommentAdapter commentAdapter;
    private Button btnDoComment,btnClose;
    private EditText commentEditText;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private String postId;
    private boolean isCurrentUserResult;
    private User user;
    private String userName,userDownloadUrl;
    private PostFragment postFragment;
    private String fragmentTag;


    public CommentFragment(String postId,PostFragment postFragment){
        this.postId = postId;
        this.postFragment = postFragment;
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = CommentFragmentBinding.inflate(inflater,container,false);
        view = binding.getRoot();







        btnDoComment = binding.btnComment;
        btnDoComment.setOnClickListener(view1 -> {
             if (commentEditText.equals("")){

             }else{
                 doComment();
             }
        });

        btnClose = binding.btnCommentClose;
        btnClose.setOnClickListener(view -> {
             postFragment.closeCommentFragment();
        });



        return view;
    }



    public void getComment(){

    }

    public void doComment(){
        HashMap<String,Object> commentData = new HashMap<>();
        commentData.put("user_uuid",auth.getCurrentUser().getUid());
        commentData.put("user_name",userName);
        commentData.put("comment",commentEditText.getText().toString());
        commentData.put("profil_photo_url",userDownloadUrl);

        firebaseFirestore.collection("PostTable").document(postId).collection("Comments").add(commentData);
    }

    public void getUserDetails(){
        if(isCurrentUserResult){
            firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                     userName = value.getData().get("userName").toString();
                     userDownloadUrl = value.getData().get("profilePhotoDowloadUrl").toString();
                }
            });
        }else{
            userName = auth.getCurrentUser().getEmail();
            userDownloadUrl = " ";
        }
    }

    public boolean isCurrentUser(){
        firebaseFirestore.collection("Users").addSnapshotListener((value, error) -> {
            if (value.isEmpty()){
                Toast.makeText(getContext(), "users table is empty", Toast.LENGTH_SHORT).show();
            }else{

                for(DocumentSnapshot document : value.getDocuments()){
                    if(document.getId() == auth.getCurrentUser().getUid()){
                        isCurrentUserResult = true;
                    }
                }

            }
        });
        return isCurrentUserResult;
    }

}
