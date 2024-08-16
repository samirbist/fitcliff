package com.gym.fitcliff.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.gym.fitcliff.dto.MongoDataDto;
import com.gym.fitcliff.model.Image;

public interface ImageMgmtService {

	Image createImage(String fileName, MultipartFile image);

	MongoDataDto getImage(Long id) throws IllegalStateException, IOException;

	void deleteImage(Long id);

}
