package projectbase.domain;

import java.util.List;
import java.util.Map;

import projectbase.utils.HqlCriterion;
import projectbase.utils.Pager;
    public interface ICommonBD<T extends  BaseDomainObjectWithTypedId<Integer>>  extends  IBusinessDelegate 
    {
    	IGenericDao<T> getDao();
        List<T> GetAll();
        T Get(Integer id);
        void Save(T domainObject);
        void Delete(T domainObject);
        void Delete(int id);
        int GetCountByQuery(String where);
        List<T> GetByQuery(String where);
        List<T> GetByQuery(String where, String sort);
        List<T> GetByQuery(Pager pager);
        List<T> GetByQuery(Pager pager, String where);
        List<T> GetByQuery(Pager pager, String where, String sort);
        T GetOneByQuery(String where);
        T GetOneByQuery(String where, boolean unique);
        T GetOneByQuery(String where, String sort);
        T GetOneByQuery(String where, String sort, boolean unique);
        DORef<T> GetRef(int id);
        List<DORef<T>> GetRefList();
        List<DORef<T>> GetRefList(String where, String sort);
        <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass, String where, String sort);
        <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass,String where, String sort,
                                    Map<String, String> propNameMap);
        <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass, String selector,String where, String sort);
        <TDto> List<TDto> GetDtoList( Class<TDto> dtoClass, String where, String sort);
        <TDto> List<TDto> GetDtoList(Class<TDto> dtoClass,String where, String sort,
                                    Map<String, String> propNameMap);
        <TDto> List<TDto> GetDtoList( Class<TDto> dtoClass, String selector,String where, String sort);
       // IQueryable<T> Query();
        
        //-----------------------
        int GetCountByQuery(HqlCriterion where);
        List<T> GetByQuery(HqlCriterion where);
        List<T> GetByQuery(HqlCriterion where, String sort);
        List<T> GetByQuery(Pager pager, HqlCriterion where);
        List<T> GetByQuery(Pager pager, HqlCriterion where, String sort);
        T GetOneByQuery(HqlCriterion where);
        T GetOneByQuery(HqlCriterion where, boolean unique);
        T GetOneByQuery(HqlCriterion where, String sort);
        T GetOneByQuery(HqlCriterion where, String sort, boolean unique);
        List<DORef<T>> GetRefList(HqlCriterion where);
        List<DORef<T>> GetRefList(HqlCriterion where, String sort);
        <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass, HqlCriterion where);
        <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass, HqlCriterion where, String sort);
        <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass,HqlCriterion where, String sort,
                                    Map<String, String> propNameMap);
        <TDto> List<TDto> GetDtoList(Pager pager, Class<TDto> dtoClass, String selector,HqlCriterion where, String sort);
        <TDto> List<TDto> GetDtoList(Class<TDto> dtoClass, HqlCriterion where, String sort);
        <TDto> List<TDto> GetDtoList( Class<TDto> dtoClass,HqlCriterion where, String sort,
                                    Map<String, String> propNameMap);
        <TDto> List<TDto> GetDtoList( Class<TDto> dtoClass, String selector,HqlCriterion where, String sort);
    }
