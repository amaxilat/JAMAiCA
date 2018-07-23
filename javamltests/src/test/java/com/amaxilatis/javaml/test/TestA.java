package com.amaxilatis.javaml.test;

import libsvm.LibSVM;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestA {
    
    static Map<String, Integer> map = new HashMap<>();
    static Map<String, Integer> map2 = new HashMap<>();
    static Map<String, Integer> mapOriginal = new HashMap<>();
    
    private static LibSVM knn;
    private static LibSVM knn2;
    
    public static void main(String[] args) {
        new TestA();
    }
    
    public TestA() {
        map.put("low", 0);
        map.put("normal", 0);
        map.put("congestion", 0);
        map2.put("low", 0);
        map2.put("normal", 0);
        map2.put("congestion", 0);
        mapOriginal.put("low", 0);
        mapOriginal.put("normal", 0);
        mapOriginal.put("congestion", 0);
        final Collection<Instance> trainData = new ArrayList<>();
        final Collection<Instance> trainData2 = new ArrayList<>();
        {
            {
                final SparseInstance instance = new SparseInstance();
                instance.put(0, Double.parseDouble("0.0"));
                instance.setClassValue("low");
                trainData.add(instance);
            }
            {
                final SparseInstance instance = new SparseInstance();
                instance.put(0, Double.parseDouble("30"));
                instance.setClassValue("normal");
                trainData.add(instance);
            }
            {
                final SparseInstance instance = new SparseInstance();
                instance.put(0, Double.parseDouble("80"));
                instance.setClassValue("congestion");
                trainData.add(instance);
            }
        }
        {
            Random rand = new Random();
            {
                for (int i = 0; i < 100; i++) {
                    final SparseInstance instance = new SparseInstance();
                    instance.put(0, (double) rand.nextInt(15));
                    instance.setClassValue("low");
                    trainData2.add(instance);
                }
            }
            {
                for (int i = 0; i < 100; i++) {
                    final SparseInstance instance = new SparseInstance();
                    instance.put(0, (double) rand.nextInt(40) + 15);
                    instance.setClassValue("normal");
                    trainData2.add(instance);
                }
            }
            {
                for (int i = 0; i < 100; i++) {
                    final SparseInstance instance = new SparseInstance();
                    instance.put(0, (double) rand.nextInt(45) + 55);
                    instance.setClassValue("congestion");
                    trainData2.add(instance);
                }
            }
        }
        knn = new LibSVM();
        knn2 = new LibSVM();
        knn.buildClassifier(new DefaultDataset(trainData));
        knn2.buildClassifier(new DefaultDataset(trainData2));
    
    
        try (BufferedReader br = new BufferedReader(new FileReader(this.getClass().getClassLoader().getResource("results-12.csv").getFile()))) {
            for (String line; (line = br.readLine()) != null; ) {
                // process the line.
                
                
                String value = line.split(",")[2];
                if (value.equals("value")) {
                    System.out.println("ERROR:" + value);
                    continue;
                }
                String result = line.split(",")[3].replaceAll("urn:oc:tagDomain:TrafficLevel:", "");
                String res = classify(Double.parseDouble(value));
                String res2 = classify2(Double.parseDouble(value));
                map.put(res, map.get(res) + 1);
                map2.put(res2, map2.get(res2) + 1);
                if (res2.equals("congestion") && !res.equals("congestion")) {
                    System.out.println("val:" + value);
                }
                if (res.equals("congestion") && !res2.equals("congestion")) {
                    System.out.println("val:" + value);
                }
                mapOriginal.put(result, mapOriginal.get(result) + 1);
                //                System.out.println(res + " VS " + result);
            }
            // line is not visible here.
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println(map.toString());
        System.out.println(map2.toString());
        //        System.out.println(mapOriginal.toString());
        
        
    }
    
    private static String classify(final double d) {
        final SparseInstance instance = new SparseInstance();
        instance.put(0, d);
        return (String) knn.classify(instance);
    }
    
    private static String classify2(final double d) {
        final SparseInstance instance = new SparseInstance();
        instance.put(0, d);
        return (String) knn2.classify(instance);
    }
}
