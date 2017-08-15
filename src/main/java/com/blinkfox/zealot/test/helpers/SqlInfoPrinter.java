package com.blinkfox.zealot.test.helpers;

import com.blinkfox.zealot.test.bean.SqlInfo;
import com.blinkfox.zealot.test.config.entity.NormalConfig;
import com.blinkfox.zealot.test.config.entity.XmlContext;
import com.blinkfox.zealot.test.log.Log;

import java.util.Arrays;

/**
 * 打印SqlInfo信息的工具类.
 * Created by blinkfox on 2017/07/24.
 */
public class SqlInfoPrinter {

    private static final Log log = Log.get(SqlInfoPrinter.class);

    private static final String PRINT_START = "------------------------------------------------------------ "
            + "Zealot生成的SQL信息 ---------------------------------------------------------";

    private static final String PRINT_END = "---------------------------------------------------------------"
            + "--------------------------------------------------------------------------";

    /** 换行符. */
    private static final String LINE_BREAK = "\n";

    /**
     * 私有构造方法.
     */
    private SqlInfoPrinter() {
        super();
    }

    /**
     * 获取新的实例.
     * @return PrintSqlInfoHelper实例
     */
    public static SqlInfoPrinter newInstance() {
        return new SqlInfoPrinter();
    }

    /**
     * 打印SqlInfo的日志信息.
     * @param nameSpace XML命名空间
     * @param zealotId XML中的zealotId
     * @param sqlInfo 要打印的SqlInfo对象
     * @param hasXml 是否包含xml的打印信息
     */
    public void printZealotSqlInfo(SqlInfo sqlInfo, boolean hasXml, String nameSpace, String zealotId) {
        // 如果可以配置的打印SQL信息，且日志级别是info级别,则打印SQL信息.
        if (NormalConfig.getInstance().isPrintSqlInfo()) {
            StringBuilder sb = new StringBuilder(LINE_BREAK);
            sb.append(PRINT_START).append(LINE_BREAK);

            // 如果是xml版本的SQL，则打印xml的相关信息.
            if (hasXml) {
                sb.append("--zealot xml: ").append(XmlContext.INSTANCE.getXmlPathMap().get(nameSpace))
                        .append(" -> ").append(zealotId).append(LINE_BREAK);
            }

            sb.append("-------- SQL: ").append(sqlInfo.getSql()).append(LINE_BREAK)
                    .append("----- Params: ").append(Arrays.toString(sqlInfo.getParamsArr())).append(LINE_BREAK);
            sb.append(PRINT_END).append(LINE_BREAK);
            log.info(sb.toString());
        }
    }

}