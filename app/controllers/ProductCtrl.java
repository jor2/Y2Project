package controllers;

import controllers.security.CheckIfCustomer;
import controllers.security.Secured;
import play.api.Environment;
import play.mvc.*;
import play.data.*;
import play.db.ebean.Transactional;

import java.util.List;
import javax.inject.Inject;

import views.html.*;

import models.users.*;
import models.products.*;


public class ProductCtrl extends Controller {

    private FormFactory formFactory;

    private play.api.Environment env;

    @Inject
    public ProductCtrl(Environment e, FormFactory f) {
        this.env = e;
        this.formFactory = f;
    }

    @Transactional
    public User getCurrentUser() {
        User u = User.getLoggedIn(session().get("email"));
        return u;
    }

    public Result genre(){
        String name = "Genre";
        return ok(genre.render("",(Customer) getCurrentUser(), name));
    }

    public Result action() {
        String name = "Action";
        Long cat = new Long("1");
        List<Product> productsList = Category.find.ref(cat).getProducts();

        return ok(action.render(env, productsList, "",(Customer)  getCurrentUser(), name));
    }

    public Result index() {
        String name = "Best Sellers";
        List<Product> productsList = Product.bestSellers();

        return ok(index.render(env, productsList, "",(Customer)  getCurrentUser(), name));
    }

    public Result early() {
        String name = "Early Access";
        Long cat = new Long("2");
        List<Product> productsList = Category.find.ref(cat).getProducts();

        return ok(early.render(env, productsList, "",(Customer) getCurrentUser(), name));
    }

    public Result explore() {
        String name = "Exploration";
        Long cat = new Long("3");
        List<Product> productsList = Category.find.ref(cat).getProducts();

        return ok(exploration.render(env, productsList, "",(Customer) getCurrentUser(), name));
    }

    public Result fps() {
        String name = "First Person Shooter";
        Long cat = new Long("4");
        List<Product> productsList = Category.find.ref(cat).getProducts();

        return ok(fps.render(env, productsList, "",(Customer) getCurrentUser(), name));
    }

    public Result openWorld() {
        String name = "Open World";
        Long cat = new Long("5");
        List<Product> productsList = Category.find.ref(cat).getProducts();

        return ok(openworld.render(env, productsList, "",(Customer) getCurrentUser(), name));
    }

    public Result rpg() {
        String name = "Role Playing Game";
        Long cat = new Long("6");
        List<Product> productsList = Category.find.ref(cat).getProducts();

        return ok(rpg.render(env, productsList, "",(Customer) getCurrentUser(), name));
    }

    public Result simulation() {
        String name = "Simulation";
        Long cat = new Long("7");
        List<Product> productsList = Category.find.ref(cat).getProducts();

        return ok(simulation.render(env, productsList, "",(Customer) getCurrentUser(), name));
    }

    public Result sports() {
        String name = "Sports";
        Long cat = new Long("8");
        List<Product> productsList = Category.find.ref(cat).getProducts();

        return ok(sports.render(env, productsList, "",(Customer) getCurrentUser(), name));
    }

    public Result strategy() {
        String name = "Strategy";
        Long cat = new Long("9");
        List<Product> productsList = Category.find.ref(cat).getProducts();

        return ok(strategy.render(env, productsList, "",(Customer) getCurrentUser(), name));
    }

