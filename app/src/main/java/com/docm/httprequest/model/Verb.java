package com.docm.httprequest.model;

/**
 * Created by Romain Mathieu on 30/10/2015.
 *
 * Enum HTTP verbs
 */
public enum Verb
{
    GET("GET"),
    POST("POST"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    PUT("PUT"),
    DELETE("DELETE"),
    TRACE("TRACE"),
    PATCH("PATCH");

    protected String verb;

    public String getVerb() {
        return verb;
    }

    /**
     * Constructor.
     *
     * @param verb (required) String value
     */
    Verb(String verb)
    {
        this.verb = verb;
    }

    /**
     * Return if Verb permit writable request
     *
     * @return Boolean
     */
    public int getPosition(){

        switch (this){
            case GET:
                return 0;
            case POST:
                return 1;
            case HEAD:
                return 2;
            case OPTIONS:
                return 3;
            case PUT:
                return 4;
            case DELETE:
                return 5;
            case TRACE:
                return 6;
            case PATCH:
                return 7;
        }
        return 0;
    }

    /**
     * Return if Verb permit writable request
     *
     * @return Boolean
     */
    public Boolean sendBody(){

        switch (this){
            case GET:
                return false;
            case POST:
                return true;
            case HEAD:
                return false;
            case OPTIONS:
                return false;
            case PUT:
                return true;
            case DELETE:
                return false;
            case TRACE:
                return false;
            case PATCH:
                return true;
        }
        return false;
    }
}
