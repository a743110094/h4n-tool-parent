package site.heaven96.filter.util.cache;

/**
 * 线程工具
 *
 * @author heaven96
 * @date 2022/04/21
 */
public class ThreadUtil {

    /**
     * 创建新线程
     *
     * @param runnable {@link Runnable}
     * @param name     线程名
     * @param isDaemon 是否守护线程
     * @return {@link Thread}
     * @since 4.1.2
     */
    public static Thread newThread(Runnable runnable, String name, boolean isDaemon) {
        final Thread t = new Thread(null, runnable, name);
        t.setDaemon(isDaemon);
        return t;
    }


    /**
     * 创建新线程，非守护线程，正常优先级，线程组与当前线程的线程组一致
     *
     * @param runnable {@link Runnable}
     * @param name     线程名
     * @return {@link Thread}
     * @since 3.1.2
     */
    public static Thread newThread(Runnable runnable, String name) {
        final Thread t = newThread(runnable, name, false);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }

}
