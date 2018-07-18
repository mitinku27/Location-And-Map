package com.example.tinku.locationandmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.Earthquick;
import Util.Constant;

public class DetailListActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> earthquicks;
    ArrayAdapter listAdapter;
    List<Earthquick> earthquickList;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);
        listView = (ListView) findViewById(R.id.mylist);
        earthquickList= new ArrayList<>();
        earthquicks = new ArrayList<>();
        queue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constant.url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //JSONArray jsonArray = null;
                try {
                    JSONArray jsonArray = response.getJSONArray("features");
                    for (int i =0;i<Constant.limit;i++) {
                        Earthquick earthquick = new Earthquick();
                        JSONObject object = jsonArray.getJSONObject(i).getJSONObject("properties");
                        JSONObject geomat = jsonArray.getJSONObject(i).getJSONObject("geometry");
                        JSONArray array = geomat.getJSONArray("coordinates");
                        earthquick.setPlace(object.getString("place"));
                        java.text.DateFormat dateFormat= java.text.DateFormat.getDateInstance();
                        String fomdat= dateFormat.format(new Date(object.getLong("time")).getTime());
                        earthquicks.add(earthquick.getPlace()+"\n"+fomdat);
                    }
                    listAdapter= new ArrayAdapter<>(DetailListActivity.this,android.R.layout.simple_list_item_1
                    ,android.R.id.text1,earthquicks);
                    listView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();
                    //listAdapter.notifyAll();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }
}
