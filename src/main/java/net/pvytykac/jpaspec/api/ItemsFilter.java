package net.pvytykac.jpaspec.api;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.ToString;
import net.pvytykac.jpaspec.db.ItemDbo.Status;
import net.pvytykac.jpaspec.queryfilter.EnumFilter;
import net.pvytykac.jpaspec.queryfilter.StringFilter;
import net.pvytykac.jpaspec.queryfilter.UuidFilter;

import java.util.Set;

@Getter
@ToString
public class ItemsFilter {

    @Valid
    private final UuidFilter uuidFilter = new UuidFilter();
    @Valid
    private final StringFilter nameFilter = new StringFilter();
    @Valid
    private final EnumFilter<Status> statusFilter = new EnumFilter<>();

    public ItemsFilter setId(String uuid) {
        this.uuidFilter.setValue(uuid);
        return this;
    }

    public ItemsFilter setName(String name) {
        this.nameFilter.setValue(name);
        return this;
    }

    public ItemsFilter setNameOperator(StringFilter.Operator operator) {
        this.nameFilter.setOperator(operator);
        return this;
    }

    public ItemsFilter setStatus(Set<String> status) {
        this.statusFilter.setValue(status);
        return this;
    }
}
