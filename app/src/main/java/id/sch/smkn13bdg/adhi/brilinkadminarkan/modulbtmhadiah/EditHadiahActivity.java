package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmhadiah;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.MainActivity;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

public class EditHadiahActivity extends AppCompatActivity {

    private ProgressDialog pd;

    EditText editnamahadiah, editpoint, editjmlhitems;

    //foto dari server
    NetworkImageView imageprev;
    ImageLoader mImageLoader;
    String url = Server.url_server +"app/hadiah/";
    String IMAGE_URL ;
    String fotodefault = "defaulthadiah.png";

    String urldata2 = "app/service_detailhadiah.php";
    String url2 = Server.url_server +urldata2;

    String urldata = "app/updatestokhadiah.php";
    String urlupdate = Server.url_server +urldata;

    Bitmap bitmap, decoded;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100

    String idhadiah, message;
    Button btnupdate, btnfoto;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hadiah_edit);

        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        editnamahadiah = (EditText) findViewById(R.id.updatenamahadiah);
        editpoint = (EditText) findViewById(R.id.updatepointhadiah);
        editjmlhitems = (EditText) findViewById(R.id.updateitemshadiah);
        imageprev = (NetworkImageView) findViewById(R.id.updateprevimage);
        btnfoto = (Button) findViewById(R.id.btnupdatefotohadiah);
        btnupdate = (Button) findViewById(R.id.btnupdatehadiah);

        //ambil data dari fragment
        idhadiah = getIntent().getStringExtra("idhadiah");

        mImageLoader = MySingleton.getInstance(getApplicationContext()).getImageLoader();
        IMAGE_URL = url + String.valueOf(fotodefault);
        imageprev.setImageUrl(IMAGE_URL, mImageLoader);

        if (idhadiah != null){
            load_datahadiah_from_server();
        }

        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pilihGambar();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idhadiah != null){
                    updatedatahadiah();
                }else{
                    tambahdatahadiah();
                }
            }
        });

    }

    private void pilihGambar(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageprev.setImageBitmap(decoded);
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                // 100 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 100));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void load_datahadiah_from_server() {
        pd.show();

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

                                String id_hadiah = jsonobject.getString("id_hadiah").trim();
                                String nama_hadiah = jsonobject.getString("nama_hadiah").trim();
                                String foto_hadiah = jsonobject.getString("foto_hadiah").trim();
                                String jumlah_point = jsonobject.getString("jumlah_point").trim();
                                String jumlah_items = jsonobject.getString("jumlah_items").trim();

                                editnamahadiah.setText(nama_hadiah);
                                editpoint.setText(jumlah_point);
                                editjmlhitems.setText(jumlah_items);

                                mImageLoader = MySingleton.getInstance(getApplicationContext()).getImageLoader();
                                IMAGE_URL = url + String.valueOf(foto_hadiah);
                                imageprev.setImageUrl(IMAGE_URL, mImageLoader);

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

                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
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
                params.put("id_hadiah", idhadiah);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    public  void updatedatahadiah(){

        pd.show();

        final String id_hadiah = idhadiah.toString();
        final String namahadiahupdate = editnamahadiah.getText().toString();
        final String pointupdate = editpoint.getText().toString();
        final String jumlahitemsupdate = editjmlhitems.getText().toString();
        final String fotoupdate;

        if(decoded == null){

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    urlupdate,
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
                                    Intent i = new Intent(getApplication(), MainActivity.class);
                                    startActivity(i);
                                    FancyToast.makeText(getApplicationContext(),message,FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                                } else {
                                    FancyToast.makeText(getApplicationContext(),message,FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
                                }
                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                            }
                            pd.hide();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error != null){

                                FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                                pd.hide();
                                finish();
                            }
                        }
                    }

            )
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("idhadiah", id_hadiah);
                    params.put("namahadiah", namahadiahupdate);
                    params.put("point", pointupdate);
                    params.put("jumlahitems", jumlahitemsupdate);
                    return params;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        } else {

            fotoupdate = getStringImage(decoded);

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    urlupdate,
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
                                    Intent i = new Intent(getApplication(), MainActivity.class);
                                    startActivity(i);
                                    FancyToast.makeText(getApplicationContext(),message,FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                                } else {
                                    FancyToast.makeText(getApplicationContext(),message,FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
                                }
                            } catch (JSONException e) {
                                // JSON error
                                e.printStackTrace();
                            }
                            pd.hide();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error != null){

                                FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                                pd.hide();
                                finish();
                            }
                        }
                    }

            )
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("idhadiah", id_hadiah);
                    params.put("namahadiah", namahadiahupdate);
                    params.put("point", pointupdate);
                    params.put("foto", fotoupdate);
                    params.put("jumlahitems", jumlahitemsupdate);
                    return params;
                }
            };

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }
    }
    public  void tambahdatahadiah(){

        pd.show();

        final String namahadiahupdate = editnamahadiah.getText().toString();
        final String pointupdate = editpoint.getText().toString();
        final String jumlahitemsupdate = editjmlhitems.getText().toString();
        final String fotoupdate = getStringImage(decoded);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                urlupdate,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("Response: ",response.toString());
                        Log.d("foto yang dipilih: ", fotoupdate);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt("success");
                            message = jObj.getString("message");

                            // Cek error node pada json
                            if (success == 1) {
                                Log.d("Add/update", jObj.toString());
                                FancyToast.makeText(getApplicationContext(),message,FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,true).show();
                            } else {
                                FancyToast.makeText(getApplicationContext(),message,FancyToast.LENGTH_LONG, FancyToast.WARNING,true).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                        pd.hide();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){

                            FancyToast.makeText(getApplicationContext(),"Terjadi ganguan dengan koneksi server",FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();
                            pd.hide();
                            finish();
                        }
                    }
                }

        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("namahadiah", namahadiahupdate);
                params.put("point", pointupdate);
                params.put("foto", fotoupdate);
                params.put("jumlahitems", jumlahitemsupdate);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
