package com.docm.httprequest.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Romain Mathieu on 30/10/2015.
 *
 * Contain all settings to make a new HTTP Request
 */
public class Request {

    private Verb verb = null;
    private String url = "";
    private String referer = "";
    private Boolean jsonMode = false;
    private List<Parameter> params;

    /**
     * Constructor.
     */
    public Request(){
        params = new ArrayList<>();
    }

    public Verb getVerb() {
        return verb;
    }

    public void setVerb(Verb verb) {
        this.verb = verb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public Boolean getJsonMode() {
        return jsonMode;
    }

    public void setJsonMode(Boolean jsonMode) {
        this.jsonMode = jsonMode;
    }

    public void setParam(String param, String value){
        params.add(new Parameter(param, value));
    }

    public List<Parameter> getParams(){
        return params;
    }

    /**
     * Return parameters for simple Query
     *
     * @return String
     */
    public String getParamsQuery()
    {
        if (params.size() < 1) return "";
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Parameter parameter : params)
        {
            if (first) {
                first = false;
            }else {
                result.append("&");
            }

            try {
                result.append(URLEncoder.encode(parameter.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(parameter.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    /**
     * Return parameters, json format
     *
     * @return String (json)
     */
    public String getParamsJson(){
        JSONObject jsonParam = new JSONObject();

        try {
            for (Parameter parameter : params){
                jsonParam.put(parameter.getName(), parameter.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonParam.toString();
    }

    /**
     * Return json parameter;
     *
     * @return JSONObject
     */
    public JSONObject getJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("verb", verb.getVerb());
            json.put("url", url);
            json.put("referer", referer);
            json.put("jsonMode", jsonMode);
            JSONArray jsonArray = new JSONArray();
            for (Parameter parameter : params)
            {
                jsonArray.put(parameter.getJson());
            }
            json.put("params", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Return array of requests from String
     *
     * @param str (required)
     * @return ArrayList<Request>
     */
    public static ArrayList<Request> getRequestsFromString(String str)
    {
        ArrayList<Request> requests = new ArrayList<>();

        String[] lines = str.split("\n");
        for (String line : lines) {
            requests.add(getRequestFromString(line));
        }

        return requests;
    }

    /**
     * Return request from String json
     *
     * @param stringJson (required)
     * @return Request
     */
    private static Request getRequestFromString(String stringJson)
    {
        JSONObject json = null;
        try {
            json = new JSONObject(stringJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json != null) return getRequestFromJson(json);
        return null;
    }

    /**
     * Return request from JSONObject
     *
     * @param json (required)
     * @return Request
     */
    private static Request getRequestFromJson(JSONObject json)
    {
        Request returnRequest = new Request();
        try {
            returnRequest.setUrl(json.getString("url"));
            returnRequest.setVerb(Verb.valueOf(json.getString("verb")));
            returnRequest.setJsonMode(json.getBoolean("jsonMode"));
            returnRequest.setReferer(json.getString("referer"));
            JSONArray jsonArray = json.getJSONArray("params");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                returnRequest.setParam(row.getString("name"), row.getString("value"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnRequest;
    }
}
