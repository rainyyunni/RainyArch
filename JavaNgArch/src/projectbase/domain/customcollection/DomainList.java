package projectbase.domain.customcollection;

import java.util.ArrayList;
import java.util.List;

import projectbase.utils.Pager;

/// <summary>
/// this class is for initializing a transient collection by user code,
/// and after hibernate takes over it,the PersistenDomainList will be swapped in.
/// so calling it's methods related to persistency will fail because only the PersistenDomainList provides  persistency.
/// </summary>
/// <typeparam name="T"></typeparam>
public class DomainList<T> extends ArrayList implements IDomainList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IDomainList<T> SetFilter(String filter) {
		throw new TransientUserCollectionException();
	}

	public List<T> View(String filter) {
		throw new TransientUserCollectionException();
	}

	public List<T> View(String filter, String sort) {
		throw new TransientUserCollectionException();
	}

	public <TDto> List<TDto> View(Class<T> entityClass, Class<TDto> dtoClass) {
		throw new TransientUserCollectionException();
	}

	public <TDto> List<TDto> View(Class<T> entityClass, Class<TDto> dtoClass, String filter) {
		throw new TransientUserCollectionException();
	}

	public <TDto> List<TDto> View(Class<T> entityClass, Class<TDto> dtoClass, String filter, String sort) {
		throw new TransientUserCollectionException();
	}

	public List<T> View(Pager pager) {
		throw new TransientUserCollectionException();
	}

	public List<T> View(Pager pager, String filter) {
		throw new TransientUserCollectionException();
	}

	public List<T> View(Pager pager, String filter, String sort) {
		throw new TransientUserCollectionException();
	}

	public List<T> Page(Pager pager) {
		throw new TransientUserCollectionException();
	}

	public <TDto> List<TDto> View(Pager pager, Class<T> entityClass, Class<TDto> dtoClass) {
		throw new TransientUserCollectionException();
	}

	public <TDto> List<TDto> View(Pager pager, Class<T> entityClass, Class<TDto> dtoClass, String filter) {
		throw new TransientUserCollectionException();
	}

	public <TDto> List<TDto> View(Pager pager, Class<T> entityClass, Class<TDto> dtoClass, String filter, String sort) {
		throw new TransientUserCollectionException();
	}

	public T GetElementById(int id) {
		throw new TransientUserCollectionException();
	}

	public void Remove(int id) {
		throw new TransientUserCollectionException();
	}

	public boolean Add(T item) {
		throw new TransientUserCollectionException();
	}

	public boolean empty() {

		throw new TransientUserCollectionException();

	}

	public boolean DbEmpty() {

		throw new TransientUserCollectionException();

	}

	public int DbCount() {

		throw new TransientUserCollectionException();

	}

}
