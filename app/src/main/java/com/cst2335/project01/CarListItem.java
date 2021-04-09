package com.cst2335.project01;

public class CarListItem {
    private String make, name;
    private int modelId;
    public CarListItem(String n, String m, int i)
    {
        name =n;
        make = m;
        modelId = i;
    }

    public String getMake() {
        return  make;
    }

    public String getName() {
        return name;
    }

    public int getModelId() {
        return modelId;
    }
}
