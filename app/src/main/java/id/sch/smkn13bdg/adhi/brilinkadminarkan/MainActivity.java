package id.sch.smkn13bdg.adhi.brilinkadminarkan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmhadiah.BagiHadiahFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmhadiah.TambahHadiahFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmlaporan.LaporanFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavbank.BankFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavbanner.BannerFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpemenang.PemenangFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpengguna.PenggunaFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpengumuman.PengumumanFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmtransaksi.ListTransaksiFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpoint.PointHariFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpoint.PointPengaturanFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavtarif.TarifFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_transaksi:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ListTransaksiFragment()).commit();
                    return true;
                case R.id.navigation_hadiah:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new BagiHadiahFragment()).commit();
                    return true;
                case R.id.navigation_point:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PointHariFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ListTransaksiFragment()).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pengguna) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PenggunaFragment()).commit();
        } else if (id == R.id.nav_pemenang) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PemenangFragment()).commit();
        } else if (id == R.id.nav_pengumuman) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PengumumanFragment()).commit();
        } else if (id == R.id.nav_tarif) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new TarifFragment()).commit();
        } else if (id == R.id.nav_bank) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new BankFragment()).commit();
        }else if (id == R.id.nav_banner) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new BannerFragment()).commit();
        }else if (id == R.id.nav_hadiah) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new TambahHadiahFragment()).commit();
        }else if (id == R.id.nav_periode) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PointPengaturanFragment()).commit();
        }else if (id == R.id.nav_laporan) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new LaporanFragment()).commit();
        }else if (id == R.id.nav_logout) {
            SharedPrefManager.getInstance(this.getApplicationContext()).logout();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
