package com.shop.dto;

import com.shop.constant.Role;
import com.shop.entity.Member;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

//  인증된 사용자 정보만 필요.
@Getter
public class SessionMember implements Serializable {
    private String name;

    private String picture;
    private String email;

    public SessionMember(Member member){
        this.name = member.getName();
        this.email = member.getEmail();
        this.picture=member.getPicture();


    }
}
