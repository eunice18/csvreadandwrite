package test.my.controller;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import test.my.dao.InventoryConfigDao;
import test.my.dao.ManufacturerDao;
import test.my.model.Manufacturer;
import test.my.model.PRCMap;

@RestController
@RequestMapping("/items")
public class InventoryConfigController {

	@Autowired
	private InventoryConfigDao inventoryConfigDao;

	@Autowired
	private ManufacturerDao manufacturerDao;

	@RequestMapping(method = RequestMethod.GET)
	public List<Manufacturer> getAll() {
		return manufacturerDao.getAllManufacturers();
	}

	@RequestMapping(method = RequestMethod.POST)
	public Manufacturer createNew(@RequestBody Manufacturer manufacturer) {

		if (manufacturer != null && manufacturer.getInventoryConfig() != null) {
			manufacturer.getInventoryConfig().setManufacturer(manufacturer);
		}
		if (manufacturer != null && manufacturer.getPrcMaps() != null) {
			List<PRCMap> prcMap = manufacturer.getPrcMaps();
			for (Iterator<PRCMap> iterator = prcMap.iterator(); iterator
					.hasNext();) {
				PRCMap item = iterator.next();
				item.setManufacturer(manufacturer);
			}
		}
		manufacturerDao.save(manufacturer);
		return manufacturer;
		// return (List<Manufacturer>) manufacturerDao.findAll();
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer id) {
		manufacturerDao.delete(id);
	}

	public List<Manufacturer> update(Manufacturer manufacturer) {
		manufacturerDao.save(manufacturer);
		return (List<Manufacturer>) manufacturerDao.findAll();
	}

}
