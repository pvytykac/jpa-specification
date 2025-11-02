package net.pvytykac.jpaspec.api;

import net.pvytykac.jpaspec.db.ItemDbo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemGetDto dboToResponseDto(ItemDbo item);

    @Mapping(target = "id", ignore = true)
    ItemDbo postDtoToDbo(ItemPostDto item);

}
