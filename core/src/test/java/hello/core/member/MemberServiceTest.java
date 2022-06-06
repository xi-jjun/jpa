package hello.core.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {
	private final MemberService memberService = new MemberServiceImpl();

	@Test
	void join() {
		// given
		Member member = new Member(1L, "Kim Jae Jun", Grade.VIP);

		// when
		memberService.join(member);

		// then
		Member findMember = memberService.findMember(member.getId());
		Assertions.assertThat(member).isEqualTo(findMember);
	}

}