package com.tunc.xlocal.fragments;

import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;
import com.tunc.xlocal.R;
import com.tunc.xlocal.adapter.CommentAdapter;
import com.tunc.xlocal.databinding.CommentFragmentBinding;
import com.tunc.xlocal.databinding.PostFragmentBinding;
import com.tunc.xlocal.model.Comment;
import com.tunc.xlocal.model.Post;
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


    private String userName,userDownloadUrl;
    private PostFragment postFragment;

    private Post post;
    private ImageView postImage;
    private TextView postDescription;

    private ArrayList<Comment> commentList;

    public CommentFragment(String postId,PostFragment postFragment,Post post){
        this.postId = postId;
        this.postFragment = postFragment;
        this.post = post;
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = CommentFragmentBinding.inflate(inflater,container,false);
        view = binding.getRoot();

        commentList = new ArrayList<>();



        getUserDetails();
        getComments();



        postDescription = binding.commentTextPost;
        postImage = binding.commentImageView;
        postDescription.setText(post.description);
        Picasso.get().load(post.postImageDownloadUrl).into(postImage);

        commentEditText = binding.inputCommentTextEdit;


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



    public void getComments(){
        firebaseFirestore.collection("PostTable").document(post.documentId).collection("Comments").addSnapshotListener((value, error) -> {

            if(value.isEmpty()){

            }else{

                for(DocumentSnapshot document : value.getDocuments()){
                   if (document.exists()){
                       Comment comment = new Comment();
                       comment.userName =  document.get("user_name").toString();
                       comment.comment = document.get("comment").toString();
                       comment.imageUrl =  document.get("profil_photo_url").toString();
                       comment.userUuid =  document.get("user_uuid").toString();
                       commentList.add(comment);
                   }else{

                   }

                }


            }

            binding.recyclerViewCommentFragment.setLayoutManager(new LinearLayoutManager(this.getContext()));
            commentAdapter = new CommentAdapter(commentList);
            binding.recyclerViewCommentFragment.setAdapter(commentAdapter);

        });



    }

    public void doComment(){

        HashMap<String,Object> commentData = new HashMap<>();
        commentData.put("user_uuid",auth.getCurrentUser().getUid());
        commentData.put("user_name",userName);
        commentData.put("comment",commentEditText.getText().toString());
        commentData.put("profil_photo_url",userDownloadUrl);

        firebaseFirestore.collection("PostTable").document(postId).collection("Comments").add(commentData).addOnSuccessListener(documentReference -> {
            increaseFieldOfUser("countOfComment",commentList.size());
        });
        commentEditText.setText("");
        commentList.clear();
        commentAdapter.notifyDataSetChanged();
    }





    public void getUserDetails(){

        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userName= auth.getCurrentUser().getEmail();
                userDownloadUrl = "null";
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userName = documentSnapshot.get("userName").toString();
                userDownloadUrl = documentSnapshot.get("profilePhotoDowloadUrl").toString();
            }
        });
    }

    public void increaseFieldOfUser(String name,int countOf){
        countOf = countOf+1;
        firebaseFirestore.collection("Users").document(post.userUudi).update(name,countOf);
    }



}
