package com.shop.dto;

import com.shop.constant.Role;
import com.shop.entity.Member;
import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 Map
    private String nameAttributeKey;
    private String name;
    private String email;

    private String picture;


    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email,String picture) {
      
    	this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture=picture;


    }
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
    	System.out.println("Dto - OauthAttributes.java1" + registrationId);
    	System.out.println("Dto - OauthAttributes.java2" + userNameAttributeName);
    	System.out.println("Dto - OauthAttributes.java3" + attributes);
        // 여기서 네이버와 페이스북 등 구분 (ofNaver, ofFacebook)
        if("naver".equals(registrationId)) {
            return ofNaver(userNameAttributeName, attributes);
        } //else if ("facebook".equals(registrationId)) {
//            return ofFacebook("id", attributes);
//        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
    	System.out.println("Dto - OauthAttributes.java4"+(String) attributes.get("name"));
    	System.out.println("Dto - OauthAttributes.java5"+(String) attributes.get("email"));
        return OAuthAttributes.builder()
                .name((String) attributes.get("email"))
                .email((String) attributes.get("email"))
                .picture((String)attributes.get("picture") )
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
    	Map<String, Object> response = (Map<String, Object>) attributes.get("response");
    	System.out.println("Dto - OauthAttributes.javaNaver"+ response);
    	//System.out.println("Dto - OauthAttributes.javaNaver"+(String) attributes.get("email"));
        
        return OAuthAttributes.builder()
                .name((String) response.get("email"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
//    private static OAuthAttributes ofFacebook(String userNameAttributeName, Map<String, Object> attributes) {
//
//        return OAuthAttributes.builder()
//                .name((String) attributes.get("name"))
//                .email((String) attributes.get("email"))
//                .picture((String) attributes.get("public_profile"))
//                .attributes(attributes)
//                .nameAttributeKey(userNameAttributeName)
//                .build();
//
//    }
    public Member toEntity(){
        return Member.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.ADMIN)
                .build();
    }



}
