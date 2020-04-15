package io.digital.supercharger.mapper;

import io.digital.supercharger.dto.DemoData;
import io.digital.supercharger.model.Demo;
import org.mapstruct.Mapper;

@Mapper(componentModel = MapperConstant.SPRING_COMPONENT)
public interface DemoMappable {

    DemoData entityToDto(Demo demo);

    Demo dtoToEntity(DemoData demoData);
}
