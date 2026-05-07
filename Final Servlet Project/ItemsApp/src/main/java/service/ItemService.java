package service;

import java.sql.SQLException;
import java.util.List;

import model.Item;

import javax.servlet.http.HttpServletRequest;

public interface ItemService {
	List<Item> getAllItem() throws SQLException;
	Item getItemById(int id) throws SQLException;
	void addItem(HttpServletRequest httpRequest);
	void updateItemById(HttpServletRequest httpRequest);
	void removeItemById(HttpServletRequest request);
}
