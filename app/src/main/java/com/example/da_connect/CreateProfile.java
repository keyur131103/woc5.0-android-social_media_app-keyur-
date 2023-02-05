package com.example.da_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CreateProfile extends AppCompatActivity {

    EditText etname,etstatus;
    Button button;
    ImageView imageView;
    Uri imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    static final int PICK_IMAGE = 1;
    AllUserMember member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        member = new AllUserMember();
        imageView = findViewById(R.id.iv_cp);
        etname = findViewById(R.id.et_name_cp);
        etstatus = findViewById(R.id.status_cp);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        storageReference = FirebaseStorage.getInstance().getReference("Profile Images");
        databaseReference = database.getReference("All Users");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if(requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null || data.getData() != null) {

                imageUri = data.getData();
                Picasso.get().load(imageUri).into(imageView);

            }

        } catch ( Exception e) {
            Toast.makeText(this, "Error"+e, Toast.LENGTH_SHORT).show();
        }


    }

    private String getFileExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }


    private void uploadData() {

        String name = etname.getText().toString();
        String status = etstatus.getText().toString();

        if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(status)) {

            final StorageReference reference = storageReference.child(System.currentTimeMillis()+ "."+getFileExt(imageUri));
            uploadTask = reference.putFile(imageUri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        Map<String,String> profile = new HashMap<>();

                        profile.put("name",name);
                        profile.put("status",status);
                        profile.put("privacy","public");


                        member.setName(name);
                        member.setStatus(status);


                        documentReference.set(profile)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(CreateProfile.this, "Profile Created!", Toast.LENGTH_SHORT).show();

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                               Intent intent = new Intent((CreateProfile.this), MainActivity.class);
                                                    startActivity(intent);
                                            }
                                        }, 2000);

                                    }
                                });
                    }

                }
            });


        }else {
            Toast.makeText(this, "Please fill All Fileds!", Toast.LENGTH_SHORT).show();
        }

    }
}