package net.pvytykac.jpaspec.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Value;
import net.pvytykac.jpaspec.db.ItemDbo.Status;

import java.math.BigDecimal;

@Value
public class ItemPostDto {
    @NotBlank
    String name;
    @PositiveOrZero
    BigDecimal price;
    @NotNull
    Status status;
}
