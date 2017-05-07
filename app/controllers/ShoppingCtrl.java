package controllers;

import controllers.security.CheckIfCustomer;
import controllers.security.Secured;
import models.products.Product;
import models.shopping.Basket;
import models.shopping.OrderItem;
import models.shopping.ShopOrder;
import models.users.Customer;
import models.users.User;
import play.db.ebean.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.With;
import views.html.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Security.Authenticated(Secured.class)
@With(CheckIfCustomer.class)
public class ShoppingCtrl extends Controller {

    private Customer getCurrentUser() {
        return (Customer)User.getLoggedIn(session().get("email"));
    }

    private double basketTotal;

    private double newTotal;

    private double discount;

    @Transactional
    public Result emptyBasket() {

        Customer c = getCurrentUser();

        c.getBasket().removeAllItems();
        c.getBasket().update();

        return ok(basket.render(c, "Basket Emptied"));
    }

    @Transactional
    public Result viewOrder(long id) {

        ShopOrder order = ShopOrder.find.byId(id);

        return ok(orderConfirmed.render(getCurrentUser(), order));
    }

    @Transactional
    public Result addToBasket(Long id) {

        Product p = Product.find.byId(id);

        Customer customer = (Customer)User.getLoggedIn(session().get("email"));

        if (customer.getBasket() == null) {
            customer.setBasket(new Basket());
            customer.getBasket().setCustomer(customer);
            customer.update();
        }

        customer.getBasket().addProduct(p);
        customer.update();

        basketTotal = getCurrentUser().getBasket().calculateBasketTotal();
        getCurrentUser().getBasket().setBasketTotal(basketTotal-discount);

        return ok(basket.render(customer, "Item Added"));
    }

    @Transactional
    public Result showBasket() {
        if(getCurrentUser().getBasket()!=null) {
            if(newTotal==0.001){
                getCurrentUser().getBasket().setBasketTotal(newTotal);
            } else {
                basketTotal = getCurrentUser().getBasket().calculateBasketTotal();
                getCurrentUser().getBasket().setBasketTotal(basketTotal - discount);
            }
            return ok(basket.render(getCurrentUser(), "Basket"));
        } else{
            flash("failure", "You must first add an item to the basket!");
            return redirect(routes.ProductCtrl.index());
        }
    }

    @Transactional
    public Result addOne(Long itemId) {

        OrderItem item = OrderItem.find.byId(itemId);

        item.increaseQty();
        item.update();

        return redirect(routes.ShoppingCtrl.showBasket());
    }

    @Transactional
    public Result removeOne(Long itemId) {

        OrderItem item = OrderItem.find.byId(itemId);

        Customer c = getCurrentUser();

        c.getBasket().removeItem(item);
        c.getBasket().update();

        return ok(basket.render(c, "Item Removed"));
    }

    @Transactional
    public Result cancelOrder(Long id){

        ShopOrder order = ShopOrder.find.byId(id);
        Calendar c1 = Calendar.getInstance();
        Date date = new Date();
        c1.setTime(date);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(order.getOrderDate());
        c2.add(Calendar.DATE, 2);

        if(c1.before(c2)) {
            for(int i=0; i<order.getItems().size(); i++){
                order.getItems().get(i).getProduct().setStock(order.getItems().get(i).getProduct().getStock()+order.getItems().get(i).getQuantity());
                order.getItems().get(i).getProduct().setCopiesSold(order.getItems().get(i).getProduct().getCopiesSold()-order.getItems().get(i).getQuantity());
                order.getItems().get(i).getProduct().update();
            }
            order.delete();
            order.update();
        }

        if(getCurrentUser().getOrders().size()>0) {

            List<ShopOrder> orders = getCurrentUser().getOrders();

            return ok(orderHistory.render(getCurrentUser(), orders));
        }else{
            return redirect(routes.ProductCtrl.index());
        }
    }

