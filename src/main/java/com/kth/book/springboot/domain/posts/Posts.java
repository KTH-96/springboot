package com.kth.book.springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity//기본 생성자는 필수 (JPA가 엔티티 객체 생성 시 기본 생성자를 사용)
public class Posts {
    @Id//해당 테이블 pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)//pk생성 규칙
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }


    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
