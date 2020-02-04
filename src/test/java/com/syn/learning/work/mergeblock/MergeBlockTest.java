package com.syn.learning.work.mergeblock;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MergeBlockTest {

    @Test
    public void merge() {
        List<Block> blocks = MergeBlock.parse("C:\\Users\\shiyinong\\Desktop\\TMP_FACE_STINFO.txt");
        List<List<String>> res = MergeBlock.merge(blocks,400);
        int a=0;
    }

}