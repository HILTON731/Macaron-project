package com.kangwon.macaronproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kangwon.macaronproject.databinding.ActivityMainBinding;
import com.kangwon.macaronproject.env.Env;
import com.kangwon.macaronproject.login.BaseActivity;
import com.kangwon.macaronproject.login.MemberInfoActivity;
import com.kangwon.macaronproject.notice_board.NoticeActivity;

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
        binding.mainnotice.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        System.out.println(i);
        if(i == R.id.mainlogout){
            mAuth.signOut();
        } else if (i == R.id.mainupdate){
            Intent intent = new Intent(MainActivity.this, MemberInfoActivity.class);
            intent.putExtra("from", Env.MAIN);
            startActivity(intent);
        } else if (i == R.id.mainnotice){
            startActivity(new Intent(MainActivity.this, NoticeActivity.class));
        }
        finish();
    }

//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//    }
    //    private void signout() {
//    }
}