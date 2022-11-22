package com.tunc.xlocal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.tunc.xlocal.databinding.RecylerRowBinding;
import com.tunc.xlocal.databinding.RowFollowRequestBinding;
import com.tunc.xlocal.model.Comment;
import com.tunc.xlocal.model.FollowRequest;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowRequestAdapter extends RecyclerView.Adapter<FollowRequestAdapter.CommentHolder>{

    ArrayList<FollowRequest> requestArrayList;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    private String userProfilUrl;
    private String userName;
    private String userUuid;
    public FollowRequestAdapter(ArrayList<FollowRequest> arrayList){
        this.requestArrayList = arrayList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        getCurretUser();

    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowFollowRequestBinding followRequestBinding = RowFollowRequestBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new CommentHolder(followRequestBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {

      holder.followRequestBinding.followRequestUserName.setText(requestArrayList.get(position).userName);
      Picasso.get().load(requestArrayList.get(position).photoUrl).into(holder.followRequestBinding.userPhoto);
      holder.followRequestBinding.followRequestAccept.setOnClickListener(view -> {
           addFriend(requestArrayList.get(position));

      });
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder{

        RowFollowRequestBinding followRequestBinding;

        public CommentHolder( RowFollowRequestBinding followRequestBinding) {
            super(followRequestBinding.getRoot());
            this.followRequestBinding = followRequestBinding;
        }
    }

    public void addFriend(FollowRequest request){
        HashMap<String,Object> followRequestData = new HashMap<>();
        followRequestData.put("user_uuid",request.userUuid);
        followRequestData.put("user_name",request.userName);
        followRequestData.put("profil_photo_url",request.photoUrl);
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Followers").add(followRequestData).addOnSuccessListener(documentReference -> {

        });

        HashMap<String,Object> followBakcData = new HashMap<>();
        followBakcData.put("user_uuid",userUuid);
        followBakcData.put("user_name",userName);
        followBakcData.put("profil_photo_url",userProfilUrl);
        firebaseFirestore.collection("Users").document(request.userUuid).collection("Followers").add(followBakcData).addOnSuccessListener(documentReference -> {
            deleteFollowRequest(request.userUuid);
        });
    }

    public void deleteFollowRequest(String userUuid){
          firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("FollowRequests").addSnapshotListener((value, error) -> {
              if(value.isEmpty()){

              }else {
                  for(DocumentSnapshot document : value.getDocuments()){
                      if (document.get("request_owner_uuid").equals(userUuid)){
                          firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("FollowRequests").document(document.getId()).delete();
                          break;
                      }
                  }
              }
          });
    }

    public void getCurretUser(){
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
            userName = (String) value.getData().get("userName");
            userProfilUrl = (String) value.get("profilePhotoDowloadUrl");
            userUuid = auth.getCurrentUser().getUid();
        });
    }

}
