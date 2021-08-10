package com.kth.book.springboot.web;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Test
    void 메인페이지_로딩() {
        //when
        String body = this.restTemplate.getForObject("/", String.class);
        //then
        Assertions.assertThat(body).contains("스프링 부트로 시작하는 웹 서비스");
    }
}