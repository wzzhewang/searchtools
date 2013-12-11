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
     * ���info level��log��Ϣ.
     *
     * @param logger ��־��¼��
     * @param message log��Ϣ,��:<code>xxx{0},xxx{1}...</code>
     * @param params log��ʽ������,����length��message������������ͬ, ��:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void info(Logger logger, String message, Object... params) {
        if (logger.isInfoEnabled()) {
            logger.info(format(message, params));
        }
    }

    /**
     * ���info level��log��Ϣ.
     *
     * @param throwable �쳣����
     * @param logger  ��־��¼��
     * @param message log��Ϣ,��:<code>xxx{0},xxx{1}...</code>
     * @param params log��ʽ������,����length��message������������ͬ, ��:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void info(Throwable throwable, Logger logger, String message, Object... params) {
        if (logger.isInfoEnabled()) {
            logger.info(format(message, params), throwable);
        }
    }

    /**
     * ���warn level��log��Ϣ.
     *
     * @param logger ��־��¼��
     * @param message log��Ϣ,��:<code>xxx{0},xxx{1}...</code>
     * @param params log��ʽ������,����length��message������������ͬ, ��:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void warn(Logger logger, String message, Object... params) {
        if (logger.isWarnEnabled()) {
            logger.warn(format(message, params));
        }
    }

    /**
     * ���warn level��log��Ϣ.
     *
     * @param throwable �쳣����
     * @param logger  ��־��¼��
     * @param message log��Ϣ,��:<code>xxx{0},xxx{1}...</code>
     * @param params log��ʽ������,����length��message������������ͬ, ��:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void warn(Throwable throwable, Logger logger, String message, Object... params) {
        if (logger.isWarnEnabled()) {
            logger.warn(format(message, params), throwable);
        }
    }

    /**
     * ���debug level��log��Ϣ.
     *
     * @param logger  ��־��¼��
     * @param message log��Ϣ,��:<code>xxx{0},xxx{1}...</code>
     * @param params log��ʽ������,����length��message������������ͬ, ��:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void debug(Logger logger, String message, Object... params) {
        if (logger.isDebugEnabled()) {
            logger.debug(format(message, params));
        }
    }

    /**
     * ���debug level��log��Ϣ.
     *
     * @param throwable �쳣����
     * @param logger  ��־��¼��
     * @param message log��Ϣ,��:<code>xxx{0},xxx{1}...</code>
     * @param params log��ʽ������,����length��message������������ͬ, ��:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void debug(Throwable throwable, Logger logger, String message, Object... params) {
        if (logger.isDebugEnabled()) {
            logger.debug(format(message, params), throwable);
        }
    }

    /**
     * ���error level��log��Ϣ.
     *
     * @param logger  ��־��¼��
     * @param message log��Ϣ,��:<code>xxx{0},xxx{1}...</code>
     * @param params log��ʽ������,����length��message������������ͬ, ��:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void error(Logger logger, String message, Object... params) {
        if (logger.isErrorEnabled()) {
            logger.error(format(message, params));
        }
    }

    /**
     * ���error level��log��Ϣ.
     *
     * @param throwable �쳣����
     * @param logger  ��־��¼��
     * @param message log��Ϣ,��:<code>xxx{0},xxx{1}...</code>
     * @param params log��ʽ������,����length��message������������ͬ, ��:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    public static void error(Throwable throwable, Logger logger, String message, Object... params) {
        if (logger.isErrorEnabled()) {
            logger.error(format(message, params), throwable);
        }
    }

    /**
     * ��־��Ϣ��������ʽ��
     *
     * @param message log��Ϣ,��:<code>xxx{0},xxx{1}...</code>
     * @param params log��ʽ������,����length��message������������ͬ, ��:<code>Object[]  object=new Object[]{"xxx","xxx"}</code>
     */
    private static String format(String message, Object... params) {
        if (params != null && params.length != 0) {
            return MessageUtil.formatMessage(message, params);
        }
        return message;

    }
}
