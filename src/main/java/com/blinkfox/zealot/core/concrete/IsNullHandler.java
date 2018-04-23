package com.blinkfox.zealot.core.concrete;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.core.IConditHandler;
import com.blinkfox.zealot.core.builder.XmlSqlInfoBuilder;
import com.blinkfox.zealot.helpers.ParseHelper;
import com.blinkfox.zealot.helpers.StringHelper;
import com.blinkfox.zealot.helpers.XmlNodeHelper;

import org.dom4j.Node;

/**
 * " IS NULL "和" IS NOT NULL "动态sql片段生成的实现类.
 * <p>标签基础示例:'<isnull match="a.id != empty" field="a.name"></isnull>'</p>
 * @author blinkfox on 2018-4-23.
 */
public class IsNullHandler implements IConditHandler {

    /**
     * 构建" IS NULL "和" IS NOT NULL "查询的动态条件sql.
     * @param source 构建所需的资源对象
     * @return 返回SqlInfo对象
     */
    @Override
    public SqlInfo buildSqlInfo(BuildSource source) {
        Node node = source.getNode();

        /* 获取field和match属性,如果匹配中'match'属性没有值或者值为真，则认为是必然生成项. */
        String fieldText = XmlNodeHelper.getAndCheckNodeText(node, ZealotConst.ATTR_FIELD);
        String matchText = XmlNodeHelper.getNodeAttrText(node, ZealotConst.ATTR_MATCH);
        Boolean matchValue = (Boolean) ParseHelper.parseExpressWithException(matchText, source.getParamObj());
        if (StringHelper.isBlank(matchText) || Boolean.TRUE.equals(matchValue)) {
            return XmlSqlInfoBuilder.newInstace(source).buildIsNullSql(fieldText);
        }

        return source.getSqlInfo();
    }
}