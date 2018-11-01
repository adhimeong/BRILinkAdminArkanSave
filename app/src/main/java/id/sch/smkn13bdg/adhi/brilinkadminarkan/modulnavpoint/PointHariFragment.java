package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpoint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.ScanCardActivity;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.SharedPrefManager;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataPointAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataPointController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.UserController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PointHariFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

    //volley
    private ProgressDialog pd;
    String urldata = "app/point_harian.php";
    String url = Server.url_server +urldata;

    String urldata2 = "app/point_tambah.php";
    String url2 = Server.url_server +urldata2;

    //list costume adapter
    List<DataPointController> dataController = new ArrayList<DataPointController>();
    DataPointAdapter adapter;
    ListView listView;

    SwipeRefreshLayout mSwipeRefreshLayout;

    TextView result;
    int success;
    String message;
    String idadmin;
    String nokartu;

    public PointHariFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_point_hari, container, false);

        //permisi kamera
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        result = (TextView) view.findViewById(R.id.result);

        UserController user = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser();
        idadmin = user.getIdadmin();

        //list point
        listView = (ListView)view.findViewById(R.id.listview01);

        FloatingActionButton fab = view.findViewById(R.id.fabpointhariini);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ScanCardActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        dataController.clear();
        adapter = new DataPointAdapter(dataController, getActivity() );

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {

                    @Override
                    public void run() {
                        result.setText(barcode.displayValue);
                        nokartu = result.getText().toString();

                        //pengumaman
                        final PrettyDialog pDialog = new PrettyDialog(getActivity());
                        pDialog
                                .setTitle("TAMBAH POINT")
                                .setTitleColor(R.color.colorAccent)
                                .setMessage("Tambah Point untuk " + nokartu + " ?")
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
                                        "TAMBAH POINT",     // button text
                                        R.color.pdlg_color_white,  // button text color
                                        R.color.hijau,  // button background color
                                        new PrettyDialogCallback() {  // button OnClick listener
                                            @Override
                                            public void onClick() {

                                                load_tambah_point(nokartu, idadmin);

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
            }
        }
    }

    public  void load_tambah_point(final String no, final String id){

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
                params.put("no_kartu", no);
                params.put("id_admin", id);
                return params;
            }

        };

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
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

                                String id_perolehan = jsonobject.getString("id_perolehan").trim();
                                String no_kartu = jsonobject.getString("no_kartu").trim();
                                String waktu = jsonobject.getString("waktu").trim();
                                String tanggal = jsonobject.getString("tanggal").trim();
                                String admin = jsonobject.getString("admin").trim();

                                DataPointController d1 = new DataPointController();
                                d1.setId_perolehan(id_perolehan.toString());
                                d1.setNo_kartu(no_kartu.toString());
                                d1.setWaktu(waktu.toString());
                                d1.setTanggal_point(tanggal.toString());
                                d1.setAdmin(admin.toString());


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
