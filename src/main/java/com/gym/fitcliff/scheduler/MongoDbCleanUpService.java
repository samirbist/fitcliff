package com.gym.fitcliff.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gym.fitcliff.entity.ImageDao;
import com.gym.fitcliff.repository.CustomerRepository;
import com.gym.fitcliff.repository.ImageRepository;
import com.gym.fitcliff.service.MongoDataService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MongoDbCleanUpService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private MongoDataService mongoDataService;

	@Transactional
	@Scheduled(cron = "0 0 0 * * *") // Runs at 12:00 AM (midnight) every day
	// @Scheduled(cron = "0 * * * * *") //runs every minute
	public void cleanUpMongoDb() {
		LocalDate todayDate = LocalDate.now();
		log.info("Starting cron job cleanUpMongoDb for date {} ", todayDate);
		LocalDate oneYearAgo = todayDate.minusYears(1);
		List<ImageDao> mongoDaoList = customerRepository.findImagesByLastDateOlderThanOneYearAndActiveImage(oneYearAgo);

		for (ImageDao imageDao : mongoDaoList) {
			imageDao.setActive(false);
			imageRepository.save(imageDao);
			log.info("Deleting mongo data for mongoId {} ", imageDao);
			mongoDataService.deleteMongoData(imageDao.getMongoId());
		}

		log.info("Ending cron job cleanUpMongoDb for date {} ", todayDate);
	}
}
