package com.kangwon.macaronproject.notice_board;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kangwon.macaronproject.MainActivity;
import com.kangwon.macaronproject.R;
import com.kangwon.macaronproject.databinding.ActivityNoticeBinding;
import com.kangwon.macaronproject.fragment.MyPostsFragment;
import com.kangwon.macaronproject.fragment.MyTopPostsFragment;
import com.kangwon.macaronproject.fragment.RecentPostFragment;
import com.kangwon.macaronproject.login.BaseActivity;

public class NoticeActivity extends BaseActivity {

    private static final String TAG = "NoticeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNoticeBinding binding = ActivityNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Create the adapter that will return a fregment for each section
        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            private final Fragment[] mFragments = new Fragment[]{
                    new RecentPostFragment(),
                    new MyPostsFragment(),
                    new MyTopPostsFragment(),
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.heading_recent),
                    getString(R.string.heading_my_posts),
                    getString(R.string.heading_my_top_posts)
            };

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        binding.container.setAdapter(mPagerAdapter);
        binding.tabs.setupWithViewPager(binding.container);

        // Button launches NewPostActivity
        binding.fabNewPost.setOnClickListener((v) -> {
            startActivity(new Intent(NoticeActivity.this, NewPostActivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}