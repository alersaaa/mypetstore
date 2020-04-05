package org.csu.mypetstorebyssm.persistence;

import org.csu.mypetstorebyssm.domain.LineItem;

import java.util.List;

public interface LineItemMapper {
    List<LineItem> getLineItemsByOrderId(int orderId);
    boolean insertLineItem(LineItem lineItem);
}
