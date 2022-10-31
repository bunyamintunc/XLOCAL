package com.tunc.xlocal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tunc.xlocal.databinding.ActivityPostBinding;


import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class PostActivity extends AppCompatActivity {

    private ImageView img;
    private  Button takePhoto;
    private ActivityPostBinding binding;
    private StorageReference mf;
    private EditText textAciklama;
    private LocationListener locationListener;
    private FirebaseAuth auth;
    private LatLng konum;
    private Uri imageUri;
    private Date currentDate;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser curentUser;
    private String documentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        textAciklama = binding.editTextAciklamaPostActivity;

        img =binding.ImageVievPostActivity;
        takePhoto = binding.postButtonInPostActivity;

        auth = FirebaseAuth.getInstance();

        mf = FirebaseStorage.getInstance().getReference();
        img.setImageResource(R.drawable.indir);

        currentDate = Calendar.getInstance().getTime();

        konum = (LatLng) getIntent().getExtras().get("konum");

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public void uploadImageView(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            System.out.println("Error");
            onCaptureImageResult(data);
        }

    }

    public void onCaptureImageResult(Intent data){
        Bitmap foto = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.JPEG,90,bytes);
        byte[] array = bytes.toByteArray();
        String file = Base64.encodeToString(array,Base64.DEFAULT);
        //img.setImageBitmap(foto);

        String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),foto,"val",null);
        imageUri = Uri.parse(path);
        img.setImageURI(imageUri);

     }



     public void save(Uri uri){
         StorageReference sr = mf.child("myImages/a.jpg");
         sr.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 System.out.println("Başarılı");
             }
         });
     }

     public  void savePost(View view){
        String aciklama = textAciklama.getText().toString();

        //kullanıcı bilgileri
         curentUser = auth.getCurrentUser();
         String currentUserUuid = curentUser.getUid();

         Date date = new Date();


         // konum ve açıklamanın olup olmadığı kontrolü
        if(aciklama.equals("") || konum == null || imageUri == null){
            Toast.makeText(this, "Fotoğraf ve konum bilgisi boş olamaz", Toast.LENGTH_SHORT).show();
        }else{
            String profilImageName = "images/postPhoto/"+currentUserUuid+date+".jpg";

            storageReference.child(profilImageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReferance = firebaseStorage.getReference(profilImageName);
                    newReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String postImageDownloadUrl = uri.toString();
                            String aciklama = textAciklama.getText().toString();
                            String userUuid = currentUserUuid.toString();
                            double latitude = konum.latitude;
                            double longitude  = konum.longitude;
                            Date date = currentDate;


                            HashMap<String,Object> postData = new HashMap<>();
                            postData.put("user_uuid",userUuid);
                            postData.put("desciription",aciklama);
                            postData.put("post_image_download_url",postImageDownloadUrl);
                            postData.put("latitude",latitude);
                            postData.put("longitude",longitude);
                            postData.put("date",date);
                            postData.put("count_of_like",0);
                            postData.put("count_of_confirm",0);
                            postData.put("count_of_join",0);
                            postData.put("count_of_comment",0);

                            firebaseFirestore.collection("Post").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    createOtherTableForPost(documentReference.getId());
                                    Toast.makeText(PostActivity.this,"Başarılı",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            });
        }

     }

     public void createOtherTableForPost(String documentId){

         HashMap<String,Object> data = new HashMap<>();
         data.put("user_uuid",curentUser.getUid());
         firebaseFirestore.collection("PostTable").document(documentId).collection("Likes").add(data);
         firebaseFirestore.collection("PostTable").document(documentId).collection("Joins").add(data);
         firebaseFirestore.collection("PostTable").document(documentId).collection("Confirms").add(data);
         firebaseFirestore.collection("PostTable").document(documentId).collection("Comments").add(data);




     }




}