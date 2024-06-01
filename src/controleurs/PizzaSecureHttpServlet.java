package controleurs;

import dao.DB;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.sql.*;

public abstract class PizzaSecureHttpServlet extends HttpServlet {
    public boolean verifToken(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String token = req.getParameter("token");
        boolean tokenExistsForUser = false;
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            ResultSet rs = smt.executeQuery("SELECT COUNT(*) as count FROM users WHERE token = '" + token + "';");
            rs.next();
            if (rs.getInt(1) == 1) tokenExistsForUser = true;
            rs.close();
            DB.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (!tokenExistsForUser) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide");
            return false;
        }
        return true;
    }
}
