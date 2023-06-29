package com.luisito.barcodescanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.barcode.common.Barcode;

import cafsoft.colombianidinfo.ColombianIDInfo;

public class MainActivity2 extends AppCompatActivity {
    private ActivityResultLauncher<Intent> actResLauncherBarcodeScan;
    private Button btnScan = null;
    private TextView documentType = null;
    private TextView documentNumber = null;
    private TextView names = null;
    private TextView lastnames = null;
    private TextView birthdate = null;
    private TextView birthplace = null;
    private TextView bloodtype = null;
    private TextView gender = null;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        ActivityResultContracts.StartActivityForResult actForRes;
        actForRes = new ActivityResultContracts.StartActivityForResult();
        actResLauncherBarcodeScan = registerForActivityResult(actForRes, result -> {
            if (result.getResultCode() == RESULT_OK) {
                assert result.getData() != null;
                String data = result.getData().getStringExtra("BarcodeData");
                int format = result.getData().getIntExtra("BarcodeFormat", -1);

                if (format == Barcode.FORMAT_PDF417) {
                    try {
                        ColombianIDInfo card = ColombianIDInfo.decode(data);

                        documentType.setText(card.getDocumentType().name().replaceAll("_", " "));

                        documentNumber.setText("" + card.getDocumentNumber());
                        names.setText(card.getName());
                        lastnames.setText(card.getFamilyName());

                        birthdate.setText(card.getDateOfBirth().substring(0,4) + '/' + card.getDateOfBirth().substring(4, 6) + '/' + card.getDateOfBirth().substring(6));

                        birthplace.setText(card.getBirthplaceCode());

                        bloodtype.setText(card.getBloodType());

                        if (card.getGender().equals("M")) {
                            gender.setText(R.string.male);
                        } else if (card.getGender().equals("F")) {
                            gender.setText(R.string.female);
                        }
                    } catch (Exception ignored) {

                    }
                }
            }
        });

        initEvents();
    }

    private void initViews(){
        btnScan = findViewById(R.id.butScan);
        documentType = findViewById(R.id.lblDocumentTypeValue);
        documentNumber = findViewById(R.id.lblNumberValue);
        names = findViewById(R.id.lblNamesValue);
        lastnames = findViewById(R.id.lblLastnamesValue);
        birthdate = findViewById(R.id.lblBirthdateValue);
        birthplace = findViewById(R.id.lblBirthplaceValue);
        bloodtype = findViewById(R.id.lblBloodtypeValue);
        gender = findViewById(R.id.lblGenderValue);
    }

    private void initEvents(){
        btnScan.setOnClickListener(view -> {
            Intent intent = new Intent(this, BarcodeScannerActivity.class);

            actResLauncherBarcodeScan.launch(intent);
        });
    }
}
