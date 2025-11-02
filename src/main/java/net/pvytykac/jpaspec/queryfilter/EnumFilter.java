package net.pvytykac.jpaspec.queryfilter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;

import java.util.Optional;
import java.util.Set;

@Data
public class EnumFilter<T extends Enum<T>> implements Filter<Set<String>, String> {

    Set<String> value;

    @Override
    public Optional<Predicate> toPredicate(Expression<String> expression, CriteriaBuilder cb) {
        return Optional.ofNullable(value)
                .filter(values -> !values.isEmpty())
                .map(expression::in);
    }
}
