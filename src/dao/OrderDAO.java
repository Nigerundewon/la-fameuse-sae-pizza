package dao;

import dto.Ingredient;
import dto.Order;
import dto.Pizza;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public OrderDAO(){}

    public List<Order> findAll() {
        ArrayList<Order> list = new ArrayList<Order>();
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            ResultSet rs = smt.executeQuery("SELECT * FROM orders;");
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

    public Order findById(int i) {
        Order order = null;
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            ResultSet rs = smt.executeQuery("SELECT * FROM orders WHERE order_id = " + i + ";");
            if (rs.next())
                order = new Order(rs.getInt(1),rs.getInt(2));
            PizzaDAO dao = new PizzaDAO();
            rs = smt.executeQuery("SELECT * FROM pizzasInOrder WHERE order_id =" + order.getId() + ";");
            while (rs.next())
                order.addPizza(dao.findById(rs.getInt(2)));
            rs.close();
            DB.close();
            return order;
        }
        catch (Exception e) {
            System.out.println("findById : " + e.getMessage());
        }
        return null;
    }

    public boolean save(Order o) {
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            ResultSet rs = smt.executeQuery("SELECT MAX(order_id) FROM orders;");
            int id;
            if (rs.next()) {
                id = rs.getInt(1);
                smt.executeUpdate("INSERT INTO orders VALUES(" + (id+1) + "," + o.getOwnerId() + ");");
                for (int i = 0 ; i < o.getPizzas().size() ; i++) {
                    smt.addBatch("INSERT INTO pizzasInOrder VALUES(" + (id+1) + "," + o.getPizzas().get(i).getId() + ");");
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

}
