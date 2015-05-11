package org.muinf.seu.pperez.keystone_getatoken;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import android.util.Log;


public class MainActivity extends ActionBarActivity {

    View rootView;

    DownloadWebpageTask ay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment implements View.OnClickListener {



        Button Botonw;



        public PlaceholderFragment() {
        }

        public void onClick(View v) {
            System.out.println(v.toString());

            //ay.execute("");

           // AsyncTask<String, Void, String> execute = DownloadWebpageTask.execute("dsdskj");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            Botonw = (Button) rootView.findViewById(R.id.gettokenbutton);
//            Botonw.setOnClickListener(this);
            Botonw.setOnClickListener(               new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ay=new DownloadWebpageTask();
                    ay.execute("");//System.out.print("djksdjsk");

                };
            });

            return rootView;
        }

    }



    class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        String res;

        protected String doInBackground(String... urls) {

            Map<String, List<String>> rr;
            String res = "";
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;
            // View rootView = findViewById(R.layout.fragment_main);

            EditText tuser;
            EditText tpassword;
            TextView ttoken;
            TextView tdomain;

            tuser = (EditText) rootView.findViewById(R.id.user);
            tpassword=  (EditText)  rootView.findViewById(R.id.passwd);
            tdomain=  (EditText)  rootView.findViewById(R.id.domain);
            ttoken =   (TextView)  rootView.findViewById(R.id.token);

            Log.v("EditText", tuser.getText().toString());

            String username = tuser.getText().toString(); //"Android_SEU_3n5_1";//
            String passwd = tpassword.getText().toString(); // "sensor";//
            String domain = tdomain.getText().toString(); // "Asignatura SEU";

            // String HeaderAccept = "application/xml";
            String HeaderContent = "application/json";
            String payload = "{ \"auth\": {" +
                    "    \"identity\": {" +
                    "      \"methods\": [\"password\"]," +
                    "      \"password\": {" +
                    "        \"user\": {\"name\": \""+username+"\", \"domain\": { \"name\": \""+domain+"\" }, \"password\": \""+passwd+"\"}" +
                    "      }" +
                    "    }" +
                   "  }" +
                    "}";

            // String encodedData = URLEncoder.encode(payload, "UTF-8");
            // String encodedData = payload;
            String leng = null;
            try {
                leng = Integer.toString(payload.getBytes("UTF-8").length);

                OutputStreamWriter wr = null;
                BufferedReader rd = null;
                StringBuilder sb = null;


                URL url = null;

                url = new URL("http://pperez-seu-ks.disca.upv.es:5000/v3/auth/tokens");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 ); // miliseconds
                conn.setConnectTimeout(15000 );
                conn.setRequestMethod("POST");

                //conn.setRequestProperty("Accept", HeaderAccept);
                conn.setRequestProperty("Content-type", HeaderContent);
                //conn.setRequestProperty("Fiware-Service", HeaderService);
                conn.setRequestProperty("Content-Length", leng);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(payload.getBytes("UTF-8"));
                os.flush();
                os.close();


                int rc = conn.getResponseCode();
                String resp = conn.getContentEncoding();
                is = conn.getInputStream();

                if (rc == 201) {
                    //read the result from the server
                    rd = new BufferedReader(new InputStreamReader(is));
                    //res=rd.readLine();
                    // cabeceras de recepcion
                    rr = conn.getHeaderFields();
                    System.out.println("headers: " + rr.toString());

                } else {
                    rr = null;
                    System.out.println("http response code error: " + rc + "\n");

                }


                String cad=rr.get("X-Subject-Token").get(0);
                return cad;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "error";
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            res=result;
            TextView ttoken =   (TextView)  rootView.findViewById(R.id.token);
            ttoken.setText(result);
        }
    }


}
