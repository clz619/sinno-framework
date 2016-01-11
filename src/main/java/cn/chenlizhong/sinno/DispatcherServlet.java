package cn.chenlizhong.sinno;

import cn.chenlizhong.sinno.bean.Data;
import cn.chenlizhong.sinno.bean.Handler;
import cn.chenlizhong.sinno.bean.Param;
import cn.chenlizhong.sinno.bean.View;
import cn.chenlizhong.sinno.helper.BeanHelper;
import cn.chenlizhong.sinno.helper.ConfigHelper;
import cn.chenlizhong.sinno.helper.ControllerHelper;
import cn.chenlizhong.sinno.util.CodecUtil;
import cn.chenlizhong.sinno.util.JsonUtil;
import cn.chenlizhong.sinno.util.ReflectionUtil;
import cn.chenlizhong.sinno.util.StreamUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

/**
 * 请求转发器
 *
 * @author : lizhong.chen
 * @version : 1.0
 * @since : 16/1/11 下午4:58
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化相关helper类
        HelperLoader.init();
        //获取ServletContext对象(用于注册Servlet)
        ServletContext servletContext = config.getServletContext();
        //注册处理jsp的Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        //注册处理静态资源的Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取请求方法和请求路径
        // GET POST
        String requestMethod = req.getMethod().toLowerCase();
        // "/"开始的访问路径
        String requestPath = req.getPathInfo();

        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            //获取Class类及其Bean实例
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);

            //创建请求参数
            Map<String, Object> paramMap = Maps.newHashMap();
            //请求参数名 POST
            Enumeration<String> paramNames = req.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String paramValue = req.getParameter(paramName);
                paramMap.put(paramName, paramValue);
            }
            //URL中的参数
            String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if (StringUtils.isNotEmpty(body)) {
                String[] params = body.split("&");
                if (ArrayUtils.isNotEmpty(params)) {
                    for (String param : params) {
                        String[] array = StringUtils.split(param, "=");
                        if (ArrayUtils.isNotEmpty(array) && 2 == array.length) {
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName, paramValue);
                        }
                    }
                }
            }

            Param param = new Param(paramMap);

            Method actionMethod = handler.getActionMethod();

            Object result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);

            if (result instanceof View) {
                //返回jsp页面
                View view = (View) result;
                String path = view.getPath();
                if (StringUtils.isNotEmpty(path)) {
                    if (path.startsWith("/")) {
                        resp.sendRedirect(req.getContextPath() + path);
                    } else {
                        Map<String, Object> model = view.getModel();
                        for (Map.Entry<String, Object> entry : model.entrySet()) {
                            req.setAttribute(entry.getKey(), entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
                    }
                }
            } else if (result instanceof Data) {
                //返回json数据
                Data data = (Data) result;
                Object model = data.getModel();
                if (model != null) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer = resp.getWriter();
                    String json = JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }
        }

    }
}
