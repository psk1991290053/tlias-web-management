package com.itheima.service;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {



    //分页查询
    PageResult<Emp> page(EmpQueryParam empQueryParam);

    //新增员工
    void save(Emp emp);

    //批量删除员工信息
    void delete(List<Integer> ids);

    //根据ID查询员工信息
    Emp getInfo(Integer id);

    //修改员工信息
    void update(Emp emp);
}
