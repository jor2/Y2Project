package models.products;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import models.users.Customer;
import play.data.validation.*;

@Entity
public class Review extends Model{

    @Id
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Customer customer;

    @Column(length = 65535, columnDefinition="Text")
    @Constraints.Required
    private String review;

    private Date reviewDate;

    public Review() {
        reviewDate = new Date();
    }

    public static Finder<Long,Review> find = new Finder<Long,Review>(Review.class);

    public static List<Review> findAll(String filter) {
        return Review.find.where()
                // name like filter value (surrounded by wildcards)
                .ilike("name", "%" + filter + "%")
                .orderBy("name asc")
                .findList();
    }

    public static List<Product> findFilter(Long id, String filter) {
        return Product.find.where()
                // Only include products from the matching cat ID
                // In this case search the ManyToMany relation
                .eq("review.id", id)
                // name like filter value (surrounded by wildcards)
                .ilike("name", "%" + filter + "%")
                .orderBy("name asc")
                .findList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
}