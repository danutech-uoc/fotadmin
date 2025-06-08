package com.example.fotadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.fotadmin.UploadNewsActivity;
import com.example.fotadmin.R;
import com.example.fotadmin.fragments.NewsCategoryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fabUpload;

    // Categories to match your Firebase nodes
    private String[] categories = {"sports", "academic", "events"};
    private String[] categoryTitles = {"Sports", "Academic", "Events"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        fabUpload = findViewById(R.id.fabUploadNews);  // Make sure ID matches your layout

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public Fragment getItem(int position) {
                return NewsCategoryFragment.newInstance(categories[position]);
            }

            @Override
            public int getCount() {
                return categories.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return categoryTitles[position];
            }
        });

        tabLayout.setupWithViewPager(viewPager);

        fabUpload.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UploadNewsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);  // Inflate menu resource
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_upload_news) {
            Intent intent = new Intent(this, UploadNewsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
