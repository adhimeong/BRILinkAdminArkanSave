package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmtransaksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.vision.barcode.Barcode;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.PrintActivity;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.ScanTextActivity;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

public class TransaksiScanTextActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    EditText nokartu, norek, nominal, penerima, bank;
    Button btntarif, btnproses, btnscan;
    TextView tarifview;
    String banknama, bankkode, txtnokartu, txtnorek, txtnominal, txtpenerima, txttarif,jenistransaksi, statustransaksi;

    private ProgressDialog pd;

    String urldata = "app/prosestransaksi.php";
    String url = Server.url_server +urldata;
    int success;
    String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_scan_text);

        //permisi kamera
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        nokartu = (EditText) findViewById(R.id.edittransaksinokartu);
        norek = (EditText) findViewById(R.id.edittransaksitujuan);
        nominal = (EditText) findViewById(R.id.edittransaksinominal);
        penerima = (EditText) findViewById(R.id.edittransaksipenerima);
        bank = (EditText) findViewById(R.id.edittransaksibank);
        btntarif = (Button) findViewById(R.id.btntransaksiloadtarif);
        btnproses = (Button) findViewById(R.id.btntransasiproses);
        btnscan =(Button) findViewById(R.id.btntransasiscan);
        tarifview = (TextView) findViewById(R.id.txtviewtransaksitarif);

        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScanTextActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btntarif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListBankTransaksiActivity.class);
                String nominaltxt = nominal.getText().toString();
                String banktxt = bank.getText().toString();
                intent.putExtra("nominal", nominaltxt);
                intent.putExtra("bank", banktxt);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btnproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtnokartu = nokartu.getText().toString();
                txtnorek = norek.getText().toString();
                txtnominal = nominal.getText().toString();
                txtpenerima = penerima.getText().toString();
                txttarif = tarifview.getText().toString();
                statustransaksi = "selesai";
                jenistransaksi = "Scan Antrian";

                load_proses_transaksi_to_server(txtnokartu, txtnorek, txtnominal, txtpenerima, banknama, jenistransaksi, txttarif, statustransaksi);

            }
        });
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

                        Intent i = new Intent(TransaksiScanTextActivity.this, PrintActivity.class);
                        i.putExtra("jenis_transksi", jenistransaksi);
                        i.putExtra("nokartu", txtnokartu);
                        i.putExtra("rektujuan", txtnorek);
                        i.putExtra("nominal", txtnominal);
                        i.putExtra("penerima", txtpenerima);
                        i.putExtra("bank", banknama);
                        i.putExtra("kode", bankkode);
                        i.putExtra("tarif", txttarif);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {

                final String camtext = data.getStringExtra("camtext");
                final String tariftxt = data.getStringExtra("tarif");

                if (camtext != null){

                    btnscan.setText("SCAN ULANG");

                    String[] separated = camtext.split("\n");
                    String kartutxt = separated[0];
                    String norektxt = separated[1];
                    String nominaltxt = separated[2];
                    String penerimatxt = separated[3];
                    String banktxt = separated[4];

                    if (kartutxt != null){
                        String kartucor = kartutxt.replace("&", "8")
                                .replace("O", "0")
                                .replace("o", "0")
                                .replace("C", "0")
                                .replace("c", "0")
                                .replace("I", "1")
                                .replace("i", "1")
                                .replace(" ", "")
                                .replace("S", "5");
                        Log.d("kartucor", kartucor);
                        nokartu.setText(kartucor);
                    }else{
                        nokartu.setText("");
                    }

                    if (norektxt != null){
                        String norekcor = norektxt.replace("&", "8")
                                .replace("O", "0")
                                .replace("o", "0")
                                .replace("C", "0")
                                .replace("c", "0")
                                .replace("I", "1")
                                .replace("i", "1")
                                .replace(" ", "")
                                .replace("S", "5");
                        norek.setText(norekcor);
                    }else{
                        norek.setText("");
                    }

                    if (nominaltxt !=null){
                        String nominalcor = nominaltxt.replace("&", "8")
                                .replace("O", "0")
                                .replace("I", "1")
                                .replace(" ", "")
                                .replace("c", "0")
                                .replace("C", "0")
                                .replace("S", "5")
                                .replace("s", "5")
                                .replace("o", "0")
                                .replace("G", "6")
                                .replace(".", "");
                        nominal.setText(nominalcor);
                    }else{
                        nominal.setText("");
                    }

                    if (penerimatxt != null){
                        penerima.setText(penerimatxt);
                    }else{
                        penerima.setText("");
                    }

                    if (banktxt != null){
                        bank.setText(banktxt);
                    }else{
                        bank.setText("");
                    }


                }else if(tariftxt != null){
                    banknama = data.getStringExtra("namabank");
                    bankkode = data.getStringExtra("kodebank");
                    bank.setText(banknama);
                    tarifview.setText(tariftxt);

                }
            }
        }
    }
}
