package com.xxx.server.controller;

import com.xxx.server.pojo.Position;
import com.xxx.server.pojo.Project;
import com.xxx.server.pojo.dto.ScheduleDTO;
import com.xxx.server.service.ProjectService;
import com.xxx.server.utils.AdminUtils;
import com.xxx.server.utils.RespBean;
import com.xxx.server.utils.RespPageBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author bing  @create 2021/1/14-上午12:09
 */
@RestController
@RequestMapping("/sta/all")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @ApiOperation(value = "获取所有项目信息(分页)")
    @GetMapping("/")
    public RespPageBean getAllProject(@RequestParam(defaultValue = "1") Integer currentPage,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      String name) {
        return projectService.getAllProjectByPage(name,currentPage,size);
    }

    @ApiOperation(value = "获取所有项目信息")
    @GetMapping("/all")
    public List<Project> getAllProject() {
        return projectService.list();
    }

    @ApiOperation(value = "项目排期")
    @PostMapping("/schedule")
    public List<Project> projectSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return projectService.projectSchedule(scheduleDTO.getName(),scheduleDTO.getTime());
    }

    @ApiOperation(value = "项目立项")
    @PostMapping("/")
    public RespBean projectCreate(@RequestBody Project project){
        project.setProjectCreateTime(LocalDate.now());
        project.setPeriod(project.getProjectEndTime().getDayOfYear() - project.getProjectStartTime().getDayOfYear());
        if(project.getProjectAdmin() == null || project.getProjectAdmin() == ""){
            project.setProjectAdmin(AdminUtils.getCurrentAdmin().getName());
        }
        if (projectService.save(project)){
            return RespBean.success("项目提出成功！");
        }
        return RespBean.error("项目提出失败！");
    }

    @ApiOperation(value = "更新项目信息")
    @PutMapping("/")
    public RespBean updateProject(@RequestBody Project project) {
        project.setProjectCreateTime(LocalDate.now());
        project.setPeriod(project.getProjectEndTime().getDayOfYear() - project.getProjectStartTime().getDayOfYear());
        if (projectService.updateById(project)) {
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "删除单条项目信息")
    @DeleteMapping("/{id}")
    public RespBean deleteProject(@PathVariable Integer id) {
        if (projectService.removeById(id)) {
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "批量删除项目信息")
    @DeleteMapping("/")
    public RespBean deleteProjectByIds(Integer[] ids) {
        if (projectService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }
}
