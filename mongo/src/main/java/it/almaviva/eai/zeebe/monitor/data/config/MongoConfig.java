package it.almaviva.eai.zeebe.monitor.data.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {
	
    @Value("${spring.data.mongodb.host}")
    private String host;

 

    @Value("${spring.data.mongodb.port}")
    private int port;

 

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

 

    @Bean
    public MongoDbFactory client() throws Exception {
        MongoClient client = new MongoClient(host, port);
        return new SimpleMongoDbFactory(client, databaseName);
    }

 

    @Bean
    public MongoOperations mongoTemplate() throws Exception {
        
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(client());
        MappingMongoConverter converter = 
                new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
            converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        
        return new MongoTemplate(client(), converter);
    }

}
