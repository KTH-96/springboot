package com.kth.book.springboot.config.auth;

import com.kth.book.springboot.config.auth.dto.OAuthAttributes;
import com.kth.book.springboot.config.auth.dto.SessionUser;
import com.kth.book.springboot.domain.user.User;
import com.kth.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
//구글 로그인 이후 가져온 사용자의 정보(email, name, picture)들을 기반으로 가입및 정보수정,세션 저장기능을 지원
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //registrationId 현재 진행중인 로그인 서비스를 비교
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        //OAuth2로그인 진행시 키가되는 필드값, PK와 같은 의미
        //구글은 기본적인 코드를 지원 ="sub", 네이버 카카오는 지원 X
        OAuthAttributes attributes = OAuthAttributes.
                of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        //OAuth2UserService 통해 가져온 OAuth2User 의 attribute를 담을 클래스
        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));
        //세션에 사용자 정보를 저장하기 위한 Dto클래스
        //왜냐하면 user클래스가 엔티티이기 때문에 언제 다른 엔티티클래스와 관계가 형성될지 모른다
        //@OneToMany @ManyToOne 같은 자식 엔티티를 가지고 있을경우 직렬화에 대상의 자식까지 포함됨
        //따라서 성능이슈, 부수 효과가 발생할 확률이 높기때문에 직렬화 기능을 가진 세션Dto를 추가로 만드는것
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(),attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
