package com.example.foodorderapp.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorderapp.DAO.UserRoomDatabase;
import com.example.foodorderapp.Entity.User;
import com.example.foodorderapp.R;

import java.sql.Date;

public class EditProfileActivity extends BaseActivity {
    private EditText etUserName, etUserPhone;
    private Button btnSave;
    private User currentUser;
    private String loggedInEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Hiển thị nút back trên ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Ánh xạ View
        etUserName = findViewById(R.id.etUserName);
        etUserPhone = findViewById(R.id.etUserPhone);
        btnSave = findViewById(R.id.btnSave);

        // Lấy email từ SharedPreferences
        loggedInEmail = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("username", null);

        if (loggedInEmail == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy email người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Lấy thông tin user từ database (Chạy trên Thread riêng để tránh block UI)
        new Thread(() -> {
            try {
                currentUser = userRoomDatabase.userDao().getUserByEmail(loggedInEmail);
                if (currentUser != null) {
                    runOnUiThread(() -> {
                        etUserName.setText(currentUser.getUsername());
                        etUserPhone.setText(currentUser.getPhoneNumber());
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(EditProfileActivity.this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(EditProfileActivity.this, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show());
            }
        }).start();

        // Xử lý sự kiện khi nhấn nút Lưu
        btnSave.setOnClickListener(view -> updateUserProfile());
    }

    private void updateUserProfile() {
        String newUserName = etUserName.getText().toString().trim();
        String newUserPhone = etUserPhone.getText().toString().trim();

        if (newUserName.isEmpty()) {
            etUserName.setError("Vui lòng nhập tên!");
            return;
        }

        // Kiểm tra số điện thoại (tuỳ chỉnh theo yêu cầu)
        if (!newUserPhone.matches("^\\d{10,11}$")) {
            etUserPhone.setError("Số điện thoại không hợp lệ!");
            return;
        }

        if (currentUser == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                currentUser.setUsername(newUserName);
                currentUser.setPhoneNumber(newUserPhone);
                currentUser.setUpdatedAt(System.currentTimeMillis());

                userRoomDatabase.userDao().updateUser(currentUser);

                runOnUiThread(() -> {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn hình trước
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(EditProfileActivity.this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Quay lại Activity trước đó
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

