package test.my.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import test.my.model.InventoryConfig;

@Transactional
public interface InventoryConfigDao extends
		CrudRepository<InventoryConfig, Long> {

	@Query("select u from InventoryConfig u where u.manufacturer.id = :id")
	public InventoryConfig getInventoryConfig(@Param("id") Long id);

}
