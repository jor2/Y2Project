package models.users;

import models.products.Review;
import models.shopping.Basket;
import models.shopping.ShopOrder;

import javax.persistence.*;
import java.util.List;

import play.data.validation.*;


@Entity
// This is a Customer of type admin
@DiscriminatorValue("customer")
// Customer inherits from the User class
public class Customer extends User {
	
    @Constraints.Required
	private String street1;

	private String street2;

    @Constraints.Required
    private String town;

    @Constraints.Required
    private String postCode;
    
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Basket basket;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private CreditCard card;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<ShopOrder> orders;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<ContactUs> messages;

    private Integer points = 0;
	
	public Customer(String email, String role, String name, String password, String street1, String street2, String town, String postCode)
	{
		super(email, role, name, password);
        this.street1 = street1;
        this.street2 = street2;
        this.town = town;
        this.postCode = postCode;
	}

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public List<ShopOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ShopOrder> orders) {
        this.orders = orders;
    }

    public CreditCard getCard() {
        return card;
    }

    public void setCard(CreditCard card) {
        this.card = card;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public List<ContactUs> getMessages() {
        return messages;
    }

    public void setMessages(List<ContactUs> messages) {
        this.messages = messages;
    }
}