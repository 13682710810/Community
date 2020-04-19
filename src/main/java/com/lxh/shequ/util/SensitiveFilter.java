package com.lxh.shequ.util;


import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: shequ
 * @description: 过滤敏感词
 * @author: KaiDo
 * @return:
 * @create: 2020-04-10 14:23
 **/
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换符号
    private static final String REPLACEMENT = "***";

    // 根节点
    private TrieNode rootNode = new TrieNode();

    /** 
    * @Description: 初始化前缀树，将敏感词添加到树 
    */
    @PostConstruct  
    public void init() {
        //把流放到try中，会自动关闭
        try (   //从target/classes下读取文件
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-word.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败: " + e.getMessage());
        }
    }

    /** 
    * @Description: 将敏感词添加到前缀树中
    */
    private void addKeyword(String keyword) {
        //默认指向根
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            //当前字符为c
            char c = keyword.charAt(i);
            //查询是否有子节点
            TrieNode childNode = tempNode.getChildNode(c);
            if (childNode == null) {
                // 初始化子节点
                childNode = new TrieNode();
                tempNode.addChildNode(c, childNode);
            }
            // 指向子节点,进入下一轮循环
            tempNode = childNode;
            // 设置结束标识
            if (i == keyword.length() - 1) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词   :stf混蛋=>stf***
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        // 指针1
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        // 存储结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);

            // 跳过符号
            if (isSymbol(c)) {
                // 若指针1处于根节点,将此符号计入结果,让指针2向下走一步
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头或中间,指针3都向下走一步
                position++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getChildNode(c);
            //情况一：下级没有节点
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            }
            //情况二：发现敏感词
            else if (tempNode.isKeyWordEnd()) {
                // 发现敏感词,将begin~position字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            }
            //情况三：指针1没有检查到叉号标记，指针2不动，3继续走
            else {
                // 检查下一个字符
                position++;
            }
        }

        // 将最后一批字符计入结果
        sb.append(text.substring(begin));

        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        //isAsciiAlphanumeri()判断是否为合法字符，不合法返回false；
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //前缀树
    private class TrieNode{
        //敏感词树的最终节点标记
        private boolean isKeyWordEnd = false;

        //子节点（key下级字符 , value下级节点）
        private Map<Character, TrieNode> childNode = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        //添加子节点
        public void addChildNode(Character c,TrieNode node){
            childNode.put(c,node);
        }
        // 获取子节点
        public TrieNode getChildNode(Character c) {
            return childNode.get(c);
        }



    }
}
