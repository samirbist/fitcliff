package com.gym.fitcliff.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import com.gym.fitcliff.dto.MongoDataDto;

public interface MongoDataService {

  public String addMongoData(String title, MultipartFile file) throws IOException;

  public MongoDataDto getMongoData(String mongoId)
      throws IllegalStateException, IOException;

  public void deleteMongoData(String mongoId);
}
