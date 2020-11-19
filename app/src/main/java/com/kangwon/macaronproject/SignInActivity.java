//package com.kangwon.macaronproject;
//
//import androidx.annotation.NonNull;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.kangwon.macaronproject.databinding.ActivitySignInBinding;
//import com.kangwon.macaronproject.models.User;
//
//public class SignInActivity extends BaseActivity implements View.OnClickListener {
//
//    private static final String TAG = "SignInActivity";
//
//    private DatabaseReference mDatabase;
//    private FirebaseAuth mAuth;
//
//    private ActivitySignInBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivitySignInBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mAuth = FirebaseAuth.getInstance();
//
//        // Views progress bar
//        setProgressBar(R.id.progressBar);
//
//        // Click listeners
//        binding.buttonSignIn.setOnClickListener(this);
//        binding.buttonSignUp.setOnClickListener(this);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        // Check auth on Activity start
//        if (mAuth.getCurrentUser() != null) {
//            onAuthSuccess(mAuth.getCurrentUser());
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        mAuth.signOut();
//    }
//
//    private void signIn() {
//        Log.d(TAG, "signIn");
//        if (!validateForm()) {
//            return;
//        }
//
//        showProgressBar();
//        String email = binding.signinEmail.getText().toString();
//        String passwd = binding.signinPassword.getText().toString();
//
//        mAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
//                hideProgressBar();
//
//                if (task.isSuccessful()) {
//                    onAuthSuccess(task.getResult().getUser());
//                } else {
//                    Toast.makeText(SignInActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private void signUp() {
//        Log.d(TAG, "signUp");
//        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
//        startActivity(intent);
//    }
//
//    private void onAuthSuccess(FirebaseUser user) {
//        String username = usernameFromEmail(user.getEmail());
//
//        // Write new user
//        writeNewUser(user.getUid(), username, user.getEmail());
//
//        // go to MainActivity
//        startActivity(new Intent(SignInActivity.this, MainActivity.class));
//        finish();
//    }
//
//    private String usernameFromEmail(String email) {
//        if (email.contains("@")) {
//            return email.split("@")[0];
//        } else {
//            return email;
//        }
//    }
//
//    private boolean validateForm() {
//        boolean result = true;
//        if (TextUtils.isEmpty(binding.signinEmail.getText().toString())) {
//            binding.signinEmail.setError("Required");
//            result = false;
//        } else {
//            binding.signinEmail.setError(null);
//        }
//
//        if (TextUtils.isEmpty(binding.signinPassword.getText().toString())) {
//            binding.signinPassword.setError("Required");
//            result = false;
//        } else {
//            binding.signinPassword.setError(null);
//        }
//
//        return result;
//    }
//
//    // [START basic_write]
//    private void writeNewUser(String uid, String name, String email) {
//        User user = new User(name, email);
//
//        mDatabase.child("users").child(uid).setValue(user);
//    }
//    // [END basic_write]
//
//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        if (i == R.id.buttonSignIn) {
//            signIn();
//        } else if (i == R.id.buttonSignUp) {
//            signUp();
//        }
//    }
//}