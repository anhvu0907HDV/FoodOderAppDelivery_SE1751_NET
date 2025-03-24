package com.example.foodorderapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;


import com.example.foodorderapp.DAO.UserDao;
import com.example.foodorderapp.DAO.UserRoomDatabase;
import com.example.foodorderapp.Entity.User;
import com.example.foodorderapp.databinding.ActivitySignupBinding;

public class SignupActivity extends BaseActivity {
    private ActivitySignupBinding binding;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Room Database
        UserRoomDatabase db = UserRoomDatabase.getInstance(this);
        userDao = db.userDao();

        setVariable();
        EdgeToEdge.enable(this);
    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(view -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });

        binding.signupBtn.setOnClickListener(view -> {
            String email = binding.userEdt.getText().toString().trim();
            String password = binding.passEdt.getText().toString().trim();
            String confirmPassword = binding.confirmPassEdt.getText().toString().trim();
            String name = binding.nameEdt.getText().toString().trim();
            String phone = binding.phoneEdt.getText().toString().trim();
            String avatarUrl = binding.avatarEdt.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(SignupActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra email đã tồn tại hay chưa trong cơ sở dữ liệu
            new Thread(() -> {
                User existingUser = userDao.getUserByEmail(email);
                if (existingUser != null) {
                    runOnUiThread(() ->
                            Toast.makeText(SignupActivity.this, "Email already exists", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    // Tạo tài khoản mới với vai trò "Customer"
                    User newUser = new User(name, email, password);
                    newUser.setPhoneNumber(phone);
                    newUser.setAvatarUrl(avatarUrl);
                    newUser.setRole("Customer"); // Gán vai trò mặc định là "Customer"
                    newUser.setCreatedAt(System.currentTimeMillis());
                    newUser.setUpdatedAt(System.currentTimeMillis());

                    userDao.insertUser(newUser);

                    runOnUiThread(() -> {
                        Toast.makeText(SignupActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        finish();
                    });
                }
            }).start();
        });
    }
}

