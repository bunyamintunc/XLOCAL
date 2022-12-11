package com.tunc.xlocal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    TextInputEditText editTextEmail;
    TextInputEditText  editTextPassword;
    MainActivity mainActivity;
    private TextInputLayout textInputLayoutPassword,getTextInputLayoutEmail;
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

        editTextEmail = view.findViewById(R.id.inputEmail);
        editTextPassword = view.findViewById(R.id.inputPassword);
        textInputLayoutPassword = view.findViewById(R.id.inputLayoutPassword);
        getTextInputLayoutEmail = view.findViewById(R.id.layout1);




       return view;
    }

    public  void login(){

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();






        if(email.isEmpty() || password.isEmpty() || password.length()<7 ){
            if (email.isEmpty()){
                getTextInputLayoutEmail.setError("email cannot be empty!");
            }else{
                getTextInputLayoutEmail.setErrorEnabled(false);
            }

            if(password.isEmpty()){
                textInputLayoutPassword.setError("password cannot be empty!");
            }else{
                textInputLayoutPassword.setErrorEnabled(false);
            }

            if (password.length()<7 && password.length()>0){
                textInputLayoutPassword.setError("password is very short");
            }else{
                textInputLayoutPassword.setErrorEnabled(false);
            }
        }else{
                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){

                        Intent goToMapsActivity = new Intent(getContext(), MapsActivity.class);
                        startActivity(goToMapsActivity);
                        getActivity().finish();
                    }else{
                        auth.signOut();
                        getTextInputLayoutEmail.setError("email must be verify!");
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
