package com.aurelia_truly_jessica.transvice_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aurelia_truly_jessica.transvice_test.databinding.ActivityDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        binding.setActivity(this);
    }

    public View.OnClickListener btnLogout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            Toast.makeText(DashboardActivity.this, "Bye", Toast.LENGTH_SHORT).show();
        }
    };

    public View.OnClickListener btnImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
        }
    };

    public View.OnClickListener btnOwnerInfo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent dashboardActivity = new Intent(DashboardActivity.this, DashboardActivity.class);
            startActivity(dashboardActivity);
        }
    };

    public View.OnClickListener btnServices = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent dashboardActivity = new Intent(DashboardActivity.this, ServiceActivity.class);
            startActivity(dashboardActivity);
        }
    };

    public View.OnClickListener btnBooking = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent dashboardActivity = new Intent(DashboardActivity.this, BookingActivity.class);
            startActivity(dashboardActivity);
        }
    };

    public View.OnClickListener btnGarageInfo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent dashboardActivity = new Intent(DashboardActivity.this, GarageInfoActivity.class);
            startActivity(dashboardActivity);
        }
    };
}