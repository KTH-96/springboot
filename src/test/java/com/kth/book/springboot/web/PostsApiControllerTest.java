package com.kth.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kth.book.springboot.domain.posts.Posts;
import com.kth.book.springboot.domain.posts.PostsRepository;
import com.kth.book.springboot.web.dto.PostsSaveRequestDto;
import com.kth.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 컨테이너 객체를 생성해 테스트에 사용할 수 있도록 해준다고 보면 됩니다, 싱글톤으로 관리되는 객체를 사용해 모든 테스트에 사용하게 됩니다.
//@ExtendWith(SpringExtension.class) SpringBootTest안에 포함
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    WebApplicationContext context;
    //테스트할 Application Context를 주입받는다.

    private MockMvc mvc;

    @BeforeEach//테스트 시작전 MockMvc 인스턴스 생성
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void clear() {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER")
        //인증된 모의(가짜) 사용자를 만들어서 사용 roles에 권한 추가 가능
        //이 어노테이션으로 ROLE_USER권한을 가진 사용자가 API를 요청하는 것과 동일한 효과를 가지게 된다.
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
        //생성된 MockMVc를 통해 API테스트
        //본문(Body)영역은 문자열로 표현하기위해 ObjectMapper를 통해 문자열을 JSON으로 변환
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @WithMockUser(roles="USER")
    void Posts_수정된다()throws Exception {
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());
        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();
        String url = "http://localhost:" + port + "/api/v1/posts/"+updateId;

        //when
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}
