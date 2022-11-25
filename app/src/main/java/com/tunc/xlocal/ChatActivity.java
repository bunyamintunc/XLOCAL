package com.tunc.xlocal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.tunc.xlocal.adapter.ChatAdapter;
import com.tunc.xlocal.adapter.FriendsAdapter;
import com.tunc.xlocal.databinding.ActivityChatBinding;
import com.tunc.xlocal.model.FollowRequest;
import com.tunc.xlocal.model.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private FollowRequest friend;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private EditText messageText;
    private ChatAdapter chatAdapter;
    private ArrayList<Message> arrayMessage;

    public ChatActivity(){
        arrayMessage = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        friend = (FollowRequest) getIntent().getSerializableExtra("user");
        getMessage();

        Picasso.get().load(friend.photoUrl).into(binding.profilPhoto);
        binding.userName.setText(friend.userName);

        messageText = binding.messageText;



        binding.btnSendMessage.setOnClickListener(view -> {
            sendMessage();
        });



    }

    public void sendMessage(){
        if (messageText.getText().equals("")){

        }else{

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String date = df.format(Calendar.getInstance().getTime());

           HashMap<String, Object> data = new HashMap<>();
           data.put("message_owner_uuid",auth.getCurrentUser().getUid());
           data.put("message",messageText.getText().toString());
           data.put("date",date);
           firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Chat").document(friend.userUuid).collection("Mesage").add(data).addOnSuccessListener(e -> {
                 firebaseFirestore.collection("Users").document(friend.userUuid).collection("Chat").document(auth.getCurrentUser().getUid()).collection("Mesage").add(data);
           });
            arrayMessage.clear();
            chatAdapter.notifyDataSetChanged();

        }
        messageText.setText("");


    }

    public void getMessage(){
        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).collection("Chat").document(friend.userUuid).collection("Mesage").orderBy("date").addSnapshotListener((value, error) -> {
            if (value.isEmpty()){

            }else{
                for(DocumentSnapshot documet : value.getDocuments()){
                    Message message = new Message();
                    message.messageOwnerUuid = documet.get("message_owner_uuid").toString();
                    message.messageText = documet.get("message").toString();
                    message.sendDate = documet.get("date").toString();
                    arrayMessage.add(message);


                }

            }

            binding.messageRecylerView.setLayoutManager(new LinearLayoutManager(this));
            chatAdapter = new ChatAdapter(arrayMessage);
            binding.messageRecylerView.setAdapter(chatAdapter);


        });
    }
}