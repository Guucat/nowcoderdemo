package com.example.demo.util;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.CharUtils;
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
 * @author shengyi
 * @create 2022/9/17 - 8:01
 */
@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private final TrieNode root;

    public SensitiveFilter() {
        root = new TrieNode();
        init();
    }

    public void init() {
        try (
                InputStream ls = this.getClass().getClassLoader().getResourceAsStream("file/sensitive-words.txt");
        ){
            if (ls == null) {
                throw new IOException("读取文件失败");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(ls));
            String word = reader.readLine();
            while (word != null) {
                this.insert(word);
                word = reader.readLine();
            }
        } catch (IOException e) {
            logger.error("加载敏感词失败：" + e.getMessage());
        }
    }

    // 时间复杂度O(n) 最坏 n^2 (当每个sub都为前缀且无敏感词时
    public String filter(String word) {
        StringBuilder stb = new StringBuilder();
        StringBuilder newWord = new StringBuilder(word);
        int left = 0;
        int p = 0;
        while (left < newWord.length()) {
            // 控制 p 范围
            if (p >= newWord.length()) {
                left++;
                p = left;
                stb = new StringBuilder();
                continue;
            }
            // 跳过符号
            if (isSymbol(newWord.charAt(p))) {
                if (left == p) {
                    p++;
                    left = p;
                    stb = new StringBuilder();
                } else {
                    p++;
                }
                continue;
            }
            stb.append(newWord.charAt(p));
            String sub = stb.toString();
            // 当前字串sub是敏感词
            if (search(sub)) {
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < p+1 - left; i++) {
                    str.append("*");
                }
                newWord.replace(left, p + 1, str.toString());
                p++;
                left = p;
                stb = new StringBuilder();
            // 是前缀
            } else if (isPrefix(sub)) {
                p++;
            // 正常字符串
            } else {
                left++;
                p = left;
                stb = new StringBuilder();

            }
        }
        return newWord.toString();
    }

    // 是符号？
    public boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF) ; //不是普通字母数字且不能是中文 True
    }

   static class TrieNode {
        private final Map<Character, TrieNode> children;
        private boolean isWord;

        public TrieNode() {
            children = new HashMap<>();
         }

    }

    public void insert(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            if (node.children.get(ch) == null) {
                node.children.put(ch, new TrieNode());
            }
            node = node.children.get(ch);
        }
        node.isWord = true;
    }

    public boolean search(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            if (node.children.get(ch) == null) {
                return false;
            }
            node = node.children.get(ch);
        }
        return node.isWord;
    }

    public boolean isPrefix(String word) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            if (node.children.get(ch) == null) {
                return false;
            }
            node = node.children.get(ch);
        }
        return true;
    }

}
