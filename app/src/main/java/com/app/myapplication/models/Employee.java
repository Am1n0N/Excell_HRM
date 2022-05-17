package com.app.myapplication.models;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Employee {

    @SerializedName("NIN")
    @Expose
    private String nin;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("dismissal_date")
    @Expose
    private Object dismissalDate;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("hiring_date")
    @Expose
    private String hiringDate;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("num_emp")
    @Expose
    private Integer numEmp;
    @SerializedName("num_ptoreq")
    @Expose
    private Integer numPtoreq;
    @SerializedName("num_recordreq")
    @Expose
    private Integer numRecordreq;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("position")
    @Expose
    private String position;



    public String getNin() {
        return nin;
    }

    public void setNin(String nin) {
        this.nin = nin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Object getDismissalDate() {
        return dismissalDate;
    }

    public void setDismissalDate(Object dismissalDate) {
        this.dismissalDate = dismissalDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHiringDate() {
        return hiringDate;
    }

    public void setHiringDate(String hiringDate) {
        this.hiringDate = hiringDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumEmp() {
        return numEmp;
    }

    public void setNumEmp(Integer numEmp) {
        this.numEmp = numEmp;
    }

    public Integer getNumPtoreq() {
        return numPtoreq;
    }

    public void setNumPtoreq(Integer numPtoreq) {
        this.numPtoreq = numPtoreq;
    }

    public Integer getNumRecordreq() {
        return numRecordreq;
    }

    public void setNumRecordreq(Integer numRecordreq) {
        this.numRecordreq = numRecordreq;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }

}