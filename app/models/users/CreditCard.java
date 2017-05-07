package models.users;

import com.avaje.ebean.Model;
import models.shopping.Basket;
import models.shopping.ShopOrder;

import javax.persistence.*;
import java.util.List;

import play.data.validation.*;

@Entity
// Customer inherits from the User class
public class CreditCard extends Model {

    @Id
    private Long id;

    @OneToOne
    private Customer customer;

    @Constraints.Required
    private String type;

    @Constraints.Required
    private String num;

    @Constraints.Required
    private String expDate;

    public CreditCard(String type, String num, String expDate)
    {
        this.type = type;
        this.num = num;
        this.expDate = expDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}