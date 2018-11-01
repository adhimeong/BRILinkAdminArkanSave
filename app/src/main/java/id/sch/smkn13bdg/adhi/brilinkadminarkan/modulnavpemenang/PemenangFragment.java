package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpemenang;

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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataPemenangAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataPemenangController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataPenggunaController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.UserController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PemenangFragment extends Fragment {

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

    String urldata = "app/pemenang_list.php";
    String url = Server.url_server +urldata;
    private ProgressDialog pd;

    String urldata2 = "app/ambil_hadiah.php";
    String url2 = Server.url_server +urldata2;

    TextView result;
    int success;
    String message;


    List<DataPemenangController> dataController = new ArrayList<DataPemenangController>();
    DataPemenangAdapter adapter;

    ListView listView;
    String idadmin;

    public PemenangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pemenang, container, false);

        //permisi kamera
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        UserController user = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser();
        idadmin = user.getIdadmin();

        listView = (ListView) view.findViewById(R.id.listview01);
        adapter = new DataPemenangAdapter(dataController, getActivity());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        FloatingActionButton fab = view.findViewById(R.id.fabklaimhadiah);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ScanCardActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        load_data_pemenang();

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

    public  void load_ambil_hadiah(final String no, final String id, final String admin){

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

    public void load_data_pemenang(){
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

                                String nama= jsonobject.getString("nama").trim();
                                String admin = jsonobject.getString("admin").trim();
                                String tanggal = jsonobject.getString("tanggal").trim();
                                String hadiah = jsonobject.getString("hadiah").trim();
                                String no_kartu = jsonobject.getString("nokartu").trim();
                                String foto = jsonobject.getString("foto").trim();
                                String status = jsonobject.getString("status").trim();

                                DataPemenangController d1 = new DataPemenangController();
                                d1.setNama(nama.toString());
                                d1.setAdmin(admin.toString());
                                d1.setTanggal(tanggal.toString());
                                d1.setHadiah(hadiah.toString());
                                d1.setNo_kartu(no_kartu.toString());
                                d1.setFoto(foto.toString());
                                d1.setStatus(status.toString());

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
