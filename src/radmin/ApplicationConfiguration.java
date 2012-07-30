package radmin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration

public class ApplicationConfiguration {

	@Bean
	public EntityService entityService() {
		return null;
	}

	@Bean
	public ScreenContext screenContext() {
		return null;
	}
}