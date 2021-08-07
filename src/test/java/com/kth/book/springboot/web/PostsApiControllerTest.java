package com.kth.book.springboot.web;

import com.kth.book.springboot.domain.posts.Posts;
import com.kth.book.springboot.domain.posts.PostsRepository;
import com.kth.book.springboot.service.posts.PostsService;
import com.kth.book.springboot.web.dto.PostsSaveRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


// 컨테이너 객체를 생성해 테스트에 사용할 수 있도록 해준다고 보면 됩니다, 싱글톤으로 관리되는 객체를 사용해 모든 테스트에 사용하게 됩니다.
//@ExtendWith(SpringExtension.class) SpringBootTest안에 포함
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PostsRepository repository;

    @AfterEach
    void tearDown() throws Exception{
        repository.deleteAll();
    }

    @Test
    void Posts_등록된다() throws Exception {
        //given
        String title = "title";
        String content = "content";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                                                            .title(title)
                                                            .content(content)
                                                            .author("author")
                                                            .build();

        String url = "http://localhost:" + port + "/api/v1/posts";
        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = repository.findAll();

        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

}