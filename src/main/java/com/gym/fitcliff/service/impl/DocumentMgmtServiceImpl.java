package com.gym.fitcliff.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gym.fitcliff.dto.MongoDataDto;
import com.gym.fitcliff.entity.DocumentImageDao;
import com.gym.fitcliff.model.DocumentImage;
import com.gym.fitcliff.repository.DocumentImageRepository;
import com.gym.fitcliff.service.DocumentMgmtService;
import com.gym.fitcliff.service.MongoDataService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentMgmtServiceImpl implements DocumentMgmtService {

	@Autowired
	private MongoDataService mongoDataService;

	@Autowired
	private DocumentImageRepository documentImageRepository;

	@Override
	@Transactional
	public DocumentImage createIdDocument(String fileName, MultipartFile file) {
		try {
			String mongoId = mongoDataService.addMongoData(fileName, file);
			DocumentImageDao dao = new DocumentImageDao();
			dao.setCreatedOn(LocalDate.now());
			dao.setFileName(fileName);
			dao.setMongoId(mongoId);
			DocumentImageDao documentImageDao = documentImageRepository.save(dao);
			DocumentImage documentImage = new DocumentImage();
			documentImage.setId(documentImageDao.getId());
			documentImage.setCreatedOn(documentImageDao.getCreatedOn());
			documentImage.setFileName(documentImageDao.getFileName());
			documentImage.setMongoId(documentImageDao.getMongoId());
			return documentImage;
		} catch (Exception ex) {
			log.error("Exception in saving template: {} ", ex);
		}

		throw new IllegalStateException("Error saving Document Id with file name : " + fileName);
	}

	@Override
	public MongoDataDto getIdDocument(Long id) throws IllegalStateException, IOException {
		final Optional<DocumentImageDao> documentImageDaoOptional = documentImageRepository.findById(id);
		if (documentImageDaoOptional.isPresent()) {
			DocumentImageDao documentImageDao = documentImageDaoOptional.get();
			MongoDataDto mongoDataDto = mongoDataService.getMongoData(documentImageDao.getMongoId());
			return mongoDataDto;
		}
		throw new IllegalStateException("Error in getting document for id : " + id);
	}

	@Override
	@Transactional
	public void deleteIdDocument(Long id) {
		final Optional<DocumentImageDao> documentImageDaoOptional = documentImageRepository.findById(id);
		if (documentImageDaoOptional.isPresent()) {
			DocumentImageDao documentImageDao = documentImageDaoOptional.get();
			documentImageRepository.delete(documentImageDao);
			mongoDataService.deleteMongoData(documentImageDao.getMongoId());
			return;
		}
		throw new IllegalStateException("Error in deleteing document id :" + id);

	}
}
