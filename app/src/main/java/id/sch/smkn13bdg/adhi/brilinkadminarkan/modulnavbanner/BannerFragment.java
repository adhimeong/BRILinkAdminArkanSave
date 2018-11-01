package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavbanner;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.BannerAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataBannerController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

/**
 * A simple {@link Fragment} subclass.
 */
public class BannerFragment extends Fragment {

    private ProgressDialog pd;
    String urldata3 = "app/tampilbanner.php";
    String url3 = Server.url_server +urldata3;

    List<DataBannerController> databannerController = new ArrayList<DataBannerController>();
    BannerAdapter bannerAdapter;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    Button pengaturanbtn;


    public BannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) view.findViewById(R.id.SliderDots);
        pengaturanbtn = (Button) view.findViewById(R.id.slidebtnpengaturan);
        pengaturanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), BannerEditActivity.class);
                startActivity(i);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_radio_button_unchecked_black_24dp));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_radio_button_checked_black_24dp));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        load_banner_from_server();
        // Inflate the layout for this fragment
        return view;
    }

    public void load_banner_from_server(){
        pd.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url3,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("string",response);

                        try {

                            bannerAdapter = new BannerAdapter(databannerController, getActivity());

                            JSONArray jsonarray = new JSONArray(response);

                            for(int i=0; i < jsonarray.length(); i++) {

                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                //String id_banner = jsonobject.getString("id,_banner").trim();
                                //String nama_banner = jsonobject.getString("nama_banner").trim();
                                String foto_banner = jsonobject.getString("foto_banner").trim();
                                String fotobanner = foto_banner.toString();

                                DataBannerController d4 = new DataBannerController();
                                d4.setFoto_banner(fotobanner);
                                databannerController.add(d4);

                                Log.d("urlbanner",fotobanner);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        viewPager.setAdapter(bannerAdapter);

                        dotscount = bannerAdapter.getCount();
                        dots = new ImageView[dotscount];

                        for(int i = 0; i < dotscount; i++){

                            dots[i] = new ImageView(getActivity());
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_radio_button_unchecked_black_24dp));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            params.setMargins(8, 0, 8, 0);

                            sliderDotspanel.addView(dots[i], params);
                        }

                        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.ic_radio_button_checked_black_24dp));
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
