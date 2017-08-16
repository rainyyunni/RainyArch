package projectbase.domain.customcollection;

import java.util.List;

import projectbase.utils.Pager;

public interface IDomainCollection<T> {
	// void Owner();
	// void Sort();
	List<T> View(String filter);

	List<T> View(String filter, String sort);
	
	<TDto>List<TDto> View(Class<T> entityClass,Class<TDto> dtoClass);
	<TDto>List<TDto> View(Class<T> entityClass,Class<TDto> dtoClass,String filter);
	<TDto>List<TDto> View(Class<T> entityClass,Class<TDto> dtoClass,String filter, String sort);
	
	List<T> View(Pager pager);

	List<T> View(Pager pager, String filter);

	List<T> View(Pager pager, String filter, String sort);
	
	<TDto>List<TDto> View(Pager pager,Class<T> entityClass,Class<TDto> dtoClass);
	<TDto>List<TDto> View(Pager pager,Class<T> entityClass,Class<TDto> dtoClass,String filter);
	<TDto>List<TDto> View(Pager pager,Class<T> entityClass,Class<TDto> dtoClass,String filter, String sort);
    //全部加载后分页
	List<T> Page(Pager pager);

	boolean Add(T item);

	T GetElementById(int id);

	void Remove(int id);

	boolean empty();

	boolean DbEmpty();

	int DbCount();
}
