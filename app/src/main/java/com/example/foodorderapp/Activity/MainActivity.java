package com.example.foodorderapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.foodorderapp.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private Button logoutBtn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private TextView navHeaderTitle;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest"); // Mặc định là "Guest"
        String userRole = sharedPreferences.getString("userRole", "Customer");
        // Lấy header view của NavigationView
        navigationView = findViewById(R.id.navigationView);

        Menu menu = navigationView.getMenu();

        // Ẩn tất cả mục menu liên quan đến Staff/Admin nếu user không phải Staff
        if (!userRole.equals("Staff") && !userRole.equals("Admin")) {
            menu.findItem(R.id.nav_dish_management).setVisible(false);
            menu.findItem(R.id.nav_menu_management).setVisible(false);
            menu.findItem(R.id.nav_new_orders).setVisible(false);
            menu.findItem(R.id.nav_revenue_stats).setVisible(false);
        }

        // Nếu user là Customer, có thể ẩn các mục khác nếu cần
        if (userRole.equals("Customer")) {
            menu.findItem(R.id.nav_settings).setVisible(false); // Ví dụ: Ẩn Settings cho Customer
        }
        headerView = navigationView.getHeaderView(0);

        // Tìm TextView trong header và cập nhật tên
        navHeaderTitle = headerView.findViewById(R.id.nav_header_title);
        navHeaderTitle.setText(username);

        // Ánh xạ
        drawerLayout = findViewById(R.id.drawerLayout);
        logoutBtn = findViewById(R.id.logoutBtn);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thêm nút Toggle để mở Drawer
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Xử lý khi nhấn vào các mục trong NavigationView
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Toast.makeText(MainActivity.this, "Home Selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_profile) {
                    Toast.makeText(MainActivity.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                } else if (id == R.id.nav_settings) {
                    Toast.makeText(MainActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_dish_management) { // Thêm mục mới
                    Toast.makeText(MainActivity.this, "Dish Management Selected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, DishManagementActivity.class));
                } else if (id == R.id.nav_menu_management) { // Thêm mục mới
                    Toast.makeText(MainActivity.this, "Menu Management Selected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, MenuManagementActivity.class));
                } else if (id == R.id.nav_new_orders) { // Thêm mục mới
                    Toast.makeText(MainActivity.this, "New Orders Selected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, NewOrdersActivity.class));
                } else if (id == R.id.nav_revenue_stats) { // Thêm mục mới
                    Toast.makeText(MainActivity.this, "Revenue Statistics Selected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, RevenueStatisticsActivity.class));
                } else if (id == R.id.nav_logout) {
                    Toast.makeText(MainActivity.this, "Logging Out", Toast.LENGTH_SHORT).show();
                    logoutUser();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void logoutUser() {
        // Xóa dữ liệu đăng nhập (nếu dùng SharedPreferences)
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa stack
        startActivity(intent);
        finish();
    }
}