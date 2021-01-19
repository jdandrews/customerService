package org.jrandrews.liveperson.customers.servicecase.domain;

public class ServiceCase {
    private long id;                                    // consider UUID here
    private long customerId;                            // consider a list here
    private String placeholderForRealServiceInfo;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getInfo() {
        return placeholderForRealServiceInfo;
    }

    public void setInfo(String serviceInfo) {
        this.placeholderForRealServiceInfo = serviceInfo;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
