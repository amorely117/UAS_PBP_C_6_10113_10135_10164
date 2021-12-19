package com.aurelia_truly_jessica.transvice_test.Model;

public class Booking {
    private Long id;
    private String tgl_service;
    private String waktu_service;
    private String jenis_service;
    private String jenis_kendaraan;

    public Booking(String tgl_service, String jam_service, String jenis_service, String jenis_kendaraan) {
        this.tgl_service = tgl_service;
        this.waktu_service = jam_service;
        this.jenis_service = jenis_service;
        this.jenis_kendaraan = jenis_kendaraan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTgl_service() {
        return tgl_service;
    }

    public void setTgl_service(String tgl_service) {
        this.tgl_service = tgl_service;
    }

    public String getWaktu_service() {
        return waktu_service;
    }

    public void setWaktu_service(String waktu_service) {
        this.waktu_service = waktu_service;
    }

    public String getJenis_service() {
        return jenis_service;
    }

    public void setJenis_service(String jenis_service) {
        this.jenis_service = jenis_service;
    }

    public String getJenis_kendaraan() {
        return jenis_kendaraan;
    }

    public void setJenis_kendaraan(String jenis_kendaraan) {
        this.jenis_kendaraan = jenis_kendaraan;
    }
}
