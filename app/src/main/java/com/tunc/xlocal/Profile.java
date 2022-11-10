package com.tunc.xlocal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.tunc.xlocal.fragments.ProfileDetailsFragment;
import com.tunc.xlocal.fragments.ProfileEditFragment;

public class Profile extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ProfileDetailsFragment profileDetailsFragment;
    private ProfileEditFragment profileEditFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getProfileDetailsFragment();
    }



    public  void getProfileDetailsFragment(){
        if(profileEditFragment != null){
            removeProfileEditFragment();
        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        profileDetailsFragment = new ProfileDetailsFragment(this);
        fragmentTransaction.add(R.id.profileActivity,profileDetailsFragment).commit();
    }

    public void getProfileEditFragment(){
        removeProfileDetailsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        profileEditFragment = new ProfileEditFragment(this);
        fragmentTransaction.add(R.id.profileActivity,profileEditFragment).commit();
    }

    public void removeProfileDetailsFragment(){
        if( profileDetailsFragment != null ){
            getSupportFragmentManager().beginTransaction().remove(profileDetailsFragment).commit();
            profileDetailsFragment = null;
        }
    }


    public  void removeProfileEditFragment(){
        if( profileEditFragment != null){
            getSupportFragmentManager().beginTransaction().remove(profileEditFragment).commit();
            profileEditFragment = null;
        }
    }


}