package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmtransaksi;

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
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.vision.barcode.Barcode;
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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.ScanCardActivity;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

public class TransaksiActivity extends AppCompatActivity {

    private ProgressDialog pd;

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

    String urldata = "app/prosestransaksi.php";
    String url = Server.url_server +urldata;

    String urldata1 = "app/cektarif.php";
    String url1 = Server.url_server +urldata1;

    EditText editnokartu, editnotujuan, editpenerima, editbank;
    TextView txttarif, txtadm;
    CurrencyEditText editnominal;
    Button btnscan, btntarif, btnproses;
    String datajenistransaksi, datanominal, databank, datanokartu, datanotujuan, datapenerima, datatarif, datastatustransaksi;
    int success;
    String message, banknama, bankkode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        //ambil data dari list jenis transaksi
        datajenistransaksi = getIntent().getStringExtra("jenistransaksi");
        datastatustransaksi = "antri";

        //permisi kamera
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        editnokartu = (EditText) findViewById(R.id.edittransaksinokartu);
        editnotujuan = (EditText) findViewById(R.id.edittransaksitujuan);
        editnominal = (CurrencyEditText)findViewById(R.id.edittransaksinominal);
        editpenerima = (EditText)findViewById(R.id.edittransaksipenerima);
        editbank = (EditText)findViewById(R.id.edittransaksibank);
        txttarif = (TextView) findViewById(R.id.txttransaksitarif);
        txtadm = (TextView)findViewById(R.id.txttransaksitxtadm);
        btntarif = (Button) findViewById(R.id.btntransaksiloadtarif);
        btnscan = (Button) findViewById(R.id.btntransaksiscan);
        btnproses = (Button)findViewById(R.id.btntransasiproses);

        switch (datajenistransaksi){
            case "Transfer BRI":
                editbank.setText("BANK BRI");
                break;
            case  "Transfer Bank Lain":

                break;
            case  "Tarik Tunai":
                editnotujuan.setHint("Nomor Rekening");
                editpenerima.setHint("Pemilik Nomor Rekening");
                break;
            case "Pulsa & Paket Data":
                editnotujuan.setHint("Nomor Ponsel");
                editpenerima.setVisibility(View.GONE);
                editbank.setVisibility(View.GONE);
                btntarif.setVisibility(View.GONE);
                txtadm.setVisibility(View.GONE);
                txttarif.setVisibility(View.GONE);
                editbank.setText("");
                editpenerima.setText("");
                txttarif.setText("0");
                break;
            case "BPJS Kesehatan":
                editnotujuan.setHint("Nomor BPJS");
                editbank.setVisibility(View.GONE);
                editpenerima.setVisibility(View.GONE);
                editnominal.setVisibility(View.GONE);
                btntarif.setVisibility(View.GONE);
                txtadm.setVisibility(View.GONE);
                txttarif.setVisibility(View.GONE);
                editbank.setText("");
                editpenerima.setText("");
                editnominal.setText("0");
                txttarif.setText("");
                break;

            case "PLN":
                editnotujuan.setHint("Nomor Pelanggan PLN");
                editbank.setVisibility(View.GONE);
                editpenerima.setVisibility(View.GONE);
                editnominal.setVisibility(View.GONE);
                btntarif.setVisibility(View.GONE);
                txtadm.setVisibility(View.GONE);
                txttarif.setVisibility(View.GONE);
                editbank.setText("");
                editpenerima.setText("");
                editnominal.setText("0");
                txttarif.setText("");
                break;
            case "Cicilan":
                editnotujuan.setHint("Nomor Pelanggan");
                editpenerima.setHint("Nama Leasing");
                editbank.setVisibility(View.GONE);
                editnominal.setVisibility(View.GONE);
                btntarif.setVisibility(View.GONE);
                txtadm.setVisibility(View.GONE);
                txttarif.setVisibility(View.GONE);
                editbank.setText("");
                editnominal.setText("0");
                txttarif.setText("");
                break;
            case "Transaksi Lainnya":

                break;
        }


        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ScanCardActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        btntarif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databank = editbank.getText().toString();

                try {
                    double dd = editnominal.getCurrencyDouble();
                    int id = (int)dd;
                    datanominal = String.valueOf(id);

                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (datajenistransaksi.equals("Transfer Bank Lain") || datajenistransaksi.equals("Tarik Tunai") ){
                    Intent intent = new Intent(getApplicationContext(), ListBankTransaksiActivity.class);
                    intent.putExtra("nominal", datanominal);
                    intent.putExtra("bank", databank);
                    startActivityForResult(intent, REQUEST_CODE);
                }else{
                    bankkode = "002";
                    load_tarif_to_server(bankkode, datanominal);
                }
            }
        });

        btnproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datanokartu = editnokartu.getText().toString();
                datanotujuan = editnotujuan.getText().toString();
                    try {
                        double dd = editnominal.getCurrencyDouble();
                        int id = (int)dd;
                        datanominal = String.valueOf(id);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                datapenerima = editpenerima.getText().toString();
                databank = editbank.getText().toString();
                datatarif = txttarif.getText().toString();

                load_proses_transaksi_to_server(datanokartu,datanotujuan, datanominal,datapenerima,databank, datajenistransaksi,datatarif,datastatustransaksi);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");
                final String tariftxt = data.getStringExtra("tarif");

                if (barcode != null){
                    editnokartu.post(new Runnable() {

                        @Override
                        public void run() {
                            editnokartu.setText(barcode.displayValue);
                        }
                    });

                }else if(tariftxt != null){

                    banknama = data.getStringExtra("namabank");
                    bankkode = data.getStringExtra("kodebank");
                    editbank.setText(banknama);
                    txttarif.setText(tariftxt);

                }
            }
        }
    }

    public void load_tarif_to_server(final String kode, final String nominal ){
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
                params.put("kodebank", kode);
                params.put("nominal", nominal);
                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void load_proses_transaksi_to_server(final String a, final String b, final String c, final String d, final String e, final String f, final String g, final String h){

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
                                Log.d("BACA", "KEBACA");
                                FancyToast.makeText(getApplicationContext(),message,FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                                finish();
                                pd.dismiss();
                            } else {
                                FancyToast.makeText(getApplicationContext(),message,FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }

                        pd.hide();

                        Intent i = new Intent(TransaksiActivity.this, PrintActivity.class);
                        i.putExtra("jenis_transksi", datajenistransaksi);
                        i.putExtra("nokartu", datanokartu);
                        i.putExtra("rektujuan", datanotujuan);
                        i.putExtra("nominal", datanominal);
                        i.putExtra("penerima", datapenerima);
                        i.putExtra("bank", banknama);
                        i.putExtra("kode", bankkode);
                        i.putExtra("tarif", datatarif);
                        startActivity(i);

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
                params.put("penerima", d);
                params.put("bank", e);
                params.put("jenis_transaksi", f);
                params.put("tariftransaksi", g);
                params.put("status_transaksi", h);
                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
