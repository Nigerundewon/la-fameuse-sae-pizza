package controleurs;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.IngredientDAO;
import dao.PizzaDAO;
import dto.Ingredient;
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

@WebServlet("/pizzas/*")
public class PizzaRestAPI extends PatchServlet  {

    PizzaDAO dao = new PizzaDAO();;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        try {
            if (!getPizzas(req, res, new ObjectMapper()))
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
            Pizza p = null;
            Ingredient i = null;
            try {
                i = obj.readValue(data, Ingredient.class);
            } catch (Exception e) {
                System.out.println("Not an ingredient: " + e.getMessage());
                try {
                    p = obj.readValue(data, Pizza.class);
                } catch (Exception e2) {
                    System.out.println("Not aa pizza: " + e2.getMessage());
                }
            }
            String[] infos = new String[1];
            if (req.getPathInfo() != null) {
                infos = req.getPathInfo().substring(1).split("/");
            }
            if (infos.length == 1 && i != null) {
                PrintWriter out = res.getWriter();
                dao.saveIngredientToPizza(Integer.parseInt(infos[0]), i.getId());
                out.print("ingredient " + i.getId() + " was added to pizza " + infos[0] + "!");
            } else if (infos[0] == null && dao.save(p)) {
                PrintWriter out = res.getWriter();
                out.print(p.getName() + " was added to the database!");
            } else {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
    @Override
    public void doPatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (verifToken(req,res)) {
            res.setContentType("application/json;charset=UTF-8");
            String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
            ObjectMapper obj = new ObjectMapper();
            Pizza p = obj.readValue(data, Pizza.class);
            String[] infos = req.getPathInfo().substring(1).split("/");
            if (dao.update(p, Integer.parseInt(infos[infos.length - 1]))) {
                PrintWriter out = res.getWriter();
                out.print(p.getName() + " was updated!");
            } else {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (verifToken(req,res)) {
            res.setContentType("application/json");
            String info = req.getPathInfo();
            try {
                String[] path = info.split("/");
                String data = path[1];
                Pizza p = dao.findById(Integer.parseInt(data));
                if (path.length == 2) {
                    if (p != null) {
                        PrintWriter out = res.getWriter();
                        out.print(p.getName() + " was deleted from the database!");
                        dao.delete(p);
                    }
                } else if (path.length == 3) {
                    PrintWriter out = res.getWriter();
                    if (dao.deleteIngredientFromPizza(Integer.parseInt(path[1]), Integer.parseInt(path[2])))
                        out.print("ingredient " + path[2] + " was deleted from pizza " + path[1] + "!");
                }
            } catch (Exception e) {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    private boolean getPizzas(HttpServletRequest req, HttpServletResponse res, ObjectMapper obj) throws IOException {
        String[] infos = null;
        PrintWriter out = res.getWriter();
        if (req.getPathInfo() != null && req.getPathInfo().length() >= 1) {
            infos = req.getPathInfo().substring(1).split("/");
        }
        if (infos == null || (infos.length == 1 && infos[0].equals(""))) {
            System.out.println("getAllPizzas");
            out.println(obj.writeValueAsString(dao.findAll()));
        }
        else if (infos.length == 1) {
            System.out.println(infos[0]);
            out.println(obj.writeValueAsString(dao.findById(Integer.parseInt(infos[0]))));
        }
        else if (infos.length == 2 && infos[1].equals("name")) {
            System.out.println(infos.length);
            out.println(obj.writeValueAsString(dao.findById(Integer.parseInt(infos[0])).getName()));
        }
        else if (infos.length == 2 && infos[1].equals("prixfinal")) {
            System.out.println(infos.length);
            out.println(obj.writeValueAsString(dao.findById(Integer.parseInt(infos[0])).getFinalPrice()));
        }
        else {
            return false;
        }
        return true;
    }

}
