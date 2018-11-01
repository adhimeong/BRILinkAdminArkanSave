package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmlaporan;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

/**
 * A simple {@link Fragment} subclass.
 */
public class LaporanMingguanFragment extends Fragment {

    private ProgressDialog pd;

    Spinner mingguke;
    String minggu;

    //volley
    String urldata = "app/laporan_pendapatanmingguan.php";
    String url = Server.url_server +urldata;

    //untuk grafik
    ArrayList yAxis;
    ArrayList yValues;
    ArrayList xAxis1;
    BarEntry values ;
    BarChart chart;
    BarData data;

    //untuk jumlah data
    TextView jumlah;
    public ArrayList<Integer> listnilai = new ArrayList<Integer>();
    public int sumdata = 0;


    public LaporanMingguanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_laporan_mingguan, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        jumlah = (TextView) view.findViewById(R.id.laporanjumlah);
        chart = (BarChart) view.findViewById(R.id.barchartlaporanlaba);
        mingguke = (Spinner) view.findViewById(R.id.spinnerlaporanmingguan);
        mingguke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minggu = mingguke.getSelectedItem().toString();
                sumdata = 0;
                load_data_from_server(minggu);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void load_data_from_server(final String a) {

        pd.show();

        xAxis1 = new ArrayList<>();
        yAxis = null;
        yValues = new ArrayList<>();

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

                                String label = jsonobject.getString("label").trim();
                                String nilai = jsonobject.getString("nilai").trim();

                                //untuk grafik
                                xAxis1.add(label);
                                values = new BarEntry(Float.valueOf(nilai),i);
                                yValues.add(values);

                                //menggambil nilai untuk jumlah pedapatan
                                listnilai.add(Integer.valueOf(nilai));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghitung jumlah pendapatan
                        for (int x =0; x <listnilai.size(); x++){
                            sumdata = sumdata + listnilai.get(x);
                        }

                        listnilai.clear();
                        jumlah.setText(String.valueOf(sumdata));


                        //untuk grafik
                        BarDataSet barDataSet1 = new BarDataSet(yValues, "LAPORAN PENDAPATAN ");
                        barDataSet1.setColor(Color.rgb(0, 82, 159));

                        yAxis = new ArrayList<>();
                        yAxis.add(barDataSet1);
                        final String labels [] = (String[]) xAxis1.toArray(new String[xAxis1.size()]);
                        data = new BarData(labels,yAxis);
                        chart.setData(data);
                        chart.setDescription("");
                        chart.animateXY(1200, 1200);
                        chart.invalidate();
                        chart.getAxisLeft().setDrawGridLines(false);
                        chart.getAxisRight().setDrawGridLines(false);
                        chart.getXAxis().setDrawGridLines(false);
                        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        YAxis yAxisLeft = chart.getAxisLeft();
                        yAxisLeft.setEnabled(false);
                        chart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
                            @Override
                            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                                Log.i("Entry", String.valueOf(e));
                                String data01 = labels[e.getXIndex()];
                                String data02 = String.valueOf(e.getVal());

                                FancyToast.makeText(getActivity().getApplicationContext(),data01 + " : " +data02,FancyToast.LENGTH_SHORT, FancyToast.WARNING,true).show();
                            }

                            @Override
                            public void onNothingSelected() {

                            }
                        });

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
                params.put("mingguke", a);
                return params;
            }

        };;

        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
