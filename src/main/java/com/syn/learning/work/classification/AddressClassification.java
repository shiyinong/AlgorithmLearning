//package com.syn.learning.work.classification;
//
//import com.hankcs.hanlp.HanLP;
//import com.hankcs.hanlp.seg.Segment;
//import com.hankcs.hanlp.seg.common.Term;
//import org.apache.commons.io.IOUtils;
//import org.tensorflow.Graph;
//import org.tensorflow.Session;
//import org.tensorflow.Tensor;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author shiyinong
// * @version 1.0
// * @date 2019/8/27 10:52
// **/
//public class AddressClassification {
//
//    private static Integer inputLength;
//
//    private static Segment segment;
//
//    static {
//        segment = HanLP.newSegment()
//                .enablePlaceRecognize(true)
//                .enableOrganizationRecognize(true)
//                .enableAllNamedEntityRecognize(true);
//        segment.seg("苏州市太仓市江苏省苏州市太仓市三港路与郑和东路交叉口东100米,苏州市");
//    }
//
//    public static void classification(String path, String path2) throws Exception {
//
//        Map<String, Integer> vocab = getVocab(path2);
//
//        Graph graph = new Graph();
//        byte[] byteArray = new byte[0];
//        try {
//            byteArray = IOUtils.toByteArray(new FileInputStream(path));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        graph.importGraphDef(byteArray);
//        Session session = new Session(graph);
//        String str = "罗阳路258弄99号";
//
//        String dir = "D:\\work\\NavGeocoder相关\\地址分类\\cnn-text-classification-tf-master\\data2\\";
//        BufferedReader br = new BufferedReader(new FileReader(dir+"address_test.txt"));
//        String line;
//        while ((line = br.readLine()) != null) {
//            int[][] input = new int[1][];
//            input[0] = getInput(vocab, line);
//            long s = System.currentTimeMillis();
//
//            List<Tensor> tensors = session.runner()
//                    .feed("dropout_keep_prob", Tensor.create((float) 1))
//                    .feed("input_x", Tensor.create(input))
//                    .fetch("output/softmax_scores")
//                    .fetch("output/predictions")
//                    .run();
//            Tensor score = tensors.get(0);
//            Tensor prediction = tensors.get(1);
//
//            float[][] scoreArray = new float[1][2];
//            score.copyTo(scoreArray);
//            long[] predictionArray = new long[1];
//            prediction.copyTo(predictionArray);
//            System.out.print((System.currentTimeMillis() - s)
//                    + "  " + scoreArray[0][0]
//                    + "  " + scoreArray[0][1]);
//            System.out.println(line+"  "+(predictionArray[0]==0?"address":"poi"));
//        }
//    }
//
//    private static Map<String, Integer> getVocab(String path) throws Exception {
//        Map<String, Integer> vocab = new HashMap<>();
//        BufferedReader br = new BufferedReader(new FileReader(path));
//        String line;
//        inputLength = Integer.parseInt(br.readLine());
//        while ((line = br.readLine()) != null) {
//            String[] strs = line.split(":");
//            vocab.put(strs[0], Integer.parseInt(strs[1]));
//        }
//        return vocab;
//    }
//
//    private static int[] getInput(Map<String, Integer> vocab, String str) {
//        List<Term> terms = segment.seg(str);
//        int[] res = new int[inputLength];
//        for (int i = 0; i < Math.min(res.length, terms.size()); i++) {
//            res[i] = vocab.getOrDefault(terms.get(i).word, 0);
//        }
//        return res;
//    }
//}
