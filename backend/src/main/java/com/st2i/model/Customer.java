package com.st2i.model;


import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;
@Entity
public class Customer {
    @Id
    @SequenceGenerator(
            name = "customer_id_sequence",
            sequenceName = "customer_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_sequence"
    )
    private Integer id ;
    private String name ;
    private String village;
    private Integer age ;
    private Integer numberOfChildren ;
    private byte[] fingerprint ;

    public Customer(Integer id, String name, String village, Integer age, Integer numberOfChildren, byte[] fingerprint) {
        this.id = id;
        this.name = name;
        this.village = village;
        this.age = age;
        this.numberOfChildren = numberOfChildren ;
        this.fingerprint = fingerprint ;

    }

    public Customer() {
    }

    public void setFingerprint(byte[] fingerprint) {
        this.fingerprint = fingerprint;
    }

    public byte[] getFingerprint() {
        return fingerprint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(village, customer.village) && Objects.equals(age, customer.age) && Objects.equals(numberOfChildren, customer.numberOfChildren) && Arrays.equals(fingerprint, customer.fingerprint);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, village, age, numberOfChildren);
        result = 31 * result + Arrays.hashCode(fingerprint);
        return result;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNumberOfChildren(Integer numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public Integer getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVillage(String email) {
        this.village = email;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVillage() {
        return village;
    }

    public Integer getAge() {
        return age;
    }
}
