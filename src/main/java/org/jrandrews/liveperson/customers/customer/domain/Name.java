package org.jrandrews.liveperson.customers.customer.domain;

public class Name {
    // this structure will depend on what the UI actually needs
    private String name;

    public Name(String completeName) {
        this.name = completeName;
    }
    
    public Name() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
