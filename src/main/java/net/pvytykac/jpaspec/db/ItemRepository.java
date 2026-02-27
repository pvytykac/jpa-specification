package net.pvytykac.jpaspec.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<ItemDbo, UUID>, JpaSpecificationExecutor<ItemDbo> {
}
