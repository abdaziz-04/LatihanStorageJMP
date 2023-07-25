package com.pens.latihanstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class EksternalActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FILENAME = "namafile.txt";
    public static String TAG = "EksternalActivity";
    public static final int REQUEST_CODE_STORAGE = 1;

    Button buatFile, ubahFile, bacaFile, deleteFile;
    public int selectEvent = 0;

    TextView textBaca;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eksternal);

        buatFile = findViewById(R.id.buttonBuatFile);
        ubahFile = findViewById(R.id.buttonUbahFile);
        bacaFile = findViewById(R.id.buttonBacaFile);
        deleteFile = findViewById(R.id.buttonHapusFile);
        textBaca = findViewById(R.id.textBaca);


        buatFile.setOnClickListener(this);
        ubahFile.setOnClickListener(this);
        bacaFile.setOnClickListener(this);
        deleteFile.setOnClickListener(this);

    }

    public boolean checkStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11 (R) or above
            return Environment.isExternalStorageManager();
        } else {
            //Below android 11
            int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED;
        }
    }


    public boolean periksaIzinPenyimpanan() {
        ActivityCompat.requestPermissions(EksternalActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    jalankanPerintah(selectEvent);
                }
                break;
        }
    }

    void buatFile() {
        String isiFile = "Coba Isi Data File Text";
        String state = Environment.getExternalStorageState();

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory(), FILENAME);

        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file, true);
            outputStream.write(isiFile.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void ubahFile() {
        String ubah = "Update Isi Data File Text";
        String state = Environment.getExternalStorageState();

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory(), FILENAME);
        Log.d("ExternalActivity", "Location : " + Environment.getExternalStorageDirectory());

        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file, false);
            outputStream.write(ubah.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bacaFile() {
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, FILENAME);

        if (file.exists()) {

            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));

                String line = br.readLine();

                while (line != null) {
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Error " + e.getMessage());
            }
            textBaca.setText(text.toString());
        }
    }

    void hapusFile() {
        File file = new File(Environment.getExternalStorageDirectory(), FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.buttonBuatFile || viewId == R.id.buttonBacaFile || viewId == R.id.buttonUbahFile || viewId == R.id.buttonHapusFile) {
            if (periksaIzinPenyimpanan()) {
                Log.d("ExternalActivity", "true");
                selectEvent = viewId;
                jalankanPerintah(viewId);
            } else {
                Log.d("ExternalActivity", "false");
            }
        }
    }

    public void jalankanPerintah(int id) {
        if (id == R.id.buttonBuatFile) {
            buatFile();
        } else if (id == R.id.buttonBacaFile) {
            bacaFile();
        } else if (id == R.id.buttonUbahFile) {
            ubahFile();
        } else if (id == R.id.buttonHapusFile) {
            hapusFile();
        }
    }
}