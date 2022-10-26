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
import com.tunc.xlocal.MapsActivity;
import com.tunc.xlocal.R;

public class RegisterFragment extends Fragment {

    View view;
    EditText registerEmail;
    EditText registerPassword;
    Button registerButton;
    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.register_fragment,container,false);

      registerEmail = view.findViewById(R.id.editTextRegisterEmail);
      registerPassword = view.findViewById(R.id.editTextRegisterPassword);
      registerButton = view.findViewById(R.id.btnRegisterKayit);
      registerButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              register();
          }
      });

      auth = FirebaseAuth.getInstance();

      return view;
    }

    public  void register(){

        String email = registerEmail.getText().toString();
        String password = registerPassword.getText().toString();

        if(email.equals("") || password.equals("")){
            Toast.makeText(getContext(),"Şifre veya Email boş geçilemz",Toast.LENGTH_LONG).show();
        }else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
                Intent goMapsActivity = new Intent(getContext(), MapsActivity.class);
                startActivity(goMapsActivity);
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            });
        }
    }
}
