package swproject.yoohoo.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swproject.yoohoo.controller.MemberForm;
import swproject.yoohoo.domain.Member;
import swproject.yoohoo.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional //변경
    public Long join(Member member) {
//        if(!validateDuplicateMember(member)) return -1L;
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers =
                memberRepository.findByEmail(member.getEmail());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
//            return false;
        }
//        return true;
    }
    /**
     * 로그인
     */
    public Member login(String loginEmail,String pwd){
        return memberRepository.findByEmail(loginEmail).stream().findFirst()
                .filter(m->m.getPassword().equals(pwd)).orElse(null);
    }

    /** 회원 수정 */
    @Transactional //변경
    public void updateMember(Long memberid, MemberForm form){
        Member member= memberRepository.findOne(memberid);
        member.setEmail(form.getEmail());
        member.setPassword(form.getPassword());
        member.setCompany(form.getCompany());
        member.setAddress(form.getAddress());
        member.setContact(form.getContact());
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}