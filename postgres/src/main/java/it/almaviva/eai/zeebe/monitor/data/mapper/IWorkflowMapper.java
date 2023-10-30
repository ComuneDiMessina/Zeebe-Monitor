package it.almaviva.eai.zeebe.monitor.data.mapper;

import it.almaviva.eai.zeebe.monitor.data.entity.WorkflowEntity;
import it.almaviva.eai.zeebe.monitor.domain.WorkflowDomain;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring", imports = { Collectors.class, Optional.class, Collection.class,
        Stream.class }, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IWorkflowMapper {
	
	WorkflowDomain map (WorkflowEntity workflowEntity);
	List<WorkflowDomain> map(Iterable<WorkflowEntity> workflowEntityIterable);

}
