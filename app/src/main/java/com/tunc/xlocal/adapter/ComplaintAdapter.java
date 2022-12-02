package com.tunc.xlocal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.tunc.xlocal.FriendsActivity;
import com.tunc.xlocal.databinding.RowChatBinding;
import com.tunc.xlocal.databinding.RowComplaintBinding;
import com.tunc.xlocal.databinding.RowFrinedsBinding;
import com.tunc.xlocal.model.Complaint;
import com.tunc.xlocal.model.FollowRequest;

import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintHolder>{

    private ArrayList<Complaint> complaintList;
    private FriendsActivity friendsActivity;
    private Button btnDeletePost,btnDeleteUser;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    public ComplaintAdapter(ArrayList<Complaint> arrayList){
        this.complaintList = arrayList;
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


    }

    @NonNull
    @Override
    public ComplaintHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowComplaintBinding rowComplaintBinding = RowComplaintBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ComplaintHolder(rowComplaintBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintHolder holder, int position) {

     Picasso.get().load(complaintList.get(position).postImage).into(holder.rowComplaintBinding.postImage);
     Picasso.get().load(complaintList.get(position).ownerPostUserImage).into(holder.rowComplaintBinding.userImage);
     holder.rowComplaintBinding.userName.setText(complaintList.get(position).ownerPostUserName);

     //postu siliyoruz.
     holder.rowComplaintBinding.btnDeletePost.setOnClickListener(view -> {
           firestore.collection("Post").document(complaintList.get(position).postId).delete().addOnSuccessListener(unused -> {
                notifyDataSetChanged();
           });
     });




    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    class ComplaintHolder extends RecyclerView.ViewHolder{

        RowComplaintBinding rowComplaintBinding;

        public ComplaintHolder( RowComplaintBinding rowComplaintBinding) {
            super(rowComplaintBinding.getRoot());
            this.rowComplaintBinding = rowComplaintBinding;
        }
    }

}