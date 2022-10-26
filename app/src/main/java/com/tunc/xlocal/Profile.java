package com.tunc.xlocal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

import com.tunc.xlocal.fragments.LoginFragment;
import com.tunc.xlocal.fragments.ProfileDetailsFragment;
import com.tunc.xlocal.fragments.ProfileEditFragment;

public class Profile extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ProfileDetailsFragment profileDetailsFragment;
    ProfileEditFragment profileEditFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getProfileDetailsFragment();
    }



    public  void getProfileDetailsFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        profileDetailsFragment = new ProfileDetailsFragment();
        fragmentTransaction.add(R.id.profileActivity,profileDetailsFragment).commit();
    }

    public void getProfileEditFragment(){
        removeProfileDetailsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        profileEditFragment = new ProfileEditFragment();
        fragmentTransaction.add(R.id.profileActivity,profileEditFragment).commit();
    }

    public void removeProfileDetailsFragment(){
        if( profileDetailsFragment != null ){
            getSupportFragmentManager().beginTransaction().remove(profileDetailsFragment).commit();
        }
    }


    public  void removeProfileEditFragment(){
        if( profileEditFragment != null){
            getSupportFragmentManager().beginTransaction().remove(profileEditFragment).commit();
        }
    }
}