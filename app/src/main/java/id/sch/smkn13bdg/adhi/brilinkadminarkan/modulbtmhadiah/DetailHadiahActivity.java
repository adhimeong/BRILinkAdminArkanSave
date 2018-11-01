package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmhadiah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.vision.barcode.Barcode;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.MainActivity;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.ScanCardActivity;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

public class DetailHadiahActivity extends AppCompatActivity{

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    private ProgressDialog pd;

    //foto dari server
    NetworkImageView imageprev;
    ImageLoader mImageLoader;
    String url = Server.url_server +"app/hadiah/";
    String IMAGE_URL ;

    String urldata2 = "app/service_detailhadiah.php";
    String url2 = Server.url_server +urldata2;

    String urldata3 = "app/perolehanpoint.php";
    String url3 = Server.url_server +urldata3;

    String urldata4 = "app/perosesambilhadiah.php";
    String url4 = Server.url_server +urldata4;

    String message, idhadiah, id_kartu, stringjmlhitemsakhir ;

    TextView txtnamahadiah, txtpointhadiah, txtitemshadiah, result, txtpointnasabah;
    Button btnscan, btnproses;

    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hadiah);

        //permisi kamera
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        txtnamahadiah = (TextView) findViewById(R.id.detailnamahadiah);
        txtpointhadiah = (TextView) findViewById(R.id.detailpointhadiah);
        txtitemshadiah = (TextView) findViewById(R.id.detailitemshadiah);
        imageprev = (NetworkImageView) findViewById(R.id.detailfotohadiah);

        result = (TextView) findViewById(R.id.detailkodenasabah);
        txtpointnasabah = (TextView) findViewById(R.id.detailpointnasabah);
        btnscan = (Button) findViewById(R.id.btndetailscan);
        btnproses = (Button) findViewById(R.id.btndetailproses);

        //ambil data dari fragment
        idhadiah = getIntent().getStringExtra("idhadiah");

        load_datahadiah_from_server();

        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ujicoba non kamera
                //result.setText("K2047345");
                //id_kartu = result.getText().toString();
                //load_pointnasabah_from_server();

                //scan qrcode
                Intent intent = new Intent(getApplicationContext(), ScanCardActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btnproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringpointhadiah = txtpointhadiah.getText().toString();
                int pointhadiah = Integer.parseInt(stringpointhadiah);

                String stringpointnasabah = txtpointnasabah.getText().toString();
                int pointnasabah = Integer.parseInt(stringpointnasabah);

                String stringjmlitms =txtitemshadiah.getText().toString();
                int jmlhitms = Integer.parseInt(stringjmlitms);

                if ( pointnasabah >= pointhadiah){
                    int jmlhitms2 = jmlhitms - 1;

                    stringjmlhitemsakhir = String.valueOf(jmlhitms2);
                    final String pointtxt = txtpointhadiah.getText().toString();
                    load_proses_to_server(pointtxt);

                }else{
                    FancyToast.makeText(getApplicationContext(),"POINT PELANGGAN KURANG",FancyToast.LENGTH_SHORT, FancyToast.ERROR,true).show();
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {

                    @Override
                    public void run() {
                        result.setText(barcode.displayValue);
                        id_kartu = result.getText().toString();
                        load_pointnasabah_from_server();

                    }
                });
            }
        }
    }

    public void load_datahadiah_from_server() {
        pd.show();

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

                                String id_hadiah = jsonobject.getString("id_hadiah").trim();
                                String nama_hadiah = jsonobject.getString("nama_hadiah").trim();
                                String foto_hadiah = jsonobject.getString("foto_hadiah").trim();
                                String jumlah_point = jsonobject.getString("jumlah_point").trim();
                                String jumlah_items = jsonobject.getString("jumlah_items").trim();

                                txtnamahadiah.setText(nama_hadiah);
                                txtpointhadiah.setText(jumlah_point);
                                txtitemshadiah.setText(jumlah_items);

                                mImageLoader = MySingleton.getInstance(getApplicationContext()).getImageLoader();
                                IMAGE_URL = url + String.valueOf(foto_hadiah);
                                imageprev.setImageUrl(IMAGE_URL, mImageLoader);

                            }
                        } catch (JSONException e) {
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
                params.put("id_hadiah", idhadiah);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void load_pointnasabah_from_server() {
        pd.show();

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

                                String nama_pelanggan = jsonobject.getString("nama_pelanggan").trim();
                                String jumlah_point = jsonobject.getString("jumlah_point").trim();
                                String tanggal_pasif = jsonobject.getString("tanggal_pasif").trim();

                                txtpointnasabah.setText(jumlah_point);

                            }
                        } catch (JSONException e) {
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
                params.put("no_kartu", id_kartu);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void load_proses_to_server(final String point){
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url4,
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
                                Intent intent = new Intent(getApplication(), MainActivity.class);
                                startActivity(intent);
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
                params.put("no_kartu", id_kartu);
                params.put("point", point);
                params.put("id_hadiah", idhadiah);
                params.put("jumlahitems", stringjmlhitemsakhir);
                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

}
