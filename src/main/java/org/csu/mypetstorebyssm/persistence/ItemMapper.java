package org.csu.mypetstorebyssm.persistence;

import org.csu.mypetstorebyssm.domain.Item;

import java.util.List;
import java.util.Map;

public interface ItemMapper {
    void updateInventoryQuantity(String itemId, int increment);
    int getInventoryQuantity(String itemId);
    List<Item> getItemListByProduct(String productId);
    Item getItem(String itemId);
}
