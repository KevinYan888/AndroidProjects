package com.cst2335.project01;

public class CarListItem {
    private String make, name;
    private Long id;
    public CarListItem(String n, String m, Long i)
    {
        name =n;
        make = m;
        id = i;
    }

    public String getMake() {
        return  make;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
