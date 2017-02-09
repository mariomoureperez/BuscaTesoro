package com.mario.localizacion;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mario.localizacion.clasesCodigoQR.IntentIntegrator;
import com.mario.localizacion.clasesCodigoQR.IntentResult;

public class CodigoQR extends AppCompatActivity {
    public static String pista2;
    public String fin="!!!ENHORABUENA¡¡¡\n\nHas encontrado el Tesoro escondido, vuelve a la base.\n\n" +
            "Recuerda la palabra escrita para demostrar que llegaste el primero.";

    public String ayuda="¿Necesitas ayuda?\n\n"+
            "1. Situate en el mapa activando el GPS.\n\n"+
            "2. Entra en el circulo azul donde estará escondida la primera pista.\n\n" +
            "3. Una vez dentro del circulo toca en la pantalla para saber a que distancia te encuentras de la pista.\n\n" +
            "4. Cuando estes a menos de 20 metros, te saldrá el punto exacto de la pista.\n\n" +
            "5. Una vez estés justo en el punto, busca el Código QR que te dirá donde esta la siguiente pista.\n\n" +
            "6. Una vez encuentres el Código QR, leelo haciendo una pulsación larga en el mapa, sigue las instrucciones y al terminar " +
            "vuelve a seguir los pasos anteriores, hasta llegar al tesoro.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_qr);
        configureButtonReader();


        Button comunicar=(Button)findViewById(R.id.button2);

        comunicar.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                if(pista2.equals("HasGanado")){
                    alertDialogo2();
                }else{
                Intent databack=new Intent();
                databack.putExtra("pista2",pista2);
                setResult(RESULT_OK,databack);
                finish();}

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

    public void alertDialogo2(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("¡¡¡ Has Ganado !!!");
        build.setMessage(fin);
        build.setPositiveButton("Aceptar",null);
        build.create();
        build.show();
    }

    public void dialogoAyuda(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("¡¡¡ AYUDA !!!");
        build.setMessage(ayuda);
        build.setPositiveButton("Aceptar",null);
        build.create();
        build.show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.add("Ayuda").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CharSequence title = item.getTitle();

        if (title != null && title.equals("Ayuda")) {
            dialogoAyuda();
        }
        return super.onOptionsItemSelected(item);
    }

}
