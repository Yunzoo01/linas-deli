package com.linasdeli.api.dto;

import com.linasdeli.api.domain.Supplier;
import lombok.Getter;

@Getter
public class SupplierDTO {
    private Integer sid;
    private String supplierName;
    private String contactName;
    private String sEmail;
    private String sPhone;
    private String note;

    public SupplierDTO() {
    }

    public SupplierDTO(Integer sid, String contactName, String sEmail, String sPhone, String note) {
        this.sid = sid;
        this.contactName = contactName;
        this.sEmail = sEmail;
        this.sPhone = sPhone;
        this.note = note;
    }

    public SupplierDTO(Supplier supplier) {
        this.sid = supplier.getSid();
        this.supplierName = supplier.getSupplierName();
        this.contactName = supplier.getContactName();
        this.sEmail = supplier.getSEmail();
        this.sPhone = supplier.getSPhone();
        this.note = supplier.getNote();
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsPhone() {
        return sPhone;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
