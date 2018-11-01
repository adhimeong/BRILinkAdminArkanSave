package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmtransaksi;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataTransaksiAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataTransaksiController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.UserController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class BatalTransaksiFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ProgressDialog pd;
    int success;
    String message;

    //volley
    String urldata = "app/transaksi_batal.php";
    String url = Server.url_server +urldata;

    String urldata1 = "app/prosesupdatetransaksi.php";
    String url1 = Server.url_server +urldata1;

    //list costume adapter
    List<DataTransaksiController> dataController = new ArrayList<DataTransaksiController>();
    DataTransaksiAdapter adapter;
    ListView listView;
    String idadmin;

    SwipeRefreshLayout mSwipeRefreshLayout;


    public BatalTransaksiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaksi_batal, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        UserController user = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser();
        idadmin = user.getIdadmin();

        //list transaksi
        listView = (ListView)view.findViewById(R.id.listview01);
        dataController.clear();

        adapter = new DataTransaksiAdapter(dataController, getActivity() );

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                final String dataidtransaksi = dataController.get(position).getId_tansaksi();
                final String datanokartu = dataController.get(position).getNo_kartu();
                Toast.makeText(getActivity(), "id transaksi : " + dataidtransaksi, Toast.LENGTH_SHORT).show();

                //pengumaman
                final PrettyDialog pDialog = new PrettyDialog(getActivity());
                pDialog
                        .setTitle("KONFIRMASI")
                        .setTitleColor(R.color.colorAccent)
                        .setMessage("Rubah status transaksi ? ")
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
                                "BERHASIL",     // button text
                                R.color.pdlg_color_white,  // button text color
                                R.color.hijau,  // button background color
                                new PrettyDialogCallback() {  // button OnClick listener
                                    @Override
                                    public void onClick() {
                                        String datastatustransaksi = "selesai";
                                        load_proses_update_transaksi_to_server(dataidtransaksi, datanokartu, datastatustransaksi, idadmin);

                                        pDialog.dismiss();
                                    }
                                }
                        )
                        .addButton(
                                "PENDING",     // button text
                                R.color.pdlg_color_white,  // button text color
                                R.color.kuning,  // button background color
                                new PrettyDialogCallback() {  // button OnClick listener
                                    @Override
                                    public void onClick() {

                                        String datastatustransaksi = "gagal";
                                        load_proses_update_transaksi_to_server(dataidtransaksi, datanokartu, datastatustransaksi, idadmin);

                                        pDialog.dismiss();
                                    }
                                }
                        )
                        .addButton(
                                "TUTUP",
                                R.color.pdlg_color_white,
                                R.color.colorPrimaryDark,
                                new PrettyDialogCallback() {
                                    @Override
                                    public void onClick() {
                                        pDialog.dismiss();
                                    }
                                }
                        )
                        .show();
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
                load_data_from_server();

            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    public void load_data_from_server() {

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

                                String id_transaksi = jsonobject.getString("id_transaksi").trim();
                                String no_kartu = jsonobject.getString("no_kartu").trim();
                                String rek_tujuan = jsonobject.getString("rek_tujuan").trim();
                                String nominal = jsonobject.getString("nominal").trim();
                                String bank = jsonobject.getString("bank_tujuan").trim();
                                String jenis = jsonobject.getString("jenis_transaksi").trim();
                                String tanggal = jsonobject.getString("tanggal").trim();
                                String penerima = jsonobject.getString("penerima").trim();
                                String admin = jsonobject.getString("admin");

                                DataTransaksiController d1 = new DataTransaksiController();
                                d1.setId_tansaksi(id_transaksi.toString());
                                d1.setNo_kartu(no_kartu.toString());
                                d1.setRek_tujuan(rek_tujuan.toString());
                                d1.setNominal(nominal.toString());
                                d1.setBank(bank.toString());
                                d1.setJenis(jenis.toString());
                                d1.setNamaadmin(admin.toString());
                                d1.setPenerima(penerima.toString());
                                d1.setTanggaltransaksi(tanggal.toString());

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

        );

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void load_proses_update_transaksi_to_server(final String idtransaksi, final String nokartu, final String status, final String admin){
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url1,
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
                                dataController.clear();
                                load_data_from_server();
                                FancyToast.makeText(getActivity().getApplicationContext(),message,FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                            } else {
                                FancyToast.makeText(getActivity().getApplicationContext(),message,FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
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

                            FancyToast.makeText(getActivity().getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
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
                params.put("id_transaksi", idtransaksi);
                params.put("no_kartu", nokartu);
                params.put("status_transaksi", status);
                params.put("id_admin", admin);
                return params;
            }

        };

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onRefresh() {
        dataController.clear();
        load_data_from_server();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
