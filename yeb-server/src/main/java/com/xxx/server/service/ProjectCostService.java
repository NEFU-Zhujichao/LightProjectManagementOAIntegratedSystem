package com.xxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.server.pojo.ProjectCost;
import com.xxx.server.utils.RespPageBean;

/**
 * <p>
 * 项目表 服务类
 * </p>
 *
 * @author Bing
 * @since 2021-01-13
 */
public interface ProjectCostService extends IService<ProjectCost> {

    RespPageBean getAllProjectCostByPage(Integer currentPage, Integer size);
}
