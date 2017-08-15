package com.blinkfox.zealot.test.core.concrete;

import com.blinkfox.zealot.test.bean.BuildSource;
import com.blinkfox.zealot.test.bean.SqlInfo;
import com.blinkfox.zealot.test.consts.ZealotConst;
import com.blinkfox.zealot.test.core.IConditHandler;
import com.blinkfox.zealot.test.core.builder.XmlSqlInfoBuilder;
import com.blinkfox.zealot.test.helpers.ParseHelper;
import com.blinkfox.zealot.test.helpers.StringHelper;
import com.blinkfox.zealot.test.helpers.XmlNodeHelper;

import org.dom4j.Node;

/**
 * in查询动态sql生成的实现类.
 * Created by blinkfox on 2016/10/31.
 */
public class InHandler implements IConditHandler {

    /**
     * 构建in查询的动态条件sql.
     * @param source 构建所需的资源对象
     * @return 返回SqlInfo对象
     */
    @Override
    public SqlInfo buildSqlInfo(BuildSource source) {
        /* 获取拼接的参数 */
        SqlInfo sqlInfo = source.getSqlInfo();
        Node node = source.getNode();

        /* 判断必填的参数是否为空 */
        String fieldText = XmlNodeHelper.getAndCheckNodeText(node, ZealotConst.ATTR_FIELD);
        String valueText = XmlNodeHelper.getAndCheckNodeText(node, ZealotConst.ATTR_VALUE);

        /* 如果匹配中字符没有，则认为是必然生成项 */
        Node matchNode = node.selectSingleNode(ZealotConst.ATTR_MATCH);
        String matchText = XmlNodeHelper.getNodeText(matchNode);
        if (StringHelper.isBlank(matchText)) {
            sqlInfo = XmlSqlInfoBuilder.newInstace(source).buildInSql(fieldText, valueText);
        } else {
            /* 如果match匹配成功，则生成数据库sql条件和参数 */
            Boolean isTrue = (Boolean) ParseHelper.parseExpressWithException(matchText, source.getParamObj());
            if (isTrue) {
                sqlInfo = XmlSqlInfoBuilder.newInstace(source).buildInSql(fieldText, valueText);
            }
        }

        return sqlInfo;
    }

}