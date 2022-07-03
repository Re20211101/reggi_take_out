package cn.king.filter;

import cn.king.common.BaseContext;
import cn.king.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns= "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.获取url
        String requestURI = request.getRequestURI();
        //2.定义不需要被处理的路径
        String[] urls = new String[]{
                "/employee/login", "/employee/logout", "/backend/**", "/front/**", "/common/**",  "/user/sendMsg", "/user/login"
        };
        //3.判断本次请求需不需要处理
        boolean check = check(requestURI, urls);
        //4.如果不需要处理，则放行
        if (check){
            log.info("无需处理：{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //5.如果需要处理，则判断是否登录,已登录
        if (request.getSession().getAttribute("employee")!=null){
            log.info("已登录：{}",requestURI);
            Long id = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request,response);
            return;
        }
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }
        //如果没有登录,向客户端页面发送响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配
     */
    public boolean check(String requestURI, String[] urls) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url,requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }



}
