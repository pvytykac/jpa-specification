package net.pvytykac.jpaspec.api;

import lombok.Value;
import net.pvytykac.jpaspec.db.ItemDbo.Status;
import net.pvytykac.jpaspec.db.ItemsFilter;
import net.pvytykac.jpaspec.queryfilter.EnumFilter;
import net.pvytykac.jpaspec.queryfilter.StringFilter;
import net.pvytykac.jpaspec.queryfilter.StringFilter.Operator;
import net.pvytykac.jpaspec.queryfilter.UuidFilter;
import org.apache.commons.collections4.CollectionUtils;
import org.springdoc.core.annotations.ParameterObject;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Value
@ParameterObject
public class ItemsQueryParameters {

    UUID id;
    Set<Status> status;
    String name;
    Operator nameOperator;

    public ItemsFilter toFilter() {
        var idFilter = Optional.ofNullable(id)
                .map(UuidFilter::equalTo)
                .orElse(UuidFilter.any());

        var statusFilter = Optional.ofNullable(status)
                .filter(CollectionUtils::isNotEmpty)
                .map(EnumFilter::of)
                .orElse(EnumFilter.any());

        var nameFilter = Optional.ofNullable(name)
                .map(nameValue -> StringFilter.of(name, nameOperator, Operator.STARTS_WITH))
                .orElse(StringFilter.any());

        return ItemsFilter.builder()
                .uuidFilter(idFilter)
                .statusFilter(statusFilter)
                .nameFilter(nameFilter)
                .build();
    }

}