    public Result deals() {
        String name = "Deals";
        Long cat = new Long("10");
        List<Product> productsList = Category.find.ref(cat).getProducts();

        return ok(deals.render(env, productsList, "",(Customer) getCurrentUser(), name));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @With(CheckIfCustomer.class)
    public Result addReview() {
        String name = "Add a review";
        Form<Review> addReviewForm = formFactory.form(Review.class);

        return ok(review.render(addReviewForm,(Customer) getCurrentUser(), name));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @With(CheckIfCustomer.class)
    public Result addReviewSubmit() {

        Form<Review> newReviewForm = formFactory.form(Review.class).bindFromRequest();
        String name = "Add a review";

        if(newReviewForm.hasErrors()) {
            return badRequest(review.render(newReviewForm,(Customer) getCurrentUser(), name));
        }

        Review newReview = newReviewForm.get();

        newReview.setCustomer((Customer) getCurrentUser());
        newReview.save();

        newReview.update();

        flash("success", "Review has been published!");

        return redirect(controllers.routes.ProductCtrl.index());
    }

    @Transactional
    public Result addMessage() {
        String name = "Contact Us";
        Form<ContactUs> addMessageForm = formFactory.form(ContactUs.class);

        return ok(contactus.render(addMessageForm,(Customer) getCurrentUser(), name));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @With(CheckIfCustomer.class)
    public Result addMessageSubmit() {

        Form<ContactUs> newMessageForm = formFactory.form(ContactUs.class).bindFromRequest();
        String name = "Error!";

        if(newMessageForm.hasErrors()) {
            return badRequest(contactus.render(newMessageForm,(Customer) getCurrentUser(), name));
        }

        ContactUs newMessage = newMessageForm.get();

        newMessage.setCustomer((Customer) getCurrentUser());
        newMessage.setAdministrator((Administrator) Administrator.find.byId("admin@products.com"));
        newMessage.save();

        newMessage.update();

        flash("success", "Issue has been sent!");

        return redirect(controllers.routes.ProductCtrl.index());
    }

    public Result getReviews(Long id) {
        String name = "Review Results";
        Product p = Product.find.byId(id);

        return ok(reviewResults.render(env, p, p.getReviews(),(Customer) getCurrentUser(), name));
    }

    @Transactional
    public Result addUser() {
        String name = "Register";
        Form<Customer> addUserForm = formFactory.form(Customer.class);
        return ok(register.render(addUserForm,(Customer) getCurrentUser(), name));
    }

    @Transactional
    public Result addUserSubmit() {

        Form<Customer> newUserForm = formFactory.form(Customer.class).bindFromRequest();

        if(newUserForm.hasErrors()) {
            String name = "Register";

            return badRequest(register.render(newUserForm,(Customer) getCurrentUser(), name));
        }

        Customer newUser = newUserForm.get();

        newUser.save();

        newUser.update();

        flash("success", "User " + newUser.getName() + " has been created!");

        return redirect(controllers.routes.ProductCtrl.index());
    }

    @Transactional
    public Result editAddress() {
        String name = "Edit Address";
        Form<EditAddress> editAddressForm = formFactory.form(EditAddress.class);

        return ok(editAddress.render(editAddressForm,(Customer) getCurrentUser(), name));
    }

    @Transactional
    public Result editAddressSubmit() {

        Customer c = (Customer) getCurrentUser();

        Form<EditAddress> editAddressForm = formFactory.form(EditAddress.class).bindFromRequest();

        if(editAddressForm.hasErrors()) {
            String name = "Error!";

            return badRequest(editAddress.render(editAddressForm,(Customer) getCurrentUser(), name));
        }

        EditAddress editAddress = editAddressForm.get();

        editAddress.updateAddress(c);

        flash("success", "User " + c.getName() + " address has been updated!");

        return redirect(controllers.routes.ProductCtrl.index());
    }

    @Transactional
    public Result listProducts(Long cat, String filter) {

        List<Category> categories = Category.findAll();
        List<Product> products;

        if (cat == 0) {
            products = Product.findAll(filter);
        }
        else {
            products = Product.findFilter(cat, filter);
        }

        return ok(listProducts.render(env, categories, products, cat, filter,(Customer) getCurrentUser()));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @With(CheckIfCustomer.class)
    public Result creditCard() {

        String name = "Add Credit Card";

        Form<CreditCard> addCreditCardForm = formFactory.form(CreditCard.class);

        return ok(creditCard.render(addCreditCardForm,(Customer) getCurrentUser(), name));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    @With(CheckIfCustomer.class)
    public Result addCreditCardSubmit() {

        String name = "Add Credit Card";

        Customer c = (Customer) getCurrentUser();

        Form<CreditCard> newCreditCardForm = formFactory.form(CreditCard.class).bindFromRequest();

        if(newCreditCardForm.hasErrors()) {

            return badRequest(creditCard.render(newCreditCardForm,(Customer) getCurrentUser(), name));
        }

        CreditCard newCreditCard = newCreditCardForm.get();

        if(CreditCardValidator.Check(newCreditCard.getNum())!=false) {

            if(c.getCard()!=null){
                c.getCard().delete();
                c.update();
            }

            newCreditCard.setCustomer((Customer) getCurrentUser());

            newCreditCard.save();

            newCreditCard.update();

            flash("success", "Credit Card has been added to your account!");

            return redirect(controllers.routes.ProductCtrl.index());
        } else{
            name = "Error with card number";

            return badRequest(creditCard.render(newCreditCardForm,(Customer) getCurrentUser(), name));
        }
    }

    @Transactional
    public Result searchResult(Long cat, String filter) {
        String name="Search Results";

        List<Product> products =  Product.findAll(filter);

        return ok(productSearch.render(env, products, cat, filter,(Customer) getCurrentUser(), name));
    }
}