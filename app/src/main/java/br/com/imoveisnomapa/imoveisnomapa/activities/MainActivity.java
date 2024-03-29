package br.com.imoveisnomapa.imoveisnomapa.activities;

import android.Manifest;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import br.com.imoveisnomapa.imoveisnomapa.R;
import br.com.imoveisnomapa.imoveisnomapa.activities.FiltroActivity;
import br.com.imoveisnomapa.imoveisnomapa.fragments.gMapFragment;
import br.com.imoveisnomapa.imoveisnomapa.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LocationManager locationManager;

    Criteria criteria = new Criteria();

    String provider;

    Location location;

    String operacao = "";

    Gson gson = new Gson();

    Boolean filtro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Solicita as permissÃµes
        String[] permissoes = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        };
        PermissionUtils.validate(this, 0, permissoes);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            filtro = bundle.getBoolean("filtro");
        }

        if (filtro == false) {
            drawer.openDrawer(Gravity.LEFT);

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            provider = locationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = locationManager.getLastKnownLocation(provider);
        } else {
            FragmentManager fm = getFragmentManager();

            gMapFragment mapFragment = new gMapFragment();

            mapFragment.setArguments(bundle);

            fm.beginTransaction().replace(R.id.content_frame, mapFragment).commit();


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Alguma permissÃ£o foi negada, agora Ã© com vocÃª :-)
                alertAndFinish();
                return;
            }
        }

        // Se chegou aqui estÃ¡ OK :-)
    }

    private void alertAndFinish() {
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name).setMessage("Para utilizar este aplicativo, vocÃª precisa aceitar as permissÃµes.");
            // Add the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.filtro) {
            Intent intent = new Intent(this, FiltroActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("operacao", operacao);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fm = getFragmentManager();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.venda) {
            gMapFragment mapFragment = new gMapFragment();

            Bundle args = new Bundle();
            args.putString("operacao", "Venda");
            args.putDouble("latitude", location.getLatitude());
            args.putDouble("longitude", location.getLongitude());

            operacao = "Venda";

            mapFragment.setArguments(args);

            fm.beginTransaction().replace(R.id.content_frame, mapFragment).commit();
        } else if (id == R.id.aluguel) {
            gMapFragment mapFragment = new gMapFragment();

            Bundle args = new Bundle();
            args.putString("operacao", "Aluguel");
            args.putDouble("latitude", location.getLatitude());
            args.putDouble("longitude", location.getLongitude());

            operacao = "Aluguel";

            mapFragment.setArguments(args);

            fm.beginTransaction().replace(R.id.content_frame, mapFragment).commit();
        } else if (id == R.id.contato) {
            Intent intent = new Intent(this, ContatoActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
