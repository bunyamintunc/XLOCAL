package com.tunc.xlocal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import com.tunc.xlocal.R;

import com.tunc.xlocal.databinding.RecylerRowBinding;
import com.tunc.xlocal.model.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder>{

    ArrayList<Comment> commentList;

    public CommentAdapter(ArrayList<Comment> arrayList){
        this.commentList = arrayList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecylerRowBinding recylerRowBinding = RecylerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CommentHolder(recylerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.recylerRowBinding.commentRowUserName.setText(commentList.get(position).userName);
        holder.recylerRowBinding.commentRowComment.setText(commentList.get(position).comment);
        Picasso.get().load(commentList.get(position).imageUrl).into(holder.recylerRowBinding.commnetRowImageView);

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder{

        RecylerRowBinding recylerRowBinding;

        public CommentHolder( RecylerRowBinding recylerRowBinding) {
            super(recylerRowBinding.getRoot());
            this.recylerRowBinding = recylerRowBinding;
        }
    }

}