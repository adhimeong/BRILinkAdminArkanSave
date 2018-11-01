package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavtarif;


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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataTarifAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataTarifControler;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.UserController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

/**
 * A simple {@link Fragment} subclass.
 */
public class TarifSesamaFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private ProgressDialog pd;
    //volley
    String urldata = "app/tarif_sesama.php";
    String url = Server.url_server +urldata;

    //list costume adapter
    List<DataTarifControler> dataController = new ArrayList<DataTarifControler>();
    DataTarifAdapter adapter;
    ListView listView;
    String idadmin;

    SwipeRefreshLayout mSwipeRefreshLayout;


    public TarifSesamaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tarif_sesama, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        //list transaksi
        listView = (ListView)view.findViewById(R.id.listview01);

        UserController user = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser();
        idadmin = user.getIdadmin();

        dataController.clear();

        adapter = new DataTarifAdapter(dataController, getActivity() );

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                final String idtarif = dataController.get(position).getIdtarif();
                final String btsbawh = dataController.get(position).getBatasbawah();
                final String btsatas = dataController.get(position).getBatasatas();
                final String tarif = dataController.get(position).getTarif();
                final String jenis = "sesama";
                final String aksi = "update";

                Intent i = new Intent(getActivity(), TarifEditActivity.class);
                i.putExtra("idtarif", idtarif);
                i.putExtra("bawah", btsbawh);
                i.putExtra("atas", btsatas);
                i.putExtra("tarif", tarif);
                i.putExtra("jenis", jenis);
                i.putExtra("aksi", aksi);
                startActivity(i);

            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fabtambahtarif);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String aksi2 = "tambah";

                Intent i2 = new Intent(getActivity(), TarifEditActivity.class);
                i2.putExtra("aksi", aksi2);
                startActivity(i2);
            }
        });

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

                // Fetching data from server
                load_data_from_server(idadmin);

            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    public void load_data_from_server(final String admin) {

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

                                String id_tarif = jsonobject.getString("id_tarif").trim();
                                String batasatas = jsonobject.getString("batas_atas").trim();
                                String batasbawah = jsonobject.getString("batas_bawah").trim();
                                String tarif = jsonobject.getString("tarif_tansaksi").trim();

                                DataTarifControler d1 = new DataTarifControler();
                                d1.setIdtarif(id_tarif.toString());
                                d1.setBatasatas(batasatas.toString());
                                d1.setBatasbawah(batasbawah.toString());
                                d1.setTarif(tarif.toString());

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
                params.put("id_admin", admin);
                return params;
            }

        };

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onRefresh() {
        dataController.clear();
        load_data_from_server(idadmin);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
