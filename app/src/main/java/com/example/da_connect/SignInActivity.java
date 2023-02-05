package com.example.da_connect;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    EditText name,contact,email,password;
    Button signin;

    MaterialButton signup;

    //GoogleSignInClient mGoogleSignInClient;
    private static Bundle RC_SIGN_IN;
    FirebaseAuth mAuth;



   // ImageView fbButton;

   // CallbackManager callbackmanager;
   // LoginManager loginmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
//facebook
       /* FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signin);

        callbackmanager = CallbackManager.Factory.create();



        LoginManager.getInstance().registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(SignInActivity.this, AfterFacebook.class));
                        loginmanager.logInWithReadPermissions(SignInActivity.this, Arrays.asList("email", "public_profile"));
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(@NonNull FacebookException exception) {
                        // App code
                    }
                });

        fbButton = findViewById(R.id.fb_btn);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginmanager.logInWithReadPermissions(SignInActivity.this, Arrays.asList("email", "public_profile"));
            }
        });


*/      //Don't have an account?SIGN UP Redirection
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = (MaterialButton) findViewById(R.id.signup);
        signin = (MaterialButton) findViewById(R.id.signin);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSignUpActivity();
            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = email.getText().toString();
                String Password = password.getText().toString();

                if(!TextUtils.isEmpty(Email) || !TextUtils.isEmpty(Password)) {

                    mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                sendtoMain();
                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(SignInActivity.this, "Error! : "+error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(SignInActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }

            }
        });
 //google



    }

    private void sendtoMain() {
        Intent intent = new Intent(SignInActivity.this,SplashScreen.class);
        startActivity(intent);
        finish();
    }

    public void openSignUpActivity() {

        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
