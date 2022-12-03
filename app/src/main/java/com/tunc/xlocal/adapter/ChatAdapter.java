package com.tunc.xlocal.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.transition.Hold;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.tunc.xlocal.R;
import com.tunc.xlocal.databinding.RowChatBinding;
import com.tunc.xlocal.databinding.RowFrinedsBinding;
import com.tunc.xlocal.model.FollowRequest;
import com.tunc.xlocal.model.Message;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder>{

    private ArrayList<Message> messageList;
    private FirebaseAuth auth;
    public ChatAdapter(ArrayList<Message> messageList){

        this.messageList = messageList;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowChatBinding rowChatBinding = RowChatBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ChatHolder(rowChatBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {


        if (messageList.get(position).messageOwnerUuid.equals(auth.getCurrentUser().getUid())){

            holder.rowChatBinding.textMessageRight.setText(messageList.get(position).messageText);
            holder.rowChatBinding.dataRight.setText(messageList.get(position).sendDate);
            holder.rowChatBinding.messageLayoutRight.setBackgroundColor(Color.parseColor("#e7eecc"));

        }else{
            holder.rowChatBinding.textMessage.setText(messageList.get(position).messageText);
            holder.rowChatBinding.date.setText(messageList.get(position).sendDate);
            holder.rowChatBinding.messageLayout.setBackgroundColor(Color.parseColor("#a8ccc2"));
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class ChatHolder extends RecyclerView.ViewHolder{

        RowChatBinding rowChatBinding;

        public ChatHolder( RowChatBinding rowChatBinding) {
            super(rowChatBinding.getRoot());
            this.rowChatBinding = rowChatBinding;
        }
    }

}