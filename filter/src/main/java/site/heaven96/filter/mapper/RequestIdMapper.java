package site.heaven96.filter.mapper;

/**
 * 请求ID映射器
 *
 * @author heaven96
 * @date 2022/04/21
 */
public interface RequestIdMapper {

    /**
     * 最大TTL MS
     */
    long MAX_TTL = 60 * 1000 * 60;
    /**
     * P
     */
    String P = "P";
    /**
     * F
     */
    String F = "F";
    String 请求重复_该请求正在处理 = "请求重复，该请求正在处理！";
    String 请求重复_该请求已处理完毕 = "请求重复，该请求已处理完毕！";

}
