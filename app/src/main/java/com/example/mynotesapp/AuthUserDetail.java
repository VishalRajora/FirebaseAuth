package com.example.mynotesapp;

public class AuthUserDetail {
    public String fullname, age, email, phone;

    public AuthUserDetail() {
    }

    public AuthUserDetail(String fullname , String age , String email , String phone) {
        this.fullname = fullname;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
