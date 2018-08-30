package com.example.sudhakar.vivaregistration;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sudhakar on 12/19/2017.
 */

public class Student implements Parcelable{
    String branch;
    String year;
    String rollNo;
    String name;
    String college;
    String mobile;
    public  Student(){}
    public Student(String branch, String year, String rollNo, String name, String college, String mobile) {
        this.branch = branch;
        this.year = year;
        this.rollNo = rollNo;
        this.name = name;
        this.college = college;
        this.mobile = mobile;
    }
    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(branch);
        parcel.writeString(year);
        parcel.writeString(rollNo);
        parcel.writeString(name);
        parcel.writeString(mobile);
        parcel.writeString(college);

    }
    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {

        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
    public Student(Parcel in){
        this.branch=in.readString();
        this.year=in.readString();
        this.rollNo=in.readString();
        this.name=in.readString();
        this.college=in.readString();
        this.mobile=in.readString();

    }
    public String toString(){
        return "\nName : "+name+" \nRoll No : "+rollNo+" \nYear:"+year+"\nBranch:"+branch+" \nCollege:"+college+" \nMobile No:"+mobile+"\n\n";
    }
}
