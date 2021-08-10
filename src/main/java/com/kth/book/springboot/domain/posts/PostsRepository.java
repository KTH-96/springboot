package com.kth.book.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    //Posts클래스로 database로 접근하게 해줄 JPaRepository 생성
    //MyBatis는 보통 Dao로 불릴 DB Layer 기본적인 CRUD 메소드 자동 생성
    @Query("SELECT p FROM Posts p order by p.id desc ")
    List<Posts> findAllDesc();

}
