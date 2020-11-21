package com.kangwon.macaronproject.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.kangwon.macaronproject.databinding.ActivityNoticeBinding;

public class NoticeActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNoticeBinding binding = ActivityNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

//            private final Fragment[] mFragments = new Fragment[]
            @Override
            public int getCount() {
                return 1;
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return null;
            }
        };
    }
}