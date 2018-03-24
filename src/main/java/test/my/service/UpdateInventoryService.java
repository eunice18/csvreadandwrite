package test.my.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import test.my.connect.MyConnection;
import test.my.main.dao.MyStoreDBDao;
import test.my.dao.InventoryConfigDao;
import test.my.dao.ManufacturerDao;
import test.my.dao.PRCMapDao;
import test.my.io.PropertyReader;
import test.my.model.InventoryConfig;
import test.my.model.InventoryRecord;
import test.my.model.Manufacturer;
import test.my.model.PRCMap;
import test.my.util.CSVReader;

@Service
public class UpdateInventoryService {

	@Autowired
	private ManufacturerDao manufacturerDao;

	@Autowired
	private PRCMapDao pRCMapDao;

	@Autowired
	private InventoryConfigDao inventoryConfigDao;

	@Autowired
	private CSVReader csvReader;

	@Autowired
	private PropertyReader propertyReader;

	@Autowired
	private MyStoreDBDao myStoreDBDao;

	@Autowired
	private MyConnection myConnection;

	private PreparedStatement updateInventoryPreparedStatement = null;

	private PreparedStatement selectInventoryPreparedStatement = null;

	private PreparedStatement insertInventoryPreparedStatement = null;

	public List<Manufacturer> getAllBrandList() {

		return manufacturerDao.getAllManufacturers();
	}

	public void updateBradWithNewTimeStamps(Manufacturer manufacturer) {
		manufacturerDao.save(manufacturer);
	}

	public void upateLive() {

	}

	public void writeInventory(Manufacturer manufacturer) throws IOException,
			ClassNotFoundException, SQLException {

		List<PRCMap> prcMaps = pRCMapDao.getPRCMap(manufacturer.getId());

		Map<String, String> map = new HashMap<String, String>();

		for (PRCMap prcMap : prcMaps) {
			map.put(prcMap.getName(), prcMap.getPrc());
		}

		String homePath = propertyReader.getProperty("app.ftp.root.path");

		Long time = -1L;
		if (manufacturer.getLastFileCreationDate() != null) {
			time = manufacturer.getLastFileCreationDate().getTime();
		}

		System.out.println("Last executed date (db) >> " + time + " -> " + new Date(time));

		List<File> files = getLatestFilefromDir(homePath + File.separator
				+ manufacturer.getFtpLocation(), time);

		Long maxTimeStamp = -1L;
		for (File file : files) {

			System.out.println("INSIDE FILE LIST LAST MODIFIED Date >> "
					+ file.lastModified() + " -> " + new Date(file.lastModified()));

			if (maxTimeStamp < (1000 * (file.lastModified() / 1000))) {
				maxTimeStamp = (1000 * (file.lastModified() / 1000));
			}

		}

		Connection connection = myConnection.getConnection();

		updateInventoryPreparedStatement = connection
				.prepareStatement("UPDATE VENDORPRICE SET STOCK=?, UPDATED_TIME=? WHERE VENDOR_PRC_PART=?");

		selectInventoryPreparedStatement = connection
				.prepareStatement("SELECT VENDOR_PRC_PART FROM VENDORPRICE WHERE VENDOR_PRC_PART=?");

		insertInventoryPreparedStatement = connection
				.prepareStatement("INSERT INTO VENDORPRICE(VENDOR_PRC_PART, STOCK, UPDATED_TIME) VALUES(?,?,?)");

		for (File file : files) {

			csvReader.initiateReader(file.getAbsolutePath());

			InventoryConfig inventoryConfig = inventoryConfigDao
					.getInventoryConfig(manufacturer.getId());
			System.out.println("READING FILE NAMEEEEEEEEEEEEEEEED CSV" + file.getName());
			// Skip first raw
			try {
				csvReader.readLine();
			} catch (Exception e) {
				System.out.printf("ERROR WHILE READING FILE" + file.getName());
				continue;
			}

			int i = 0;
			int ignore1 = 0;
			int ignore2 = 0;
			int ignore3 = 0;
			int ignore4 = 0;
			while (csvReader.hasNext()) {
				String[] strings = null;
				try {
					strings = csvReader.readLine();
				} catch (Exception e) {
						System.out.printf("ERROR WHILE READING FILE" + file.getName());
						continue;
				}

				InventoryRecord inventoryRecord = new InventoryRecord();

				try {
					if (strings[inventoryConfig.getInventoryValueColumnNumber()] == null
							|| strings[inventoryConfig
							.getInventoryValueColumnNumber()].trim() == "") {
						if (ignore1 == 0 || ignore1 % 1000 == 0) {
							System.out.println("Ignored due to Inventory value is"
									+ " empty >> manufacturer ->" + manufacturer.getName()
									+ " --> count : " + ignore1);
						}
						ignore1++;
						continue;
					}
				} catch (Exception e) {
					System.out.println("Ignored due to Inventory value reading exception"
							+ " >> manufacturer ->" + manufacturer.getName());
						ignore1++;
						continue;
				}
				
				Double invent;
				try {
					invent = Double.parseDouble(strings[inventoryConfig
							.getInventoryValueColumnNumber()].trim());
				} catch (Exception e) {
					if(ignore2 == 0 || ignore2 % 1000 == 0) {
						System.out.println("Ignored due to inventory value "
								+ "can't cast >> manufacturer ->" + manufacturer.getName()
								+ " --> count : " + ignore2);
					}
					ignore2 ++;
					continue;
				}

				inventoryRecord.setInventory(invent);

				String itemId = "";
				try {
					itemId = strings[inventoryConfig.getItemIdColumnNumber()];
				} catch (Exception e) {
						System.out.printf("ERROR WHILE READING FILE at ITEM ID" + file.getName());
				}

				if (itemId == null || itemId == "")
				{
					if(ignore3 == 0 || ignore3 % 1000 == 0)
						System.out.println("Ignored due to Item id is "
								+ "empty >> manufacturer ->" + manufacturer.getName()
								+ " --> count : " + ignore3);
					ignore3 ++;
					continue;
				}

				inventoryRecord.setItemId(itemId);

				String prc = "";

				try {
					prc = map.get(strings[inventoryConfig
							.getBrandNameColumnNumber()]);
				} catch (Exception e) {
						System.out.printf("ERROR WHILE READING FILE at PRC" + file.getName());
				}

				if (prc == null || prc == ""){
					if(ignore4 == 0 || ignore4 %1000 == 0)
						System.out.println("Ignored due to PRC is "
								+ "empty >> manufacturer ->" + manufacturer.getName()
								+ " --> count : " + ignore4);
					ignore4 ++;
					continue;
				}

				inventoryRecord.setPrc(prc);

				myStoreDBDao.writeInventory(inventoryRecord,
						updateInventoryPreparedStatement,
						insertInventoryPreparedStatement,
						selectInventoryPreparedStatement);

				if (i % 1000 == 0) {
					System.out.println("ITEM ID: " + itemId);
				}
				i++;
			}

			System.out.println("Brand: " + manufacturer.getName()
					+ " Written: " + i + " Items");
			csvReader.dismantleReader();

			deleteFile(file.getAbsolutePath());

		}

		if (files != null && !files.isEmpty()) {
			manufacturer.setLastFileCreationDate(new Date(maxTimeStamp));
			System.out.println("Latest modified date (insert to db) >> " 
					+ maxTimeStamp + " -> " + new Date(maxTimeStamp));
			manufacturerDao.save(manufacturer);
		}

	}

