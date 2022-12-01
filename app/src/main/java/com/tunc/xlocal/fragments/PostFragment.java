package com.tunc.xlocal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.tunc.xlocal.MapsActivity;
import com.tunc.xlocal.R;
import com.tunc.xlocal.databinding.PostFragmentBinding;
import com.tunc.xlocal.model.Post;
import com.tunc.xlocal.model.User;

import java.util.HashMap;

public class PostFragment extends Fragment {

     //Post fragment icin degiskenler
    private View view;
    private Marker marker;
    private Button btnClose, btnLike, btnJoin,btnComment;
    private ImageButton btnComplaint;
    private PostFragmentBinding binding;
    private ImageView postImageView,postIconImageView;
    private Post post;
    private TextView description, textCountLike, textCountComment, textCountJoin, textCountConfirm, postIconUserName;
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
    private User userOfPost;
    private LinearLayout linearLayout;
    private long countOfLike,CountOfJoin;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PostFragmentBinding.inflate(inflater,container,false);
        view = binding.getRoot();

        //view uzerindeki elemanlar bulunuyor.
        insertDefaultValues();
        isLike();
        isJoin();
        isDidComplaint();
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

        postIconImageView = binding.postIconImageView;
        postIconUserName = binding.postIconUserName;

        //post'un like, join, confirm comment sayilarini aliyoruz.
        like = (int) post.countOfLike;
        join = (int) post.countOfJoin;
        confirm = (int) post.countOfConfirm;
        comment = (int) post.countOfComment;
        postDocumentId = post.documentId.toString();

        btnComplaint = binding.btnComplaint;
        btnComplaint.setOnClickListener(view -> {
            HashMap<String,Object> complaintData = new HashMap<>();
            complaintData.put("post_id",post.documentId);
            complaintData.put("owner_post_id",post.userUudi);
            complaintData.put("post_url",post.postImageDownloadUrl);
            complaintData.put("owner_complaint",auth.getCurrentUser().getUid());
            complaintData.put("owner_image",userOfPost.profilUrl);
            complaintData.put("owner_user_name",userOfPost.username);

            firebaseFirestore.collection("Complaints").add(complaintData).addOnSuccessListener(documentReference -> {

            });
        });

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

        // yorum iconuna tıklanması
         btnComment = binding.btnComment;
         btnComment.setOnClickListener(view -> {
             doComment();
         });


         // postu paylaşan kullanıcı bilgisine tıklandığında
         linearLayout = binding.postLinearLayout;
         linearLayout.setOnClickListener(view -> {
                gotToProfile();
         });



        description.setText(post.description);
        textCountComment.setText(String.valueOf(comment));
        textCountConfirm.setText(String.valueOf(confirm));
        textCountJoin.setText(String.valueOf(join));
        textCountLike.setText(String.valueOf(like));
        Picasso.get().load(post.postImageDownloadUrl).into(postImageView);

        //icon icin eklenenler
         getUserOfPost();


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
            increaseUserInteraction("countOfLike",countOfLike);
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
                              lowerUserInteraction("countOfLike",countOfLike);
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

    //
    public void getUserOfPost(){
        userOfPost = new User();
        firebaseFirestore.collection("Users").document(post.userUudi).get().addOnSuccessListener(documentSnapshot -> {
           if (documentSnapshot.getData().isEmpty()){

           }else{
               countOfLike = (long) documentSnapshot.get("countOfLike");
               postIconUserName.setText(documentSnapshot.get("userName").toString());
               Picasso.get().load(documentSnapshot.get("profilePhotoDowloadUrl").toString()).into(postIconImageView);
               userOfPost.profilUrl = documentSnapshot.get("profilePhotoDowloadUrl").toString();
               userOfPost.username = documentSnapshot.get("userName").toString();
           }
        }).addOnFailureListener(e -> {
             Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_LONG).show();
        });
    }

    public void gotToProfile(){
           mapsActivity.goUserInfoActivity(post.userUudi);
    }

    public void increaseUserInteraction(String nameOfAction,long countOfValue){
        int count = (int) countOfValue;
        count+=1;
        firebaseFirestore.collection("Users").document(post.userUudi).update(nameOfAction,count);
    }
    public void lowerUserInteraction(String nameOfAction, long countOfValue) {
        int count = (int) countOfValue;
        if(count<0){
            count-=1;
        }
        firebaseFirestore.collection("Users").document(post.documentId).update(nameOfAction,count);


    }

    public void isDidComplaint(){
        firebaseFirestore.collection("Complaints").addSnapshotListener((value, error) -> {
            if (value.isEmpty()){

            }else{

                for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                    if ( documentSnapshot.get("post_id").equals(post.documentId) && documentSnapshot.get("owner_complaint").equals(auth.getCurrentUser().getUid())){
                        btnComplaint.setClickable(false);
                    }
                }
            }
        });
    }

}
