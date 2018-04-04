package server;

/**
 * Created by walid on 27/01/2018.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class RestHelper {

    private static final String ERR_CONN_MSG = "There is no connectivity to establish an HTTP connection.";

    public RestResponse getUser(Context context, String url, Map<String, String> headers,String body)
            throws IOException, NetworkException {

        if (!checkConnectivity(context)) {
            throw new NetworkException(ERR_CONN_MSG);
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = getConnection(url, "POST", headers);
            writePostData(urlConnection, body);
            RestResponse response = connect(urlConnection);
            //Log.d("url conection to string",response.getHttpContent());
            return response;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }


    public RestResponse get(Context context, String url, Map<String, String> headers)
            throws IOException, NetworkException {

        if (!checkConnectivity(context)) {
            throw new NetworkException(ERR_CONN_MSG);
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = getConnection(url, "GET", headers);
            RestResponse response = connect(urlConnection);
            return response;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
    //http://localhost:8080/tfg/rest/UserService/userByMail/kkk@kkk
    //http://localhost:8080/tfg/rest/UserService/userByMail/kkk@kkk
    public RestResponse post(Context context, String url, Map<String, String> headers, String body)
            throws IOException, NetworkException {

        if (!checkConnectivity(context)) {
            throw new NetworkException(ERR_CONN_MSG);
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = getConnection(url, "POST", headers);
            writePostData(urlConnection, body);
            RestResponse response = connectCreated(urlConnection);
            Log.d("response",String.valueOf(response.getHttpResponseCode()) + " " + String.valueOf(response.getHttpResponseCode()));
            return response;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    //http://localhost:8080/tfg/rest/UserService/userByMail/kkk@kkk
    /*public RestResponse check(Context context, String url, Map<String, String> headers, String body)
            throws IOException, NetworkException {

        if (!checkConnectivity(context)) {
            throw new NetworkException(ERR_CONN_MSG);
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = getConnection(url, "POST", headers);
            writePostData(urlConnection, body);
            RestResponse response = checkResult(urlConnection);
            //Log.d("response",String.valueOf(response.getHttpResponseCode()) + " " + String.valueOf(response.getHttpResponseCode()));
            return response;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }*/
    public RestResponse put(Context context, String url, Map<String, String> headers, String body)
            throws IOException, NetworkException {

        if (!checkConnectivity(context)) {
            throw new NetworkException(ERR_CONN_MSG);
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = getConnection(url, "PUT", headers);
            writePostData(urlConnection, body);
            RestResponse response = connect(urlConnection);
            return response;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public RestResponse delete(Context context, String url, Map<String, String> headers)
            throws IOException, NetworkException  {

        if (!checkConnectivity(context)) {
            throw new NetworkException(ERR_CONN_MSG);
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = getConnection(url, "DELETE", headers);
            RestResponse response = connectCreated(urlConnection);
            return response;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private HttpURLConnection getConnection(String urlDir, String method, Map<String, String> headers)
            throws IOException {
        URL url = new URL(urlDir);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(method);
        urlConnection.setUseCaches(false);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return urlConnection;
    }

    private void writePostData(HttpURLConnection urlConnection, String body) throws IOException {
        OutputStream os = null;
        try {
            urlConnection.setDoOutput(true);
            os = urlConnection.getOutputStream();
            os.write(body.getBytes());
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }


   /*private RestResponse connect(HttpURLConnection urlConnection) throws IOException {
        InputStream is = null;
        try {
            int code = urlConnection.getResponseCode();
            is = (code == HttpURLConnection.HTTP_OK) ?
                    urlConnection.getInputStream() :
                    urlConnection.getErrorStream();

            String response = readStringFromStream(is);
            return new RestResponse(code, response);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }*/

    private RestResponse connect(HttpURLConnection urlConnection) throws IOException {
        InputStream is = null;
        String response;
        try {
            int code = urlConnection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                is = urlConnection.getInputStream();
                response = readStringFromStream(is);
            } else if (code == HttpURLConnection.HTTP_NO_CONTENT) {
                response = null;
            }else{
                Log.d("NOT_FOUND","NOT_FOUND");
                is = urlConnection.getErrorStream();
                response = readStringFromStream(is);
            }

            return new RestResponse(code, response);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private RestResponse connectCreated(HttpURLConnection urlConnection) throws IOException {
        InputStream is = null;
        try {
            int code = urlConnection.getResponseCode();

            is = (code == HttpURLConnection.HTTP_CREATED) ?
                    urlConnection.getInputStream() :
                    urlConnection.getErrorStream();

            String response = readStringFromStream(is);
            return new RestResponse(code, response);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String readStringFromStream(InputStream is) throws IOException {
        String line;
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is));
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

    private boolean checkConnectivity(Context context) {

        // Obtain an instance of the Connectivity Manager.
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Obtain the current status of the network.
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}