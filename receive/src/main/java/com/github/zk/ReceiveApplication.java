package com.github.zk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ReceiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceiveApplication.class, args);
    }

}
