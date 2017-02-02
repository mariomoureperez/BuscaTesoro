package com.mario.localizacion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mario.localizacion.clasesCodigoQR.IntentIntegrator;
import com.mario.localizacion.clasesCodigoQR.IntentResult;

public class CodigoQR extends AppCompatActivity {
    public static String pista2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_qr);
        configureButtonReader();


        Button comunicar=(Button)findViewById(R.id.button2);

        comunicar.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent databack=new Intent();
                databack.putExtra("pista2",pista2);
                setResult(RESULT_OK,databack);
                finish();

            }


        });
    }

    private void configureButtonReader() {
        final ImageButton buttonReader = (ImageButton)findViewById(R.id.imageButton);
        buttonReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(CodigoQR.this).initiateScan();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        handleResult(scanResult);
    }

    private void handleResult(IntentResult scanResult) {
        if (scanResult != null) {
            updateUITextViews(scanResult.getContents(), scanResult.getFormatName());
        } else {
            Toast.makeText(this, "No se ha leído nada :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUITextViews(String scan_result, String scan_result_format) {

        final TextView tvResult = (TextView)findViewById(R.id.tvResult);
        tvResult.setText(scan_result);
        //Le pasamos el resultado del QR a la variable pista2 que se la pasara a la activity principal en el metodo oncreate()
        pista2=scan_result;
        ////**********/////
        Linkify.addLinks(tvResult, Linkify.ALL);
    }
}
