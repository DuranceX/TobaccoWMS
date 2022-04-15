package com.cardy.design.entity;

public class CustomerTest {
    private String name;
    private String address;
    private String[] mainPurchase;

    public CustomerTest() {
    }

    public CustomerTest(String name, String address, String[] mainPurchase) {
        this.name = name;
        this.address = address;
        this.mainPurchase = mainPurchase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String[] getMainPurchase() {
        return mainPurchase;
    }

    public void setMainPurchase(String[] mainPurchase) {
        this.mainPurchase = mainPurchase;
    }
}
