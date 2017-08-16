package projectbase.domain;

public interface IGenericDao<T extends BaseDomainObjectWithTypedId<Integer>> extends
		IGenericDaoWithTypedId<T, Integer> {
}
