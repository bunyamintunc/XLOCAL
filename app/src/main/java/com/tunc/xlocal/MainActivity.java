package com.tunc.xlocal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

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

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null  ){
            Intent goToMapsActivity = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(goToMapsActivity);
            finish();
        }else{
            //login fragment bağlanması
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
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        loginFragment = new LoginFragment();
        fragmentTransaction.add(R.id.framebirinci,loginFragment).commit();
    }
}