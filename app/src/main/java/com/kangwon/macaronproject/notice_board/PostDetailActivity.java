package com.kangwon.macaronproject.notice_board;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kangwon.macaronproject.R;
import com.kangwon.macaronproject.databinding.ActivityPostDetailBinding;
import com.kangwon.macaronproject.login.BaseActivity;
import com.kangwon.macaronproject.models.Post;
import com.kangwon.macaronproject.models.User;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostKey;
    private CommentAdapter mAdapter;
    private ActivityPostDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null){
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        mPostReference = FirebaseDatabase.getInstance().getReference().child("posts").child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference().child("post-comments").child(mPostKey);

        binding.buttonPostComment.setOnClickListener(this);
        binding.recyclerPostComments.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);

                binding.postAuthorLayout.postAuthor.setText(post.author);
                binding.postTextLayout.postTitle.setText(post.title);
                binding.postTextLayout.postBody.setText(post.body);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
                Toast.makeText(PostDetailActivity.this, "Failed to load post.", Toast.LENGTH_SHORT).show();
            }
        };
        mPostReference.addValueEventListener(postListener);

        mPostListener = postListener;

        mAdapter = new CommentAdapter(this, mCommentsReference);
        binding.recyclerPostComments.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mPostListener != null){
            mPostReference.removeEventListener(mPostListener);
        }

        mAdapter.cleanupListener();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.buttonPostComment){
            postComment();
        }

    }

    private void postComment() {
        final String uid = getUid();
        FirebaseDatabase.getInstance().getReference().child("users-info").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
//                String authorName = user.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder{
        public TextView authorView;
        public TextView bodyView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            authorView = itemView.findViewById(R.id.commentAuthor);
            bodyView = itemView.findViewById(R.id.commentBody);
        }
    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();

        public CommentAdapter(final Context context, DatabaseReference ref){
            mContext = context;
            mDatabaseReference = ref;

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    Comment comment = dataSnapshot.getValue(Comment.class);

                    mCommentIds.add(dataSnapshot.getKey());
                    mComments.add(comment);
                    notifyItemInserted(mComments.size() - 1);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    Comment newComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if(commentIndex > -1){
                        mComments.set(commentIndex, newComment);

                        notifyItemChanged(commentIndex);
                    } else {
                        Log.w(TAG, "onchildChanged:unknown_child:" + commentKey);
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if(commentIndex > -1) {
                        mCommentIds.remove(commentIndex);
                        mComments.remove(commentIndex);

                        notifyItemRemoved(commentIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child" + commentKey);
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "postComments:onCancelled", error.toException());
                    Toast.makeText(mContext, "Failed to load comments.", Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);

            mChildEventListener = childEventListener;
        }
        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public void cleanupListener() {
        }
    }
}
