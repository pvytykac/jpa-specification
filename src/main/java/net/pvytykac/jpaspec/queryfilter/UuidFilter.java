package net.pvytykac.jpaspec.queryfilter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Optional;
import java.util.UUID;

@Value
@AllArgsConstructor
public class UuidFilter implements Filter<UUID, UUID> {

    UUID value;

    public static UuidFilter any() {
        return new UuidFilter(null);
    }

    public static UuidFilter equalTo(UUID uuid) {
        return new UuidFilter(uuid);
    }

    @Override
    public Optional<Predicate> toPredicate(Expression<UUID> expression, CriteriaBuilder cb) {
        return Optional.ofNullable(value)
                .map(uuid -> cb.equal(expression, uuid));
    }
}
