package test.my.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import test.my.model.PRCMap;

public interface PRCMapDao extends CrudRepository<PRCMap, Long> {

	@Query("select u from PRCMap u where u.manufacturer.id = :id")
	public List<PRCMap> getPRCMap(@Param("id") Long id);
}
