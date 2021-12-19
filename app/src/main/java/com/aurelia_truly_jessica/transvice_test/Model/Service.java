package com.aurelia_truly_jessica.transvice_test.Model;

public class Service {
    private Long id;
    private String jenis_service;
    private String lama_pengerjaan;
    private String jenis_kendaraan;

    public Service(String jenis_service, String lama_pengerjaan, String jenis_kendaraan) {
        this.jenis_service = jenis_service;
        this.lama_pengerjaan = lama_pengerjaan;
        this.jenis_kendaraan = jenis_kendaraan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJenis_service() {
        return jenis_service;
    }

    public void setJenis_service(String jenis_service) {
        this.jenis_service = jenis_service;
    }

    public String getLama_pengerjaan() {
        return lama_pengerjaan;
    }

    public void setLama_pengerjaan(String lama_pengerjaan) {
        this.lama_pengerjaan = lama_pengerjaan;
    }

    public String getJenis_kendaraan() {
        return jenis_kendaraan;
    }

    public void setJenis_kendaraan(String jenis_kendaraan) {
        this.jenis_kendaraan = jenis_kendaraan;
    }
}
