package com.agilehackathon.model;

public class Practice {
    private String name;
    private int id;
    private String address;
    private String location;

    public Practice(String name, int id, String address, String location) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.location = location;
    }


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }

}
