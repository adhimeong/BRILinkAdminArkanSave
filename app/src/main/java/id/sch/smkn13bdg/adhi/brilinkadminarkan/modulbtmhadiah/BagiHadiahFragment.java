package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmhadiah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataHadiahAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataHadiahController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.UserController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class BagiHadiahFragment extends Fragment {

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

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

    TextView result;
    int success;
    String message;
    String idadmin;

    public BagiHadiahFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hadiah_bagi, container, false);

        //permisi kamera
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        UserController user = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser();
        idadmin = user.getIdadmin();

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        result = (TextView) view.findViewById(R.id.result);

        FloatingActionButton fab = view.findViewById(R.id.fabhadiah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ScanCardActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //list hadiah
        listView = (ListView)view.findViewById(R.id.listview01);
        dataController.clear();

        adapter = new DataHadiahAdapter(dataController, getActivity() );

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String dataidhadiah = dataController.get(position).getId_hadiah();

                Intent i = new Intent(getActivity(), DetailHadiahActivity.class);
                i.putExtra("idhadiah", dataidhadiah);
                startActivity(i);

            }

        });

        load_data_from_server();

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
                        String hasil = result.getText().toString();
                        String[] separated = hasil.split(",");
                        final String nokartu = separated[0];
                        final String idhadiah = separated[1];

                        //pengumaman
                        final PrettyDialog pDialog = new PrettyDialog(getActivity());
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

                                                load_ambil_hadiah(nokartu, idhadiah, idadmin);

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

    public  void load_ambil_hadiah(final String no, final String id, final String admin ){

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
                params.put("id_hadiah", id);
                params.put("idadmin", admin);
                return params;
            }

        };

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
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
