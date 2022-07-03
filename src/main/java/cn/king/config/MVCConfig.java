package cn.king.config;

import cn.king.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class MVCConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源访问
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /**
     * 设置消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
       //1.创建消息转换器对象
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //2.设置对象转换器，底层使用jackson将java对象转换为json
        mappingJackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());
        //3.将上面的jackson对象追加到mvc框架的消息转换器中
        converters.add(0,mappingJackson2HttpMessageConverter);

    }
}
