package controleurs;

import dao.DB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import java.sql.*;

@WebServlet("/generateToken")
public class GenerateToken extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        String login = req.getParameter("login");
        String pwd = req.getParameter("pwd");
        System.out.println(pwd.hashCode());
        boolean userExists = false;
        try {
            DB.open();
            Statement smt = DB.getConnection().createStatement();
            ResultSet rs = smt.executeQuery("SELECT COUNT(*) as count FROM users WHERE login = '" + login + "' AND pwd = '" + pwd.hashCode() +"';");
            rs.next();
            if (rs.getInt(1) == 1) userExists = true;
            rs.close();
            DB.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (userExists) {
            String token = UUID.randomUUID().toString();
            try {
                DB.open();
                Statement smt = DB.getConnection().createStatement();
                ResultSet rs = smt.executeQuery("SELECT token FROM users WHERE login = '" + login + "';");
                rs.next();
                String tmp = rs.getString(1);
                if (tmp.equals("")) {
                    smt.executeUpdate("UPDATE users SET token ='" + token + "' WHERE login = '" + login + "';");
                }
                else {
                    token = tmp;
                }
                rs.close();
                DB.close();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }

            out.println("Votre token est : " + token);
        } else {
            out.println("Who even are you?");
        }
    }
}
