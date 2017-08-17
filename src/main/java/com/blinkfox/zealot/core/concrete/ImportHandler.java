package com.blinkfox.zealot.core.concrete;

import com.blinkfox.zealot.bean.BuildSource;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.consts.ZealotConst;
import com.blinkfox.zealot.core.IConditHandler;
import com.blinkfox.zealot.core.Zealot;
import com.blinkfox.zealot.helpers.ParseHelper;
import com.blinkfox.zealot.helpers.StringHelper;
import com.blinkfox.zealot.helpers.XmlNodeHelper;

import org.dom4j.Node;

/**
 * 引用import标签对应的动态sql生成处理器的实现类.
 * <p>引用标签的主要内容：`[import match="" namespace="" zealotid="" value="" /]`</p>
 * Created by blinkfox on 2017/8/15.
 */
public class ImportHandler implements IConditHandler {
    /**
     * 构建import标签的sqlInfo信息.
     * @param source 构建所需的资源对象
     * @return 返回SqlInfo对象
     */
    @Override
    public SqlInfo buildSqlInfo(BuildSource source) {
        // 判断是否生成，如果不匹配则不生成SQL片段.
        String matchText = XmlNodeHelper.getNodeAttrText(source.getNode(), ZealotConst.ATTR_MATCH);
        if (ParseHelper.isNotMatch(matchText, source.getParamObj())) {
            return source.getSqlInfo();
        }

        // 获取命名空间的文本值.
        String nameSpaceText = XmlNodeHelper.getNodeAttrText(source.getNode(), ZealotConst.ATTR_NAME_SPACE);
        String nameSpace = StringHelper.isNotBlank(nameSpaceText) ? nameSpaceText : source.getNameSpace();

        // 获取ZealotId的文本值.
        String zealotIdText = XmlNodeHelper.getNodeAttrText(source.getNode(), ZealotConst.ATTR_ZEALOT_ID);
        Node node = XmlNodeHelper.getNodeBySpaceAndId(nameSpace, zealotIdText);

        // 获取valueText值，如果valueText不为空，则视为将此valueText的解析值再次传入到引入的模板中作为新的上下文参数.
        String valueText = XmlNodeHelper.getNodeAttrText(source.getNode(), ZealotConst.ATTR_VALUE);
        if (StringHelper.isNotBlank(valueText)) {
            Object paramObj = ParseHelper.parseExpressWithException(valueText, source.getParamObj());
            return Zealot.buildSqlInfo(nameSpace, source.getSqlInfo(), node, paramObj);
        }

        // 使用默认参数对象传入到待解析的引入模板中.
        return Zealot.buildSqlInfo(nameSpace, source.getSqlInfo(), node, source.getParamObj());
    }

}