package com.aurelia_truly_jessica.transvice_test;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aurelia_truly_jessica.transvice_test.Api.BookingApi;
import com.aurelia_truly_jessica.transvice_test.Model.Booking;
import com.aurelia_truly_jessica.transvice_test.Model.BookingResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AddEditActivity extends AppCompatActivity {
    private AutoCompleteTextView edJenisKendaraan, edJenisService;
    private EditText etTglService, etWaktuService;
    private LinearLayout layoutLoading;
    private RequestQueue queue;

    String[] jenisServiceList = {"Ganti Oli","Tambal Ban", "Cek Rem", "Cek Filter"};
    String[] jenisKendaraanList = {"Sepeda Motor", "Mobil"};

    ArrayAdapter<String> adapterJenisServiceList;
    ArrayAdapter<String> adapterJenisKendaraanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        // Declare request queue
        queue = Volley.newRequestQueue(this);
        etTglService = findViewById(R.id.etTglService);
        etWaktuService = findViewById(R.id.etWaktuService);
        edJenisKendaraan = findViewById(R.id.etJenisKendaraan);
        edJenisService = findViewById(R.id.etJenisService);
        layoutLoading = findViewById(R.id.layout_loading);

        adapterJenisServiceList = new ArrayAdapter<String>(this, R.layout.list_jenis_service, jenisServiceList);
        edJenisService.setAdapter(adapterJenisServiceList);

        adapterJenisKendaraanList = new ArrayAdapter<String>(this, R.layout.list_jenis_kendaraan,jenisKendaraanList);
        edJenisKendaraan.setAdapter(adapterJenisKendaraanList);

        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnSave = findViewById(R.id.btn_save);

        long id = getIntent().getLongExtra("id", -1);
        if (id == -1) {
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createBooking();
                }
            });
        } else {
            getBookingById(id);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateBooking(id);
                }
            });
        }
    }
    private void getBookingById(long id) {
        setLoading(true);
        StringRequest stringRequest = new StringRequest(GET, BookingApi.GET_BY_ID_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                BookingResponse bookingResponse = gson.fromJson(response, BookingResponse.class);
                Booking booking = bookingResponse.getBookingList().get(0);

                etTglService.setText(booking.getTgl_service());
                etWaktuService.setText(booking.getWaktu_service());
                edJenisService.setText(booking.getJenis_service(), false);
                edJenisKendaraan.setText(booking.getJenis_kendaraan(), false);

                Toast.makeText(AddEditActivity.this, bookingResponse.getMessage(), Toast.LENGTH_SHORT).show();
                setLoading(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try {
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(AddEditActivity.this, errors.getString("message"),
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AddEditActivity.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
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

    private void createBooking() {
        setLoading(true);
        Booking booking = new Booking(
                etTglService.getText().toString(),
                etWaktuService.getText().toString(),
                edJenisService.getText().toString(),
                edJenisKendaraan.getText().toString());

        StringRequest stringRequest = new StringRequest(POST, BookingApi.ADD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                         /* Deserialiasai data dari response JSON dari API
                         menjadi java object MahasiswaResponse menggunakan Gson */
                        BookingResponse bookingResponse = gson.fromJson(response, BookingResponse.class);
                        Toast.makeText(AddEditActivity.this, bookingResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                        setLoading(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(AddEditActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AddEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            // Adding body request which is object booking
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                 /* Serialization of data from java object StudentResponse
                    to JSON string using Gson */
                String requestBody = gson.toJson(booking);
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
            // Declare content type from body request that have been added
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        // Adding request to queue request
        queue.add(stringRequest);
    }

    private void updateBooking(long id) {
        setLoading(true);
        Booking booking = new Booking(
                etTglService.getText().toString(),
                etWaktuService.getText().toString(),
                edJenisService.getText().toString(),
                edJenisKendaraan.getText().toString());

        StringRequest stringRequest = new StringRequest(POST,
                BookingApi.UPDATE_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                BookingResponse bookingResponse = gson.fromJson(response, BookingResponse.class);
                Toast.makeText(AddEditActivity.this, bookingResponse.getMessage(), Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                setLoading(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(AddEditActivity.this,
                            errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AddEditActivity.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
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
            // Adding body request which is object booking
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();

                String requestBody = gson.toJson(booking);
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }
            // Declare content type from body request that have been added
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        // Adding request to queue request
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
//            layoutLoading.setVisibility(View.INVISIBLE);
//        }
    }
}