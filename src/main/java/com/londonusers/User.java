package com.londonusers;

public class User
{
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String ip_address;
    private double latitude;
    private double longitude;

    public User() { }


    public int getId()
    {
        return id;
    }

    public String getFirst_name()
    {
        return first_name;
    }

    public String getLast_name()
    {
        return last_name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getIp_address()
    {
        return ip_address;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }
}
