package com.tunc.xlocal.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tunc.xlocal.Profile;
import com.tunc.xlocal.R;

import java.util.HashMap;

public class ProfileEditFragment extends Fragment {

    private View view;
    private Button saveChaneButton;
    private EditText firstName;
    private ImageView profilPhoto;
    private EditText surname;
    private EditText userName;
    private EditText gender;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri imageData;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityResultLauncher<String> permissionLauncher;


    private Profile profileActivity;

    public ProfileEditFragment(Profile profile){
        this.profileActivity = profile;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_edit_fragment,container,false);

        insertValue();

        registerLauncher();

        profilPhoto.setImageResource(R.drawable.indir);
        profilPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectProfilPhoto();
            }
        });

        saveChaneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();

            }
        });
        return view;
    }

    public void selectProfilPhoto(){

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            // hata olabilir
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view, "Galeri için izin gerekli",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver ", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }else{
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }

    }

    public void registerLauncher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if(intentFromResult != null){
                        imageData = intentFromResult.getData();
                        profilPhoto.setImageURI(imageData);
                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }else{
                    Toast.makeText(getContext(),"İzin gerekli",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void insertValue(){
        firstName = view.findViewById(R.id.editTextEditProfileUserName);
        surname = view.findViewById(R.id.editTextEditProfileSurname);
        userName = view.findViewById(R.id.editTextEditProfileNickName);
        gender = view.findViewById(R.id.editTextEditProfileGender);
        saveChaneButton = view.findViewById(R.id.saveChaneButton);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        profilPhoto = view.findViewById(R.id.profilPhoto);
    }

    public  void saveUserInfo(){
        FirebaseUser user = auth.getCurrentUser();
        String userUuid = user.getUid().toString();

        String profilImageName = "images/profilPhoto/"+userUuid+".jpg";

        storageReference.child(profilImageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference newReferance = firebaseStorage.getReference(profilImageName);
                newReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String profilePhotoDownloadUrl = uri.toString();
                        String Name = firstName.getText().toString();
                        String SurName = surname.getText().toString();
                        String Username = userName.getText().toString();
                        String Gender = gender.getText().toString();


                        String userEmail = user.getEmail();


                        HashMap<String,Object> userData = new HashMap<>();
                        userData.put("name",Name);
                        userData.put("profilePhotoDowloadUrl",profilePhotoDownloadUrl);
                        userData.put("surname",SurName);
                        userData.put("userName",Username);
                        userData.put("gender",Gender);
                        userData.put("email",userEmail);
                        userData.put("countOfLike",0);
                        userData.put("countOfConfirm",0);
                        userData.put("countOfJoin",0);
                        userData.put("countOfFollowers",0);
                        userData.put("countOfComment",0);

                        firebaseFirestore.collection("Users").document(userUuid).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                  profileActivity.getProfileDetailsFragment();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });




    }
}
