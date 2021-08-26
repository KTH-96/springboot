package com.kth.book.springboot.config.auth;

import com.kth.book.springboot.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
//HandlerMethodArgumentResolver의 기능은 조건에 맞는 경우 메소드가 있다면 HandlerMethodArgumentResolver의 구현체가
//지정한 값으로 해당 메소드의 파라미터로 넘길수 있다.
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        //컨트롤러 메서드의 특정 파라미터를 지원하는지 판단합니다.
        //파라미터에 @LoginUser이 붙어있고, 파라미터 클래스 타입이 SessionUser.class인 경우 true반환
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //파라미터에 전달할 객체 생성(여기서는 세션에서 객체 가져옴)
        return httpSession.getAttribute("user");
    }
}
