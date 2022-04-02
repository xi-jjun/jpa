package jdbc.basic.practicejdbc.config;

import jdbc.basic.practicejdbc.repository.MemberRepositoryJdbc;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class SpringConfig {
	private final DataSource dataSource;

	/**
	 * memberRepository 를 spring bean 으로 등록해서 관리한다.
	 * 자동으로 우리의 구현체를 주입하도록 만들었다.
	 * (물론 여기서 interface 의 구현체가 아니긴 하다...)
	 * (하지만 dataSource 를 자동주입 하도록 한 것이다) - DI
	 * @return
	 */
	@Bean
	public MemberRepositoryJdbc memberRepositoryJdbc() {
		return new MemberRepositoryJdbc(dataSource);
	}
}
