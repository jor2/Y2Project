package models.users;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import play.data.validation.*;

@Entity
public class ContactUs extends Model{

    @Id
    private Long id;

    @ManyToOne
    private Administrator administrator;

    @ManyToOne
    private Customer customer;

    @Column(length = 65535, columnDefinition="Text")
    @Constraints.Required
    private String message;

    private Date messageDate;

    public ContactUs() {
        messageDate = new Date();
    }

    public static Finder<Long,ContactUs> find = new Finder<Long,ContactUs>(ContactUs.class);

    public static List<ContactUs> findAll(String filter) {
        return ContactUs.find.where()
                // name like filter value (surrounded by wildcards)
                .ilike("name", "%" + filter + "%")
                .orderBy("name asc")
                .findList();
    }

    // Generate options for an HTML select control
    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        options.put("1", "Where's my stuff?");
        options.put("2", "Problem with an order.");
        options.put("3", "Returns and refunds");
        options.put("4", "Payment issues.");
        options.put("5", "Change an order.");
        options.put("6", "Promotion issues.");
        options.put("7", "Different Order Issue.");
        return options;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }
}