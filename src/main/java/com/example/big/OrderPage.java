package com.example.big;

import com.example.big.utils.Order;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "OrderPage", value = "/order")
public class OrderPage extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object id = session.getAttribute("id");

        if (id == null) {
            request.getRequestDispatcher("/login").forward(request, response);
        } else {
            request.getRequestDispatcher("/buy.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String stockTicker = request.getParameter("ticker");
        int shares = Integer.parseInt(request.getParameter("shares"));
        String orderType = request.getParameter("orderType");

        HttpSession session = request.getSession();
        String id = session.getAttribute("id").toString();

        Order order = new Order(id, stockTicker, orderType, shares);
        String message = order.newOrder();

        request.setAttribute("message", message);
        doGet(request, response);

    }
}
