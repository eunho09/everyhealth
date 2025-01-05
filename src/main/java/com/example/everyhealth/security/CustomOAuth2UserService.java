package com.example.everyhealth.security;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.MemberRole;
import com.example.everyhealth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        if(provider.equals("google")){
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId;
        String name = oAuth2UserInfo.getName();
        String picture = oAuth2UserInfo.getPicture();

        Member findMember = memberRepository.findByLoginId(loginId);
        Member member;

        if (findMember == null) {
            member = new Member(name, loginId, MemberRole.USER, providerId, picture);
        } else {
            member = findMember;
        }

        memberRepository.save(member);

        return new CustomOAuth2UserDetails(member, oAuth2User.getAttributes());
    }
}
