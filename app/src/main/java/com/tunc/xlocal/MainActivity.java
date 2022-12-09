package com.tunc.xlocal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Maps;
import com.google.firebase.auth.FirebaseAuth;
import com.tunc.xlocal.fragments.LoginFragment;
import com.tunc.xlocal.fragments.RegisterFragment;


public class MainActivity extends AppCompatActivity {


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LoginFragment loginFragment;
    RegisterFragment registerFragment;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Log"," main activity çalıştı");
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null ){
            Log.d("Log","maps activityyseine gidiliyot");
            Intent goToMapsActivity = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(goToMapsActivity);
            finish();


        }else{
            //login fragment bağlanması
            Log.d("Log","login yapan kullanıcı yok main activity");
             addLoginFragment();


        }



    }

    public  void removeLoginFragment(){
        if(loginFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(loginFragment).commit();
        }
    }

    public void removeRegisterFragment(){
         if(registerFragment != null){
             getSupportFragmentManager().beginTransaction().remove(registerFragment).commit();
         }
    }

    public  void addRegisterFragment(){
        //register fragment bağlanması
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        registerFragment = new RegisterFragment(this);
        fragmentTransaction.add(R.id.framebirinci,registerFragment).commit();
    }

    public void addLoginFragment(){
        Log.d("Log","add login fragment çalışıyor.");
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        loginFragment = new LoginFragment();
        fragmentTransaction.add(R.id.framebirinci,loginFragment).commit();
        Log.d("Log","add login fragment bitti.");
    }



    public void goToMapsActivity(){
        finish();
        Intent gotToStartActivity = new Intent(this,MapsActivity.class);
        startActivity(gotToStartActivity);

    }

    public void singInWithGoogle(GoogleSignInClient googleSignInClient){

        Intent singInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(singInIntent,1000);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                goToMapsActivity();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
}