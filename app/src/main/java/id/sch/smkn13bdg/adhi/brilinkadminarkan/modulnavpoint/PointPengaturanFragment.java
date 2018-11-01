package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavpoint;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.SharedPrefManager;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataTransaksiController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.UserController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class PointPengaturanFragment extends Fragment {

    private ProgressDialog pd;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    //volley
    String urldata = "app/point_periode.php";
    String url = Server.url_server +urldata;

    String urldata1 = "app/point_updateperiode.php";
    String url1 = Server.url_server +urldata1;

    String idadmin;

    TextView tanggalpasif;
    TextView tanggalupdate;
    Button btnprosesupdate;
    Button btndatepicker;
    String tanggalbaru;
    String message;
    int success;


    public PointPengaturanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_point_pengaturan, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        UserController user = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getUser();
        idadmin = user.getIdadmin();

        tanggalpasif = (TextView) view.findViewById(R.id.pointtanggalpasif);
        tanggalupdate = (TextView) view.findViewById(R.id.pointtanggalupadte);
        btndatepicker = (Button) view.findViewById(R.id.pointbtndatepicker);
        btnprosesupdate = (Button) view.findViewById(R.id.pointbtnupdate);

        btndatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        btnprosesupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tanggalbaru = tanggalupdate.getText().toString();

                if (tanggalbaru.equals("")){

                    FancyToast.makeText(getActivity().getApplicationContext(),"Pilih Tanggal yang baru terlebih dahulu",FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();

                }else{

                    //pengumaman
                    final PrettyDialog pDialog = new PrettyDialog(getActivity());
                    pDialog
                            .setTitle("UPDATE PERIODE")
                            .setTitleColor(R.color.colorAccent)
                            .setMessage("Ubah Peridode menjadi "+tanggalbaru+" ?")
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
                                    "UPDATE",     // button text
                                    R.color.pdlg_color_white,  // button text color
                                    R.color.hijau,  // button background color
                                    new PrettyDialogCallback() {  // button OnClick listener
                                        @Override
                                        public void onClick() {

                                            load_update_periode(tanggalbaru, idadmin);
                                            load_data_from_server(idadmin);
                                            tanggalupdate.setText("");
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
            }
        });

        load_data_from_server(idadmin);

        // Inflate the layout for this fragment
        return view;
    }

    private void showDateDialog(){

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tanggalupdate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void load_update_periode(final String tanggal, final String admin){
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
                                Log.d("Add/update", jObj.toString());
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
                params.put("tanggal", tanggal);
                params.put("id_admin", admin);

                return params;
            }

        };

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);


    }

    public void load_data_from_server(final String admin) {

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

                                String tanggal = jsonobject.getString("tanggal_pasif").trim();
                                tanggalpasif.setText(String.valueOf(tanggal));
                            }
                        } catch (JSONException e) {
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

}
