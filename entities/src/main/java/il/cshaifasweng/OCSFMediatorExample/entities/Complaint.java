package il.cshaifasweng.OCSFMediatorExample.entities;

import java.util.Date;

public class Complaint {
    String info;
    Date creationDate;
    ABS_Purchase purchase;
    boolean isClosed;
    int id;
    CustomerService customerService;

    public Complaint(String info, Date creationDate, ABS_Purchase purchase, boolean isClosed, int id, CustomerService customerService) {
        this.info = info;
        this.creationDate = creationDate;
        this.purchase = purchase;
        this.isClosed = isClosed;
        this.id = id;
        this.customerService = customerService;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public ABS_Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(ABS_Purchase purchase) {
        this.purchase = purchase;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

}
