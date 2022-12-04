package com.tunc.xlocal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.tunc.xlocal.ComplaintsActivity;
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
    private ComplaintsActivity complaintsActivity;
    private FirebaseUser firebaseUser;
    public ComplaintAdapter(ArrayList<Complaint> arrayList, ComplaintsActivity complaintsActivity){
        this.complaintList = arrayList;
        this.complaintsActivity = complaintsActivity;
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

     Picasso.get().load(complaintList.get(position).postImage).into(holder.rowComplaintBinding.cardUserPostPic);
     Picasso.get().load(complaintList.get(position).ownerPostUserImage).into(holder.rowComplaintBinding.cardUserProfile);
     holder.rowComplaintBinding.cardUserName.setText(complaintList.get(position).ownerPostUserName);

     //postu siliyoruz.
     holder.rowComplaintBinding.cardDeletePostButton.setOnClickListener(view -> {
           firestore.collection("Post").document(complaintList.get(position).postId).delete().addOnSuccessListener(unused -> {

               firestore.collection("Complaints").addSnapshotListener((value, error) -> {
                     if (value.isEmpty()){
                     }else{
                         for (DocumentSnapshot document : value.getDocuments()){
                                if (document.get("post_id").equals(complaintList.get(position).postId)){
                                    firestore.collection("Complaints").document(document.getId()).delete();
                                    complaintsActivity.changedData();
                                }
                         }
                     }
                });
           });
     });


     holder.rowComplaintBinding.cardDeleteUserButton.setOnClickListener(view -> {
          
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