package com.example.passwordmanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText accountNameEditText;
    private EditText passwordEditText;
    private Button saveButton;
    private Button exportButton;
    private ListView passwordListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> passwordList;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountNameEditText = findViewById(R.id.accountName);
        passwordEditText = findViewById(R.id.password);
        saveButton = findViewById(R.id.saveButton);
        exportButton = findViewById(R.id.exportButton);
        passwordListView = findViewById(R.id.passwordList);

        passwordList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, passwordList);
        passwordListView.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePassword();
            }
        });

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportToExcel();
            }
        });
    }

    private void savePassword() {
        String accountName = accountNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (!accountName.isEmpty() && !password.isEmpty()) {
            passwordList.add(accountName + ": " + password);
            adapter.notifyDataSetChanged();
            accountNameEditText.setText("");
            passwordEditText.setText("");
        } else {
            Toast.makeText(this, "Please enter both account name and password", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportToExcel() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            createExcelFile();
        }
    }

    private void createExcelFile() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Passwords");

        int rowCount = 0;
        for (String entry : passwordList) {
            Row row = sheet.createRow(rowCount++);
            String[] parts = entry.split(": ");
            row.createCell(0).setCellValue(parts[0]);
            row.createCell(1).setCellValue(parts[1]);
        }

        File file = new File(Environment.getExternalStorageDirectory(), "passwords.xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
            Toast.makeText(this, "Exported to Excel successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to export to Excel", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createExcelFile();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
