package com.tunc.xlocal.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tunc.xlocal.FriendsActivity;
import com.tunc.xlocal.databinding.RowChatBinding;
import com.tunc.xlocal.databinding.RowComplaintBinding;
import com.tunc.xlocal.databinding.RowFrinedsBinding;
import com.tunc.xlocal.model.Complaint;
import com.tunc.xlocal.model.FollowRequest;

import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintHolder>{

    ArrayList<Complaint> complaintList;
    private FriendsActivity friendsActivity;
    public ComplaintAdapter(ArrayList<Complaint> arrayList){
        this.complaintList = arrayList;


    }

    @NonNull
    @Override
    public ComplaintHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowComplaintBinding rowComplaintBinding = RowComplaintBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ComplaintHolder(rowComplaintBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintHolder holder, int position) {




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