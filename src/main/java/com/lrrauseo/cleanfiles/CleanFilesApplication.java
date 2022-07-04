package com.lrrauseo.cleanfiles;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class CleanFilesApplication implements CommandLineRunner {

	@Value("${application.base_path}")
	private String basePath;

	@Value("${application.expiration_days}")
	private int expirationDays;

	public static void main(String[] args) {
		SpringApplication.run(CleanFilesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		File baseDirectory = new File(basePath);
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_MONTH, -expirationDays);

		// System.out.println("classpath: " +
		// ClassPathResource.class.getResource("/").getPath());

		System.out.println("Limit Date: " + new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
		for (File fileEntry : baseDirectory.listFiles()) {
			if (fileEntry.isDirectory()) {
				System.out.println("Directory: " + fileEntry.getName());
				Date arquivoDate = new SimpleDateFormat("yyyyMMdd").parse(fileEntry.getName());
				if (arquivoDate.before(calendar.getTime())) {
					System.out.println("Deleting: " + fileEntry.getName());
					FileUtils.deleteDirectory(fileEntry);
				}
			} else {
				System.out.println("File: " + fileEntry.getName());
			}
		}

	}

}
