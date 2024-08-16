package com.gym.fitcliff.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.gym.fitcliff.dto.MongoDataDto;
import com.gym.fitcliff.model.DocumentImage;

public interface DocumentMgmtService {

	DocumentImage createIdDocument(String fileName, MultipartFile image);

	MongoDataDto getIdDocument(Long id) throws IllegalStateException, IOException;

	void deleteIdDocument(Long id);

}
