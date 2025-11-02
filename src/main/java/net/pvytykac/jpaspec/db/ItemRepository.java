package net.pvytykac.jpaspec.db;

import jakarta.persistence.criteria.Predicate;
import net.pvytykac.jpaspec.api.ItemsFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface ItemRepository extends JpaRepository<ItemDbo, UUID>, JpaSpecificationExecutor<ItemDbo> {

    default Page<ItemDbo> findAll(ItemsFilter filter, Pageable pageable) {
        var specification = (Specification<ItemDbo>) (root, query, cb) -> {
            var namePredicate = filter.getNameFilter()
                    .toPredicate(root.get("name"), cb);
            var idPredicate = filter.getUuidFilter()
                    .toPredicate(root.get("id"), cb);
            var statusPredicate = filter.getStatusFilter()
                    .toPredicate(root.get("status").as(String.class), cb);

            var predicates = Stream.of(idPredicate, namePredicate, statusPredicate)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toArray(Predicate[]::new);

            return cb.and(predicates);
        };

        return findAll(specification, pageable);
    }

}
