package net.pvytykac.jpaspec.queryfilter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Optional;

@Data
public class StringFilter implements Filter<String, String> {

    String value;

    @NotNull
    Operator operator = Operator.EXACT_MATCH;

    @Override
    public Optional<Predicate> toPredicate(Expression<String> expression, CriteriaBuilder cb) {
        return Optional.ofNullable(value)
                .map(operator::formatForQuery)
                .map(query -> cb.like(expression, query));
    }

    public enum Operator {
        EXACT_MATCH("%s"),
        STARTS_WITH("%s%%"),
        ENDS_WITH("%%%s"),
        CONTAINS("%%%s%%");

        private final String pattern;

        Operator(String pattern) {
            this.pattern = pattern;
        }

        public String formatForQuery(String value) {
            return String.format(pattern, value);
        }
    }
}
