package test.my.io;

import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertyReader {
	@Inject
	private Environment environment;

	public String getProperty(String key) {
		return environment.getProperty(key);
	}

}
