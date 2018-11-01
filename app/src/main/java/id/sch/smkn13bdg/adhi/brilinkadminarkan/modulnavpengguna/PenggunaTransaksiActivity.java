package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpengguna;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import faranjit.currency.edittext.CurrencyEditText;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.PrintActivity;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.SharedPrefManager;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.UserController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

public class PenggunaTransaksiActivity extends Activity {

    private ProgressDialog pd;

    String urldata = "app/prosestransaksi.php";
    String url = Server.url_server +urldata;

    String urldata1 = "app/cektarif.php";
    String url1 = Server.url_server +urldata1;

    EditText notujuan, edittarif;
    String no_kartu,rektujuan,nominal,transaksi,bank,message,tariftransaksi, statustransaksi, cek;
    Spinner banktujuan, jenistransaksi;
    Button btnproses;
    TextView txttarif, nokartu;
    String idadmin;

    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna_transaksi);

        //ambil data dari fragment
        no_kartu = getIntent().getStringExtra("nokartu");

        UserController user = SharedPrefManager.getInstance(this.getApplicationContext()).getUser();
        idadmin = user.getIdadmin();

        banktujuan = (Spinner)findViewById(R.id.spinnertransasibank);
        jenistransaksi = (Spinner)findViewById(R.id.spinnertransasijenistransaksi);
        nokartu = (TextView) findViewById(R.id.transaksinokartu);
        notujuan = (EditText) findViewById(R.id.edittransaksitujuan);
        btnproses =(Button) findViewById(R.id.btntransasiproses);
        txttarif = (TextView) findViewById(R.id.txttarif);
        edittarif = (EditText) findViewById(R.id.edittransaksitarif);
        final CurrencyEditText nominaltransaksi = (CurrencyEditText) findViewById(R.id.edittransaksinominal);
        nokartu.setText(no_kartu);

        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        jenistransaksi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                transaksi = jenistransaksi.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), "dipilih " + jenistransaksi.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        banktujuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                bank = banktujuan.getSelectedItem().toString();

                if (bank.equals("BANK TUJUAN")){
                    Toast.makeText(getApplicationContext(), "PILIH BANK TUJUAN", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        double dd = nominaltransaksi.getCurrencyDouble();
                        int id = (int) dd;
                        nominal = String.valueOf(id);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    load_tarif_to_server();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_kartu = nokartu.getText().toString();
                rektujuan = notujuan.getText().toString();
                bank = banktujuan.getSelectedItem().toString();
                cek = edittarif.getText().toString();
                transaksi = jenistransaksi.getSelectedItem().toString();
                statustransaksi = "selesai";

                try {
                    double d = nominaltransaksi.getCurrencyDouble();
                    int i = (int) d;
                    nominal = String.valueOf(i);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (cek.matches("")){
                    tariftransaksi = txttarif.getText().toString();
                }else{
                    tariftransaksi = edittarif.getText().toString();
                }

                load_proses_transaksi_to_server(no_kartu, rektujuan, nominal, bank, transaksi, tariftransaksi, statustransaksi, idadmin);

                Intent i = new Intent(getApplicationContext(), PrintActivity.class);
                i.putExtra("nokartu", no_kartu);
                i.putExtra("rektujuan", rektujuan);
                i.putExtra("nominal", nominal);
                i.putExtra("bank", bank);
                i.putExtra("tarif", tariftransaksi);
                startActivity(i);
            }
        });

    }

    public void load_proses_transaksi_to_server( final String a, final String b, final String c, final String d, final String e, final String f, final String g, final String h){
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
                                Log.d("Add transaksi", jObj.toString());
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
                        }
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                params.put("no_kartu", a);
                params.put("rektujuan", b);
                params.put("nominal", c);
                params.put("bank", d);
                params.put("jenis_transaksi", e);
                params.put("tariftransaksi", f);
                params.put("status_transaksi", g);
                params.put("id_admin", h);
                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void load_tarif_to_server(){
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

                                String tarif = jsonobject.getString("tarif_tansaksi").trim();
                                txttarif.setText(tarif.toString());

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

        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("banktujuan", bank);
                params.put("nominal", nominal);
                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
