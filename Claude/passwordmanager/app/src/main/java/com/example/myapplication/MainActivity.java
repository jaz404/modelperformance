package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.os.Environment;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PasswordAdapter adapter;
    private List<Password> passwords;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        passwords = dbHelper.getAllPasswords();

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new PasswordAdapter(passwords);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showAddPasswordDialog());

        Button exportButton = findViewById(R.id.exportButton);
        exportButton.setOnClickListener(v -> exportToExcel());
    }

    private void showAddPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_password, null);
        builder.setView(dialogView);

        final EditText accountNameInput = dialogView.findViewById(R.id.accountNameInput);
        final EditText usernameInput = dialogView.findViewById(R.id.usernameInput);
        final EditText passwordInput = dialogView.findViewById(R.id.passwordInput);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String accountName = accountNameInput.getText().toString();
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            Password newPassword = new Password(accountName, username, password);
            dbHelper.addPassword(newPassword);
            passwords.add(newPassword);
            adapter.notifyDataSetChanged();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }



// ... (in MainActivity class)

    private void exportToExcel() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Passwords");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Account Name");
        headerRow.createCell(1).setCellValue("Username");
        headerRow.createCell(2).setCellValue("Password");

        // Add data rows
        List<Password> passwords = dbHelper.getAllPasswords();
        for (int i = 0; i < passwords.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Password password = passwords.get(i);
            row.createCell(0).setCellValue(password.getAccountName());
            row.createCell(1).setCellValue(password.getUsername());
            row.createCell(2).setCellValue(password.getPassword());
        }

        // Auto-size columns
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        String fileName = "PasswordManager_Export_" + System.currentTimeMillis() + ".xlsx";
        File file = new File(getExternalFilesDir(null), fileName);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
            Toast.makeText(this, "Exported to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Export failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}