package dto;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int ownerId;
    private List<Pizza> pizzas = new ArrayList<Pizza>();

    public Order(int id, int oid, Pizza... piz) {
        this.orderId = id;
        this.ownerId = oid;
        for (Pizza p : piz) {
            this.pizzas.add(p);
        }
    }

    public Order() {}

    public int getId() {
        return this.orderId;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public boolean addPizza(Pizza p) {
        return this.pizzas.add(p);
    }

    public List<Pizza> getPizzas() {
        return this.pizzas;
    }

    public int getFinalPrice() {
        int i = 0;
        for (Pizza p : this.pizzas) {
            i += p.getFinalPrice();
        }
        return i;
    }
}
