package dao;

import dto.Ingredient;
import dto.Pizza;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PizzaDAO {
    public PizzaDAO() {
    }

    public List<Pizza> findAll() {
        ArrayList<Pizza> list = new ArrayList<Pizza>();
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            ResultSet rs = smt.executeQuery("SELECT * FROM pizza;");
            while (rs.next()) {
                list.add(findById(rs.getInt(1)));
            }
            rs.close();
            DB.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public Pizza findById(int i) {
        Pizza piz = null;
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            ResultSet rs = smt.executeQuery("SELECT * FROM pizza WHERE id = " + i + ";");
            if (rs.next())
                piz = new Pizza(rs.getInt(1),rs.getString(2),rs.getString(3), rs.getInt(4));
            IngredientDAO dao = new IngredientDAO();
            rs = smt.executeQuery("SELECT * FROM ingredientsOnPizza WHERE pizza_id =" + piz.getId() + ";");
            while (rs.next())
                piz.addIngredient(dao.findById(rs.getInt(2)));
            rs.close();
            DB.close();
            return piz;
        }
        catch (Exception e) {
            System.out.println("findById : " + e.getMessage());
        }
        return null;
    }

    public boolean save(Pizza p) {
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            ResultSet rs = smt.executeQuery("SELECT MAX(id) FROM pizza;");
            int id;
            if (rs.next()) {
                id = rs.getInt(1);
                smt.executeUpdate("INSERT INTO pizza (id,name,pate,price) VALUES (" + (id+1) + ",'" + p.getName() + "','" + p.getPate() + "'," + p.getPrice() + ");");
                for (int i = 0 ; i < p.getIngredients().size() ; i++) {
                    smt.addBatch("INSERT INTO ingredientsOnPizza VALUES(" + (id+1) + "," + p.getIngredients().get(i).getId() + ");");
                }
                smt.executeBatch();
            }

            DB.close();
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean saveIngredientToPizza(int pizz_id, int ingr_id) {
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            System.out.println("Pizza ID: " + pizz_id + "\nIngredient ID: " + ingr_id);
            smt.addBatch("INSERT INTO ingredientsOnPizza VALUES(" + pizz_id + "," + ingr_id + ");");
            smt.executeBatch();
            DB.close();
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteIngredientFromPizza(int pizz_id, int ingr_id) {
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            System.out.println("Pizza ID: " + pizz_id + "\nIngredient ID: " + ingr_id);
            smt.addBatch("DELETE FROM ingredientsOnPizza WHERE pizza_id=" + pizz_id + " AND ingredient_id=" + ingr_id + ";");
            smt.executeBatch();
            DB.close();
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean update(Pizza p, int id) {
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            if (p.getName() != null) {
                smt.addBatch("UPDATE pizza SET name = '" + p.getName() + "' WHERE id = " + id + ";");
            }
            if (p.getPate() != null) {
                smt.addBatch("UPDATE pizza SET pate = '" + p.getPate() + "' WHERE id = " + id + ";");
            }
            if (p.getPrice() != 0) {
                smt.addBatch("UPDATE pizza SET price = " + p.getPrice() + " WHERE id = " + id + ";");
            }
            smt.executeBatch();
            DB.close();
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean delete(Pizza p) {
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            smt.addBatch("DELETE FROM pizza WHERE id =" + p.getId() + ";");
            smt.addBatch("DELETE FROM ingredientsOnPizza WHERE pizza_id =" + p.getId() + ";");
            smt.executeBatch();
            DB.close();
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
