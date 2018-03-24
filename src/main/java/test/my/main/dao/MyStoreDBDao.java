package test.my.main.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import test.my.model.InventoryRecord;

@Component
public class MyStoreDBDao {

	public Boolean writeInventory(InventoryRecord inventoryRecord,
			PreparedStatement updateInveotory,
			PreparedStatement insertInveotory, PreparedStatement selectInveotory) {

		try {

			selectInveotory.setString(1, inventoryRecord.getPrc()
					+ inventoryRecord.getItemId());
			ResultSet rs = selectInveotory.executeQuery();
			if (rs.next()) {
				System.out.println("Inserting");
				System.out.println(inventoryRecord.getInventory());
				System.out.println(inventoryRecord.getPrc()
						+ inventoryRecord.getItemId());
				updateInveotory.setDouble(1, inventoryRecord.getInventory());
				updateInveotory.setDate(2, new Date(System.currentTimeMillis()));
				updateInveotory.setString(3, inventoryRecord.getPrc()
						+ inventoryRecord.getItemId());
				updateInveotory.executeUpdate();
			} else {
				System.out.println("Updating");
				System.out.println(inventoryRecord.getInventory());
				System.out.println(inventoryRecord.getPrc()
						+ inventoryRecord.getItemId());
				insertInveotory.setString(1, inventoryRecord.getPrc()
						+ inventoryRecord.getItemId());

				insertInveotory.setDouble(2, inventoryRecord.getInventory());
				insertInveotory.setDate(3, new Date(System.currentTimeMillis()));
				insertInveotory.execute();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Exception Occured: " + e.getMessage());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception Occured: " + e.getMessage());
			return false;
		}
		return true;
	}

	/*
	 * public Boolean closeConnection() {
	 * 
	 * if (connection != null) { try { connection.close(); return true; } catch
	 * (Exception e) { return false; } } return true; }
	 */
}
