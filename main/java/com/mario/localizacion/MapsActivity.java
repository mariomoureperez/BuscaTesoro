package com.mario.localizacion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,GoogleMap.OnMapLongClickListener {
    public static final int LOCATION_REQUEST_CODE = 1;
    public final static int CODIGO=1;
    public static String result="";
    public static String distancia;
    private GoogleMap mMap;
    public static double lat1, lng1;//latitud y longitud de mi posicion
    public static double lat2 =42.236323; //latitud de la primera pista
    public static double lng2 = -8.712158; //longitud de la primera pista
    public static double lat3 =42.236974; //latitud de la segunda pista
    public static double lng3 =-8.713468; //longitud de la segunda pista
    //marca vigo=42.236323, -8.712158
    //marca casa= 41.973718,-8.749834
    //marca2 vigo=42.236974, -8.713468
    public static Marker marcaT;

    private GoogleApiClient apiClient;
    private static final String LOGTAG = "android-localizacion";

    // Ejemplo: Crear círculo con radio de 100m
    // y centro (42.236954,  -8.712717)
    LatLng center = new LatLng(42.236954, -8.712717);
    int radius = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //activar la escucha para detectar si pulsamos la pantalla y que nos salan las coordenadas
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        // Add a marker in vigo and move the camera
        LatLng tesoro = new LatLng(lat2, lng2);

        marcaT = mMap.addMarker(new MarkerOptions().position(tesoro).title("Tesoro").snippet("Marca Tesoro"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tesoro));
        marcaT.setVisible(false);

        // Controles UI
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        CircleOptions circleOptions = new CircleOptions()
                .center(center)
                .radius(radius)
                .strokeColor(Color.parseColor("#0D47A1"))
                .strokeWidth(4)
                .fillColor(Color.argb(32, 33, 150, 243));
        // Añadir círculo
        Circle circle = mMap.addCircle(circleOptions);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // ¿Permisos asignados?
            if (permissions.length > 0 &&
                    permissions[0].equals(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }

        }
    }
    //Método de la interfaz onMapClickListener para que al pulsar en el mapa haga lo que queramos descrito en el método
    @Override
    public void onMapClick(LatLng latLng) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Asi tendremos nuestra localización
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        updateUI(lastLocation);
        calcularDistancia();

        if(result.equals("HasGanado")){
            Toast.makeText(this, "Has Ganado", Toast.LENGTH_LONG).show();
        }

        if(result.equals("SiguientePista")){
            LatLng tesoro2 = new LatLng(lat3, lng3);
            lat2=lat3;
            lng2=lng3;
            marcaT.remove();
            marcaT=mMap.addMarker(new MarkerOptions().position(tesoro2).title("Tesoro2").snippet("Marca Tesoro2").visible(false));
            Toast.makeText(this, "Vuelve a pulsar para actualizar la distancia", Toast.LENGTH_SHORT).show();
            result="";
            }else{
            Toast.makeText(this, distancia+" metros ", Toast.LENGTH_LONG).show();
        }

    }




    //calculamos la distancia entre mi posición y la pista.
    public void calcularDistancia() {
        /*String la= String.valueOf(lat1);
        String lo =String.valueOf(lng1);
        Toast.makeText(this, la+" "+lo, Toast.LENGTH_LONG).show();*/

        double earthRadius = 6372.795477598;

        double dLat = Math.toRadians(lat1-lat2);
        double dLng = Math.toRadians(lng1-lng2);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(lat1)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        double distMet=dist*1000;
        int distCort=(int) distMet;
        distancia=String.valueOf(distCort);



        if(distMet<=20){
            marcaT.setVisible(true);
        }else {
            marcaT.setVisible(false);
        }



    }
    //Recogemos los datos de nuestra posición
    private void updateUI(Location loc) {

        if (loc != null) {
            lat1=loc.getLatitude();
            lng1=loc.getLongitude();
            //Tosat para saber longitud y latitud de mi posicion
           // Toast.makeText(this, String.valueOf(lat1)+" "+String.valueOf(lng1), Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(this, "Latitud y Longitud desconocidas", Toast.LENGTH_LONG).show();

        }


}

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            //Obtener lat y long de mi posicion y se lo pasamos al updateUI.
            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services

        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
    }


    //metodo para detectar un toque largo en la pantalla
    @Override
    public void onMapLongClick(LatLng latLng) {
        Intent intent=new Intent(this,CodigoQR.class);
        //mensaje que pasaremos a la activity CodigoQR que en principio no nos hace falta
       // intent.putExtra("llamar","mensaje desde fragment");
        startActivityForResult(intent,CODIGO);
    }

    //metodo para comunicar las actividades entre si y pasar datos
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == CODIGO){
            if(resultCode == RESULT_OK){
                 result=data.getStringExtra("pista2");

            }
        }
    }
}
