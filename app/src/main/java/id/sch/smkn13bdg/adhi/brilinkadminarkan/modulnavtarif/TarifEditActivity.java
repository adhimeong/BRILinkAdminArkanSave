package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavtarif;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import faranjit.currency.edittext.CurrencyEditText;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.SharedPrefManager;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.UserController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

public class TarifEditActivity extends AppCompatActivity {

    private ProgressDialog pd;

    RadioGroup tarifjenisgroup;

    String urldata = "app/tarif_proses.php";
    String url = Server.url_server +urldata;

    CurrencyEditText atas, bawah, tarif;
    String atastxt, bawahtxt, tariftxt, idtariftxt, jenistxt, aksitxt;
    String statas, stbawah, sttarif, stidtarif, stjenis, staksi, idadmin;
    TextView jenis;
    Button btnproses, btnhapus;

    int success;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarif_edit);

        UserController user = SharedPrefManager.getInstance(this.getApplicationContext()).getUser();
        idadmin = user.getIdadmin();

        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        //ambil data dari fragment
        bawahtxt = getIntent().getStringExtra("bawah");
        atastxt = getIntent().getStringExtra("atas");
        tariftxt = getIntent().getStringExtra("tarif");
        aksitxt = getIntent().getStringExtra("aksi");

        //set tampilan dengan data yang ada
        atas = (CurrencyEditText) findViewById(R.id.tarifbatasatas);
        bawah = (CurrencyEditText) findViewById(R.id.tarifbatasbawah);
        tarif = (CurrencyEditText) findViewById(R.id.tariftarif);
        jenis = (TextView) findViewById(R.id.tarifjenis);
        btnproses = (Button) findViewById(R.id.tarifbtnproses);
        btnhapus = (Button) findViewById(R.id.tarifbtnhapus);
        tarifjenisgroup = (RadioGroup) findViewById(R.id.tarifjenisradiogroup);

        if (aksitxt.equals("tambah")){
            //atur radio button
            idtariftxt = "0";
            tarifjenisgroup.setVisibility(View.VISIBLE);
        }else{
            btnhapus.setVisibility(View.VISIBLE);
            //update data diambil dari fragmen sebelumnya
            idtariftxt = getIntent().getStringExtra("idtarif");
            jenistxt = getIntent().getStringExtra("jenis");
        }

        btnhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                statas = "0";
                stbawah = "0";
                sttarif = "0";
                stidtarif = idtariftxt;
                stjenis = jenistxt;
                staksi = "hapus";

                load_proses_tarif(statas, stbawah, sttarif, stidtarif, stjenis, staksi, idadmin);

            }
        });


        btnproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double d1 = 0;
                double d2 = 0;
                double d3 = 0;
                try {
                    d1 = atas.getCurrencyDouble();
                    d2 = bawah.getCurrencyDouble();
                    d3 = tarif.getCurrencyDouble();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int id1 = (int) d1;
                int id2 = (int) d2;
                int id3 = (int) d3;

                statas = String.valueOf(id1);
                stbawah = String.valueOf(id2);
                sttarif = String.valueOf(id3);

                if (aksitxt.equals("tambah")){
                    int id = tarifjenisgroup.getCheckedRadioButtonId();
                    switch (id){
                        case R.id.radiosesama :
                            jenistxt = "sesama";
                            break;
                        case R.id.radioantar :
                            jenistxt = "antar";
                            break;
                    }
                }

                stidtarif = idtariftxt;
                stjenis = jenistxt;
                staksi = aksitxt;

                load_proses_tarif(statas, stbawah, sttarif, stidtarif, stjenis, staksi, idadmin);
            }
        });

        //untuk tampilan
        if (aksitxt.equals("tambah")){
            btnproses.setText("SIMPAN TARIF");
            jenis.setText("TAMBAH BARU");

        }else{
            btnproses.setText("UPDATE TARIF");
            atas.setText(atastxt);
            bawah.setText(bawahtxt);
            tarif.setText(tariftxt);

            if (jenistxt.equals("sesama")){
                jenis.setText("SESAMA BRI");
            }else if (jenistxt.equals("antar")){
                jenis.setText("ANTAR BANK");
            }
        }

    }



    public void load_proses_tarif(final String a, final String b, final String c, final String d, final String e, final String f, final String g){
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
                params.put("atas", a);
                params.put("bawah", b);
                params.put("tarif", c);
                params.put("idtarif", d);
                params.put("jenis", e);
                params.put("aksi", f);
                params.put("idadmin", g);
                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
