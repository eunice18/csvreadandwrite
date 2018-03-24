package test.my;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BasisDataSourceConfiguration {
	
	@Value("${my.live.uri}")
	private String uri;
	
	@Value("${my.live.password}")
	private String password;
	
	@Value("${my.live.userName}")
	private String userName;
	
	
	
	@Bean
	@Primary
	@ConfigurationProperties(prefix="spring.datasource")
	public javax.sql.DataSource primaryDataSource() {
	    return DataSourceBuilder.create().build();
	}
	
	
	@Bean(destroyMethod = "close")
	@Qualifier("myDataSource")
    public javax.sql.DataSource myDataSource() {
		
		PoolProperties poolProperties = new PoolProperties();
		poolProperties.setUrl(uri);
		poolProperties.setDriverClassName("com.basis.jdbc.BasisDriver");
		poolProperties.setUsername(userName);
		poolProperties.setPassword(password);
		poolProperties.setTestWhileIdle(false);
		poolProperties.setTestOnBorrow(true);
		poolProperties.setValidationQuery("SELECT 1");
		poolProperties.setMaxActive(10);
		poolProperties.setInitialSize(10);
		poolProperties.setMinIdle(10);
		poolProperties.setMaxIdle(5);
		poolProperties.setLogAbandoned(true);
		
		DataSource datasource = new DataSource();
		datasource.setPoolProperties(poolProperties);
		return datasource;
    }

}
