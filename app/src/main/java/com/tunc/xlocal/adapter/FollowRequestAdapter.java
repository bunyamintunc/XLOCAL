package com.tunc.xlocal.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tunc.xlocal.databinding.RecylerRowBinding;
import com.tunc.xlocal.databinding.RowFollowRequestBinding;
import com.tunc.xlocal.model.Comment;
import com.tunc.xlocal.model.FollowRequest;

import java.util.ArrayList;

public class FollowRequestAdapter extends RecyclerView.Adapter<FollowRequestAdapter.CommentHolder>{

    ArrayList<FollowRequest> requestArrayList;

    public FollowRequestAdapter(ArrayList<FollowRequest> arrayList){
        this.requestArrayList = arrayList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowFollowRequestBinding followRequestBinding = RowFollowRequestBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CommentHolder(followRequestBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {

      holder.followRequestBinding.followRequestText.setText(requestArrayList.get(position).userName);

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

}