    @Transactional
    public Result placeOrder() {
        Customer c = getCurrentUser();
        int index=0;
        double points = 0;
        int stock;
        int basketQuantity;
        basketTotal = getCurrentUser().getBasket().calculateBasketTotal();

        if(c.getCard()!=null && c.getStreet1()!=null) {
            if (c.getBasket().getBasketItems().size() > 0) {
                for (int i = 0; i < c.getBasket().getBasketItems().size(); i++) {
                    index = i;
                    stock = c.getBasket().getBasketItems().get(i).getProduct().getStock();
                    basketQuantity = c.getBasket().getBasketItems().get(i).getQuantity();

                    if (stock - basketQuantity >= 0) {
                        ShopOrder order = new ShopOrder();
                        order.setCustomer(c);
                        order.setItems(c.getBasket().getBasketItems());
                        order.save();

                        for (OrderItem oi : order.getItems()) {
                            oi.setOrder(order);
                            oi.setBasket(null);
                            oi.update();
                        }
                        points = order.calculateOrderTotal() * 100;
                        if (newTotal <= 0) {
                            order.setOrderTotal(basketTotal);
                        } else {
                            order.setOrderTotal(basketTotal - discount);
                        }
                        newTotal = 0;
                        basketTotal = 0;
                        discount = 0;
                        order.update();
                        c.getBasket().setBasketItems(null);
                        c.getBasket().setBasketTotal(0);
                        c.getBasket().update();

                        for (int x = 0; x < order.getItems().size(); x++) {
                            if (order.getItems().get(x).getProduct().getStock() - order.getItems().get(x).getQuantity() >= 0) {
                                order.getItems().get(x).getProduct().setStock(order.getItems().get(x).getProduct().getStock() - order.getItems().get(x).getQuantity());
                                if (order.getItems().get(x).getProduct().getCopiesSold() == null) {
                                    order.getItems().get(x).getProduct().setCopiesSold(order.getItems().get(x).getQuantity());
                                } else {
                                    order.getItems().get(x).getProduct().setCopiesSold(order.getItems().get(x).getProduct().getCopiesSold() + order.getItems().get(x).getQuantity());
                                }
                                order.getItems().get(x).getProduct().update();

                                if (c.getPoints() == null) {
                                    c.setPoints((int) points);
                                    c.update();
                                } else {
                                    c.setPoints(c.getPoints() + (int) points);
                                    c.update();
                                }

                            } else {
                                String s = "Not enough stock!";
                                return ok(basket.render(c, s));
                            }
                        }

                        return ok(orderConfirmed.render(c, order));
                    } else {
                        String s = "" + c.getBasket().getBasketItems().get(index).getProduct().getName() + " is out of stock.";
                        c.getBasket().setBasketItems(null);
                        c.getBasket().update();
                        return ok(basket.render(c, s));
                    }
                }
            }
        } else{
            String s = "Update info!";
            return ok(basket.render(c, s));
        }
        c.getBasket().setBasketItems(null);
        c.getBasket().update();
        String s = "Invalid Order!";
        return ok(basket.render(c, s));
    }

    @Transactional
    public Result loyaltyPoints() {
        Customer c = getCurrentUser();
        basketTotal = c.getBasket().calculateBasketTotal()-discount;
        int points = c.getPoints();
        double basketPoints;
        int tempDiscount = points / 1000;

        if(points > 0) {
            basketPoints = basketTotal * 1000;

            if (basketTotal - tempDiscount < 0) {
                newTotal = 0.001;
                c.getBasket().setBasketTotal(newTotal);
                c.getBasket().update();

                c.setPoints(c.getPoints() - (int) basketPoints);
                c.update();

                discount += basketPoints / 1000;
            } else {
                discount += points / 1000;
                newTotal = basketTotal - discount;
                c.getBasket().setBasketTotal(newTotal);
                c.getBasket().update();

                c.setPoints(0);
                c.update();
            }
        } else{
            return redirect(routes.ShoppingCtrl.showBasket());
        }

        return showBasket();
    }

    @Transactional
    public Result getOrders(){
        Customer c = getCurrentUser();

        if(c.getOrders().size()>0) {

            List<ShopOrder> orders = c.getOrders();

            return ok(orderHistory.render(c, orders));
        }else{
            flash("failure", "You must first make an order!");
            return redirect(routes.ProductCtrl.index());
        }
    }
}