package net.pvytykac.jpaspec.queryfilter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.util.Optional;

@Data
public class UuidFilter implements Filter<String, java.util.UUID> {

    @UUID
    String value;

    @Override
    public Optional<Predicate> toPredicate(Expression<java.util.UUID> expression, CriteriaBuilder cb) {
        return Optional.ofNullable(value)
                .map(java.util.UUID::fromString)
                .map(uuid -> cb.equal(expression, uuid));
    }
}
