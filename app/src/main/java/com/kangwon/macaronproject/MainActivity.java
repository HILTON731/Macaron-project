package com.kangwon.macaronproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kangwon.macaronproject.databinding.ActivityMainBinding;
import com.kangwon.macaronproject.env.Env;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ActivityMainBinding binding;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding.mainlogout.setOnClickListener(this);
        binding.mainupdate.setOnClickListener(this);
//        binding.maintextview.setText(mDatabase.child("user-info").child(mAuth.getUid()).getDatabase().);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.mainlogout){
            signout();
        } else if (i == R.id.mainupdate){
            Intent intent = new Intent(MainActivity.this, MemberInfoActivity.class);
            intent.putExtra("from", Env.MAIN);
            startActivity(intent);
            finish();
        }
    }

    private void signout() {
        mAuth.signOut();
        finish();
    }
}