package com.example.big;

import com.example.big.utils.StockTicker;
import com.example.big.utils.UserHistory;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet(name = "Stock", value = "/stockticker")
public class Stock extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object id = session.getAttribute("id");
        String ticker = request.getParameter("ticker");

        JSONObject stockInfo = new StockTicker(ticker).stockInfo();
        JSONArray userHistory = new UserHistory(id.toString(), ticker.toUpperCase()).getUserHistory();
        System.out.println(userHistory);

        request.setAttribute("ticker", ticker);
        request.setAttribute("stockTicker", stockInfo);
        request.setAttribute("userHistory", userHistory);

        request.getRequestDispatcher("/stock.jsp").forward(request, response);
    }
}
