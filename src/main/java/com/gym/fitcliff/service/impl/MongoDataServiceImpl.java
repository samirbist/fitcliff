package com.gym.fitcliff.service.impl;

import com.gym.fitcliff.dto.MongoDataDto;
import com.gym.fitcliff.service.MongoDataService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.IOException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MongoDataServiceImpl implements MongoDataService {

  @Autowired private GridFsTemplate gridFsTemplate;

  @Autowired private GridFsOperations operations;

  @Override
  public String addMongoData(String title, MultipartFile file) throws IOException {
    DBObject metaData = new BasicDBObject();
    metaData.put("title", title);
    ObjectId id =
        gridFsTemplate.store(
            file.getInputStream(), file.getName(), file.getContentType(), metaData);
    return id.toString();
  }

  @Override
  public MongoDataDto getMongoData(String mongoId)
      throws IllegalStateException, IOException {
    GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(mongoId)));
    MongoDataDto lrbMongoDataDto = new MongoDataDto();
    lrbMongoDataDto.setFileName(file.getMetadata().get("title").toString());
    lrbMongoDataDto.setStream(operations.getResource(file).getInputStream());
    return lrbMongoDataDto;
  }

  @Override
  public void deleteMongoData(String mongoId) {
    gridFsTemplate.delete(new Query(Criteria.where("_id").is(mongoId)));
  }
}
