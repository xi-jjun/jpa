package jdbc.basic.practicejdbc.repository;

import jdbc.basic.practicejdbc.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * create table member
 * (
 *     id   bigint generated by default as identity,
 *     name varchar(255),
 *     primary key (id)
 * );
 * 로 H2 DB 에 table 만들어줘야 한다. generated by default as identity 무시하면
 * 일일히 ID 값도 insert 해줘야 하기 때문에 아래 코드에서 PK null 오류가 난다.
 */
//@Repository  // SpringConfig class 에 등록되어 있는데 이 annotation 쓰면 에러 난다...
public class MemberRepositoryJdbc {
	private final DataSource dataSource;

	public MemberRepositoryJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Member save(Member member) {
		// 직접 SQL Query 를 작성해줘야 한다.
		String sql = "insert into member(name) values(?)";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection(); // connection 가져오기
			/**
			 sql – an SQL statement that may contain one or more '?' IN parameter placeholders
			 autoGeneratedKeys – a flag indicating whether auto-generated keys should be returned;
			 */
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // DB 에 Key(id) 자동으로 설정된다.

			// parameterIndex 번째의 ?에 member.getName() 값을 넣어준다. - data binding
			// SQL 의 첫번째 (?) 부분에 들어가는 값은 member.getName() 이라는 뜻
			pstmt.setString(1, member.getName());

			pstmt.executeUpdate(); // DB 에 실제 query 날라간다

			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				member.setId(rs.getLong(1));
			} else {
				throw new SQLException("id 조회 실패");
			}
			return member;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
	}

	public Optional<Member> findByName(String name) {
		String sql = "select * from member where name = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery(); // 조회는 살짝 다르다.

			if (rs.next()) {
				Member member = new Member();
				member.setId(rs.getLong("id"));
				member.setName(rs.getString("name"));
				return Optional.of(member);
			}
			return Optional.empty();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
	}

	public List<Member> findAll() {
		String sql = "select * from member";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			List<Member> members = new ArrayList<>();
			while (rs.next()) {
				Member member = new Member();
				member.setId(rs.getLong("id"));
				member.setName(rs.getString("name"));
				members.add(member);
			}
			return members;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			close(conn, pstmt, rs);
		}
	}

	private Connection getConnection() {
		// spring 에서 JDBC 를 쓰기 위해 DataSourceUtils 에서 connection 를 가져온 것이다.
		return DataSourceUtils.getConnection(dataSource);
	}

	private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn != null) {
				close(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void close(Connection conn) throws SQLException {
		DataSourceUtils.releaseConnection(conn, dataSource);
	}
}
