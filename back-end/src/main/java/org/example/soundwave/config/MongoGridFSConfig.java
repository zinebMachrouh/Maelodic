package org.example.soundwave.config;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoGridFSConfig {

    private final MongoClient mongoClient;

    public MongoGridFSConfig(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Bean
    public GridFSBucket gridFSBucket() {
        MongoDatabase database = mongoClient.getDatabase("yourDatabaseName");
        return GridFSBuckets.create(database);
    }
}