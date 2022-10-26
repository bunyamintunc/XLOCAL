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
import com.tunc.xlocal.MainActivity;
import com.tunc.xlocal.MapsActivity;
import com.tunc.xlocal.R;

public class LoginFragment extends Fragment {

    View view;
    Button loginButton;
    Button registerButton;
    FirebaseAuth auth;
    EditText editTextEmail;
    EditText editTextPassword;
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.login_fragment,container,false);

        loginButton = view.findViewById(R.id.loginButton);
        registerButton = view.findViewById(R.id.button);

        loginButton.setOnClickListener(view -> {
           login();
        });

        registerButton.setOnClickListener(view -> {
            goToRegisterFragment();
        });

        auth = FirebaseAuth.getInstance();

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
       return view;
    }

    public  void login(){

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(email.equals("") || password.equals(""))
            System.out.println("şifre ve email boş geçilemez");
        else{
              auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
                  Intent goToMapsActivity = new Intent(getContext(), MapsActivity.class);
                  startActivity(goToMapsActivity);
                  getActivity().finish();
              }).addOnFailureListener(e -> Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show());
        }

    }
    public void goToRegisterFragment(){

        mainActivity = (MainActivity) getActivity();
        mainActivity.removeLoginFragment();
        mainActivity.addRegisterFragment();

    }

}
