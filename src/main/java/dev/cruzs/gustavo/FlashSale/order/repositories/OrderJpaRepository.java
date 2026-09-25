package dev.cruzs.gustavo.FlashSale.order.repositories;

import dev.cruzs.gustavo.FlashSale.order.models.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderModel, UUID> {
}
