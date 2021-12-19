package com.aurelia_truly_jessica.transvice_test;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aurelia_truly_jessica.transvice_test.Adapter.BookingAdapter;
import com.aurelia_truly_jessica.transvice_test.Api.BookingApi;
import com.aurelia_truly_jessica.transvice_test.Model.BookingResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {
    public static final int LAUNCH_ADD_ACTIVITY = 123;
    private SwipeRefreshLayout srBooking;
    private BookingAdapter adapter;
    private SearchView svBooking;
    private LinearLayout layoutLoading;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Declare request queue
        queue = Volley.newRequestQueue(this);
        layoutLoading = findViewById(R.id.layout_loading);
        srBooking = findViewById(R.id.sr_booking);
        svBooking = findViewById(R.id.sv_booking);

        srBooking.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllBooking();
            }
        });

        svBooking.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookingActivity.this, AddEditActivity.class);
                startActivityForResult(i, LAUNCH_ADD_ACTIVITY);
            }
        });

        RecyclerView rvBooking = findViewById(R.id.rv_tampilBooking);
        adapter = new BookingAdapter(new ArrayList<>(), this);
        rvBooking.setLayoutManager(new LinearLayoutManager(BookingActivity.this,
                LinearLayoutManager.VERTICAL, false));
        rvBooking.setAdapter(adapter);
        getAllBooking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == Activity.RESULT_OK)
            getAllBooking();
    }

    private void getAllBooking() {
        srBooking.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(GET, BookingApi.GET_ALL_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                 /* Deserialization data from JSON response from API
                 to java object MahasiswaResponse using Gson */
                BookingResponse bookingResponse = gson.fromJson(response, BookingResponse.class);
                adapter.setBookingList(bookingResponse.getBookingList());
                adapter.getFilter().filter(svBooking.getQuery());
                Toast.makeText(BookingActivity.this, bookingResponse.getMessage(), Toast.LENGTH_SHORT).show();
                srBooking.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srBooking.setRefreshing(false);
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(BookingActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(BookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void deleteBooking(long id) {
        setLoading(true);
        StringRequest stringRequest = new StringRequest(POST,
                BookingApi.DELETE_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                /* Deserialization data from JSON response from API
                to java object MahasiswaResponse using Gson */
                BookingResponse bookingResponse = gson.fromJson(response, BookingResponse.class);
                setLoading(false);
                Toast.makeText(BookingActivity.this, bookingResponse.getMessage(), Toast.LENGTH_SHORT).show();
                getAllBooking();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(BookingActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(BookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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