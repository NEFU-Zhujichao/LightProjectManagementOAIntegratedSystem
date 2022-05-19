package com.xxx.server.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.xxx.server.pojo.Admin;
import com.xxx.server.service.AdminService;
import com.xxx.server.utils.FastDFSUtils;
import com.xxx.server.utils.RespBean;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 个人中心
 *
 * @author bing  @create 2021/1/20-下午5:14
 */
@RestController
public class AdminInfoController {

    @Autowired
    private AdminService adminService;

    @ApiOperation(value = "更新当前用户信息")
    @PutMapping("/admin/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication) {
        if (adminService.updateById(admin)) {
            // 更新成功重新设置 authentication 对象
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin, null,
                    authentication.getAuthorities()));
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/admin/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String, Object> info) {
        String oldPass = (String) info.get("oldPass");
        String pass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        return adminService.updateAdminPassword(oldPass,pass,adminId);
    }

    @ApiOperation(value = "更新用户头像")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "头像", dataType = "MultipartFile")})
    @PostMapping("/admin/userface")
    public RespBean updateAdminUserFace(MultipartFile file,Integer id,Authentication authentication) throws IOException {
        //获取上传文件地址
        String url = addPicture(file);
        return adminService.updateAdminUserFace(url, id, authentication);
    }


    @ApiOperation(value = "获取文件URL")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "URL", dataType = "MultipartFile")})
    @PostMapping("/url")
    public String addPicture(MultipartFile file) throws IOException {

        //上传图片
        String oldFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String newFileName = uuid + "-" + oldFileName;
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "testEndPoint";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "testAccessKeyId";
        String accessKeySecret = "TestAccessKeySecret";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "testBucketName";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        InputStream inputStream = file.getInputStream();
        PutObjectResult result = ossClient.putObject(bucketName, newFileName, inputStream);
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 180);
        URL url = ossClient.generatePresignedUrl("nljbucket", newFileName, expiration);
        ossClient.shutdown();
        return url.toString();
    }
}
