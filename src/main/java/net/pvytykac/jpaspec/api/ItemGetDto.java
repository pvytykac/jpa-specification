package net.pvytykac.jpaspec.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import net.pvytykac.jpaspec.db.ItemDbo.Status;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class ItemGetDto {
    UUID id;
    String name;
    BigDecimal price;
    Status status;
}
