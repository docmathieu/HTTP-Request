package com.docm.httprequest.controller;

import android.content.Context;
import android.os.AsyncTask;

import com.docm.httprequest.R;
import com.docm.httprequest.imodel.CallBack;
import com.docm.httprequest.model.Request;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


/**
 * Created by Romain Mathieu on 13/11/2015.
 *
 * This controller is used to read and save Requests
 */
public class FileController extends AsyncTask<ArrayList<Request>, Void, String>
{
    public static final String METHOD_READ = "METHOD_READ";
    public static final String METHOD_WRITE = "METHOD_WRITE";

    public static final String FILE_NAME = "requests";

    private Context context;
    private CallBack listener;
    private String method = METHOD_READ;
    private String eol = System.getProperty("line.separator");


    /**
     * Constructor.
     */
    public FileController(Context context)
    {
        super();
        this.context = context;
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
     * Set Method (METHOD_READ, METHOD_WRITE, ...)
     *
     * @param method (required) Target FileController Method
     */
    public void setMethod(String method)
    {
        this.method = method;
    }

    /**
     * CallBack
     *
     * @param str (required) Response from the server
     */
    @Override
    protected void onPostExecute(String str)
    {
        if (str.startsWith(context.getString(R.string.error))){
            listener.appendCallBack(str);
            return;
        }

        switch (method) {
            case METHOD_READ:
                listener.loadRequestsCallBack(Request.getRequestsFromString(str));
                break;

            case METHOD_WRITE:
                listener.appendCallBack(str);
                break;
        }
    }

    /**
     * Async call for HTTP request.
     *
     * @param param (required) List of Request, we use only the first
     * @return String
     */
    @SafeVarargs
    @Override
    protected final String doInBackground(ArrayList<Request>... param)
    {
        String returnMessage = "";

        switch (method){
            case METHOD_READ:
                returnMessage += readMessage();                                 // TEST READ
                break;

            case METHOD_WRITE:
                ArrayList<Request> requests = param[0];
                String fullMessage = "";
                for (int i = 0; i < requests.size(); i++) {
                    Request request = requests.get(i);
                    fullMessage += request.getJson().toString() + eol;
                }
                //returnMessage += writeMessage(fullMessage);                   // TEST WRITE
                writeMessage(fullMessage);
                returnMessage += context.getString(R.string.request_save);
                break;
        }

        return returnMessage;
    }

    /**
     * Save message on file
     *
     * @param message (required)
     * @return String
     */
    private String writeMessage(String message)
    {
        String returnMessage = "";
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)));
            writer.append(message);
            returnMessage += message;
        } catch (Exception e) {
            e.printStackTrace();
            returnMessage += e.toString();
        }

        if (writer != null) {
            try {
                writer.close();
                return returnMessage;
            } catch (IOException e) {
                e.printStackTrace();
                returnMessage += e.toString();
            }
        }

        return context.getString(R.string.error) + ": " + context.getString(R.string.save_file_error) + "\n" + returnMessage;
    }

    /**
     * Read message on file
     *
     * @return String
     */
    private String readMessage()
    {
        String returnMessage = "";
        BufferedReader input = null;

        try {
            input = new BufferedReader(new InputStreamReader(context.openFileInput(FILE_NAME)));
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = input.readLine()) != null) {
                buffer.append(line).append(eol);
            }
            returnMessage = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            returnMessage += e.toString();
        }

        if (input != null) {
            try {
                input.close();
                return returnMessage;
            } catch (IOException e) {
                e.printStackTrace();
                returnMessage += e.toString();
            }
        }

        return context.getString(R.string.error) + ": " + context.getString(R.string.read_file_error) + "\n" + returnMessage;
    }
}
