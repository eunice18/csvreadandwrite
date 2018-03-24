package test.my.cron;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import test.my.model.Manufacturer;
import test.my.service.UpdateInventoryService;

@Component
public class InventoryUpdateCron {

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	UpdateInventoryService updateInventoryService;


	@Scheduled(cron = "0 1/1 * * * ?")
	public void execute() {
		System.out.println("||| Start cron to execute files on :" + new Date());
		
		List<Manufacturer> manufacturers = updateInventoryService
				.getAllBrandList();
		System.out.println("RETRIEVED MANUFACTURERS FROM DATABASE");
		for (Manufacturer manufacturer : manufacturers) {
			System.out.println(manufacturer.getName());
		}

		for (Manufacturer manufacturer : manufacturers) {
			System.out.println(">>> Brand Name : " + manufacturer.getName());
			try {
				updateInventoryService.writeInventory(manufacturer);
			} catch (ClassNotFoundException | IOException | SQLException e) {
				e.printStackTrace();
			}
			System.out.println(">>> Completed writing inventory Brand Name : " + manufacturer.getName());
		}

	}

}
