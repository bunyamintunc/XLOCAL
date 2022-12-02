package com.tunc.xlocal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tunc.xlocal.adapter.ComplaintAdapter;
import com.tunc.xlocal.adapter.FriendsAdapter;
import com.tunc.xlocal.databinding.ActivityComplaintsBinding;
import com.tunc.xlocal.model.Complaint;

import java.util.ArrayList;

public class ComplaintsActivity extends AppCompatActivity {

    private ArrayList<Complaint> complaints;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private Complaint complaint;



    private ActivityComplaintsBinding binding;
    private ComplaintAdapter complaintAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        complaints = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        getComplaints();



    }

    public void getComplaints(){
        firestore.collection("Complaints").addSnapshotListener((value, error) -> {
            if (value.isEmpty()){

            }else {
                for(DocumentSnapshot document : value.getDocuments()){
                    complaint = new Complaint();
                    complaint.ownerComplaintId = document.get("owner_complaint").toString();
                    complaint.ownerPostUser = document.get("owner_post_id").toString();
                    complaint.postId = document.get("post_id").toString();
                    complaint.ownerPostUserImage = document.get("owner_image").toString();
                    complaint.ownerPostUserName = document.get("owner_user_name").toString();
                    complaint.postImage = document.get("post_url").toString();
                    complaints.add(complaint);
                }

                binding.complaintRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                complaintAdapter = new ComplaintAdapter(complaints,this);
                binding.complaintRecyclerView.setAdapter(complaintAdapter);

            }
        });
    }

    public void changedData(){
        complaints.clear();
        complaintAdapter.notifyDataSetChanged();
    }
}