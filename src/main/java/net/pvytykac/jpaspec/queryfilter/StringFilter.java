package net.pvytykac.jpaspec.queryfilter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@Value
@AllArgsConstructor
@NullMarked
public class StringFilter implements Filter<String, String> {

    @Nullable
    String value;
    Operator operator;

    public static StringFilter any() {
        return new StringFilter(null, Operator.STARTS_WITH);
    }

    public static StringFilter of(@Nullable String value, @Nullable Operator operator, Operator defaultOperator) {
        return new StringFilter(value, operator != null ? operator : defaultOperator);
    }

    public static StringFilter startsWith(String value) {
        return new StringFilter(value, Operator.STARTS_WITH);
    }

    public static StringFilter endsWith(String value) {
        return new StringFilter(value, Operator.ENDS_WITH);
    }

    public static StringFilter contains(String value) {
        return new StringFilter(value, Operator.CONTAINS);
    }

    public static StringFilter equalTo(String value) {
        return new StringFilter(value, Operator.EXACT_MATCH);
    }

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
