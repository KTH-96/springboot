package com.kth.book.springboot.web;

import com.kth.book.springboot.config.auth.LoginUser;
import com.kth.book.springboot.config.auth.dto.SessionUser;
import com.kth.book.springboot.service.posts.PostsService;
import com.kth.book.springboot.web.dto.PostsResponseDto;
import com.kth.book.springboot.web.dto.PostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostsService postsService;
    private final HttpSession httpSession;
    @GetMapping("/")
    //결국 @LoginUser SessionUser user 사용시 어느 컨트롤러 에서나 세션정보 가져올 수 있음
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());
        //CustomOAuth2UserService에서 로그인 성공시 세션에 SessionUser를 저장 하도록 구성
        //즉 로그인 성공시  httpSession.getAttribute("user")에서 값을 가져올 수 있다
        if(user != null){
            model.addAttribute("userName", user.getName());//정보없으면 로그인 버튼
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id,Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("posts", dto);
        return "posts-update";
    }

}
