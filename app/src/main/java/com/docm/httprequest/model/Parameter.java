package com.docm.httprequest.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Romain Mathieu on 30/10/2015.
 *
 * Parameter and value associate for HTTP Request
 */
public class Parameter {

    String name = "";
    String value = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    /**
     * Constructor.
     *
     * @param name (required) Parameter name
     * @param value (required) Parameter value
     */
    public Parameter(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    /**
     * Return json parameter;
     *
     * @return JSONObject
     */
    public JSONObject getJson()
    {
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
