package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavbank;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

public class BankEditActivity extends AppCompatActivity {

    private ProgressDialog pd;

    String urldata = "app/bank_proses.php";
    String url = Server.url_server +urldata;

    String idbanktxt, namabanktxt, kodebanktxt, aksitxt, idbank;
    EditText nama;
    EditText kode;
    String namabaru;
    String kodebaru;
    Button btnhapus, btnupdate, btntambah;
    String aksi;

    int success;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_edit);

        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        //ambil data dari fragment
        idbanktxt = getIntent().getStringExtra("idbank");
        aksitxt = getIntent().getStringExtra("aksi");

        nama = (EditText) findViewById(R.id.banknama);
        kode = (EditText) findViewById(R.id.bankkode);
        btnhapus = (Button) findViewById(R.id.bankbtnhapus);
        btnupdate = (Button) findViewById(R.id.bankbtnupdate);
        btntambah = (Button) findViewById(R.id.bankbtntambah);

        if (aksitxt.equals("tambah")){
            //atur radio button
            btnhapus.setVisibility(View.INVISIBLE);
            btnupdate.setVisibility(View.INVISIBLE);
            btntambah.setVisibility(View.VISIBLE);

        }else if (aksitxt.equals("edit")){

            btnhapus.setVisibility(View.VISIBLE);
            btnupdate.setVisibility(View.VISIBLE);
            btntambah.setVisibility(View.INVISIBLE);

            //update data diambil dari fragmen sebelumnya
            namabanktxt = getIntent().getStringExtra("namabank");
            kodebanktxt = getIntent().getStringExtra("kodebank");

            nama.setText(namabanktxt);
            kode.setText(kodebanktxt);
        }


        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aksi = "update";
                namabaru = nama.getText().toString();
                kodebaru = kode.getText().toString();
                load_proses_bank(idbanktxt, namabaru, kodebaru, aksi);
            }
        });

        btnhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aksi = "hapus";
                namabaru = "kosong";
                kodebaru = "kosong";
                load_proses_bank(idbanktxt, namabaru, kodebaru, aksi);
            }
        });

        btntambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aksi = "tambah";
                idbank = "kosong";
                namabaru = nama.getText().toString();
                kodebaru = kode.getText().toString();
                load_proses_bank(idbank, namabaru, kodebaru, aksi);
            }
        });
    }

    public void load_proses_bank(final String a, final String b, final String c, final String d){
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
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
                                Log.d("Add/update transaksi", jObj.toString());
                                FancyToast.makeText(getApplicationContext(),message,FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                            } else {
                                FancyToast.makeText(getApplicationContext(),message,FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                        pd.hide();
                        finish();
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
                params.put("idbank", a);
                params.put("namabank", b);
                params.put("kodebank", c);
                params.put("aksi", d);
                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
