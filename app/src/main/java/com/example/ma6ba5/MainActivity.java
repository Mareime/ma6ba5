package com.example.ma6ba5;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        setupViewPager();
        setupBottomNavigation();

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Recettes" : "Boissons");
        }).attach();
    }

    // IMPORTANT: Reset navigation state when returning from other activities
    @Override
    protected void onResume() {
        super.onResume();
        // Always reset to home when returning to MainActivity
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        // Set home as default selected
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Already on home, do nothing
                return true;

            } else if (itemId == R.id.nav_favorites) {
                // Open Favorites Activity
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);

                // IMPORTANT: Don't return true immediately - let the state reset in onResume
                return false; // This prevents the icon from staying highlighted

            }
            return false;
        });
    }

    public class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return position == 0 ? new RecettesFragment() : new BoissonsFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}