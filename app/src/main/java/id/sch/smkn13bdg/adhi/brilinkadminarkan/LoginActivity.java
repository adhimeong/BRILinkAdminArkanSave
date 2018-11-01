package id.sch.smkn13bdg.adhi.brilinkadminarkan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.UserController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

public class LoginActivity extends AppCompatActivity {

    Button loginbtn;
    EditText username, password;
    String user, pass;

    String urldata = "app/loginadmin.php";
    String url = Server.url_server +urldata;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        //if the user is already logged in we will directly start the main activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        loginbtn = (Button) findViewById(R.id.loginbottonlogin);
        username = (EditText) findViewById(R.id.loginusername);
        password = (EditText) findViewById(R.id.loginpassword);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();

                load_data_from_server(user, pass);
            }
        });
    }

    public void load_data_from_server(final String a, final String b ) {
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
                                String idadmin = jsonobject.getString("id_admin").trim();
                                String username = jsonobject.getString("username").trim();
                                String password = jsonobject.getString("password").trim();

                                UserController user = new UserController( idadmin, username, password);
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                finish();

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

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
                params.put("username", a);
                params.put("password", b);
                return params;
            }
        };


        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
