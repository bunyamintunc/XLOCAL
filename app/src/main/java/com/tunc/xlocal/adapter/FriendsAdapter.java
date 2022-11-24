package com.tunc.xlocal.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tunc.xlocal.ChatActivity;
import com.tunc.xlocal.FriendsActivity;
import com.tunc.xlocal.databinding.RecylerRowBinding;
import com.tunc.xlocal.databinding.RowFrinedsBinding;
import com.tunc.xlocal.model.Comment;
import com.tunc.xlocal.model.FollowRequest;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsHolder>{

    ArrayList<FollowRequest> friendList;
    private FriendsActivity friendsActivity;
    public FriendsAdapter(ArrayList<FollowRequest> arrayList, FriendsActivity friendsActivity){
        this.friendList = arrayList;
        this.friendsActivity = friendsActivity;

    }

    @NonNull
    @Override
    public FriendsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowFrinedsBinding rowFrinedsBinding = RowFrinedsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new FriendsHolder(rowFrinedsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsHolder holder, int position) {

       holder.rowFrinedsBinding.userName.setText(friendList.get(position).userName);
       Picasso.get().load(friendList.get(position).photoUrl).into(holder.rowFrinedsBinding.userProfilPhoto);

       holder.rowFrinedsBinding.sendMessageButton.setOnClickListener(view -> {
           friendsActivity.startChatWithFriends(friendList.get(position));

       });


    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    class FriendsHolder extends RecyclerView.ViewHolder{

        RowFrinedsBinding rowFrinedsBinding;

        public FriendsHolder( RowFrinedsBinding rowFrinedsBinding) {
            super(rowFrinedsBinding.getRoot());
            this.rowFrinedsBinding = rowFrinedsBinding;
        }
    }

}