package org.csu.mypetstorebyssm.persistence;

import org.csu.mypetstorebyssm.domain.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper {
    Category getCategory(String categoryId);
}
