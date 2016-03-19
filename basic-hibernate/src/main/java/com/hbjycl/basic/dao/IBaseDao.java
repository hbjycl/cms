package com.hbjycl.basic.dao;

import com.hbjycl.basic.model.Pager;

import java.util.List;
import java.util.Map;

/**
 * Created by hbjycl on 2016/3/19.
 * 公共的Dao处理对象，这个对象包含了Hibernate的所有基本操作
 */
public interface IBaseDao<T> {

    /**
     * 添加对象
     *
     * @param t
     * @return
     */
    T add(T t);

    /**
     * 更新对象
     *
     * @param t
     */

    void update(T t);

    /**
     * 删除对象
     *
     * @param id
     */

    void delete(int id);

    /**
     * 根据id加载对象
     *
     * @param id
     * @return
     */
    T load(int id);

    /**
     * 不分页列表对象
     *
     * @param hql  查询对象的hql
     * @param args 查询参数
     * @return 一组不分页的列表
     */
    List<T> list(String hql, Object[] args);

    List<T> list(String sql, Object arg);

    List<T> list(String sql);

    /**
     * 基于别名和查询参数的混合列表对象
     *
     * @param hql
     * @param args
     * @param alias 别名参数
     * @return
     */
    List<T> list(String hql, Object[] args, Map<String, Object> alias);

    List<T> listByAlias(String hql, Map<String, Object> alias);


    /**
     * 分页列表对象
     *
     * @param hql  查询对象的hql
     * @param args 查询参数
     * @return 一组不分页的列表
     */
    Pager<T> find(String hql, Object[] args);

    Pager<T> find(String sql, Object arg);

    Pager<T> find(String sql);

    Pager<T> find(String hql, Object[] args, Map<String, Object> alias);

    Pager<T> findByAlias(String hql, Map<String, Object> alias);


    /**
     * 根据hql查询一组对象
     *
     * @param hql
     * @param args
     * @return
     */
    Object queryObject(String hql, Object[] args);

    Object queryObject(String hql, Object arg);

    Object queryObject(String hql);

    Object queryObject(String hql, Object[] args, Map<String, Object> alias);

    Object queryObjectByAlias(String hql, Map<String, Object> alias);

    /**
     * 根据hql更新对象
     *
     * @param hql
     * @param args
     */
    void updateByHql(String hql, Object[] args);

    void updateByHql(String hql, Object arg);

    void updateByHql(String hql);

    /**
     * 不分页根据sql查询列表，不包含关联对象
     *
     * @param sql       查询的sql语句
     * @param args      查询条件
     * @param clz       查询的实体对象
     * @param hasEntity 该对象是否是一个hibernate所管理的实体，如果不是需要使用seResultTransForm查询
     * @return
     */
    List<Object> listBySql(String sql, Object[] args, Class<T> clz, boolean hasEntity);

    List<Object> listBySql(String sql, Object arg, Class<T> clz, boolean hasEntity);

    List<Object> listBySql(String sql, Class<T> clz, boolean hasEntity);

    List<Object> listBySql(String sql, Object[] args, Map<String, Object> alias, Class<T> clz, boolean hasEntity);

    List<Object> listByAliasSql(String sql, Map<String, Object> alias, Class<T> clz, boolean hasEntity);

    /**
     * 分页根据sql查询列表，不包含关联对象
     *
     * @param sql       查询的sql语句
     * @param args      查询条件
     * @param clz       查询的实体对象
     * @param hasEntity 该对象是否是一个hibernate所管理的实体，如果不是需要使用seResultTransForm查询
     * @return
     */
    Pager<Object> findBySql(String sql, Object[] args, Class<T> clz, boolean hasEntity);

    Pager<Object> findBySql(String sql, Object arg, Class<T> clz, boolean hasEntity);

    Pager<Object> findBySql(String sql, Class<T> clz, boolean hasEntity);

    Pager<Object> findBySql(String sql, Object[] args, Map<String, Object> alias, Class<T> clz, boolean hasEntity);

    Pager<Object> findByAliasSql(String sql, Map<String, Object> alias, Class<T> clz, boolean hasEntity);


}
