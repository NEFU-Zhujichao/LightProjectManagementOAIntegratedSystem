package com.xxx.server.controller;

import com.xxx.server.pojo.ProjectCost;
import com.xxx.server.service.ProjectCostService;
import com.xxx.server.utils.RespBean;
import com.xxx.server.utils.RespPageBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author bing  @create 2021/1/14-上午12:09
 */
@RestController
@RequestMapping("/sta/pers")
public class ProjectCostController {

    @Autowired
    private ProjectCostService projectCostService;
    // 专家根据经验得出权重值
    private Map<String,Double> weight = new HashMap<String,Double>(){
        {
            put("需求分析",0.3);
            put("概要设计",0.2);
            put("详细设计",0.2);
            put("项目开发",0.2);
            put("项目测试",0.1);
        }
    };

    @ApiOperation(value = "预测项目成本风险")
    @GetMapping("/risk")
    public Map predictProjectRisk() {
        Map<String,List<ProjectCost>> map = new HashMap<>();
        Map<String,Double> result = new HashMap<>();
        getAllProjectCost().forEach(projectCost -> {
            if (map.containsKey(projectCost.getProjectName())){
                List<ProjectCost> projectCosts = map.get(projectCost.getProjectName());
                projectCosts.add(projectCost);
                map.put(projectCost.getProjectName(),projectCosts);
            }else {
                List<ProjectCost> list = new ArrayList<>();
                list.add(projectCost);
                map.put(projectCost.getProjectName(),list);
            }
        });
        map.forEach((k,v) -> {
            final double[] resRisk = {0};
            v.forEach(projectCost -> {
                if (projectCost.getSchedule() == 100){
                    projectCost.setRisk(0);
                }else{
                    BigDecimal tmp = new BigDecimal((double) projectCost.getDays() / projectCost.getPeriod());
                    double schedule = tmp.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    int scheduleInt = (int) (schedule*100);
                    if (scheduleInt <= projectCost.getSchedule()){
                        projectCost.setRisk(0);
                    }else {
                        int risk = scheduleInt - projectCost.getSchedule();
                        if (risk > 0){
                            Double weight = this.weight.get(projectCost.getStageName());
                            double score = new BigDecimal((double) risk / 100 * weight).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            projectCost.setRisk(score);
                            resRisk[0] += score;
                        }
                    }
                }
                projectCostService.updateById(projectCost);
            });
            result.put(k, new BigDecimal(resRisk[0]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        });
        return result;
    }

    @ApiOperation(value = "获取所有项目成本信息(分页)")
    @GetMapping("/")
    public RespPageBean getAllProjectCostByPage(@RequestParam(defaultValue = "1") Integer currentPage,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return projectCostService.getAllProjectCostByPage(currentPage,size);
    }

    @ApiOperation(value = "获取所有项目成本信息")
    @GetMapping("/all")
    public List<ProjectCost> getAllProjectCost() {
        return projectCostService.list();
    }

    @ApiOperation(value = "增加项目阶段成本")
    @PostMapping("/")
    public RespBean projectCostCreate(@RequestBody ProjectCost projectCost){
        if (projectCostService.save(projectCost)){
            return RespBean.success("项目阶段成本添加成功！");
        }
        return RespBean.error("项目阶段成本添加失败！");
    }

    @ApiOperation(value = "更新项目成本信息")
    @PutMapping("/")
    public RespBean updateProjectCost(@RequestBody ProjectCost projectCost) {
        if (projectCostService.updateById(projectCost)) {
            predictProjectRisk();
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "删除单条项目成本信息")
    @DeleteMapping("/{id}")
    public RespBean deleteProjectCost(@PathVariable Integer id) {
        if (projectCostService.removeById(id)) {
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "批量删除项目成本信息")
    @DeleteMapping("/")
    public RespBean deleteProjectCostByIds(Integer[] ids) {
        if (projectCostService.removeByIds(Arrays.asList(ids))) {
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }
}
