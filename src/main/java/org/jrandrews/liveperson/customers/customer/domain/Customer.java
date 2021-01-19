package org.jrandrews.liveperson.customers.customer.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {
    private long id = -1;		// this should probably be a UUID
    private String ssn;
    private Name name;
    private List<ContactInfo> contactInfoList = new ArrayList<>();

    public Customer() {}

    public Customer(Customer customer) {
    	id = customer.id;
        ssn = customer.ssn;
        name = new Name(customer.name.getName());
        // TODO: build copy constructors for ServiceCase and ContactInfo
        contactInfoList = new ArrayList<>(customer.contactInfoList);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public List<ContactInfo> getContactInfoList() {
        return Collections.unmodifiableList(contactInfoList);
    }

    public void setContactInfoList(List<ContactInfo> contactInfoList) {
        this.contactInfoList = contactInfoList;
    }

    public void addContactInfo(ContactInfo contactInfo) {
        this.contactInfoList.add(contactInfo);
    }

    public static Customer mask(Customer customer) {
        Customer masked = new Customer(customer);
        masked.ssn = customer.ssn == null ? null : maskSsn(customer.ssn);
        return masked;
    }

    private static String maskSsn(String ssn2) {
        // TODO: return something clever, like verifying the format and exposing just the last 4 digits
        return "***-**-****";
    }
}
