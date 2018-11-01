package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpengguna;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataPenggunaAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataPenggunaController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

public class PenggunaListActivity extends Activity {

    String urldata = "app/pengguna_cari.php";
    String url = Server.url_server +urldata;
    private ProgressDialog pd;

    //list costume adapter
    List<DataPenggunaController> dataController = new ArrayList<DataPenggunaController>();
    DataPenggunaAdapter adapter;

    ListView listView;
    String cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna_list);

        //ambil data dari fragment
        cari = getIntent().getStringExtra("nokartu");

        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        listView = (ListView)findViewById(R.id.listview02);
        adapter = new DataPenggunaAdapter(dataController, this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                final String datanokartu = dataController.get(position).getNokartu();
                Intent i = new Intent(getApplicationContext(), PenggunaDetailActivity.class);
                i.putExtra("nokartu", datanokartu);
                startActivity(i);
            }
        });

        load_data_from_server(cari);
    }

    public void load_data_from_server(final String isi) {
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


                                String nokartu = jsonobject.getString("nokartu").trim();
                                String nama = jsonobject.getString("nama").trim();
                                String foto = jsonobject.getString("foto").trim();
                                String alamat = jsonobject.getString("alamat").trim();
                                String kontak = jsonobject.getString("kontak").trim();
                                String email = jsonobject.getString("email").trim();

                                DataPenggunaController d1 = new DataPenggunaController();
                                d1.setNokartu(nokartu.toString());
                                d1.setNama(nama.toString());
                                d1.setFoto(foto.toString());
                                d1.setAlamat(alamat.toString());
                                d1.setKontak(kontak.toString());
                                d1.setEmail(email.toString());

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
                            finish();
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
                params.put("cari", isi);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
