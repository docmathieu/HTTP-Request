package com.docm.httprequest.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.docm.httprequest.R;
import com.docm.httprequest.imodel.CallBack;
import com.docm.httprequest.model.Request;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Romain Mathieu on 30/10/2015.
 *
 * This controller is used for doing HTTP requests.
 */
public class HttpController extends AsyncTask<Request, Void, String>
{
    public static final int TIMEOUT_VALUE = 10000;

    private CallBack listener;
    private Bitmap bitmap;
    //private Boolean contentTypeImage = false;

    /**
     * Constructor.
     */
    public HttpController()
    {
        super();
    }

    /**
     * Listener Setter
     * Used for callBack
     *
     * @param listener (required) Target Method to call at the end of request
     */
    public void setListener(CallBack listener)
    {
        this.listener = listener;
    }

    /**
     * CallBack
     *
     * @param str (required) Response from the server
     */
    @Override
    protected void onPostExecute(String str)
    {
        if (bitmap != null){
            listener.imageCallBack(bitmap);
        }else{
            listener.messageCallBack(str);
        }
    }

    /**
     * Async call for HTTP request.
     *
     * @param param (required) List of Request, we use only the first
     * @return String
     */
    @Override
    protected String doInBackground(Request... param)
    {
        String responseStr = "";
        HttpURLConnection connection = null;

        Request request = param[0];
        String dataUrl = request.getUrl();
        if ((!dataUrl.startsWith("http://")) && (!dataUrl.startsWith("https://")))
            dataUrl = "http://" + dataUrl;

        String mimeType = request.getMimeType();
        String referer = request.getReferer();
        String login = request.getLogin();
        String password = request.getPassword();

        try {
            // Create connection

            // !(POST, PUT, PATCH)
            if (!request.getVerb().sendBody()){
                dataUrl = dataUrl + "?" + request.getParamsQuery();
            }

            URL url = new URL(dataUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(TIMEOUT_VALUE);
            connection.setConnectTimeout(TIMEOUT_VALUE);
            connection.setRequestMethod(request.getVerb().toString());
            connection.setUseCaches(false);
            if (mimeType.length() > 0) connection.setRequestProperty("Content-Type", mimeType);
            if (referer.length() > 0) connection.setRequestProperty("Referer", referer);

            // Authorization, basic authentication
            if (login.length() > 0){
                String loginPassword = login + ":" + password;
                String basicAuth = "Basic " + Base64.encodeToString(loginPassword.getBytes(), Base64.NO_WRAP);
                connection.setRequestProperty ("Authorization", basicAuth);
            }

            connection.setDoInput(true);

            // (POST, PUT, PATCH)
            if (request.getVerb().sendBody()){
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(request.getParamsQuery());
                writer.flush();
                writer.close();
            }

            // Response
            int statusCode = connection.getResponseCode();

            // Gets response headers
            Map<String, List<String>> headers = null;
            if (statusCode == HttpURLConnection.HTTP_OK) {
                headers = connection.getHeaderFields();
            }

            InputStream is = null;
            StringBuilder response = new StringBuilder();

            response.append("http status: " + statusCode + '\n');
            response.append("Returned headers:" + '\n' + getReturningHeadersString(headers));
            response.append('\n');
            response.append("Data:" + "\n");

            if (statusCode < HttpURLConnection.HTTP_BAD_REQUEST){
                // Response ok
                is = connection.getInputStream();
                if (getImageType(connection)){
                    bitmap = BitmapFactory.decodeStream(is);
                    return "";
                }
            }else{
                // Response error
                is = connection.getErrorStream();

            }

            // Trace response
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }
            rd.close();

            responseStr = response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            String error = "ERROR:\n";
            error += e.getMessage() + "\n";
            error += e.getClass() + "\n";
            return error;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

        return responseStr;
    }

    /**
     * Get String from HTTP request result headers
     *
     * @param headers
     * @return
     */
    private String getReturningHeadersString(Map<String, List<String>> headers)
    {
        if (headers == null){
            return "none\n";
        }

        String result = "";

        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            List<String> values = entry.getValue();
            String valuesString = "";

            for (String value : values){
                if (!"".equals(valuesString)){
                    valuesString += ", ";
                }
                valuesString += value;
            }

            if (entry.getKey() == null){
                result += "  " + valuesString + "\n";
            }else{
                result += "  " + entry.getKey() + " = " + valuesString + "\n";
            }
        }

        return result;
    }

    /**
     * Get content type of HTTP request response
     * Determine if data can be showed as image
     *
     * @param connection (required)
     * @return Boolean
     */
    private boolean getImageType(HttpURLConnection connection)
    {
        String type = connection.getContentType();
        if (type.contains("image/png")) return true;
        if (type.contains("image/gif")) return true;
        if (type.contains("image/jpeg")) return true;
        if (type.contains("image/tiff")) return true;
        return false;
    }
}
