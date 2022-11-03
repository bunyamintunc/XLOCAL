package com.tunc.xlocal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tunc.xlocal.databinding.RcylerviewCommentRowBinding;
import com.tunc.xlocal.model.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommetHolder> {

    private ArrayList<Comment> commentList;

    public CommentAdapter(ArrayList<Comment> commentList){
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RcylerviewCommentRowBinding rcylerviewCommentRowBinding = RcylerviewCommentRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CommetHolder(rcylerviewCommentRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommetHolder holder, int position) {
       holder.binding.commentRowUserName.setText(commentList.get(position).userName);
       holder.binding.commentRowComment.setText(commentList.get(position).comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommetHolder extends RecyclerView.ViewHolder{

        RcylerviewCommentRowBinding binding;

        public CommetHolder( RcylerviewCommentRowBinding rcylerviewCommentRowBinding) {
            super(rcylerviewCommentRowBinding.getRoot());

            this.binding = rcylerviewCommentRowBinding;
        }
    }

}
