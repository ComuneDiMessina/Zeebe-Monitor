package it.almaviva.eai.zeebe.monitor.data.mapper;

import it.almaviva.eai.zeebe.monitor.data.entity.IncidentDocument;
import it.almaviva.eai.zeebe.monitor.domain.IncidentDomain;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring", imports = { Collectors.class, Optional.class, Collection.class,
        Stream.class }, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IIncidentMapper {
	
	IncidentDomain map(IncidentDocument incidentEntity);
	List<IncidentDomain> map(Iterable<IncidentDocument> incidentEntityIterable);

}
