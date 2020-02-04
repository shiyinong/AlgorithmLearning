package com.syn.learning.work.mergeblock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author shiyinong
 * @version 1.0
 * @date 2019/8/29 14:17
 **/
public class MergeBlock {

    public static List<List<String>> merge(List<Block> blocks, int maxCount) {
        Set<Block> visited = new HashSet<>();
        List<List<String>> res = new ArrayList<>();
        for (Block block : blocks) {
            if(visited.contains(block)) continue;
            visited.add(block);
            List<String> merged = new ArrayList<>();
            merged.add(block.getId());
            List<Block> mergedBlock = new ArrayList<>();
            mergedBlock.add(block);
            Queue<Block> que = new LinkedList<>();
            que.offer(block);
            int count = block.getCount();
            while (count < maxCount && !que.isEmpty()) {
                int size = que.size();
                for (int i = 0; i < size && count < maxCount; i++) {
                    Block cur = que.poll();
                    for (Block next : cur.getAdjacent()) {
                        if (visited.contains(next)) continue;
                        visited.add(next);
                        merged.add(next.getId());
                        mergedBlock.add(next);
                        if ((count += next.getCount()) < maxCount) {
                            que.offer(next);
                        } else {
                            break;
                        }
                    }
                }
            }
            for (Block b : mergedBlock) {
                System.out.print(b.getId() + " : " + b.getCount() + "\t");
            }
            System.out.println(count);
            res.add(merged);
        }
        return res;
    }

    public static List<Block> parse(String path) {
        List<Block> blocks = new ArrayList<>();
        Map<String, List<String>> g = new HashMap<>();
        Map<String, Block> map = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                Block block = new Block();
                String[] fields = line.split(",");
                block.setId(fields[0]);
                block.setCount(Integer.parseInt(fields[2]));
                List<String> adjacent = new ArrayList<>();
                Collections.addAll(adjacent, fields[1].split("\\|"));
                g.put(fields[0], adjacent);
                blocks.add(block);
                map.put(block.getId(), block);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Block block : blocks) {
            List<Block> adjacent = new ArrayList<>();
            for (String str : g.get(block.getId())) {
                adjacent.add(map.get(str));
            }
            block.setAdjacent(adjacent);
        }
        return blocks;
    }
}
