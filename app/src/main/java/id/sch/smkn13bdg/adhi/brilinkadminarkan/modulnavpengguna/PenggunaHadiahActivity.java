package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpengguna;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.SharedPrefManager;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataHadiahAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataHadiahController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.UserController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class PenggunaHadiahActivity extends AppCompatActivity {

    private ProgressDialog pd;
    //volley
    String urldata = "app/service_hadiah.php";
    String url = Server.url_server +urldata;

    String urldata2 = "app/ambil_hadiah.php";
    String url2 = Server.url_server +urldata2;

    //list costume adapter
    List<DataHadiahController> dataController = new ArrayList<DataHadiahController>();
    DataHadiahAdapter adapter;
    ListView listView;

    int success;
    String message;
    String idadmin;
    String nokartu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna_hadiah);

        // id admin diambil dari sistem loogin
        UserController user = SharedPrefManager.getInstance(this.getApplicationContext()).getUser();
        idadmin = user.getIdadmin();

        //id pengguna ambil data dari activity sebelumnya
        nokartu= getIntent().getStringExtra("nokartu");

        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        //list hadiah
        listView = (ListView)findViewById(R.id.listview02);
        dataController.clear();

        adapter = new DataHadiahAdapter(dataController, this );

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                final String idhadiah = dataController.get(position).getId_hadiah();

                //pengumaman
                final PrettyDialog pDialog = new PrettyDialog(PenggunaHadiahActivity.this);
                pDialog
                        .setTitle("BERI HADIAH")
                        .setTitleColor(R.color.colorAccent)
                        .setMessage("Lepaskan hadiah untuk " + nokartu + " ?")
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
                                "LEPASKAN",     // button text
                                R.color.pdlg_color_white,  // button text color
                                R.color.hijau,  // button background color
                                new PrettyDialogCallback() {  // button OnClick listener
                                    @Override
                                    public void onClick() {

                                        load_ambil_hadiah(nokartu, idhadiah);

                                        pDialog.dismiss();
                                    }
                                }
                        )

                        .addButton(
                                "BATAL",     // button text
                                R.color.pdlg_color_white,  // button text color
                                R.color.merah,  // button background color
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

        load_data_from_server();
    }

    public void load_data_from_server() {
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

                                String id_hadiah = jsonobject.getString("id_hadiah").trim();
                                String nama_hadiah = jsonobject.getString("nama_hadiah").trim();
                                String foto_hadiah = jsonobject.getString("foto_hadiah").trim();
                                String jumlah_point = jsonobject.getString("jumlah_point").trim();
                                String jumlah_items = jsonobject.getString("jumlah_items").trim();

                                DataHadiahController d1 = new DataHadiahController();
                                d1.setId_hadiah(id_hadiah.toString());
                                d1.setNama_hadiah(nama_hadiah.toString());
                                d1.setFoto_hadiah(foto_hadiah.toString());
                                d1.setJumlah_point(jumlah_point.toString());
                                d1.setJumlah_items(jumlah_items.toString());

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

        );

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public  void load_ambil_hadiah(final String no, final String id){

        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url2,
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
                params.put("id_hadiah", id);
                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
