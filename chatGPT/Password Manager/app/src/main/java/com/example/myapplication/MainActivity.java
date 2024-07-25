package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private EditText accountNameEditText, usernameEditText, passwordEditText;
    private Button addPasswordButton, exportPasswordsButton;
    private ListView passwordListView;
    private AppDatabase db;
    private List<String> passwordList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountNameEditText = findViewById(R.id.account_name);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        addPasswordButton = findViewById(R.id.add_password);
        exportPasswordsButton = findViewById(R.id.export_button);
        passwordListView = findViewById(R.id.password_list);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "passwords-db")
                .allowMainThreadQueries()
                .build();

        passwordList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, passwordList);
        passwordListView.setAdapter(adapter);

        loadPasswords();

        addPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPassword();
            }
        });

        exportPasswordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    try {
                        exportToExcel();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    requestPermission();
                }
            }
        });
    }

    private void loadPasswords() {
        List<Password> passwords = db.passwordDao().getAll();
        for (Password password : passwords) {
            passwordList.add(password.accountName + ": " + password.username + " / " + password.password);
        }
        adapter.notifyDataSetChanged();
    }

    private void addPassword() {
        String accountName = accountNameEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Password passwordEntity = new Password();
        passwordEntity.accountName = accountName;
        passwordEntity.username = username;
        passwordEntity.password = password;

        db.passwordDao().insert(passwordEntity);

        passwordList.add(accountName + ": " + username + " / " + password);
        adapter.notifyDataSetChanged();

        accountNameEditText.setText("");
        usernameEditText.setText("");
        passwordEditText.setText("");
    }

    private void exportToExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Passwords");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Account Name");
        headerRow.createCell(1).setCellValue("Username");
        headerRow.createCell(2).setCellValue("Password");

        List<Password> passwords = db.passwordDao().getAll();
        int rowIndex = 1;
        for (Password password : passwords) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(password.accountName);
            row.createCell(1).setCellValue(password.username);
            row.createCell(2).setCellValue(password.password);
        }

        // Save to Downloads directory
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (directory != null) {
            File file = new File(directory, "Passwords.xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            Toast.makeText(this, "Exported to Excel successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Directory not available", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    exportToExcel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Permission Denied. We can't save files.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
