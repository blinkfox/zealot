package com.blinkfox.zealot.helpers;

import com.blinkfox.zealot.consts.ZealotConst;
import org.dom4j.Node;

/**
 * Zealot相关的帮助类
 * Created by blinkfox on 2016/10/30.
 */
public class ZealotHelper {

    /**
     * 获取xml节点的文本值，如果是对象是空的，则转为空字符串
     * @param node
     * @return
     */
    public static String getNodeText(Node node) {
        return node == null ? "": node.getText();
    }

    /**
     * 检查节点是否为空
     * @param node
     * @return
     */
    public static String getAndCheckNodeText(Node node, String nodeName) {
		/* 判断必填的参数是否为空 */
        Node fieldNode = (Node) node.selectSingleNode(nodeName);
        String fieldText = ZealotHelper.getNodeText(fieldNode);
        if (StringHelper.isBlank(fieldText)) {
            throw new RuntimeException("填写的字段值是空的");
        }
        return fieldText;
    }

    /**
     * 检查和获取开始和结束文本的内容，返回一个数组
     * @param node
     * @return
     */
    public static String[] getBothNodeText(Node node) {
        Node startNode = (Node) node.selectSingleNode(ZealotConst.ATTR_START);
        Node endNode = (Node) node.selectSingleNode(ZealotConst.ATTR_ENT);
        String startText = ZealotHelper.getNodeText(startNode);
        String endText = ZealotHelper.getNodeText(endNode);
        if (StringHelper.isBlank(startText) && StringHelper.isBlank(endText)) {
            throw new RuntimeException("填写的开始和结束字段值是空的");
        }
        return new String[] {startText, endText};
    }

}