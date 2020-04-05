package org.csu.mypetstorebyssm;

import org.csu.mypetstorebyssm.domain.Category;
import org.csu.mypetstorebyssm.domain.Product;
import org.csu.mypetstorebyssm.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.util.List;

@SpringBootTest
@MapperScan("org.csu.mypetstorebyssm.persistence")
class MypetstorebyssmApplicationTests {

    @Autowired
    CatalogService catalogService;

    @Test
    void contextLoads() {
    }

    @Test
    void testCategory(){
        Category category = catalogService.getCategory("BIRDS");
        System.out.println(category.getCategoryId()+","+ category.getName()+","+ category.getDescription());
    }

    @Test
    void testProduct(){
        List<Product> productList = catalogService.getProductListByCategory("BIRDS");
        System.out.println(productList.size());
    }

}
