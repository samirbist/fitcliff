package com.gym.fitcliff.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gym.fitcliff.dto.MongoDataDto;
import com.gym.fitcliff.entity.ImageDao;
import com.gym.fitcliff.model.Image;
import com.gym.fitcliff.repository.ImageRepository;
import com.gym.fitcliff.service.ImageMgmtService;
import com.gym.fitcliff.service.MongoDataService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageMgmtServiceImpl implements ImageMgmtService {
	
	@Autowired
	private MongoDataService mongoDataService;

	@Autowired
	private ImageRepository imageRepository;
	
	@Override
	@Transactional
	public Image createImage(String fileName, MultipartFile file) {
		try {
			String mongoId = mongoDataService.addMongoData(fileName, file);
			ImageDao dao = new ImageDao();
			dao.setCreatedOn(LocalDate.now());
			dao.setFileName(fileName);
			dao.setMongoId(mongoId);
			ImageDao imageDao = imageRepository.save(dao);
			Image image = new Image();
			image.setId(imageDao.getId());
			image.setCreatedOn(imageDao.getCreatedOn());
			image.setFileName(imageDao.getFileName());
			image.setMongoId(imageDao.getMongoId());
			return image;
		} catch (Exception ex) {
			log.error("Exception in saving template: {} ", ex);
		}

		throw new IllegalStateException("Error saving image Id with file name : " + fileName);
	}

	@Override
	public MongoDataDto getImage(Long id) throws IllegalStateException, IOException {
		final Optional<ImageDao> imageDaoOptional = imageRepository.findById(id);
		if (imageDaoOptional.isPresent()) {
			ImageDao imageDao = imageDaoOptional.get();
			MongoDataDto mongoDataDto = mongoDataService.getMongoData(imageDao.getMongoId());
			return mongoDataDto;
		}
		throw new IllegalStateException("Error in getting image for id : " + id);
	
	}

	@Override
	@Transactional
	public void deleteImage(Long id) {
		final Optional<ImageDao> imageDaoOptional = imageRepository.findById(id);
		if (imageDaoOptional.isPresent()) {
			ImageDao imageDao = imageDaoOptional.get();
			imageRepository.delete(imageDao);
			mongoDataService.deleteMongoData(imageDao.getMongoId());
			return;
		}
		throw new IllegalStateException("Error in deleteing image id :" + id);

	}

}
