package cn.king.controller;

import cn.king.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("upload")
    public R<String> upload(@RequestBody MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        //动态获取文件类型
        String fileSubstring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用uuid随机生成文件名，防止覆盖同名文件
        String filename = UUID.randomUUID().toString() + fileSubstring;
        //这个file是一个临时文件，需要转存到指定位置，否则本次请求完成后会删除临时文件
        log.info("上传文件{}", file.toString());
        //创建一个目录
        File dir = new File(basePath);
        //判断目录是否存在，若不存在则创建
        if (!dir.exists()) {
            dir.mkdir();
        }
        //将文件转存
        file.transferTo(new File(basePath + filename));
        return R.success(filename);
    }

    /**
     * 下载文件
     */
    @GetMapping("/download")
    public void downLoad(String name, HttpServletResponse response) throws Exception {
        //将文件内容读取进来
        FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
        //将文件内容写回浏览器
        ServletOutputStream outputStream = response.getOutputStream();
        //设置返回文件类型
        response.setContentType("image/jpeg");
        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = fileInputStream.read(bytes)) != -1) {
            outputStream.write(bytes);
            outputStream.flush();
        }

        outputStream.close();
        fileInputStream.close();

    }
}
