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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tunc.xlocal.MainActivity;
import com.tunc.xlocal.MapsActivity;
import com.tunc.xlocal.R;

public class RegisterFragment extends Fragment {

    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    View view;
    EditText registerEmail;
    EditText registerPassword;
    Button registerButton, registerWithGoogleButton;
    FirebaseAuth auth;
    private ActionCodeSettings actionCodeSettings;
    private MainActivity mainActivity;

    public RegisterFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      view = inflater.inflate(R.layout.register_fragment,container,false);


      googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
      googleSignInClient = GoogleSignIn.getClient(getContext(),googleSignInOptions);

      registerWithGoogleButton = view.findViewById(R.id.registerWithGoogleBtn);
      registerWithGoogleButton.setOnClickListener(view -> {
           mainActivity.singInWithGoogle(googleSignInClient);
      });

      registerEmail = view.findViewById(R.id.inputEmail);
      registerPassword = view.findViewById(R.id.inputPassword);
      registerButton = view.findViewById(R.id.registerButton);
      registerButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              register();
          }
      });

      auth = FirebaseAuth.getInstance();

         actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://www.example.com/finishSignUp?cartId=1234")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                                "com.tunc.xlocal",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();


      return view;
    }

    public  void register(){

        String email = registerEmail.getText().toString();
        String password = registerPassword.getText().toString();

        if(email.equals("") || password.equals("")){
            Toast.makeText(getContext(),"Şifre veya Email boş geçilemz",Toast.LENGTH_LONG).show();
        }else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(),"Email send go your email addres for link",Toast.LENGTH_LONG).show();
                            mainActivity.removeRegisterFragment();
                            mainActivity.addLoginFragment();

                        }
                    }
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            });
        }

    }


}
