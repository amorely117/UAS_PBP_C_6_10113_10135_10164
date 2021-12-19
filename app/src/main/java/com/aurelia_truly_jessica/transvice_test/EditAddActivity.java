package com.aurelia_truly_jessica.transvice_test;

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
import com.aurelia_truly_jessica.transvice_test.Api.ServiceApi;
import com.aurelia_truly_jessica.transvice_test.Model.Service;
import com.aurelia_truly_jessica.transvice_test.Model.ServiceResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class EditAddActivity extends AppCompatActivity {
    private AutoCompleteTextView edJenisKendaraan;
    private EditText etLamaPengerjaan, etJenisService;
    private LinearLayout layoutLoading;
    private RequestQueue queue;

    String[] jenisKendaraanList = {"Sepeda Motor", "Mobil"};

    ArrayAdapter<String> adapterJenisKendaraanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add);

        // Declare request queue
        queue = Volley.newRequestQueue(this);
        etLamaPengerjaan = findViewById(R.id.etLama_Pengerjaan);
        etJenisService = findViewById(R.id.etJenis_Service);
        edJenisKendaraan = findViewById(R.id.etJenis_Kendaraan);
        layoutLoading = findViewById(R.id.layout_loading);

        adapterJenisKendaraanList = new ArrayAdapter<String>(this, R.layout.list_jenis_kendaraan,jenisKendaraanList);
        edJenisKendaraan.setAdapter(adapterJenisKendaraanList);

        Button btnCancel = findViewById(R.id.btn_Cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnSave = findViewById(R.id.btn_Save);

        long id = getIntent().getLongExtra("id", -1);
        if (id == -1) {
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createService();
                }
            });
        } else {
            getServiceById(id);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateService(id);
                }
            });
        }
    }
    private void getServiceById(long id) {
        setLoading(true);
        StringRequest stringRequest = new StringRequest(GET, ServiceApi.GET_BY_ID_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                ServiceResponse serviceResponse = gson.fromJson(response, ServiceResponse.class);
                Service service = serviceResponse.getServiceList().get(0);

                etJenisService.setText(service.getJenis_service());
                etLamaPengerjaan.setText(service.getLama_pengerjaan());
                edJenisKendaraan.setText(service.getJenis_kendaraan(), false);

                Toast.makeText(EditAddActivity.this, serviceResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditAddActivity.this, errors.getString("message"),
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(EditAddActivity.this, e.getMessage(),
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

    private void createService() {
        setLoading(true);
        Service service = new Service(
                etJenisService.getText().toString(),
                etLamaPengerjaan.getText().toString(),
                edJenisKendaraan.getText().toString());

        StringRequest stringRequest = new StringRequest(POST, ServiceApi.ADD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                         /* Deserialiasai data dari response JSON dari API
                         menjadi java object MahasiswaResponse menggunakan Gson */
                        ServiceResponse serviceResponse = gson.fromJson(response, ServiceResponse.class);
                        Toast.makeText(EditAddActivity.this, serviceResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditAddActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(EditAddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            // Adding body request which is object service
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                 /* Serialization of data from java object StudentResponse
                    to JSON string using Gson */
                String requestBody = gson.toJson(service);
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

    private void updateService(long id) {
        setLoading(true);
        Service service = new Service(
                etJenisService.getText().toString(),
                etLamaPengerjaan.getText().toString(),
                edJenisKendaraan.getText().toString());

        StringRequest stringRequest = new StringRequest(POST,
                ServiceApi.UPDATE_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                ServiceResponse serviceResponse = gson.fromJson(response, ServiceResponse.class);
                Toast.makeText(EditAddActivity.this, serviceResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditAddActivity.this,
                            errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(EditAddActivity.this, e.getMessage(),
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
            // Adding body request which is object service
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();

                String requestBody = gson.toJson(service);
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