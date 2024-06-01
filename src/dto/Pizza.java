package dto;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    private int id;
    private String name;
    private String pate;
    private int price;
    private List<Ingredient> ingredients = new ArrayList<Ingredient>();

    public Pizza(int id,String n, String pa, int p, Ingredient... ingredients) {
        this.id = id;
        this.name = n;
        this.pate = pa;
        this.price = p;
        for (Ingredient i : ingredients) {
            this.ingredients.add(i);
        }
    }

    public Pizza(int id, String p, String n) {
        this(id,n,p,0);
    }

    public Pizza() {}
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPate() {
        return this.pate;
    }

    public int getPrice() {
        return this.price;
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public int getFinalPrice() {
        int p = this.price;
        for (Ingredient i : ingredients) {
            p += i.getPrice();
        }
        return p;
    }

    public boolean addIngredient(Ingredient i) {
        return this.ingredients.add(i);
    }

    public boolean removeIngredient(Ingredient i) {
        return this.ingredients.remove(i);
    }

    public String ingredientsListToString() {
        String str = "";
        for (int i = 0 ; i < ingredients.size() ; i++) {
            str += ingredients.get(i).getName();
            if (i != ingredients.size()-1)
                str+= ", ";
        }
        return str;
    }

}