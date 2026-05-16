package controller;

import com.google.gson.*;
import model.ItemDetails;
import service.ItemDetailsService;
import service.impl.ItemDetailsServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/itemdetails/*")
public class ItemDetailsController extends HttpServlet {

    // used this gson builder to allow gson to be aware with how to convert String date received within request body to local date
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                    (json, type, context) -> LocalDate.parse(json.getAsString()))
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                    (date, type, context) -> new JsonPrimitive(date.toString()))
            .create();

    private ItemDetailsService itemDetailsService = new ItemDetailsServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String itemDetailIdPathParam = req.getPathInfo().substring(1);

        if (itemDetailIdPathParam.matches("\\d+")) {
            Long itemDetailId = Long.parseLong(itemDetailIdPathParam);
            try {
                ItemDetails itemDetails = itemDetailsService.getItemDetails(itemDetailId);
                if (itemDetails != null) {
                    resp.setContentType("application/json");
                    resp.getWriter().write(gson.toJson(itemDetails));
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Item not found");
                }

            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ItemDetails itemDetails = gson.fromJson(req.getReader(), ItemDetails.class);
        try {
            itemDetailsService.addItemDetails(itemDetails);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ItemDetails newItemDetails = gson.fromJson(req.getReader(), ItemDetails.class);
        try {
            itemDetailsService.updateItemDetails(newItemDetails);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String itemDetailIdPathParam = req.getPathInfo().substring(1);

        if (itemDetailIdPathParam.matches("\\d+")) {
            Long itemDetailId = Long.parseLong(itemDetailIdPathParam);
            try {
                itemDetailsService.deleteItemDetails(itemDetailId);
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
}
