package controleurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.IngredientDAO;
import dto.Ingredient;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/ingredients/*")
public class IngredientRestAPI extends PizzaSecureHttpServlet  {

    IngredientDAO dao = new IngredientDAO();;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        try {
            if (!getIngredients(req, res, new ObjectMapper()))
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        catch (Exception e) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (verifToken(req,res)) {
            res.setContentType("application/json");
            String info = req.getPathInfo();
            try {
                String[] path = info.split("/");
                String data = path[1];
                Ingredient i = dao.findById(Integer.parseInt(data));
                if (i != null) {
                    PrintWriter out = res.getWriter();
                    out.print(i.getName() + " was deleted from the database!");
                    dao.delete(i);
                }
            } catch (Exception e) {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (verifToken(req,res)) {
            res.setContentType("application/json;charset=UTF-8");
            String data = new BufferedReader(new InputStreamReader(req.getInputStream())).readLine();
            ObjectMapper obj = new ObjectMapper();
            Ingredient i = obj.readValue(data, Ingredient.class);
            if (dao.save(i)) {
                PrintWriter out = res.getWriter();
                out.print(i.getName() + " was added to the database!");
            } else {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    private boolean getIngredients(HttpServletRequest req, HttpServletResponse res, ObjectMapper obj) throws IOException {
        String[] infos = null;
        PrintWriter out = res.getWriter();
        if (req.getPathInfo() != null) {
            infos = req.getPathInfo().substring(1).split("/");
        }
        if (infos == null || (infos.length == 1 && infos[0].isEmpty())) {
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
        else {
            return false;
        }
        return true;
    }
}
