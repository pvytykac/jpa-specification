package net.pvytykac.jpaspec.queryfilter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.util.Optional;

public interface Filter<T, U> {

    T getValue();

    Optional<Predicate> toPredicate(Expression<U> expression, CriteriaBuilder cb);

}
