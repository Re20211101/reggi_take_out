package cn.king.common;

/**
 * 基于TreadLocal封装的工具类，用于保存当前线程的用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        Long id = threadLocal.get();
        return id;
    }

}
