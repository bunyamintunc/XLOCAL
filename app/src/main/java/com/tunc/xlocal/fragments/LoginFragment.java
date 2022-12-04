package com.tunc.xlocal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.tunc.xlocal.MainActivity;
import com.tunc.xlocal.MapsActivity;
import com.tunc.xlocal.R;

public class LoginFragment extends Fragment {

    View view;
    Button loginButton;
    Button registerButton;
    private FirebaseAuth auth;
    EditText editTextEmail;
    EditText editTextPassword;
    MainActivity mainActivity;
    private FirebaseUser user;
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.login_fragment,container,false);

        loginButton = view.findViewById(R.id.loginBtn);
        registerButton = view.findViewById(R.id.registerBtn);

        loginButton.setOnClickListener(view -> {
           login();
        });

        registerButton.setOnClickListener(view -> {
            goToRegisterFragment();
        });

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        editTextEmail = view.findViewById(R.id.loyout2);
        editTextPassword = view.findViewById(R.id.inputPassword);




       return view;
    }

    public  void login(){

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(email.equals("") || password.equals(""))
            System.out.println("şifre ve email boş geçilemez");
        else{
              auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
                  user = FirebaseAuth.getInstance().getCurrentUser();
                  if(user.isEmailVerified()){

                      Intent goToMapsActivity = new Intent(getContext(), MapsActivity.class);
                      startActivity(goToMapsActivity);
                      getActivity().finish();
                  }else{
                      auth.signOut();
                      Toast.makeText(getContext(),"You must verify your email",Toast.LENGTH_LONG).show();
                  }

              }).addOnFailureListener(e -> Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show());
        }

    }
    public void goToRegisterFragment(){

        mainActivity = (MainActivity) getActivity();
        mainActivity.removeLoginFragment();
        mainActivity.addRegisterFragment();

    }



}
