package muschterm.ofx_api.db;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;

public interface SpecificationRepository<T, Id> extends PanacheRepositoryBase<T, Id> {

	default List<T> query(JPQLSpecification<T, Id> specification) {
		return specification.query(this);
	}

}
