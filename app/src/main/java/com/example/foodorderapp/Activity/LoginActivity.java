package com.example.foodorderapp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import com.example.foodorderapp.Activity.Admin.AdminUserManagementActivity;
import com.example.foodorderapp.Entity.User;
import com.example.foodorderapp.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
    }

    private void setVariable() {
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.userEdt.getText().toString().trim();
                String pass = binding.passEdt.getText().toString().trim();

                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lấy user từ database
                User user = userRoomDatabase.userDao().getUserByEmail(email);
                if (user == null || !user.getPassword().equals(pass)) {
                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lưu thông tin đăng nhập vào SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", email);
                editor.putString("userRole", user.getRole()); // Giả sử User có method getRole()
                editor.apply();

                // Điều hướng theo role
                if (user.getRole().equals("Admin")) {
                    /*startActivity(new Intent(LoginActivity.this, AdminActivity.class));*/
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                finish();
            }
        });
    }
}