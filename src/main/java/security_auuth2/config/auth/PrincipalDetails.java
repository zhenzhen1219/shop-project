package security_auuth2.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;

import lombok.Data;

// Authentication 객체에 저장할 수 있는 유일한 타입
@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	private static final long serialVersionUID = 1L;
	private Member member;
	private Map<String, Object> attributes;
	

	
	@Autowired
	MemberRepository memberRepository;
	
	// 일반 시큐리티 로그인시 사용
	public PrincipalDetails(Member member) {
		System.out.println("일반로그인은>?");
		this.member = member;
	}
	
	
    public PrincipalDetails() {
    }
	
	// OAuth2.0 로그인시 사용
//	public PrincipalDetails(Member member, Map<String, Object> attributes) {
//		System.out.println("PrincipalDetails---------------------------" + attributes);
//		System.out.println(email);
//		this.member = member;
//		this.attributes = attributes;
//		this.email = email;
//	}
//	
    
    
    public PrincipalDetails(Member member ,Map<String, Object> attributes) {
		System.out.println("PrincipalDetails---------------------------" + attributes);
		//System.out.println(attributes.get(member.getEmail()));
		this.member = member;
		this.attributes = attributes;
		
	}
	

	
	
	
	
	
//	public PrincipalDetails(Member member, String email) {
//		System.out.println("PrincipalDetails---------------------------" + attributes);
//		this.member = member;
//		this.email = email;
//	}
	
	public Member getUser() {
		return member;
	}

	@Override
	public String getPassword() {
		return member.getPassword();
	}

	@Override
	public String getUsername() {
		return member.getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collet = new ArrayList<GrantedAuthority>();
		collet.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {
                return member.getEmail();
            }
			
		});
		return collet;
	}

	// 리소스 서버로 부터 받는 회원정보
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	// User의 PrimaryKey
	@Override
	public String getName() {
		return member.getId()+"";
	}

	
}
