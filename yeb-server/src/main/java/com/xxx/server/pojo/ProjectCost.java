package com.xxx.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_project_cost")
@ApiModel(value="ProjectCost对象", description="项目成本")
public class ProjectCost implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "阶段名称")
    private String stageName;

    @ApiModelProperty(value = "阶段成本")
    private double stageCost;

    @ApiModelProperty(value = "完成进度")
    private int schedule;

    @ApiModelProperty(value = "已进行多少天")
    private int days;

    @ApiModelProperty(value = "工期")
    private int period;

    @ApiModelProperty(value = "风险值")
    private double risk;
}
