package service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.Item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ItemService {
	List<Item> getAllItem(HttpServletRequest request) throws SQLException;
	Item getItemById(HttpServletRequest request) throws SQLException;
	void addItem(HttpServletRequest httpRequest) throws IOException, SQLException;
	void updateItemById(HttpServletRequest httpRequest,HttpServletResponse httpResponse) throws IOException, SQLException;
	void removeItemById(HttpServletRequest request) throws SQLException;
}
