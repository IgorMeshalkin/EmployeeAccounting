package com.igormeshalkin.dao;

import java.util.List;

public interface EmployeeAccountingDao<T> {

    public List<T> findAll();

    public T findById(Long id);

    public void save(T t);

    public void update(T t);

    public void delete(Long id);
}
