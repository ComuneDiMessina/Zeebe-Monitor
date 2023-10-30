package it.almaviva.eai.zeebe.monitor.data.mapper;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import it.almaviva.eai.zeebe.monitor.data.entity.HazelcastEntity;
import it.almaviva.eai.zeebe.monitor.domain.HazelcastConfigDomain;

@Mapper(componentModel = "spring", imports = { Collectors.class, Optional.class, Collection.class,
        Stream.class }, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IHazelcastMapper {
	
	HazelcastConfigDomain map(HazelcastEntity hazelcastEntity);

}
