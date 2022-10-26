package com.tunc.xlocal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.tunc.xlocal.databinding.ActivityPostBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;


public class PostActivity extends AppCompatActivity {

    private ImageView img;
    private  Button takePhoto;
    private ActivityPostBinding binding;
    private StorageReference mf;
    private EditText textAciklama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        textAciklama = binding.editTextAciklamaPostActivity;

        img =binding.ImageVievPostActivity;
        takePhoto = binding.postButtonInPostActivity;

        mf = FirebaseStorage.getInstance().getReference();
        img.setImageResource(R.drawable.indir);
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
        Uri uri = Uri.parse(path);
        img.setImageURI(uri);

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

     public  void savePost(){
        String aciklama = textAciklama.getText().toString();

     }



}