package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpengguna;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.SharedPrefManager;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataHadiahAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataPemenangAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataPointAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataTransaksiAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataHadiahController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataPemenangController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataPointController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataTransaksiController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.UserController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class PenggunaDetailActivity extends AppCompatActivity {

    String nokartu;
    TabHost mtabs;
    String idadmin;
    String cari;
    TextView viewnama, viewnokartu, viewkontak, viewalamat, viewemail, viewjmlhtrans, viewjmlhpoint;

    private ProgressDialog pd;

    //data transaksi
    String urldata1 = "app/pengguna_detail_transaksi.php";
    String url1 = Server.url_server +urldata1;
    List<DataTransaksiController> dataController1 = new ArrayList<DataTransaksiController>();
    DataTransaksiAdapter adapter1;
    ListView listView1;

    //data point
    String urldata2 = "app/pengguna_detail_point.php";
    String url2 = Server.url_server +urldata2;
    List<DataPointController> dataController2 = new ArrayList<DataPointController>();
    DataPointAdapter adapter2;
    ListView listView2;

    //data hadiah
    String urldata3 = "app/pengguna_detail_pemenang.php";
    String url3 = Server.url_server +urldata3;
    List<DataPemenangController> dataController3 = new ArrayList<DataPemenangController>();
    DataPemenangAdapter adapter3;
    ListView listView3;

    //data profil penguna
    NetworkImageView imageprev;
    ImageLoader mImageLoader;
    String url = Server.url_server +"app/images/";
    String IMAGE_URL ;
    String fotodefault = "defaultprofile.jpg";
    String urldata4 = "app/profilpelanggan.php";
    String url4 = Server.url_server +urldata4;

    //data jumlah transaksi point pengguna
    String urldata5 = "app/pengguna_detail_jumlahpointtrans.php";
    String url5 = Server.url_server +urldata5;

    //tambah point
    String urldata6 = "app/point_tambah.php";
    String url6 = Server.url_server +urldata6;
    int success;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna_detail);

        //ambil data dari fragment
        cari = getIntent().getStringExtra("nokartu");

        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        viewnama = (TextView) findViewById(R.id.txtprofilnama);
        viewkontak = (TextView)findViewById(R.id.txtprofilkontak);
        viewemail = (TextView) findViewById(R.id.txtprofilemail);
        viewalamat = (TextView) findViewById(R.id.txtprofilalamat);
        viewnokartu = (TextView) findViewById(R.id.penggunadetailnokartu);
        imageprev = (NetworkImageView) findViewById(R.id.profilprevimage);
        viewjmlhtrans = (TextView) findViewById(R.id.penggunadetailjmlhtransaksi);
        viewjmlhpoint = (TextView) findViewById(R.id.penggunadetailjmlhpoint);

        load_datapelanggan_from_server(cari);
        load_jumlah_data_from_server(cari);

        mtabs = (TabHost) findViewById(R.id.tabHost);
        mtabs.setup();
        //tab satu
        TabHost.TabSpec mSpec = mtabs.newTabSpec("TRANSAKSI");
        mSpec.setContent(R.id.tab1);
        mSpec.setIndicator("TRANSAKSI");
        mtabs.addTab(mSpec);
        //tab dua
        mSpec = mtabs.newTabSpec("POINT");
        mSpec.setContent(R.id.tab2);
        mSpec.setIndicator("POINT");
        mtabs.addTab(mSpec);
        //tab tiga
        mSpec = mtabs.newTabSpec("HADIAH");
        mSpec.setContent(R.id.tab3);
        mSpec.setIndicator("HADIAH");
        mtabs.addTab(mSpec);

        UserController user = SharedPrefManager.getInstance(this.getApplicationContext()).getUser();
        idadmin = user.getIdadmin();

        //list transaksi
        listView1 = (ListView)findViewById(R.id.listview01);
        dataController1.clear();
        adapter1 = new DataTransaksiAdapter(dataController1, this );
        listView1.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
        load_data_transaksi_from_server(cari);

        //list point
        listView2 = (ListView)findViewById(R.id.listview02);
        dataController2.clear();
        adapter2 = new DataPointAdapter(dataController2, this );
        listView2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        load_data_point_from_server(cari);


        //list hadiah
        listView3 = (ListView)findViewById(R.id.listview03);
        dataController3.clear();
        adapter3 = new DataPemenangAdapter(dataController3, this );
        listView3.setAdapter(adapter3);
        adapter3.notifyDataSetChanged();
        load_data_hadiah_from_server(cari);

        final FloatingActionButton fab2 = findViewById(R.id.fab2);
        final FloatingActionButton fab3 = findViewById(R.id.fab3);
        final FloatingActionButton fab4 = findViewById(R.id.fab4);

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PenggunaTransaksiActivity.class);
                i.putExtra("nokartu", cari);
                startActivity(i);

            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pengumaman
                final PrettyDialog pDialog = new PrettyDialog(PenggunaDetailActivity.this);
                pDialog
                        .setTitle("TAMBAH POINT")
                        .setTitleColor(R.color.colorAccent)
                        .setMessage("Tambah Point untuk " + cari + " ?")
                        .setAnimationEnabled(false)
                        .setIcon(
                                R.drawable.pdlg_icon_info,     // icon resource
                                R.color.colorAccent,      // icon tint
                                new PrettyDialogCallback() {   // icon OnClick listener
                                    @Override
                                    public void onClick() {
                                        pDialog.dismiss();
                                    }
                                })
                        .addButton(
                                "TAMBAH POINT",     // button text
                                R.color.pdlg_color_white,  // button text color
                                R.color.colorAccent,  // button background color
                                new PrettyDialogCallback() {  // button OnClick listener
                                    @Override
                                    public void onClick() {

                                        load_tambah_point(cari, idadmin);

                                        pDialog.dismiss();
                                    }
                                }
                        )

                        .addButton(
                                "BATAL",     // button text
                                R.color.pdlg_color_white,  // button text color
                                R.color.colorPrimaryDark,  // button background color
                                new PrettyDialogCallback() {  // button OnClick listener
                                    @Override
                                    public void onClick() {

                                        pDialog.dismiss();
                                    }
                                }
                        )
                        .show();

            }
        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PenggunaHadiahActivity.class);
                i.putExtra("nokartu", cari);
                startActivity(i);
            }
        });




    }

    public void load_datapelanggan_from_server(final String namakartu) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url4,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            if (jsonarray.length() == 0){
                                finish();
                                FancyToast.makeText(getApplicationContext(),"No Kartu tidak ditemukan",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            }

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                nokartu = jsonobject.getString("no_kartu").trim();
                                String nama = jsonobject.getString("nama_pelanggan").trim();
                                String foto = jsonobject.getString("foto");
                                String alamat = jsonobject.getString("alamat").trim();
                                String kontak = jsonobject.getString("kontak").trim();
                                String email = jsonobject.getString("email").trim();

                                viewnokartu.setText(nokartu);

                                if (nama.equals("null")){
                                    viewnama.setText("Nama : kosong");
                                }else{
                                    viewnama.setText(nama);
                                }

                                if (kontak.equals("null")){
                                    viewkontak.setText("Kontak : kosong");
                                }else{
                                    viewkontak.setText(kontak);
                                }

                                if (email.equals("null")){
                                    viewemail.setText("Email : kosong");
                                }else{
                                    viewemail.setText(email);
                                }

                                if (alamat.equals("null")){
                                    viewalamat.setText("Alamat : kosong");
                                }else{
                                    viewalamat.setText(alamat);
                                }

                                if (foto.equals("null")){
                                    mImageLoader = MySingleton.getInstance(getApplicationContext()).getImageLoader();
                                    IMAGE_URL = url + String.valueOf(fotodefault);
                                    imageprev.setImageUrl(IMAGE_URL, mImageLoader);
                                }else{
                                    mImageLoader = MySingleton.getInstance(getApplicationContext()).getImageLoader();
                                    IMAGE_URL = url + String.valueOf(foto);
                                    imageprev.setImageUrl(IMAGE_URL, mImageLoader);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            pd.hide();
                            finish();
                        }
                    }
                }

        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("namakartu", namakartu);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void load_jumlah_data_from_server(final String kartu){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url5,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            if (jsonarray.length() == 0){
                                finish();
                                FancyToast.makeText(getApplicationContext(),"No Kartu tidak ditemukan",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            }

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String jmlhtransaksi = jsonobject.getString("jumlah_transaksi").trim();
                                String jmlhpoint = jsonobject.getString("jumlah_point").trim();

                                viewjmlhtrans.setText(jmlhtransaksi);
                                viewjmlhpoint.setText(jmlhpoint);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            pd.hide();
                            finish();
                        }
                    }
                }

        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("no_kartu", kartu);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    public void load_data_transaksi_from_server(final String kartu) {
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url1,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String id_transaksi = jsonobject.getString("id_transaksi").trim();
                                String no_kartu = jsonobject.getString("no_kartu").trim();
                                String rek_tujuan = jsonobject.getString("rek_tujuan").trim();
                                String nominal = jsonobject.getString("nominal").trim();
                                String bank = jsonobject.getString("bank_tujuan").trim();
                                String jenis = jsonobject.getString("jenis_transaksi").trim();

                                DataTransaksiController d1 = new DataTransaksiController();
                                d1.setId_tansaksi(id_transaksi.toString());
                                d1.setNo_kartu(no_kartu.toString());
                                d1.setRek_tujuan(rek_tujuan.toString());
                                d1.setNominal(nominal.toString());
                                d1.setBank(bank.toString());
                                d1.setJenis(jenis.toString());

                                dataController1.add(d1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter1.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                        }
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("no_kartu", kartu);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void load_data_point_from_server(final String kartu) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url2,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String id_perolehan = jsonobject.getString("id_perolehan").trim();
                                String no_kartu = jsonobject.getString("no_kartu").trim();
                                String waktu = jsonobject.getString("waktu").trim();
                                String tanggal = jsonobject.getString("tanggal").trim();
                                String admin = jsonobject.getString("admin").trim();

                                DataPointController d1 = new DataPointController();
                                d1.setId_perolehan(id_perolehan.toString());
                                d1.setNo_kartu(no_kartu.toString());
                                d1.setWaktu(waktu.toString());
                                d1.setTanggal_point(tanggal.toString());
                                d1.setAdmin(admin.toString());


                                dataController2.add(d1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter2.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                        }
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("no_kartu", kartu);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void load_data_hadiah_from_server(final String kartu) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url3,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String nama= jsonobject.getString("nama").trim();
                                String admin = jsonobject.getString("admin").trim();
                                String tanggal = jsonobject.getString("tanggal").trim();
                                String hadiah = jsonobject.getString("hadiah").trim();
                                String no_kartu = jsonobject.getString("nokartu").trim();
                                String foto = jsonobject.getString("foto").trim();
                                String status = jsonobject.getString("status").trim();

                                DataPemenangController d1 = new DataPemenangController();
                                d1.setNama(nama.toString());
                                d1.setAdmin(admin.toString());
                                d1.setTanggal(tanggal.toString());
                                d1.setHadiah(hadiah.toString());
                                d1.setNo_kartu(no_kartu.toString());
                                d1.setFoto(foto.toString());
                                d1.setStatus(status.toString());

                                dataController3.add(d1);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter3.notifyDataSetChanged();

                        pd.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            pd.hide();
                        }
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("no_kartu", kartu);
                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public  void load_tambah_point(final String no, final String id){

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url6,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("Response: ",response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt("success");
                            message = jObj.getString("message");

                            // Cek error node pada json
                            if (success == 1) {
                                Log.d("Add/update", jObj.toString());
                                /*Intent intent = new Intent(getApplication(), MainActivity.class);
                                startActivity(intent);*/
                                FancyToast.makeText(getApplicationContext(),message,FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                            } else {
                                FancyToast.makeText(getApplicationContext(),message,FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                        pd.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            pd.hide();
                        }
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("no_kartu", no);
                params.put("id_admin", id);
                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


}
