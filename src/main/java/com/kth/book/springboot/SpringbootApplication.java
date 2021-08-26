package com.kth.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableJpaAuditing가 삭제되고 config 패키지에 JpaConfig를 생성하여 추가
@SpringBootApplication
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}

}
