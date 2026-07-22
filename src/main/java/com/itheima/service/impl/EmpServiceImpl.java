package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.*;
import com.itheima.service.EmpLogService;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {


    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Autowired
    private EmpLogService empLogService;


    //---------原始分页查询
//    @Override
//    public PageResult<Emp> page(Integer page, Integer pageSize) {
//        //1.调用mapper接口，查询总记录数
//        Long total=empMapper.count();
//        //2.调用mapper接口，查询结果列表
//        Integer start=(page-1)*pageSize;
//        List<Emp> rows = empMapper.list(start,pageSize);
//        //3.封装结果 PageResult
//        return new PageResult<Emp>(total,rows);
//    }


    //PageHelper实现分页查询
//    @Override
//    public PageResult<Emp> page(Integer page, Integer pageSize, String name, Integer gender, LocalDate begin, LocalDate end) {
//        //1.设置分页参数
//        PageHelper.startPage(page,pageSize);
//
//        //2.执行查询
//        List<Emp> empList = empMapper.list(name,gender,begin,end);
//
//        //3.解析查询结果，并封装
//        Page<Emp> p = (Page<Emp>) empList;
//        return new PageResult<Emp>(p.getTotal(),p.getResult());
//
//    }

    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        //1.设置分页参数
        PageHelper.startPage(empQueryParam.getPage(),empQueryParam.getPageSize());

        //2.执行查询
        List<Emp> empList = empMapper.list(empQueryParam);

        //3.解析查询结果，并封装
        Page<Emp> p = (Page<Emp>) empList;
        return new PageResult<Emp>(p.getTotal(),p.getResult());

    }

    //新增员工
    //@Transactional//事务管理的注解 - 默认出现运行时异常RunTimeException才会回滚
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void save(Emp emp) {
        try {
            //1.保存员工基本信息
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);
            //2.保存员工工作经历信息
            List<EmpExpr> exprList=emp.getExprList();
            if(!CollectionUtils.isEmpty(exprList)){
                //遍历集合，为empID赋值
                exprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
                empExprMapper.insertBatch(exprList);
            }
        } finally {
            EmpLog empLog=new EmpLog(null,LocalDateTime.now(),"新增员工:"+emp);
            empLogService.insertLog(empLog);
        }
    }

    //批量删除员工
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void delete(List<Integer> ids) {
        //1.删除员工的基本信息
        empMapper.deleteByIds(ids);

        //2.删除员工的工作经历信息
        empExprMapper.deleteByEmpIds(ids);
    }


    //根据ID查询员工信息
    @Override
    public Emp getInfo(Integer id) {
        return empMapper.getById(id);
    }


    //修改员工信息
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        //1.根据ID修改员工的基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);
        //2.根据ID修改员工工作经历信息
        //2.1先根据员工ID删除掉原有的工作经历
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));
        //2.2再添加这个员工新的工作经历
        List<EmpExpr> exprList = emp.getExprList();
        if(!exprList.isEmpty()){
            exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    //员工登录
    @Override
    public LoginInfo login(Emp emp) {
        //1.调用mapper接口，根据用户名和密码查询员工信息
        Emp e = empMapper.selectByUsernameAndPassword(emp);
        //2.判断：是否存在这个员工，如果存在，组装登录成功信息
        if(e != null){
            log.info("登录成功，员工信息: {}",e);
            return new LoginInfo(e.getId(),e.getUsername(),e.getName(),"");
        }
        //3.不存在，返回null
        return null;
    }


}
