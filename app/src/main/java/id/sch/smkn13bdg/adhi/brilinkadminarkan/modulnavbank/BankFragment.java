package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavbank;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.SharedPrefManager;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataBankAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataBankController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.UserController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavtarif.TarifEditActivity;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

/**
 * A simple {@link Fragment} subclass.
 */
public class BankFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private ProgressDialog pd;
    //volley
    String urldata = "app/bank_list.php";
    String url = Server.url_server +urldata;

    //list costume adapter
    List<DataBankController> dataController = new ArrayList<DataBankController>();
    DataBankAdapter adapter;
    ListView listView;
    String idadmin;

    FloatingActionButton fabtambah;
    EditText caribank;
    Button caribtn;
    String aksi;

    SwipeRefreshLayout mSwipeRefreshLayout;


    public BankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        UserController user = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser();
        idadmin = user.getIdadmin();

        //list transaksi
        listView = (ListView)view.findViewById(R.id.listview01);
        caribtn = (Button) view.findViewById(R.id.bankbtncari);
        caribank = (EditText) view.findViewById(R.id.bankcari);
        fabtambah = (FloatingActionButton) view.findViewById(R.id.fabtambahbank);
        fabtambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aksi = "tambah";
                Intent i = new Intent(getActivity(), BankEditActivity.class);
                i.putExtra("aksi", aksi);
                startActivity(i);

            }
        });

        caribtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aksi = "cari";
                String cari = String.valueOf(caribank.getText());
                dataController.clear();
                load_data_from_server(cari, aksi);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                aksi = "edit";
                String idbank = dataController.get(position).getIdbank();
                String namabank = dataController.get(position).getNamabank();
                String kodebank = dataController.get(position).getKodebank();
                Intent i = new Intent(getActivity(), BankEditActivity.class);
                i.putExtra("idbank", idbank);
                i.putExtra("namabank", namabank);
                i.putExtra("kodebank", kodebank);
                i.putExtra("aksi", aksi);
                startActivity(i);
            }
        });

        dataController.clear();

        adapter = new DataBankAdapter(dataController, getActivity() );
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                aksi = "semua";
                // Fetching data from server
                dataController.clear();
                load_data_from_server(idadmin, aksi);

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void load_data_from_server(final String data, final String prosesaksi) {

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
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            FancyToast.makeText(getActivity().getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
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

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onRefresh() {
        dataController.clear();
        aksi = "semua";
        load_data_from_server(idadmin, aksi);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
