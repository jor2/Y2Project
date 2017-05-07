package models.products;

import java.util.*;
import javax.persistence.*;

import play.data.validation.*;

import com.avaje.ebean.*;

// Product entity managed by Ebean
@Entity
public class Product extends Model {

    @Id
    private Long id;

    // many to many mapping
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "products")
    private List<Category> categories = new ArrayList<Category>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews;
    
    // List of category ids - this will be bound to checkboxes in the view form
    private List<Long> catSelect = new ArrayList<Long>();

    @Constraints.Required
    private String name;

    @Column(length = 65535, columnDefinition="Text")
    @Constraints.Required
    private String description;

    @Constraints.Required
    private int	stock;

    @Constraints.Required
    private double price;

    private String trailer=null;

    private Integer copiesSold = 0;

    // Default constructor
    public Product() {
    }

    // Constructor to initialise object
    public Product(Long id, String name, String description, int stock, double price){
        this.id = id;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
    }
	
	//Generic query helper for entity Computer with id Long
    public static Finder<Long,Product> find = new Finder<Long,Product>(Product.class);

    // Find all Products in the database
    // Filter product name 
    public static List<Product> findAll(String filter) {
        return Product.find.where()
                        // name like filter value (surrounded by wildcards)
                        .ilike("name", "%" + filter + "%")
                        .orderBy("name asc")
                        .findList();
    }
    
    // Find all Products for a category
    // Filter product name 
    public static List<Product> findFilter(Long catID, String filter) {
        return Product.find.where()
                        // Only include products from the matching cat ID
                        // In this case search the ManyToMany relation
                        .eq("categories.id", catID)
                        // name like filter value (surrounded by wildcards)
                        .ilike("name", "%" + filter + "%")
                        .orderBy("name asc")
                        .findList();
    }

    // Generate options for an HTML select control
    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Product p: Product.find.orderBy("name").findList()) {
            options.put(p.id.toString(), p.name);
        }
        return options;
    }

    public static List<Product> bestSellers(){
        List<Product> temp = Product.findAll("");

        Collections.sort(temp, (p1, p2) -> Integer.compare(p1.getCopiesSold(), p2.getCopiesSold()));

        List<Product> bestSellers = new ArrayList<>();
        for(int i=temp.size()-1; i>temp.size()-6; i--){
            bestSellers.add(temp.get(i));
        }
        return bestSellers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Long> getCatSelect() {
        return catSelect;
    }

    public void setCatSelect(List<Long> catSelect) {
        this.catSelect = catSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Integer getCopiesSold() {
        if(copiesSold==null){
            copiesSold = 0;
        }
        return copiesSold;
    }

    public void setCopiesSold(Integer copiesSold) {
        this.copiesSold = copiesSold;
    }
}