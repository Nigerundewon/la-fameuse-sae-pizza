package controleurs;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
public abstract class PatchServlet extends PizzaSecureHttpServlet {
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, res);
        } else {
            super.service(req, res);
        }
    }
    public abstract void doPatch(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException;
}