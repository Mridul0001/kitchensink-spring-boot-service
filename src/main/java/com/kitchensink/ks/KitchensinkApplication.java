package com.kitchensink.ks;

import com.kitchensink.ks.utils.ExcludeFromJacocoGeneratedReport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class KitchensinkApplication {

	@ExcludeFromJacocoGeneratedReport
	public static void main(String[] args) {
		SpringApplication.run(KitchensinkApplication.class, args);
	}

}
