package com.tunc.xlocal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.TransitionRes;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.tunc.xlocal.MainActivity;
import com.tunc.xlocal.MapsActivity;
import com.tunc.xlocal.Profile;
import com.tunc.xlocal.R;
import com.tunc.xlocal.model.User;

public class ProfileDetailsFragment  extends Fragment {
    View view;
    FirebaseAuth auth;
    ImageView profilePhoto;
    TextView userName;
    TextView userEmail;
    Button goToEditProfileButton;
    Profile profileActivity;
    FirebaseFirestore firebaseFirestore;
    User newUser;
    private Button btnLogout;
    private boolean isCurrentUserResult;
    private Profile profile;

    public ProfileDetailsFragment(Profile profile){

        this.profileActivity = profile;

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        isCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_details_fragment,container,false);

             insertToDetails();



        goToEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileActivity.getProfileEditFragment();
            }
        });

        getUserDetails();

        return view;
    }

    public void insertToDetails(){

        profilePhoto = view.findViewById(R.id.profilPhoto);
        userName = view.findViewById(R.id.textViewUserDetailUserName);
        userEmail = view.findViewById(R.id.textViewUserDetailEmail);
        goToEditProfileButton = view.findViewById(R.id.btnGoToEditProfile);
        profilePhoto.setImageResource(R.drawable.indir);
        btnLogout = view.findViewById(R.id.btnLogOut);

        btnLogout.setOnClickListener(view -> {
            logOut();
        });



    }

    public void getUserDetails(){

        if(isCurrentUserResult){

        firebaseFirestore.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        String name = (String) value.getData().get("name");
                        String username = (String) value.getData().get("userName");
                        String surname = (String) value.getData().get("surname");
                        String gender =  (String) value.getData().get("gender");
                        String profilUrl = (String) value.getData().get("profilePhotoDowloadUrl");

                        System.out.println(name +" "+ surname);
                        newUser = new User(name,username,surname,gender,profilUrl);

                        if(newUser != null){
                            System.out.println("ben calisiyorum");
                            insertToVeriableForCurrentUser();

                         }


            }
        });
        }else{
            System.out.println(newUser);
            userName.setText(auth.getCurrentUser().getUid());
            userEmail.setText(auth.getCurrentUser().getEmail());
        }


    }

    public  void insertToVeriableForCurrentUser(){
        String name = newUser.getName() +" "+newUser.getSurname();
        String username = "@"+newUser.getUsername();
        userName.setText(name);
        userEmail.setText(username);
        Picasso.get().load(newUser.getProfilUrl()).into(profilePhoto);
    }


    public boolean isCurrentUser(){
        firebaseFirestore.collection("Users").addSnapshotListener((value, error) -> {
            if (value.isEmpty()){
                Toast.makeText(getContext(), "users table is empty", Toast.LENGTH_SHORT).show();
            }else{

                for(DocumentSnapshot document : value.getDocuments()){
                        if (auth.getCurrentUser().getUid().equals(document.getId())){
                            isCurrentUserResult = true;
                            getUserDetails();
                        }
                }
            }
        });
        return isCurrentUserResult;
    }

    private void logOut(){

   profileActivity.logOut();


    }


}
