package com.kuraps.tiketsaya;

public class MyTicket {

    String wisata_name,wisata_location,ticket_total,wisata_icon,id_ticket,date_buy,time_buy,date_wisata,expired_time,pesawat,total_harga;

    public MyTicket() {
    }

    public MyTicket(String wisata_name, String wisata_location, String ticket_total,String wisata_icon,String id_ticket
    ,String date_buy, String time_buy, String date_wisata, String expired_time, String pesawat, String total_harga) {
        this.wisata_name = wisata_name;
        this.wisata_location = wisata_location;
        this.ticket_total = ticket_total;
        this.wisata_icon = wisata_icon;
        this.id_ticket = id_ticket;
        this.date_buy = date_buy;
        this.time_buy = time_buy;
        this.date_wisata = date_wisata;
        this.expired_time = expired_time;
        this.pesawat = pesawat;
        this.total_harga = total_harga;
    }

    public String getWisata_name() {
        return wisata_name;
    }

    public void setWisata_name(String wisata_name) {
        this.wisata_name = wisata_name;
    }

    public String getWisata_location() {
        return wisata_location;
    }

    public void setWisata_location(String wisata_location) {
        this.wisata_location = wisata_location;
    }

    public String getId_ticket() {
        return id_ticket;
    }

    public void setId_ticket(String id_ticket) { this.id_ticket = id_ticket; }

    public String getTicket_total() {
        return ticket_total;
    }

    public void setTicket_total(String ticket_total) {
        this.ticket_total = ticket_total;
    }

    public String getWisata_icon() {
        return wisata_icon;
    }
    public void setWisata_icon(String wisata_icon) {
        this.wisata_icon = wisata_icon;
    }

    public String getTotal_harga() {
        return total_harga;
    }
    public void setTotal_harga(String total_harga) { this.total_harga = total_harga; }
    public String getDate_buy() {
        return date_buy;
    }
    public void setDate_buy(String date_buy) { this.date_buy = date_buy; }
    public String getTime_buy() {
        return time_buy;
    }
    public void setTime_buy(String time_buy) { this.time_buy = time_buy; }
    public String getDate_wisata() {
        return date_wisata;
    }
    public void setDate_wisata(String date_wisata) { this.date_wisata = date_wisata; }
    public String getExpired_time() {
        return expired_time;
    }
    public void setExpired_time(String expired_time) { this.expired_time = expired_time; }
    public String getPesawat() {
        return pesawat;
    }
    public void setPesawat(String pesawat) { this.pesawat = pesawat; }
}


