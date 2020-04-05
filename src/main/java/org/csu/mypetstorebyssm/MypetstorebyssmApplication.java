package org.csu.mypetstorebyssm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.csu.mypetstorebyssm.persistence")
public class MypetstorebyssmApplication {

    public static void main(String[] args) {
        SpringApplication.run(MypetstorebyssmApplication.class, args);
    }

}
