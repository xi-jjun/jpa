package hello.core.member;

import java.util.HashMap;
import java.util.Map;

public class MemoryMemberRepositoryImpl implements MemberRepository {
	// 동시성 이슈 때문에 ConcurrentHashMap 을 쓰는게 맞긴 하다. 연습이니깐 간단하게 HashMap 사용
	private static Map<Long, Member> store = new HashMap<>();

	@Override
	public void save(Member member) {
		store.put(member.getId(), member);
	}

	@Override
	public Member findById(Long id) {
		return store.get(id);
	}
}
