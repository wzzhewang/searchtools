package com.alipay.tools.search.util;

import com.alibaba.common.lang.MessageUtil;
import com.alibaba.common.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: wangzhe
 * Date: 12/11/13
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogUtil {
    public static void error(Logger logger, String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message);
        }
    }

    public static void error(Logger logger, String message, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            logger.error(message, throwable);
        }
    }

    public static void warn(Logger logger, String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    public static void warn(Logger logger, String message, Throwable throwable) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, throwable);
        }
    }

    public static void info(Logger logger, String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public static void debug(Logger logger, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public static void debug(Logger logger, String message, Throwable throwable) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, throwable);
        }
    }
    /**
     * 输出info level的log信息.
     *
     * @param logger 日志记录器
     * @param message log信息,如:<code>xxx{0},xxx{1}...</code>
     * @param params log格式化参数,数组length与message参数化个数相同, 如:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void info(Logger logger, String message, Object... params) {
        if (logger.isInfoEnabled()) {
            logger.info(format(message, params));
        }
    }

    /**
     * 输出info level的log信息.
     *
     * @param throwable 异常对象
     * @param logger  日志记录器
     * @param message log信息,如:<code>xxx{0},xxx{1}...</code>
     * @param params log格式化参数,数组length与message参数化个数相同, 如:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void info(Throwable throwable, Logger logger, String message, Object... params) {
        if (logger.isInfoEnabled()) {
            logger.info(format(message, params), throwable);
        }
    }

    /**
     * 输出warn level的log信息.
     *
     * @param logger 日志记录器
     * @param message log信息,如:<code>xxx{0},xxx{1}...</code>
     * @param params log格式化参数,数组length与message参数化个数相同, 如:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void warn(Logger logger, String message, Object... params) {
        if (logger.isWarnEnabled()) {
            logger.warn(format(message, params));
        }
    }

    /**
     * 输出warn level的log信息.
     *
     * @param throwable 异常对象
     * @param logger  日志记录器
     * @param message log信息,如:<code>xxx{0},xxx{1}...</code>
     * @param params log格式化参数,数组length与message参数化个数相同, 如:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void warn(Throwable throwable, Logger logger, String message, Object... params) {
        if (logger.isWarnEnabled()) {
            logger.warn(format(message, params), throwable);
        }
    }

    /**
     * 输出debug level的log信息.
     *
     * @param logger  日志记录器
     * @param message log信息,如:<code>xxx{0},xxx{1}...</code>
     * @param params log格式化参数,数组length与message参数化个数相同, 如:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void debug(Logger logger, String message, Object... params) {
        if (logger.isDebugEnabled()) {
            logger.debug(format(message, params));
        }
    }

    /**
     * 输出debug level的log信息.
     *
     * @param throwable 异常对象
     * @param logger  日志记录器
     * @param message log信息,如:<code>xxx{0},xxx{1}...</code>
     * @param params log格式化参数,数组length与message参数化个数相同, 如:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void debug(Throwable throwable, Logger logger, String message, Object... params) {
        if (logger.isDebugEnabled()) {
            logger.debug(format(message, params), throwable);
        }
    }

    /**
     * 输出error level的log信息.
     *
     * @param logger  日志记录器
     * @param message log信息,如:<code>xxx{0},xxx{1}...</code>
     * @param params log格式化参数,数组length与message参数化个数相同, 如:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void error(Logger logger, String message, Object... params) {
        if (logger.isErrorEnabled()) {
            logger.error(format(message, params));
        }
    }

    /**
     * 输出error level的log信息.
     *
     * @param throwable 异常对象
     * @param logger  日志记录器
     * @param message log信息,如:<code>xxx{0},xxx{1}...</code>
     * @param params log格式化参数,数组length与message参数化个数相同, 如:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void error(Throwable throwable, Logger logger, String message, Object... params) {
        if (logger.isErrorEnabled()) {
            logger.error(format(message, params), throwable);
        }
    }

    /**
     * 日志信息参数化格式化
     *
     * @param message log信息,如:<code>xxx{0},xxx{1}...</code>
     * @param params log格式化参数,数组length与message参数化个数相同, 如:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    private static String format(String message, Object... params) {
        if (params != null && params.length != 0) {
            return MessageUtil.formatMessage(message, params);
        }
        return message;

    }
}
