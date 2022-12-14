package com.example.big;

import com.example.big.utils.User;
import com.example.big.utils.WebPortfolio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "Servlet", urlPatterns ={"/"})
public class Servlet extends HttpServlet {

    public static JSONObject UserInfo(String id) throws SQLException, JSONException {

        User user = new User(id);

        float userCash = user.getUserBalance();
        float portfolioValue = user.getUserPortfolioValue();
        float totalSpent = user.getAmountSpent();

        float profitLoss = (portfolioValue + userCash) - 5000;
        float changeTotal = (profitLoss / totalSpent) * 100;

        if (Double.isInfinite(changeTotal)) changeTotal = 0;

        JSONObject userInfo = new JSONObject();
        userInfo.put("cash", userCash);
        userInfo.put("portfolio", portfolioValue);
        userInfo.put("pl", profitLoss);
        userInfo.put("ct", changeTotal);

        return userInfo;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        Object id = session.getAttribute("id");
        if (id == null) {
            response.sendRedirect("/login");
        } else {
            try {
                JSONObject userInfo = UserInfo(id.toString());
                WebPortfolio WebPortfolio = new WebPortfolio(id.toString());
                JSONArray webPortfolio = WebPortfolio.webPortfolio();

                request.setAttribute("userinfo", userInfo);
                request.setAttribute("webPortfolio", webPortfolio);
                request.getRequestDispatcher("/portfolio.jsp").forward(request, response);

            } catch (SQLException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
