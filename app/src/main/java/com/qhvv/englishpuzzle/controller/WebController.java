package com.qhvv.englishpuzzle.controller;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qhvv.englishpuzzle.configuration.IRequestHande;
import com.qhvv.englishpuzzle.configuration.IWebService;

/**
 * Created by Vo Quang Hoa on 19/09/2015.
 */
public class WebController implements IWebService {
    private static WebController instance;
    public static synchronized WebController getInstance(){
        if(instance == null){
            instance = new WebController();
        }
        return instance;
    }

    private WebController(){

    }

    public void DownloadImage(Context context, String url,final IRequestHande<Bitmap> handler){
        ImageRequest ir = new ImageRequest(BASE_URL + url, new Response.Listener<Bitmap>() {
            public void onResponse(Bitmap response) {
                handler.onSuccess(response);
            }
        }, 0, 0, null, new Response.ErrorListener(){
            public void onErrorResponse(VolleyError volleyError) {
                if(handler!=null){
                    handler.onError(volleyError.getMessage());
                }
            }
        });

        RequestQueue rq = Volley.newRequestQueue(context);

        rq.add(ir);
    }

    public void DownloadContent(Context context,String url, final IRequestHande<String> handler){
        StringRequest stringRequest = new StringRequest(Request.Method.GET,BASE_URL+ url, new Response.Listener<String>(){
            public void onResponse(String s) {
                if(handler!=null){
                    handler.onSuccess(s);
                }
            }
        }, new Response.ErrorListener(){
            public void onErrorResponse(VolleyError volleyError) {
                handler.onError(volleyError.getMessage());
            }
        });

        RequestQueue rq = Volley.newRequestQueue(context);

        rq.add(stringRequest);
    }
}
