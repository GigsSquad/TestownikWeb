package pl.wropol.service;

import java.util.List;

/**
 * Created by evelan on 1/15/16.
 */
public interface IService<T> {

    void save(T entity);

    List<T> findAll();

    Long count();

    T findOne(Long id);
}
