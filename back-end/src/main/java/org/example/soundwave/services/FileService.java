package org.example.soundwave.services;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@Slf4j
@AllArgsConstructor
public class FileService {
    private GridFsOperations gridFsOperations;

    public String saveFile(MultipartFile file) {
        try {
            ObjectId fileId = gridFsOperations.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
            return fileId.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file in GridFS", e);
        }
    }

    public GridFsResource getFile(String fileId) {
        GridFSFile gridFSFile = gridFsOperations.findOne(query(where("_id").is(fileId)));
        if (gridFSFile == null) {
            throw new IllegalArgumentException("File not found with ID: " + fileId);
        }
        return gridFsOperations.getResource(gridFSFile);
    }

    public void deleteFile(String fileId) {
        gridFsOperations.delete(query(where("_id").is(fileId)));
    }
}