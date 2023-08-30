package top.anets.oauth2.sentinel;

/**
 * @author ftm
 * @date 2023-08-28 17:27
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanUtil implements ApplicationContextAware {
    private static Logger log = LoggerFactory.getLogger(SpringBeanUtil.class);
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (context == null) {
            context = applicationContext;
        }
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static Object getBean(String name) {
        try {
            return getContext().getBean(name);
        } catch (Exception e) {
            log.error("系统异常", e);
            return null;
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return getContext().getBean(clazz);
        } catch (Exception e) {
            log.error("系统异常", e);
            return null;
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        try {
            return getContext().getBean(name, clazz);
        } catch (Exception e) {
            log.error("系统异常", e);
            return null;
        }
    }

}
