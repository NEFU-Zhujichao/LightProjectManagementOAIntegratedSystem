package com.xxx.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.server.mapper.ProjectCostMapper;
import com.xxx.server.pojo.Project;
import com.xxx.server.pojo.ProjectCost;
import com.xxx.server.service.ProjectCostService;
import com.xxx.server.utils.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 职位 服务实现类
 * </p>
 *
 * @author Bing
 * @since 2021-01-13
 */
@Service
public class ProjectCostServiceImpl extends ServiceImpl<ProjectCostMapper, ProjectCost> implements ProjectCostService{

    @Autowired
    private ProjectCostMapper projectCostMapper;

    @Override
    public RespPageBean getAllProjectCostByPage(Integer currentPage, Integer size) {
        // 开启分页
        Page<ProjectCost> page = new Page<>(currentPage, size);
        Page<ProjectCost> projectCostByPage = projectCostMapper.getAllProjectCostByPage(page);
        return new RespPageBean(projectCostByPage.getTotal(), projectCostByPage.getRecords());
    }
}
