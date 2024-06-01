package dto;
public class Ingredient {
    private int id;
    private String name;
    private int price;

    public Ingredient(int i, String n, int p) {
        this.id = i;
        this.name = n;
        this.price = p;
    }
    public Ingredient() {}
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

}