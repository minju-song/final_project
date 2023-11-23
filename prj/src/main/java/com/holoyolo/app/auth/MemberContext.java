package com.holoyolo.app.auth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.holoyolo.app.member.service.MemberVO;

import lombok.Getter;

/*
 * 세션에 추가정보 담기.
 */
@Getter
public class MemberContext extends User{

	private final String memberName;
    private final String nickname;
    private final String lockYn;
    private final String snsYn;

    public MemberContext(MemberVO memberVO, List<GrantedAuthority> authorities) {
        super(memberVO.getMemberId(), memberVO.getPassword(), authorities);
        this.memberName = memberVO.getMemberName();
        this.nickname = memberVO.getNickname();
        this.lockYn = memberVO.getLockYn();
        this.snsYn = memberVO.getSnsYn();
    }

	
}
