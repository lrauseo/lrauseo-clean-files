package com.lrrauseo.cleanfiles;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class CleanFilesApplication implements CommandLineRunner {

	@Value("${application.base_path}")
	private String basePath;

	@Value("${application.expiration_days}")
	private int expirationDays;

	@Value("${application.date_format}")
	private String dateFormat;

	public static void main(String[] args) {
		SpringApplication.run(CleanFilesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		File baseDirectory = new File(basePath);
		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_MONTH, -expirationDays);

		log.info("Limit Date: " + new SimpleDateFormat(dateFormat).format(calendar.getTime()));
		for (File fileEntry : baseDirectory.listFiles()) {
			if (fileEntry.isDirectory()) {
				log.info("Directory: " + fileEntry.getName());
				try {

					Date arquivoDate = new SimpleDateFormat(dateFormat).parse(fileEntry.getName());
					if (arquivoDate.before(calendar.getTime())) {
						log.info("Deleting: " + fileEntry.getName());
						FileUtils.deleteDirectory(fileEntry);
					}
				} catch (ParseException e) {
					log.warn("Não foi possivel excluir o arquivo '{}' nomenclatura desconhecida para exclusão", fileEntry.getName());
				}
			} else {
				log.info("File: " + fileEntry.getName());
			}
		}

	}

}
