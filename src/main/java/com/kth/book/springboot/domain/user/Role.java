package com.kth.book.springboot.domain.user;

import com.kth.book.springboot.domain.posts.PostsRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "손님"), USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;
}
