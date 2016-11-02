package com.blinkfox.zealot.core.concrete;

import org.dom4j.Node;
import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.core.IConditHandler;
import com.blinkfox.zealot.helpers.BuildSqlInfoHelper;
import com.blinkfox.zealot.helpers.Dom4jHelper;
import com.blinkfox.zealot.helpers.ParseHelper;
import com.blinkfox.zealot.helpers.StringHelper;

/**
 * in查询动态sql生成的实现类
 * Created by blinkfox on 2016/10/31.
 */
public class InHandler implements IConditHandler {

	/**
     * 构建in查询的动态条件sql
     * @param source
     * @return
     */
    @Override
    public SqlInfo buildSqlInfo(BuildSource source) {
        /* 获取拼接的参数 */
        SqlInfo sqlInfo = source.getSqlInfo();
        Node node = source.getNode();

		/* 判断必填的参数是否为空 */
        String fieldText = Dom4jHelper.getAndCheckNodeText(node, ZealotConst.ATTR_FIELD);
        String valueText = Dom4jHelper.getAndCheckNodeText(node, ZealotConst.ATTR_VALUE);

		/* 如果匹配中字符没有，则认为是必然生成项 */
        Node matchNode = node.selectSingleNode(ZealotConst.ATTR_MATCH);
        String matchText = Dom4jHelper.getNodeText(matchNode);
        if (StringHelper.isBlank(matchText)) {
            sqlInfo = BuildSqlInfoHelper.buildInSql(source, fieldText, valueText);
        } else {
			/* 如果match匹配成功，则生成数据库sql条件和参数 */
            Boolean isTrue = (Boolean) ParseHelper.parseWithMvel(matchText, source);
            if (isTrue) {
                sqlInfo = BuildSqlInfoHelper.buildInSql(source, fieldText, valueText);
            }
        }

        return sqlInfo;
    }

}