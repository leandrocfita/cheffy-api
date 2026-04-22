package br.com.fiap.cheffy.infrastructure.persistence.order.repository;

import br.com.fiap.cheffy.infrastructure.persistence.order.entity.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {

    @Query("""
        SELECT DISTINCT o FROM OrderJpaEntity o
        LEFT JOIN FETCH o.items
        WHERE o.id = :id
    """)
    Optional<OrderJpaEntity> findByIdWithItems(@Param("id") UUID id);

    @Query("""
        SELECT DISTINCT o FROM OrderJpaEntity o
        LEFT JOIN FETCH o.items
        WHERE o.customerId = :customerId
    """)
    List<OrderJpaEntity> findAllByCustomerIdWithItems(@Param("customerId") UUID customerId);
}
