package com.cardy.design.entity;

public class InventoryTest{
    public static int TYPE_MATERIAL = 0;
    public static int TYPE_PRODUCT = 1;

    String name;
    String model;
    String state;
    int hostCount;
    int deliveryCount;
    String area;
    int type;

    public InventoryTest(String name, String model, String state, int hostCount, int deliveryCount, String area, int type) {
        this.name = name;
        this.model = model;
        this.state = state;
        this.hostCount = hostCount;
        this.deliveryCount = deliveryCount;
        this.area = area;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getHostCount() {
        return hostCount;
    }

    public void setHostCount(int hostCount) {
        this.hostCount = hostCount;
    }

    public int getDeliveryCount() {
        return deliveryCount;
    }

    public void setDeliveryCount(int deliveryCount) {
        this.deliveryCount = deliveryCount;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
