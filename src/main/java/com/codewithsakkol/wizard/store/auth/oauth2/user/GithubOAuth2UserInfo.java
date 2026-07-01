package com.codewithsakkol.wizard.store.auth.oauth2.user;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        String email = (String) attributes.get("email");
        if (email == null) {
            String login = (String) attributes.get("login");
            if (login != null) {
                return login + "@github.com";
            }
        }
        return email;
    }
}
