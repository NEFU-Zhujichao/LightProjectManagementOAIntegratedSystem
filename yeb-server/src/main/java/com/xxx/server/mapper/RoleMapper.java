package com.xxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.server.pojo.Role;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2021-01-13
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户 id 查询对应角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);
}
