package net.pvytykac.jpaspec.db;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import net.pvytykac.jpaspec.db.ItemDbo.Status;
import net.pvytykac.jpaspec.queryfilter.EnumFilter;
import net.pvytykac.jpaspec.queryfilter.StringFilter;
import net.pvytykac.jpaspec.queryfilter.UuidFilter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.stream.Stream;

@Value
@Builder
@AllArgsConstructor
@NullMarked
public class ItemsFilter implements Specification<ItemDbo> {

    @Builder.Default
    UuidFilter uuidFilter = UuidFilter.any();
    @Builder.Default
    StringFilter nameFilter = StringFilter.any();
    @Builder.Default
    EnumFilter<Status> statusFilter = EnumFilter.any();

    @Override
    public @Nullable Predicate toPredicate(Root<ItemDbo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        var namePredicate = nameFilter.toPredicate(root.get(ItemDbo_.name), cb);
        var idPredicate = uuidFilter.toPredicate(root.get(ItemDbo_.id), cb);
        var statusPredicate = statusFilter.toPredicate(root.get(ItemDbo_.status), cb);

        var predicates = Stream.of(idPredicate, namePredicate, statusPredicate)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toArray(Predicate[]::new);

        return cb.and(predicates);
    }
}
