package com.xxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.server.mapper.ProjectMapper;
import com.xxx.server.pojo.Project;
import com.xxx.server.service.ProjectService;
import com.xxx.server.utils.RespPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author Bing
 * @since 2021-01-13
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 根据项目提出者所能工作的时间做出项目排期
     * @param name
     * @return
     */
    @Override
    public List<Project> projectSchedule(String name,int time) {
        List<Project> projects = projectMapper.selectList(new QueryWrapper<Project>().eq("projectAdmin", name).eq("enabled",0));
        List<Project> scheduleProjects = scheduleAlgorithm(projects,time);
        // 排期好的项目变更启用状态
        scheduleProjects.forEach(project -> {
            project.setEnabled(1);
            projectMapper.updateById(project);
        });
        return scheduleProjects;
    }

    @Override
    public RespPageBean getAllProjectByPage(String name,Integer currentPage, Integer size) {
        // 开启分页
        Page<Project> page = new Page<>(currentPage, size);
        Page<Project> projectByPage = projectMapper.getAllProjectByPage(page,name);
        return new RespPageBean(projectByPage.getTotal(), projectByPage.getRecords());
    }

    /**
     * 排期算法
     * @param projects
     */
    private List<Project> scheduleAlgorithm(List<Project> projects,int time) {
        // 按照项目结束时间正序排序
        projects.sort((a,b) -> {
            if (a.getProjectEndTime().getYear() != b.getProjectEndTime().getYear()){
                return a.getProjectEndTime().getDayOfYear() - b.getProjectEndTime().getDayOfYear();
            }else {
                return a.getProjectEndTime().getYear() - b.getProjectEndTime().getYear();
            }
        });
        int j = -1;
        List<Project> result = new ArrayList<>();
        // 有项目并且第一个项目的用时小于等于给定时间
        if (projects.size() > 0 && (projects.get(0).getProjectEndTime().getDayOfYear() - projects.get(0).getProjectStartTime().getDayOfYear()) <= time){
            time = time - (projects.get(0).getProjectEndTime().getDayOfYear() - projects.get(0).getProjectStartTime().getDayOfYear());
            j = 0;
            result.add(projects.get(0));
        }
        // 贪心排期
        for(int i = 1;i < projects.size();i++){
            if (projects.get(i).getProjectStartTime().getDayOfYear() >= projects.get(j).getProjectEndTime().getDayOfYear()){
                int temp = projects.get(i).getProjectEndTime().getDayOfYear() - projects.get(i).getProjectStartTime().getDayOfYear();
                if (time < temp) break;
                time = time - (projects.get(i).getProjectEndTime().getDayOfYear() - projects.get(j).getProjectEndTime().getDayOfYear());
                j = i;
                result.add(projects.get(i));
            }
        }
        return result;
    }
}
