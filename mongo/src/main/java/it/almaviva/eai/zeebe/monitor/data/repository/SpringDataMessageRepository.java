package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.MessageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataMessageRepository extends MongoRepository<MessageDocument, Long>{

}
