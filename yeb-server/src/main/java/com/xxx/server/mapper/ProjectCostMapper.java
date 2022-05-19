package com.xxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.server.pojo.ProjectCost;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 项目表 Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2021-01-13
 */
@Repository
public interface ProjectCostMapper extends BaseMapper<ProjectCost> {

    Page<ProjectCost> getAllProjectCostByPage(Page<ProjectCost> page);
}
