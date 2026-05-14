package service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.Item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ItemService {
	void setHttpRequest(HttpServletRequest req);
	List<Item> getAllItem() throws SQLException;
	Item getItemById(long itemId) throws SQLException;
	void addItem(HttpServletRequest httpRequest) throws IOException;
	void updateItemById(HttpServletRequest httpRequest);
	void removeItemById(HttpServletRequest request);
}
