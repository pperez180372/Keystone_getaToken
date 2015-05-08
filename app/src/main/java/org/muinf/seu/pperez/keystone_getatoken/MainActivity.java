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


public class MainActivity extends ActionBarActivity {


    private EditText tuser;
    private EditText tpassword;
    private TextView ttoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tuser=  (EditText) findViewById(R.id.user);
        tpassword=  (EditText)  findViewById(R.id.passwd);
        ttoken =   (TextView)  findViewById(R.id.token);
        Button Botonw = (Button) findViewById(R.id.gettokenbutton);


        Botonw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                class DownloadWebpageTask extends AsyncTask<String, Void, String> {
                    @Override
                    protected String doInBackground(String... urls) {

                        Map<String, List<String>> rr;
                        String res = "";
                        InputStream is = null;
                        // Only display the first 500 characters of the retrieved
                        // web page content.
                        int len = 500;
                        String HeaderAccept = "application/xml";
                        String HeaderContent = "application/xml";
                        String HeaderService = "myTenant";
                        String payload = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Request xsi:schemaLocation=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd\" ReturnPolicyIdList=\"false\" CombinedDecision=\"false\" xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\"><Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">role12345</AttributeValue></Attribute></Attributes><Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\"> <Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">fiware:orion:tenant1234:us-west-1:res9876</AttributeValue></Attribute></Attributes><Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\"><Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">read</AttributeValue></Attribute> </Attributes></Request>";
                        // String encodedData = URLEncoder.encode(payload, "UTF-8");
                        // String encodedData = payload;
                        String leng = null;
                        try {
                            leng = Integer.toString(payload.getBytes("UTF-8").length);

                            OutputStreamWriter wr = null;
                            BufferedReader rd = null;
                            StringBuilder sb = null;


                            URL url = null;

                            url = new URL("http://pperez-seu-ks.disca.upv.es:8080/pdp/v3");

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(10000 /* milliseconds */);
                            conn.setConnectTimeout(15000 /* milliseconds */);
                            conn.setRequestMethod("POST");

                            conn.setRequestProperty("Accept", HeaderAccept);
                            conn.setRequestProperty("Content-type", HeaderContent);
                            conn.setRequestProperty("Fiware-Service", HeaderService);
                            conn.setRequestProperty("Content-Length", leng);
                            conn.setDoOutput(true);

                            OutputStream os = conn.getOutputStream();
                            os.write(payload.getBytes("UTF-8"));
                            os.flush();
                            os.close();


                            int rc = conn.getResponseCode();
                            String resp = conn.getContentEncoding();
                            is = conn.getInputStream();

                            if (rc == 200) {
                                //read the result from the server
                                rd = new BufferedReader(new InputStreamReader(is));
                                //res=rd.readLine();
                                // cabeceras de recepcion
                                rr = conn.getHeaderFields();


                            } else {
                                rr = null;
                                System.out.println("http response code error: " + rc + "\n");

                            }


                            System.out.println("headers: " + rr.toString());

                            // Convert the InputStream into a string
                            Reader reader = null;
                            reader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                            char[] buffer = new char[len];
                            reader.read(buffer);
                            return new String(buffer);


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

                        ttoken.setText(result);
                    }
                }


            }
        });

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
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}