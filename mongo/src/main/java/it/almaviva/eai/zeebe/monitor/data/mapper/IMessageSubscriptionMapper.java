package it.almaviva.eai.zeebe.monitor.data.mapper;

import it.almaviva.eai.zeebe.monitor.data.entity.MessageSubscriptionDocument;
import it.almaviva.eai.zeebe.monitor.domain.MessageSubscriptionDomain;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring", imports = { Collectors.class, Optional.class, Collection.class,
        Stream.class }, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IMessageSubscriptionMapper {
	
	MessageSubscriptionDomain map(MessageSubscriptionDocument messageSubscriptionEntity);
	List<MessageSubscriptionDomain> map(Iterable<MessageSubscriptionDocument> messageSubscriptionEntityIterable);


}
