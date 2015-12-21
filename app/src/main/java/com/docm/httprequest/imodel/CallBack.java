package com.docm.httprequest.imodel;

import android.graphics.Bitmap;

import com.docm.httprequest.model.Request;

import java.util.ArrayList;

/**
 * Created by Romain Mathieu on 04/11/2015.
 *
 * CallBack for HTTP request response
 */
public interface CallBack {
    void messageCallBack(String message);
    void imageCallBack(Bitmap bmp);
    void appendCallBack(String message);
    void selectRequestCallBack(Request request);
    void loadRequestsCallBack(ArrayList<Request> requests);
}
