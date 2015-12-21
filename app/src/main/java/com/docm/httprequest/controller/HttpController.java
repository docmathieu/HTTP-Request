package com.docm.httprequest.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

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

        String referer = request.getReferer();

        try {
            // Create connection
            if (!request.getVerb().sendBody()){
                if (request.getJsonMode()){
                    dataUrl = dataUrl + "?json=" + request.getParamsJson();
                }else{
                    dataUrl = dataUrl + "?" + request.getParamsQuery();
                }
            }

            URL url = new URL(dataUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(TIMEOUT_VALUE);
            connection.setConnectTimeout(TIMEOUT_VALUE);
            connection.setRequestMethod(request.getVerb().toString());
            connection.setUseCaches(false);
            if (referer.length() > 0) connection.setRequestProperty("Referer", request.getReferer());
            if (request.getJsonMode()) connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoInput(true);
            if (request.getVerb().sendBody()){
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                if (request.getJsonMode()){
                    writer.write(request.getParamsJson());
                }else{
                    writer.write(request.getParamsQuery());
                }
                writer.flush();
                writer.close();
            }

            // Get Response
            InputStream is = connection.getInputStream();
            if (getImageType(connection)){
                bitmap = BitmapFactory.decodeStream(is);
                return "";
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();

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
