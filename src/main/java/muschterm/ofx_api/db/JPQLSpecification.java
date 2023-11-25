package muschterm.ofx_api.db;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JPQLSpecification<T, Id> {

	private static final int DEFAULT_PAGE_SIZE = 25;

	private final String sql;

	private final Map<String, Object> parameters = new HashMap<>();

	private String whereSql;

	private Page page;

	private Sort sort;

	private byte parameterIndex;

	protected JPQLSpecification(String sql) {
		this.sql = sql;
	}

	private void where(String jpql) {
		if (whereSql == null) {
			whereSql = " WHERE ";
		}
		else {
			whereSql += " AND ";
		}

		whereSql += jpql;
	}

	protected void equals(String field, Object value) {
		if (value == null) {
			isNull(field);
		}
		else {
			where("%s = :%s".formatted(field, parameter(value)));
		}
	}

	protected void lessThan(String field, Object value) {
		where("%s < :%s".formatted(field, parameter(value)));
	}

	protected void lessThanOrEqual(String field, Object value) {
		where("%s <= :%s".formatted(field, parameter(value)));
	}

	protected void greaterThan(String field, Object value) {
		where("%s > :%s".formatted(field, parameter(value)));
	}

	protected void greaterThanOrEqual(String field, Object value) {
		where("%s >= :%s".formatted(field, parameter(value)));
	}

	protected void between(String field, Object value1, Object value2) {
		where("%s BETWEEN :%s AND :%s".formatted(field, parameter(value1), parameter(value2)));
	}

	/**
	 * Use a pattern on the given field.
	 * <p>
	 * The percent sign <code>%</code> represents zero, one, or multiple characters
	 * The underscore sign <code>_</code> represents one, single character
	 *
	 * @param field   the field to compare
	 * @param pattern The pattern to search on
	 */
	protected void like(String field, String pattern) {
		where(field + " LIKE :%s".formatted(parameter(pattern)));
	}

	protected void isNull(String field) {
		where("%s IS NULL".formatted(field));
	}

	private String parameter(Object value) {
		var parameter = "value" + parameterIndex++;
		parameters.put(parameter, value);
		return parameter;
	}

	List<T> query(SpecificationRepository<T, Id> repository) {
		PanacheQuery<T> query;

		var finalSql = sql;
		if (whereSql != null) {
			if (finalSql == null) {
				finalSql = whereSql;
			}
			else {
				finalSql += whereSql;
			}
		}

		if (finalSql.length() > 0) {
			if (sort != null) {
				query = repository.find(finalSql, sort, parameters);
			}
			else {
				query = repository.find(finalSql, parameters);
			}
		}
		else {
			if (sort != null) {
				query = repository.findAll(sort);
			}
			else {
				query = repository.findAll();
			}
		}

		if (page == null) {
			page = Page.ofSize(DEFAULT_PAGE_SIZE);
		}

		try (var devices = query.page(page).stream()) {
			return new ArrayList<>(devices.toList());
		}
	}

	public JPQLSpecification<T, Id> sort(Sort sort) {
		this.sort = sort;
		return this;
	}

	public JPQLSpecification<T, Id> page(Page page) {
		this.page = page;
		return this;
	}

}
