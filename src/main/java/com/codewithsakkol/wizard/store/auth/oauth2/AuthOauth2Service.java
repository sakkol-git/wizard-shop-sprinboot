package com.codewithsakkol.wizard.store.auth.oauth2;

import com.codewithsakkol.wizard.store.auth.CustomUserDetails;
import com.codewithsakkol.wizard.store.common.ResourceNotFoundException;
import com.codewithsakkol.wizard.store.users.Role;
import com.codewithsakkol.wizard.store.users.User;
import com.codewithsakkol.wizard.store.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.codewithsakkol.wizard.store.auth.oauth2.user.OAuth2UserInfo;
import com.codewithsakkol.wizard.store.auth.oauth2.user.OAuth2UserInfoFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthOauth2Service extends DefaultOAuth2UserService {
    public final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        try {
            return processOAuth2User(request, oAuth2User);
        }catch (Exception e){
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    public OAuth2User processOAuth2User(OAuth2UserRequest request, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(request.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();
        String providerId = oAuth2UserInfo.getId();
        
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        AuthProvider authProvider = AuthProvider.valueOf(request.getClientRegistration().getRegistrationId().toUpperCase());

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getProvider().equals(authProvider)) {
                user.setProvider(authProvider);
                user.setProviderId(providerId);
                userRepository.save(user);
            }
        }else {
                //Auto-register new User
                user = User.builder()
                        .name(name)
                        .email(email)
                        .provider(authProvider)
                        .providerId(providerId)
                        .role(Role.USER)
                        .password("")
                        .build();
                user = userRepository.save(user);
            }
            CustomUserDetails userDetails = new CustomUserDetails(user);
            userDetails.setAttributes(oAuth2User.getAttributes());
            return userDetails;
        }
}
