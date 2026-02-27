package net.pvytykac.jpaspec.queryfilter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Value
@Builder
@AllArgsConstructor
public class EnumFilter<T extends Enum<T>> implements Filter<Set<T>, T> {

    Set<T> value;

    public static <T extends Enum<T>> EnumFilter<T> any() {
        return new EnumFilter<>(null);
    }

    public static <T extends Enum<T>> EnumFilter<T> of(Collection<T> values) {
        return new EnumFilter<>(Set.copyOf(values));
    }

    @SafeVarargs
    public static <T extends Enum<T>> EnumFilter<T> of(T... values) {
        return new EnumFilter<>(Set.of(values));
    }

    @Override
    public Optional<Predicate> toPredicate(Expression<T> expression, CriteriaBuilder cb) {
        return Optional.ofNullable(value)
                .filter(values -> !values.isEmpty())
                .map(expression::in);
    }
}
