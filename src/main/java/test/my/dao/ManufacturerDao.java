package test.my.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import test.my.model.Manufacturer;

public interface ManufacturerDao extends CrudRepository<Manufacturer, Integer> {

	@Query("select u from Manufacturer u")
	List<Manufacturer> getAllManufacturers();

}
