package com.hbjycl.basic.dao;

import com.hbjycl.basic.model.Pager;
import com.hbjycl.basic.model.SystemContext;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TransferQueue;

/**
 * Created by hbjycl on 2016/3/19.
 */
public class BaseDao<T> implements IBaseDao<T> {
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Inject
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    private Class<T> clz;

    public Class<T> getClz() {
        if (clz == null) {
            clz = ((Class<T>)
                    (((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
        }
        return clz;
    }

    /**
     * 添加对象
     *
     * @param t
     * @return
     */
    @Override
    public T add(T t) {
        getSession().save(t);
        return t;
    }

    /**
     * 更新对象
     *
     * @param t
     */
    @Override
    public void update(T t) {
        getSession().update(t);
    }

    /**
     * 删除对象
     *
     * @param id
     */
    @Override
    public void delete(int id) {
        getSession().delete(this.load(id));
    }

    /**
     * 根据id加载对象
     *
     * @param id
     * @return
     */
    @Override
    public T load(int id) {
        return (T) getSession().load(getClz(), id);
    }

    /**
     * 不分页列表对象
     *
     * @param hql  查询对象的hql
     * @param args 查询参数
     * @return 一组不分页的列表
     */
    @Override
    public List<T> list(String hql, Object[] args) {
        return this.list(hql, args, null);
    }

    @Override
    public List<T> list(String hql, Object arg) {
        return this.list(hql, new Object[]{arg});
    }

    @Override
    public List<T> list(String hql) {
        return this.list(hql, null);
    }

    /**
     * 基于别名和查询参数的混合列表对象
     *
     * @param hql
     * @param args
     * @param alias 别名参数
     * @return
     */
    @Override
    public List<T> list(String hql, Object[] args, Map<String, Object> alias) {
        hql = initSort(hql);
        Query query = getSession().createQuery(hql);
        setAliasParameter(query, alias);
        setParameter(query, args);
        return query.list();
    }

    @Override
    public List<T> listByAlias(String hql, Map<String, Object> alias) {
        return list(hql, null, alias);
    }

    private String initSort(String hql) {
        String order = SystemContext.getOrder();
        String sort = SystemContext.getSort();
        if (sort != null && !"".equals(sort.trim())) {
            hql += " order by " + sort;
            if (!"desc".equalsIgnoreCase(order)) hql += " asc";
            else hql += " desc";
        }
        return hql;
    }

    private void setParameter(Query query, Object[] args) {
        if (args != null && args.length > 0) {
            int index = 0;
            for (Object arg : args) {
                query.setParameter(index++, arg);
            }
        }
    }

    private void setAliasParameter(Query query, Map<String, Object> alias) {
        if (alias != null) {
            Set<String> keys = alias.keySet();
            for (String key : keys) {
                Object val = alias.get(key);
                if (val instanceof Collection) {
                    query.setParameterList(key, (Collection) val);
                } else {
                    query.setParameter(key, val);
                }
            }
        }
    }

    /**
     * 分页列表对象
     *
     * @param hql  查询对象的hql
     * @param args 查询参数
     * @return 一组不分页的列表
     */
    @Override
    public Pager<T> find(String hql, Object[] args) {
        return this.find(hql, args, null);
    }

    @Override
    public Pager<T> find(String hql, Object arg) {
        return this.find(hql, new Object[]{arg});
    }

    @Override
    public Pager<T> find(String hql) {
        return this.find(hql, null);
    }

    private void setPagers(Query query, Pager pager) {
        Integer pageSize = SystemContext.getPageSize();
        Integer pageOffSet = SystemContext.getPageOffSet();
        if (pageOffSet == null || pageOffSet < 0) pageOffSet = 0;
        if (pageSize == null || pageSize < 0) pageSize = 10;
        query.setFirstResult(pageOffSet).setMaxResults(pageSize);
        pager.setOffset(pageOffSet);
        pager.setSize(pageSize);
    }

    private String getCountHql(String hql, boolean isHql) {
        String end = hql.substring(hql.indexOf("from"));
        String result = "select count(*) " + end;
        if (isHql) {
            result.replaceAll("fetch", "");
        }
        return result;
    }

    /**
     * 基于别名和查询参数的混合分页对象
     *
     * @param hql
     * @param args
     * @param alias 别名参数
     * @return
     */
    @Override
    public Pager<T> find(String hql, Object[] args, Map<String, Object> alias) {
        hql = initSort(hql);
        String countHql = getCountHql(hql,true);
        countHql = initSort(countHql);
        Query countQuery = getSession().createQuery(countHql);
        Query query = getSession().createQuery(hql);
        //设置别名
        setAliasParameter(query, alias);
        setAliasParameter(countQuery, alias);
        //设置参数
        setParameter(query, args);
        setParameter(countQuery, args);
        Pager<T> pager = new Pager<>();
        setPagers(query, pager);
        List<T> datas = query.list();
        pager.setDatas(datas);
        long total = (long) countQuery.uniqueResult();
        pager.setTotal(total);
        return null;
    }

    @Override
    public Pager<T> findByAlias(String hql, Map<String, Object> alias) {
        return find(hql, null, alias);
    }

    /**
     * 根据hql查询一组对象
     *
     * @param hql
     * @param args
     * @return
     */
    @Override
    public Object queryObject(String hql, Object[] args) {
        return queryObject(hql, args, null);
    }

    @Override
    public Object queryObject(String hql, Object arg) {
        return queryObject(hql, new Object[]{arg});
    }

    @Override
    public Object queryObject(String hql) {
        return queryObject(hql, null);
    }

    @Override
    public Object queryObject(String hql, Object[] args, Map<String, Object> alias) {
        Query query = getSession().createQuery(hql);
        setParameter(query, args);
        setAliasParameter(query, alias);
        return query.list();
    }

    @Override
    public Object queryObjectByAlias(String hql, Map<String, Object> alias) {
        return queryObject(hql, null, alias);
    }


    /**
     * 根据hql更新对象
     *
     * @param hql
     * @param args
     */

    @Override
    public void updateByHql(String hql, Object[] args) {
        Query query = getSession().createQuery(hql);
        setParameter(query, args);
        query.executeUpdate();
    }

    @Override
    public void updateByHql(String hql, Object arg) {
        this.updateByHql(hql, new Object[]{arg});
    }

    @Override
    public void updateByHql(String hql) {
        this.updateByHql(hql, null);
    }


    /**
     * 不分页根据sql查询列表，不包含关联对象
     *
     * @param sql       查询的sql语句
     * @param args      查询条件
     * @param clz       查询的实体对象
     * @param hasEntity 该对象是否是一个hibernate所管理的实体，如果不是需要使用seResultTransForm查询
     * @return
     */
    @Override
    public List<Object> listBySql(String sql, Object[] args, Class<T> clz, boolean hasEntity) {
        return listBySql(sql, args, null, clz, hasEntity);
    }

    @Override
    public List<Object> listBySql(String sql, Object arg, Class<T> clz, boolean hasEntity) {
        return listBySql(sql, new Object[]{arg}, clz, hasEntity);
    }

    @Override
    public List<Object> listBySql(String sql, Class<T> clz, boolean hasEntity) {
        return listBySql(sql, null, clz, hasEntity);
    }

    @Override
    public List<Object> listBySql(String sql, Object[] args, Map<String, Object> alias, Class<T> clz, boolean hasEntity) {
        sql = initSort(sql);
        SQLQuery sqlQuery = getSession().createSQLQuery(sql);
        setParameter(sqlQuery, args);
        setAliasParameter(sqlQuery, alias);
        if (hasEntity) {
            sqlQuery.addEntity(clz);
        } else
            sqlQuery.setResultTransformer(Transformers.aliasToBean(clz));
        return sqlQuery.list();
    }

    @Override
    public List<Object> listByAliasSql(String sql, Map<String, Object> alias, Class<T> clz, boolean hasEntity) {
        return listBySql(sql, null, alias, clz, hasEntity);
    }

    /**
     * 分页根据sql查询列表，不包含关联对象
     *
     * @param sql       查询的sql语句
     * @param args      查询条件
     * @param clz       查询的实体对象
     * @param hasEntity 该对象是否是一个hibernate所管理的实体，如果不是需要使用seResultTransForm查询
     * @return
     */
    @Override
    public Pager<Object> findBySql(String sql, Object[] args, Class<T> clz, boolean hasEntity) {
        return findBySql(sql,args,null,clz,hasEntity);
    }

    @Override
    public Pager<Object> findBySql(String sql, Object arg, Class<T> clz, boolean hasEntity) {
        return findBySql(sql,new Object[]{arg},clz,hasEntity);
    }

    @Override
    public Pager<Object> findBySql(String sql, Class<T> clz, boolean hasEntity) {
        return findBySql(sql,null,clz,hasEntity);
    }

    @Override
    public Pager<Object> findBySql(String sql, Object[] args, Map<String, Object> alias, Class<T> clz, boolean hasEntity) {
       String countSql = getCountHql(sql,false);
        countSql = initSort(countSql);
        sql = initSort(sql);
        SQLQuery sqlQuery = getSession().createSQLQuery(sql);
        SQLQuery countQuery = getSession().createSQLQuery(countSql);
        setAliasParameter(sqlQuery,alias);
        setAliasParameter(countQuery,alias);
        setParameter(sqlQuery,args);
        setParameter(countQuery,args);
        Pager pager = new Pager();
        setPagers(sqlQuery,new Pager());
        if(hasEntity){
            sqlQuery.addEntity(clz);
        }
        else sqlQuery.setResultTransformer(Transformers.aliasToBean(clz));
        List<Object> datas= sqlQuery.list();
        pager.setDatas(datas);
        long total = (long) countQuery.uniqueResult();
        pager.setTotal(total);
        return pager;
    }

    @Override
    public Pager<Object> findByAliasSql(String sql, Map<String, Object> alias, Class<T> clz, boolean hasEntity) {
        return findBySql(sql,null,alias,clz,hasEntity);
    }
}
