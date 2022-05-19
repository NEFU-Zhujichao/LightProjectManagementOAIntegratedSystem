package com.xxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.server.pojo.Admin;
import com.xxx.server.pojo.Project;
import com.xxx.server.pojo.Role;
import com.xxx.server.utils.RespBean;
import com.xxx.server.utils.RespPageBean;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 项目表 服务类
 * </p>
 *
 * @author Bing
 * @since 2021-01-13
 */
public interface ProjectService extends IService<Project> {

    /**
     * 根据项目提出者做出项目排期
     * @param name
     * @return
     */
    List<Project> projectSchedule(String name,int time);

    RespPageBean getAllProjectByPage(String name,Integer currentPage, Integer size);
}
