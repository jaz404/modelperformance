package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportToExcelActivity extends AppCompatActivity {
    private PasswordStorage passwordStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_excel);

        passwordStorage = new PasswordStorage(this);

        exportToExcel();
    }

    private void exportToExcel() {
        List<Password> passwords = passwordStorage.getPasswords();
        // ... (Excel export logic similar to previous response)
    }
}
