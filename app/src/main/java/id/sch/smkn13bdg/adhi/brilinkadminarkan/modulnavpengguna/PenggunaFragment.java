package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpengguna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.ScanCardActivity;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataPelangganAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataPelangganController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmhadiah.DetailHadiahActivity;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PenggunaFragment extends Fragment {

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

    String urldata = "app/perolehansemuapoint.php";
    String url = Server.url_server +urldata;

    String urldata2 = "app/pelangganaktif.php";
    String url2 = Server.url_server +urldata2;
    private ProgressDialog pd;

    //list costume adapter
    List<DataPelangganController> dataController = new ArrayList<DataPelangganController>();
    DataPelangganAdapter adapter;

    ListView listView;
    EditText result;
    TextView penggunajumlah;
    Button scanbtn;
    Button caribtn;
    String nokartu;


    public PenggunaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengguna, container, false);

        //permisi kamera
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        listView = (ListView) view.findViewById(R.id.listview02);
        adapter = new DataPelangganAdapter(dataController, getActivity());
        scanbtn = (Button) view.findViewById(R.id.penggunabtnscan);
        caribtn = (Button) view.findViewById(R.id.penggunabtncari);
        result = (EditText) view.findViewById(R.id.penggunaedit);
        penggunajumlah = (TextView) view.findViewById(R.id.penggunajmlh);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ScanCardActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        caribtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //dapat  berupa nomor kartu atau nama
                nokartu = result.getText().toString();

                if (nokartu.equals("")){
                    FancyToast.makeText(getActivity().getApplicationContext(),"nomor kartu kosong",FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
                }else{
                    Intent i = new Intent(getActivity(), PenggunaListActivity.class);
                    i.putExtra("nokartu", nokartu);
                    startActivity(i);
                }
            }
        });

        load_data_from_server();
        load_data_user_aktif();

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
                    }
                });
            }
        }
    }

    public void load_data_user_aktif(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url2,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String jumlahpenggunaaktif = jsonobject.getString("jumlah_pengguna").trim();
                                penggunajumlah.setText(jumlahpenggunaaktif + " Pelanggan");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

                                String tglpasif = jsonobject.getString("tanggal_pasif").trim();
                                String nama_pelanggan = jsonobject.getString("nama_pelanggan").trim();
                                String foto_pelanggan = jsonobject.getString("foto_pelanggan").trim();
                                String jumlah_point_pelanggan = jsonobject.getString("jumlah_point").trim();

                                DataPelangganController d1 = new DataPelangganController();
                                d1.setTanggal_pasif(tglpasif.toString());
                                d1.setNama_pelanggan(nama_pelanggan.toString());
                                d1.setFoto_pelanggan(foto_pelanggan.toString());
                                d1.setJumlah_point(jumlah_point_pelanggan.toString());

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
