package net.pvytykac.jpaspec.db;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "items")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String name;
    BigDecimal price;

    @Enumerated(EnumType.STRING)
    Status status;

    public enum Status {
        PENDING, ACTIVE, DISABLED,

        @JsonEnumDefaultValue
        UNKNOWN
    }
}
