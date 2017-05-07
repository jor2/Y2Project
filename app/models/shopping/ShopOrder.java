package models.shopping;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;
import java.util.Date;

import com.avaje.ebean.*;

import models.users.*;

// ShopOrder entity managed by Ebean
@Entity
public class ShopOrder extends Model {

    @Id
    private Long id;
    
    private Date OrderDate;
    
    @OneToMany(mappedBy="order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
    
    @ManyToOne
    private Customer customer;

    @Transient
    public static int count=0;

    private double orderTotal;

    // Default constructor
    public ShopOrder() {
        OrderDate = new Date();
    }
    
    public double calculateOrderTotal() {

        orderTotal = 0;
        for (OrderItem i : items) {
            orderTotal += i.getItemTotal();
        }

        return orderTotal;
    }
	
	//Generic query helper
    public static Finder<Long,ShopOrder> find = new Finder<Long,ShopOrder>(ShopOrder.class);

    //Find all Products in the database
    public static List<ShopOrder> findAll() {
        return ShopOrder.find.all();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return OrderDate;
    }

    public String getFormattedDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date = sdf.format(getOrderDate());
        return date;
    }

    public void setOrderDate(Date orderDate) {
        OrderDate = orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        count++;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public static int getCount() {
        return ShopOrder.count;
    }

    public static void setCount(int count) {
        ShopOrder.count = count;
    }

    public Calendar getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c1 = Calendar.getInstance();
        Date date = new Date();
        c1.setTime(date);
        return c1;
    }

    public Calendar getOrderExpiryDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c2 = Calendar.getInstance();
        c2.setTime(getOrderDate());
        c2.add(Calendar.DATE, 2);
        return c2;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }
}

