package com.itheima.mapper;


import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

//员工信息
@Mapper
public interface EmpMapper {
//    -------------原始分页查询的实现方式

//    //查询总记录数
//    @Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
//    public Long count();
//
//    //分页查询
//    @Select("select e.*,d.name deptName from emp e left join dept d on e.dept_id = d.id"+
//            " order by e.update_time desc limit #{start},#{pageSize}")
//    public List<Emp> list(Integer start, Integer pageSize);


    //条件查询员工信息
    //@Select("select e.*,d.name deptName from emp e left join dept d on e.dept_id = d.id"+
            //" order by e.update_time desc")
    public List<Emp> list(EmpQueryParam empQueryParam);

    //新增员工基本信息
    @Options(useGeneratedKeys = true,keyProperty = "id")//获取到生成的主键
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time)" +
            "values(#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);


    //根据ID批量删除员工的基本信息
    void deleteByIds(List<Integer> ids);


    //根据ID查询员工信息以及员工的工作经历信息
    Emp getById(Integer id);

    //根据ID更新员工的基本信息
    void updateById(Emp emp);

    //统计员工职位人数
    List<Map<String,Object>> countEmpJobData();

    //统计员工性别人数
    List<Map<String, Object>> countEmpGenderData();

    //根据用户名和密码查询员工信息
    @Select("select id,username,name from emp where username = #{username} and password = #{password}")
    Emp selectByUsernameAndPassword(Emp emp);

}
