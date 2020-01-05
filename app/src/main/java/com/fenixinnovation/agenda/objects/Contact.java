package com.fenixinnovation.agenda.objects;

import java.io.Serializable;
import java.util.Objects;

public class Contact implements Serializable {

    private int key;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String created;

    public Contact() {
    }

    public Contact(int key, String name, String surname, String phone, String email, String created) {
        this.key = key;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.created = created;
    }


    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return key == contact.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
