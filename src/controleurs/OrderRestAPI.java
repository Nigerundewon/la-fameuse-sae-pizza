package controleurs;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.OrderDAO;
import dao.PizzaDAO;
import dto.Ingredient;
import dto.Order;
import dto.Pizza;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@WebServlet("/commandes/*")
public class OrderRestAPI extends PizzaSecureHttpServlet {

    OrderDAO dao = new OrderDAO();;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        try {
            if (!getOrders(req, res, new ObjectMapper()))
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        catch (Exception e) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (verifToken(req,res)) {
            res.setContentType("application/json;charset=UTF-8");
            String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
            ObjectMapper obj = new ObjectMapper();
            Order o = obj.readValue(data, Order.class);
            if (dao.save(o)) {
                PrintWriter out = res.getWriter();
                out.print("Order was added to the database!");
            } else {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }


    private boolean getOrders(HttpServletRequest req, HttpServletResponse res, ObjectMapper obj) throws IOException {
        String[] infos = null;
        PrintWriter out = res.getWriter();
        if (req.getPathInfo() != null && req.getPathInfo().length() >= 1) {
            infos = req.getPathInfo().substring(1).split("/");
        }
        if (infos == null || (infos.length == 1 && infos[0].equals(""))) {
            out.println(obj.writeValueAsString(dao.findAll()));
        }
        else if (infos.length == 1) {
            out.println(obj.writeValueAsString(dao.findById(Integer.parseInt(infos[0]))));
        }
        else if (infos.length == 2 && infos[1].equals("prixfinal")) {
            out.println(obj.writeValueAsString(dao.findById(Integer.parseInt(infos[0])).getFinalPrice()));
        }
        else {
            return false;
        }
        return true;
    }

}
