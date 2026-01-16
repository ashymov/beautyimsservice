package com.sashymov.beautyimsservice.dao;

import com.sashymov.beautyimsservice.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {
    List<Order> findAllByCustomerId(Long customerId);

    List<Order> findAllByCustomerName(String customerName);

    List<Order> findAllByCustomerEmail(String customerEmail);

    List<Order> findAllByCustomerPhone(String customerPhone);

    @Query("""
        select (count(o) > 0)
        from Order o
        where o.user.id = :userId
          and o.status = com.sashymov.beautyimsservice.enums.OrderStatus.ACTIVE
          and o.startTime < :end
          and o.endTime   > :start
    """)
    boolean existsOverlappingActiveOrder(Long userId, LocalDateTime start, LocalDateTime end);

    // если потом понадобится редактирование заказа:
    @Query("""
        select (count(o) > 0)
        from Order o
        where o.user.id = :userId
          and o.id <> :orderId
          and o.status = com.sashymov.beautyimsservice.enums.OrderStatus.ACTIVE
          and o.startTime < :end
          and o.endTime   > :start
    """)
    boolean existsOverlappingActiveOrderExcluding(Long userId, Long orderId, LocalDateTime start, LocalDateTime end);

    @Query("""
select o
from Order o
where o.user.id = :userId
  and o.status = com.sashymov.beautyimsservice.enums.OrderStatus.ACTIVE
  and o.startTime < :rangeEnd
  and o.endTime   > :rangeStart
order by o.startTime
""")
    List<Order> findActiveOrdersInRange(Long userId, LocalDateTime rangeStart, LocalDateTime rangeEnd);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
 update Order o
 set o.status = com.sashymov.beautyimsservice.enums.OrderStatus.CANCELLED,
     o.canceledAt = :canceledAt,
     o.cancelReason = :reason
 where o.id = :orderId
   and o.status <> com.sashymov.beautyimsservice.enums.OrderStatus.CANCELLED
   and o.status <> com.sashymov.beautyimsservice.enums.OrderStatus.COMPLETED
""")
    int cancelOrder(Long orderId, LocalDateTime canceledAt, String reason);


}
