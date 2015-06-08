package hyuse.how_much;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * Created by hwang-gyojun on 2015. 5. 4..
 */
public class PostJSON {
    private String url = "http://bear.3dwise.com:8000/price/";
    private String result;
    private String type;
    /* Kyojun Hwang  code */
    public void setType(String type) {
        result = null;
        this.type = type;
    }

    public void send(String sub_id, String region_si) {
        if (type.equals("result")) {
            String data = "?sub_id=" + sub_id + "&region_si=" + region_si;
            String u = url + type + data;
            new HttpAsyncTask().execute(u, "get");
        }
    }

    public void send() {
        String u = url + type;
        new HttpAsyncTask().execute(u, "get");
    }

    public void send(String _data) {
        String data = "";
        String u = "";

        switch (type) {
            case "main":
                data = "?category_id=" + _data;
                u = url + type + data;
                new HttpAsyncTask().execute(u, "get");
                break;
            case "sub":
                data = "?main_id=" + _data;
                u = url + type + data;
                new HttpAsyncTask().execute(u, "get");
                break;
            case "auto_complete":
                data = "?data=" + _data.replace(" ", "");
                u = url + type + data;
                new HttpAsyncTask().execute(u, "get");
                break;
            case "home":
                new HttpAsyncTask().execute(_data, "post");
                break;
        }
    }

    public String returnResult() throws ExecutionException, InterruptedException {
        return result;
    }
    /* Kyojun Hwang  code end */

    /* haryeong code */
    public void GET(final String urlString){
        final int[] end = {1};
        final String[] responseString = new String[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = null;
                try
                {
                    URI url = new URI(urlString);

                    HttpGet httpGet = new HttpGet(url);

                    httpResponse = httpClient.execute(httpGet);
                    responseString[0] = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
                    end[0] = 0;
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while(end[0] == 1){}

        result = responseString[0];
    }
    /* haryeong code end*/

    /* Kyojun Hwang  code */
    public void POST(String json){
        InputStream inputStream = null;
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            StringEntity se = new StringEntity(json, HTTP.UTF_8);

            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();

            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream,"utf-8"));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... objs) {
            if (objs[1].equals("get"))
                GET(objs[0]);
            else if (objs[1].equals("post"))
                POST(objs[0]);

            return null;
        }
    }
    /* Kyojun Hwang  code end */
}
