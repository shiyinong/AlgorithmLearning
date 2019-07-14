package com.syn.learning.work.splitword;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/7/5 15:14
 **/
public class SplitWordDemo {

    private String str="成都市金堂县成都市金堂县淮口镇成阿工业园区湖南路西段16号";

    private List<String> texts = new ArrayList<>();

    private static Segment segment;
    static {
        segment = HanLP.newSegment()
                .enablePlaceRecognize(true)
                .enableOrganizationRecognize(true)
                .enableAllNamedEntityRecognize(true);
        segment.seg("苏州市太仓市江苏省苏州市太仓市三港路与郑和东路交叉口东100米,苏州市");
    }

    @Test
    public void hanlpDemo() {
//        List<com.hankcs.hanlp.seg.common.Term> terms = HanLP.segment(str);

        long l = System.currentTimeMillis();
//        List<com.hankcs.hanlp.seg.common.Term> terms = segment.seg(str);
//        System.out.println(terms);
        int i=0;
        for (String str : texts) {
            System.out.println(segment.seg(str));
            //System.out.println("===   "+per.seg(str));
            if(i++==10000) break;
        }
        System.out.println(System.currentTimeMillis() - l);
    }

    @Before
    public void readText(){
        BufferedReader br=null;
        try {
            br=new BufferedReader(new FileReader("./data/segment_test.txt"));
            String line;
            while((line=br.readLine())!=null){
                texts.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void ansjDemo(){

        Result parse = ToAnalysis.parse(str);
        List<Term> terms = parse.getTerms();
        System.out.println(terms);
    }

    @Test
    public void wordDemo(){
        List<Word> words = WordSegmenter.segWithStopWords(str);

        System.out.println(words);
    }

    @Test
    public void ikDemo() throws IOException {
        IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(str), true);
        Lexeme lexeme;
        while((lexeme=ikSegmenter.next())!=null){
            System.out.println(lexeme.toString());
        }
    }






}
