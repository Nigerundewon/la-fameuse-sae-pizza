package dao;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import dto.Ingredient;
public class IngredientDAO {
    public IngredientDAO() {
    }

    public List<Ingredient> findAll() {
        ArrayList<Ingredient> list = new ArrayList<Ingredient>();
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            ResultSet rs = smt.executeQuery("SELECT * FROM ingredient;");

            while (rs.next()) {
                list.add(new Ingredient(rs.getInt(1),rs.getString(2),rs.getInt(3)));
            }
            rs.close();
            DB.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public Ingredient findById(int i) {
        Ingredient ig = null;
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            ResultSet rs = smt.executeQuery("SELECT * FROM ingredient WHERE id = " + i + ";");
            if (rs.next())
                ig = new Ingredient(rs.getInt(1),rs.getString(2),rs.getInt(3));
            rs.close();
            DB.close();
            return ig;
        }
        catch (Exception e) {
            System.out.println("findById : " + e.getMessage());
        }
        return null;
    }

    public boolean save(Ingredient i) {
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            ResultSet rs = smt.executeQuery("SELECT MAX(id) FROM ingredient;");
            int id;
            if (rs.next()) {
                id = rs.getInt(1);
                smt.executeUpdate("INSERT INTO ingredient (id,name,price) VALUES (" + (id+1) + ",'" + i.getName() + "'," + i.getPrice() + ");");
            }
            DB.close();
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean delete(Ingredient i) {
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            smt.executeUpdate("DELETE FROM ingredient WHERE id =" + i.getId() + ";");
            DB.close();
            return true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
