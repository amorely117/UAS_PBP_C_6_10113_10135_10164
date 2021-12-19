package com.aurelia_truly_jessica.transvice_test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aurelia_truly_jessica.transvice_test.Adapter.ServiceAdapter;
import com.aurelia_truly_jessica.transvice_test.Api.ServiceApi;
import com.aurelia_truly_jessica.transvice_test.Model.ServiceResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class ServiceActivity extends AppCompatActivity {
    public static final int LAUNCH_ADD_ACTIVITY = 123;
    private SwipeRefreshLayout srService;
    private ServiceAdapter adapter;
    private SearchView svService;
    private LinearLayout layoutLoading;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        // Declare request queue
        queue = Volley.newRequestQueue(this);
        layoutLoading = findViewById(R.id.layout_loading);
        srService = findViewById(R.id.sr_service);
        svService = findViewById(R.id.sv_service);

        srService.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllService();
            }
        });

        svService.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ServiceActivity.this, EditAddActivity.class);
                startActivityForResult(i, LAUNCH_ADD_ACTIVITY);
            }
        });

        RecyclerView rvService = findViewById(R.id.rv_tampilService);
        adapter = new ServiceAdapter(new ArrayList<>(), this);
        rvService.setLayoutManager(new LinearLayoutManager(ServiceActivity.this,
                LinearLayoutManager.VERTICAL, false));
        rvService.setAdapter(adapter);
        getAllService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == Activity.RESULT_OK)
            getAllService();
    }

    private void getAllService() {
        srService.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(GET, ServiceApi.GET_ALL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                 /* Deserialization data from JSON response from API
                 to java object MahasiswaResponse using Gson */
                ServiceResponse serviceResponse = gson.fromJson(response, ServiceResponse.class);
                adapter.setServiceList(serviceResponse.getServiceList());
                Toast.makeText(ServiceActivity.this, serviceResponse.getMessage(), Toast.LENGTH_SHORT).show();
                srService.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srService.setRefreshing(false);
                try {
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(ServiceActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ServiceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            // Adding header in request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        // Adding request to request queue
        queue.add(stringRequest);
    }
    public void deleteService(long id) {
        setLoading(true);
        StringRequest stringRequest = new StringRequest(POST,
                ServiceApi.DELETE_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                /* Deserialization data from JSON response from API
                to java object MahasiswaResponse using Gson */
                ServiceResponse serviceResponse = gson.fromJson(response, ServiceResponse.class);
                setLoading(false);
                Toast.makeText(ServiceActivity.this, serviceResponse.getMessage(), Toast.LENGTH_SHORT).show();
                getAllService();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(ServiceActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ServiceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            // Adding header pada request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        // Adding request to request queue
        queue.add(stringRequest);
    }

    // Function for display loading layout
    private void setLoading(boolean isLoading) {
//        if (isLoading) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            layoutLoading.setVisibility(View.VISIBLE);
//        } else {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            layoutLoading.setVisibility(View.GONE);
//        }
    }
}