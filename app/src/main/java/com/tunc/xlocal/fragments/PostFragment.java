package com.tunc.xlocal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.tunc.xlocal.MapsActivity;
import com.tunc.xlocal.R;
import com.tunc.xlocal.databinding.PostFragmentBinding;
import com.tunc.xlocal.model.Post;

import java.util.HashMap;

public class PostFragment extends Fragment {

     //Post fragment icin degiskenler
    private View view;
    private Marker marker;
    private Button btnClose, btnLike, btnJoin,btnComment;
    private PostFragmentBinding binding;
    private ImageView postImageView;
    private Post post;
    private TextView description, textCountLike, textCountComment, textCountJoin, textCountConfirm;
    private int like, comment, join, confirm;
    private MapsActivity mapsActivity;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private String postDocumentId;
    private boolean result;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private CommentFragment commentFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PostFragmentBinding.inflate(inflater,container,false);
        view = binding.getRoot();

        //view uzerindeki elemanlar bulunuyor.
        insertDefaultValues();
        isLike();
        isJoin();
        getPost();

        return  view;
    }

    //MapsActivityden gelen markerler yakalaniyor.
    public PostFragment(Marker marker){
        this.marker = marker;
        this.post = (Post) marker.getTag();
    }

    //default constructor.
    public PostFragment(){

    }

    //default degerler ataniyor.
    public void insertDefaultValues(){

        //postFragmenti maps fragmentten ayırmak icin yolunu bilmem gerekiyor
        mapsActivity = (MapsActivity) getActivity();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        postImageView = binding.imageViewPostFragment;
        description = binding.textMarkerDescription;
        textCountComment = binding.textCommnet;
        textCountConfirm = binding.textConfirm;;
        textCountJoin = binding.textJoin;
        textCountLike = binding.textLike;

        //post'un like, join, confirm comment sayilarini aliyoruz.
        like = (int) post.countOfLike;
        join = (int) post.countOfJoin;
        confirm = (int) post.countOfConfirm;
        comment = (int) post.countOfComment;
        postDocumentId = post.documentId.toString();

        //post fragment'i maps fragmentten ayiriyoruz.
        btnClose = binding.btnClose;
        btnClose.setOnClickListener(view -> {
            mapsActivity.removePostFragmentInMapsFragment();
        });

        //like
        btnLike = binding.btnLike;
        btnLike.setOnClickListener(view ->{
            like();
        });

        // Mevcut kullanici katilim
        btnJoin = binding.btnJoin;
        btnJoin.setOnClickListener(view -> {
              join();
        });


         btnComment = binding.btnComment;
         btnComment.setOnClickListener(view -> {
             doComment();
         });



        description.setText(postDocumentId);
        textCountComment.setText(String.valueOf(comment));
        textCountConfirm.setText(String.valueOf(confirm));
        textCountJoin.setText(String.valueOf(join));
        textCountLike.setText(String.valueOf(like));
        Picasso.get().load(post.postImageDownloadUrl).into(postImageView);



    }

    //giris yapan kulalnici post'u begenmis mi?
    public boolean isLike(){

         firebaseFirestore.collection("PostTable").document(post.documentId).collection("Likes")
                 .whereEqualTo("user_uuid",auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         if(queryDocumentSnapshots.isEmpty()){
                             result = false;
                         }else{
                             result = true;
                             btnLike.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.ic_icon_when_like_24));
                         }
                     }
                 });
        return result;
    }

    //giris yapan kulanici katilim saglamis mi?
    public boolean isJoin(){
        firebaseFirestore.collection("PostTable").document(post.documentId).collection("Joins")
                .whereEqualTo("user_uuid",auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            result = false;
                        }else{
                            result = true;
                            btnJoin.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.ic_icon_when_join_24));
                        }
                    }
                });
        return result;
    }

    //giris yapan kullanici post'u teyit etmis mi?
    public boolean isConfirm(){
        firebaseFirestore.collection("PostTable").document(post.documentId).collection("Confirms")
                .whereEqualTo("user_uuid",auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            result = false;
                        }else{
                            result = true;
                        }
                    }
                });
        return result;
    }


    public void like(){
        boolean isLikeResult = isLike();
        if(isLikeResult){
            disLike();
        }else{
            HashMap<String,Object> likeData = new HashMap<>();
            likeData.put("user_uuid",auth.getCurrentUser().getUid());
            firebaseFirestore.collection("PostTable").document(post.documentId).collection("Likes").add(likeData);
            like = like + 1;
            firebaseFirestore.collection("Post").document(post.documentId).update("count_of_like",(long) like);
            textCountLike.setText(String.valueOf(like));
        }
    }

    public void disLike(){
          firebaseFirestore.collection("PostTable").document(post.documentId).collection("Likes")
                  .whereEqualTo("user_uuid",auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                      @Override
                      public void onSuccess(QuerySnapshot documnetQuery) {
                          if (documnetQuery.isEmpty()){

                          }else{
                             for(QueryDocumentSnapshot document : documnetQuery){
                                 firebaseFirestore.collection("PostTable").document(post.documentId).collection("Likes").document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void unused) {

                                     }
                                 }).addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                                     }
                                 });
                             }
                             like = like - 1;
                             firebaseFirestore.collection("Post").document(post.documentId).update("count_of_like",(long) like);
                             btnLike.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.ic_icon_like_24));
                             textCountLike.setText(String.valueOf(like));
                          }
                      }
                  });
    }

    public void join(){
        boolean isJoinResult = isJoin();

        if(isJoinResult){
           disagree();
        }else{
            HashMap<String,Object> likeData = new HashMap<>();
            likeData.put("user_uuid",auth.getCurrentUser().getUid());
            firebaseFirestore.collection("PostTable").document(post.documentId).collection("Joins").add(likeData);
            join = join + 1;
            firebaseFirestore.collection("Post").document(post.documentId).update("count_of_join",(long) join);
            textCountJoin.setText(String.valueOf(join));
        }
    }

    // katilmama drumu
    public void disagree(){
        firebaseFirestore.collection("PostTable").document(post.documentId).collection("Joins")
                .whereEqualTo("user_uuid",auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documnetQuery) {
                        if (documnetQuery.isEmpty()){

                        }else{
                            for(QueryDocumentSnapshot document : documnetQuery){
                                firebaseFirestore.collection("PostTable").document(post.documentId).collection("Joins").document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            join = join - 1;
                            firebaseFirestore.collection("Post").document(post.documentId).update("count_of_join",(long) join);
                            btnJoin.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.ic_icon_join_24));
                            textCountJoin.setText(String.valueOf(join));
                        }
                    }
                });
    }

    public void getPost(){
        firebaseFirestore.collection("Post").document(post.documentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData().isEmpty()){

                }else{
                    textCountLike.setText(String.valueOf(documentSnapshot.get("count_of_like")));
                    textCountComment.setText(String.valueOf(documentSnapshot.get("count_of_comment")));
                    textCountJoin.setText(String.valueOf(documentSnapshot.get("count_of_join")));
                    textCountConfirm.setText(String.valueOf(documentSnapshot.get("count_of_confirm")));
                }
            }
        });
    }

    //yorumlari görmek veya yorum yapmak icin comment fragmentini post fragmnetine bagliyoruz.
    public void doComment(){
        fragmentManager = getParentFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        mapsActivity.hiddeButton();
        onPause();
        commentFragment = new CommentFragment(post.documentId, this,post);
        fragmentTransaction.add(this.getId(), commentFragment).commit();

    }

    public void closeCommentFragment(){
        if(commentFragment != null){
            getParentFragmentManager().beginTransaction().remove(commentFragment).commit();
            commentFragment = null;

            //fragment kapandıktan sonra kamera galeri ve profil butonları yeniden göster
            mapsActivity.showButton();
        }
    }



}
