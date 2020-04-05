package org.csu.mypetstorebyssm.persistence;

import org.csu.mypetstorebyssm.domain.Order;

public interface OrderStatusMapper {
    int insertOrderStatus(Order order);
}
