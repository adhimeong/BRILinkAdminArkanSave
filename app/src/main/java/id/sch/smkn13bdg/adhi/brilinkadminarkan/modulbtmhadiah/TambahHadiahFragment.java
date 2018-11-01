package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmhadiah;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.List;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataHadiahAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataHadiahController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;


/**
 * A simple {@link Fragment} subclass.
 */
public class TambahHadiahFragment extends Fragment {

    private ProgressDialog pd;

    //volley
    String urldata = "app/service_hadiah.php";
    String url = Server.url_server +urldata;

    //list costume adapter
    List<DataHadiahController> dataController = new ArrayList<DataHadiahController>();
    DataHadiahAdapter adapter;
    ListView listView;
    Button btntambah;



    public TambahHadiahFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hadiah_tambah, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        //list hadiah
        listView = (ListView)view.findViewById(R.id.listview01);
        btntambah = (Button) view.findViewById(R.id.btntmbhhadiah);
        btntambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EditHadiahActivity.class);
                startActivity(i);
            }
        });

        dataController.clear();

        adapter = new DataHadiahAdapter(dataController, getActivity() );

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String dataidhadiah = dataController.get(position).getId_hadiah();

                Intent i = new Intent(getActivity(), EditHadiahActivity.class);
                i.putExtra("idhadiah", dataidhadiah);
                startActivity(i);

                /*DetailHadiahFragment secondFragtry = new DetailHadiahFragment();
                Bundle mBundle = new Bundle();
                mBundle.putString(KEY_FRG, datajumlahpoint);
                secondFragtry.setArguments(mBundle);

                FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, secondFragtry, DetailHadiahFragment.class.getSimpleName());
                mFragmentTransaction.addToBackStack(null).commit();*/
            }

        });

        load_data_from_server();
        // Inflate the layout for this fragment
        return view;
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

                            FancyToast.makeText(getActivity().getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            pd.hide();
                        }
                    }
                }

        );

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