	private List<File> getLatestFilefromDir(String dirPath,
			Long LastModifiedTime) {

		List<File> fileList = new ArrayList<File>();
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			System.out.println("NO FILES IN DIRECTORY PATH "  + dirPath);
			return fileList;
		}

		for (int i = 0; i < files.length; i++) {
			System.out.println("FILES FOUND IN DIRECTORY PATH "  + dirPath);
			System.out.println("FILES FOUND IN DIRECTORY PATH LAST MODIFIED DATE "  + LastModifiedTime);
			System.out.println("FILES FOUND IN DIRECTORY PATH LAST MODIFIED CALCULATED "  + (1000 * (files[i].lastModified() / 1000)));
			System.out.println("FILES NAME "  + files[i].getName());
			System.out.println("FILES IS HIDDEN "  + files[i].isHidden());
			System.out.println("NEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
			if ((1000 * (files[i].lastModified() / 1000)) > LastModifiedTime
					&& !files[i].isHidden() && !files[i].isDirectory() && files[i].getName().contains(".csv")) {
				System.out.println("ADDED FILE WITH NAME "  + files[i].getName());
				fileList.add(files[i]);
			}
		}

		System.out.println("GET DIRECCTORY NUMBER OF FILES AFTER LAST EXECUTION : " 
				+ fileList.size() + " IN FOLDER: " + dirPath);
		return fileList;
	}

	private Boolean deleteFile(String path) {
		// File file = new File(path);
		// file.deleteOnExit();
		System.out.println("FILE DELETED");
		return true;
	}

}
