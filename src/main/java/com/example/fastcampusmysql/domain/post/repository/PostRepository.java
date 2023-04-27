package com.example.fastcampusmysql.domain.post.repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PostRepository {
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	final static String TABLE = "Post";

	private final static RowMapper<DailyPostCount> DAILY_POST_COUNT_MAPPER = (ResultSet resultSet, int rowNum) -> new DailyPostCount(
		resultSet.getLong("memberId"),
		resultSet.getObject("createdDate", LocalDate.class),
		resultSet.getLong("count")
	);

	public Post save(Post post) {
		if (post.getId() == null) {
			return insert(post);
		}

		// 현재는 수정 기능 X
		throw new UnsupportedOperationException("Post는 갱신을 지원하지 않습니다.");
	}

	private Post insert(Post post) {
		SimpleJdbcInsertOperations simpleJdbcInsert =
			new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
				.withCatalogName("fast_sns")
				.withTableName(TABLE)                          // INSERT INTO 'TABLE'
				.usingGeneratedKeyColumns("id");    // AUTO_INCREMENT
		SqlParameterSource params = new BeanPropertySqlParameterSource(post);

		long id = simpleJdbcInsert
			.executeAndReturnKey(params)
			.longValue();

		return Post
			.builder()
			.id(id)
			.memberId(post.getMemberId())
			.contents(post.getContents())
			.createdDate(post.getCreatedDate())
			.createdAt(post.getCreatedAt())
			.build();
	}

	public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request) {
		String sql = String.format("""
				SELECT createdDate, memberId, count(id)
				FROM %s
				WHERE memberId = :memberId and createdDate BETWEEN :firstDate and :lastDate
				GROUP BY memberId, createdDate
			""", TABLE);

		SqlParameterSource params = new BeanPropertySqlParameterSource(request);

		return namedParameterJdbcTemplate.query(sql, params, DAILY_POST_COUNT_MAPPER);
	}
}