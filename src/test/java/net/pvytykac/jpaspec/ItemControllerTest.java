package net.pvytykac.jpaspec;

import net.pvytykac.jpaspec.api.ItemController;
import net.pvytykac.jpaspec.api.ItemMapperImpl;
import net.pvytykac.jpaspec.api.ItemsFilter;
import net.pvytykac.jpaspec.db.ItemRepository;
import net.pvytykac.jpaspec.queryfilter.EnumFilter;
import net.pvytykac.jpaspec.queryfilter.StringFilter;
import net.pvytykac.jpaspec.queryfilter.UuidFilter;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest({ItemController.class, ItemMapperImpl.class})
class ItemControllerTest {

    @MockitoBean
    private ItemRepository repository;

    @Test
    void getWithAllFilters(@Autowired WebTestClient client) {
        var filterCaptor = ArgumentCaptor.forClass(ItemsFilter.class);
        var pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        var uuid = UUID.randomUUID().toString();

        when(repository.findAll(filterCaptor.capture(), pageableCaptor.capture()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        client.get()
                .uri(builder -> builder.path("/v1/items")
                        .queryParam("name", "Juice")
                        .queryParam("nameOperator", "STARTS_WITH")
                        .queryParam("id", uuid)
                        .queryParam("status", "ACTIVE", "PENDING")
                        .queryParam("page", 5)
                        .queryParam("size", 25)
                        .queryParam("sort", "name,asc")
                        .build())
                .exchange()
                .expectStatus().isOk();

        assertThat(filterCaptor.getValue()).isNotNull();

        assertThat(filterCaptor.getValue())
                .extracting(ItemsFilter::getNameFilter)
                .returns("Juice", StringFilter::getValue)
                .returns(StringFilter.Operator.STARTS_WITH, StringFilter::getOperator);

        assertThat(filterCaptor.getValue())
                .extracting(ItemsFilter::getUuidFilter)
                .returns(uuid, UuidFilter::getValue);

        assertThat(filterCaptor.getValue())
                .extracting(ItemsFilter::getStatusFilter)
                .returns(Set.of("ACTIVE", "PENDING"), EnumFilter::getValue);


        assertThat(pageableCaptor.getValue()).isNotNull();
        assertThat(pageableCaptor.getValue())
                .returns(5, Pageable::getPageNumber)
                .returns(25, Pageable::getPageSize);

        assertThat(pageableCaptor.getValue().getSort()).isNotNull();
        assertThat(pageableCaptor.getValue().getSort().getOrderFor("name"))
                .returns(Sort.Direction.ASC, Sort.Order::getDirection);
    }

}
