package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmtransaksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataBankAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataBankController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

public class ListBankTransaksiActivity extends AppCompatActivity {

    String namabank, nominal;
    private ProgressDialog pd;
    //volley
    String urldata = "app/bank_list.php";
    String url = Server.url_server +urldata;

    String urldata1 = "app/cektarif.php";
    String url1 = Server.url_server +urldata1;

    //list costume adapter
    List<DataBankController> dataController = new ArrayList<DataBankController>();
    DataBankAdapter adapter;
    ListView listView;
    String tarif, banknama, bankkode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bank_transaksi);

        namabank = getIntent().getStringExtra("bank");
        nominal = getIntent().getStringExtra("nominal");

        listView = (ListView)findViewById(R.id.listview01);
        pd = new ProgressDialog(ListBankTransaksiActivity.this);
        pd.setMessage("loading");

        dataController.clear();

        adapter = new DataBankAdapter(dataController, this );
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        load_data_bank_from_server(namabank, "cari");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                banknama = dataController.get(position).getNamabank();
                bankkode = dataController.get(position).getKodebank();
                load_tarif_to_server(banknama, bankkode, nominal);
            }
        });
    }

    public void load_data_bank_from_server(final String data, final String prosesaksi) {
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String id = jsonobject.getString("id_bank").trim();
                                String nama = jsonobject.getString("nama_bank").trim();
                                String kode = jsonobject.getString("kode_bank").trim();

                                DataBankController d1 = new DataBankController();
                                d1.setIdbank(id.toString());
                                d1.setNamabank(nama.toString());
                                d1.setKodebank(kode.toString());

                                dataController.add(d1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();
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
                params.put("data", data);
                params.put("aksi", prosesaksi);
                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void load_tarif_to_server(final String bank, final String kode,final String nominal){
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

                                tarif = jsonobject.getString("tarif_tansaksi").trim();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent();
                        intent.putExtra("tarif", tarif);
                        intent.putExtra("namabank", bank);
                        intent.putExtra("kodebank", kode);
                        setResult(RESULT_OK, intent);
                        pd.dismiss();
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
}
