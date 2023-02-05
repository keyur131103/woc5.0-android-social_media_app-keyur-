package com.example.da_connect;

import static com.example.da_connect.CreateProfile.PICK_IMAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    ImageView imageView;
    ProgressBar progressBar;
    private Uri selectedUri;
    private static final int PICK_FILE = 1;
    UploadTask uploadTask;
    EditText etdesc;
    Button btn_choosefile_post, btn_uploadfile_post;
    VideoView videoView;
    String url,name;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db1,db2,db3;

    MediaController mediaController;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mediaController = new MediaController(this);

        progressBar = findViewById(R.id.pb_post);
        videoView = findViewById(R.id.vv_post);
        imageView = findViewById(R.id.iv_post);
        btn_choosefile_post = findViewById(R.id.btn_choosefile_post);
        btn_uploadfile_post = findViewById(R.id.btn_uploadfile_post);
        etdesc = findViewById(R.id.et_desc_post);
        db1 = database.getReference("All Images");
        db2 = database.getReference("All Videos");
        db3 = database.getReference("All Posts");

        FirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       // String currentuid = user.getUid();

        btn_uploadfile_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dopost();
            }
        });

        btn_choosefile_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaController.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivity(intent,PICK_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_FILE || requestCode == RESULT_OK || data != null || data.getData() != null) {
            selectedUri = data.getData();
            if(selectedUri.toString().contains("image")) {
                Picasso.get().load(selectedUri).into(imageView);
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                type = "iv";
            }else if(selectedUri.toString().contains("video")) {
                videoView.setMediaController(mediaController);
                videoView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVideoURI(selectedUri);
                videoView.start();
                type = "vv";
            }else {
                Toast.makeText(this, "No file selected!", Toast.LENGTH_SHORT).show();
            }
        }
        private String getFileExt(Uri uri) {
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
        }

        @Override
        protected void onStart() {

                super.onStart();

                FirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String currentuid = user.getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("user").document(currentuid);

        documentReference.get()
                .addOnCompleteListener((task) -> {

                    if(task.getResult().exists()) {
                        name = task.getResult().getString("name");
                        url = task.getResult().getString("url");

                    }else {
                        Toast.makeText(PostActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                });


        }

    }
    void Dopost() {
        FirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //String currentuid = user.getUid();

        String desc = etdesc.getText().toString();

       // Calendar cdate = Calendar.getInstance();
        //SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
        //final String savetime = currenttime.format(ctime.getTime());

     //   String time = savedate +":"+ savetime;

        if(TextUtils.isEmpty(desc) || selectedUri != null) {

            final StorageReference reference = storageReference.child(System.currentTimeMillis()+ "," +getFileExt(selectedUri));
            uploadTask = reference.putFile(selectedUri);

            Task<Uri> uriTask = uploadTask.continueWithTask((task) -> {
                if(!task.isSuccessful()) {
                    throw task.getException();
                }

                return reference.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                    }
                }
            });
        }
    }

    private long getFileExt(Uri selectedUri) {
    }


}