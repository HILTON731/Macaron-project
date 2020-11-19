package com.kangwon.macaronproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kangwon.macaronproject.databinding.ActivityMemberInfoBinding;
import com.kangwon.macaronproject.env.Env;
import com.kangwon.macaronproject.models.Info;
import com.kangwon.macaronproject.models.User;

import java.util.HashMap;
import java.util.Map;

public class MemberInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MemberInfoActivity";
    private static final String REQUIRED = "Required";
    private static int CODE;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ActivityMemberInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        CODE = intent.getExtras().getInt("from");
        Log.d(TAG, Integer.toString(CODE)+":MAIN:CODENUM");

        // Views
        setProgressBar(R.id.meminfoprogressBar);

        binding.meminfoupdateBtn.setOnClickListener(this);
        binding.meminforevokeBtn.setOnClickListener(this);
        binding.meminfocancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.meminfoupdateBtn){
            update();
        } else if(i == R.id.meminfocancelBtn) {
            cancel();
        } else if(i == R.id.meminforevokeBtn){
            revoke();
        }
    }



    private void update() {
        String username = binding.meminfousername.getText().toString();
        String phone = binding.meminfophoneNum.getText().toString();
        boolean isowner = binding.meminfoIsowner.isChecked();

        if(TextUtils.isEmpty(username)){
            binding.meminfousername.setError(REQUIRED);
            return;
        }

        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if(user == null){
                    Log.d(TAG, "User " + userId + "is unexpectedly null");
                    Toast.makeText(MemberInfoActivity.this, "ERR: Could not fetch user", Toast.LENGTH_SHORT).show();
                } else {
                    writeNewPost(userId, username, phone, isowner);
                    startActivity(new Intent(MemberInfoActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "getUser:onCancelled", error.toException());
            }
        });

    }

    // [START write_user_info_out]
    private void writeNewPost(String id, String username, String phone, boolean isowner) {

        String key = mDatabase.child("user-info").push().getKey();
        Info info = new Info(id, username, phone, isowner);
        Map<String, Object> infoValues = info.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/user-info/"+ id + "/" + key, infoValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_user_info_out]

    private void cancel(){
        switch(CODE){
            case Env.MAIN:
                break;
            case Env.SIGNIN:
                revoke();
                break;
        }
        finish();
    }


    private void revoke() {
        String user = mAuth.getCurrentUser().getUid();
        for(String table: Env.DBTABLES){
            mDatabase.child(table).child(user).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "mDatabase:user:"+user+":delete completely");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "mDatabase:user:"+user+":delete Failed");
                }
            });
            mAuth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "mAuth:user:"+user+":delete completely");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "mAuth:user:"+user+":delete Failed");
                }
            });
        }
        finish();
    }
}