package com.amaxilatis.javaml.test;

import libsvm.LibSVM;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sparkworks.cs.client.impl.DataClientImpl;
import net.sparkworks.cs.client.impl.ResourceClientImpl;
import net.sparkworks.cs.common.dto.Granularity;
import net.sparkworks.cs.common.dto.QueryTimeRangeResourceDataCriteriaDTO;
import net.sparkworks.cs.common.dto.QueryTimeRangeResourceDataDTO;
import net.sparkworks.cs.common.dto.QueryTimeRangeResourceDataResultDTO;
import net.sparkworks.cs.common.dto.ResourceDTO;
import net.sparkworks.cs.common.dto.ResourceDataDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

import static com.amaxilatis.javaml.test.Constants.GRANT_TYPE;
import static com.amaxilatis.javaml.test.Constants.SCOPE;
import static com.amaxilatis.javaml.test.Constants.SERVER_URL;
import static com.amaxilatis.javaml.test.Constants.TOKEN_URL;

public class TestB {
    
    private static final String EXTREME = "extreme";
    private static final String COMFORTABLE = "comfortable";
    private static final String RECOMMENDED = "recommended";
    private static final String HIGH = "high";
    
    static Map<String, Integer> map = new HashMap<>();
    static Map<String, Integer> map2 = new HashMap<>();
    
    private static LibSVM knn2;
    private static ResourceClientImpl rClient;
    private static DataClientImpl dataClient;
    
    public static void main(String[] args) throws IOException {
        new TestB();
    }
    
    public TestB() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("connection.properties"));
    
        final String clientId = properties.getProperty("clientId");
        final String clientSecret = properties.getProperty("clientSecret");
        final String username = properties.getProperty("username");
        final String password = properties.getProperty("password");
        final String[] resHumidityIds = properties.getProperty("resHumidityIds").split(",");
        
        map.put(EXTREME, 0);
        map.put(COMFORTABLE, 0);
        map.put(RECOMMENDED, 0);
        map.put(HIGH, 0);
        
        map2.put(EXTREME, 0);
        map2.put(COMFORTABLE, 0);
        map2.put(RECOMMENDED, 0);
        map2.put(HIGH, 0);
        
        final Collection<Instance> trainData2 = new ArrayList<>();
        {
            Random rand = new Random();
            {
                for (int i = 0; i < 50; i++) {
                    final SparseInstance instance = new SparseInstance();
                    instance.put(0, (double) rand.nextInt(30));
                    instance.setClassValue("extreeme");
                    trainData2.add(instance);
                }
                for (int i = 0; i < 50; i++) {
                    final SparseInstance instance = new SparseInstance();
                    instance.put(0, (double) rand.nextInt(20) + 80);
                    instance.setClassValue("extreeme");
                    trainData2.add(instance);
                }
            }
            {
                for (int i = 0; i < 70; i++) {
                    final SparseInstance instance = new SparseInstance();
                    instance.put(0, (double) rand.nextInt(15) + 30);
                    instance.setClassValue("comfortable");
                    trainData2.add(instance);
                }
                for (int i = 0; i < 30; i++) {
                    final SparseInstance instance = new SparseInstance();
                    instance.put(0, (double) rand.nextInt(5) + 55);
                    instance.setClassValue("comfortable");
                    trainData2.add(instance);
                }
            }
            {
                for (int i = 0; i < 100; i++) {
                    final SparseInstance instance = new SparseInstance();
                    instance.put(0, (double) rand.nextInt(10) + 45);
                    instance.setClassValue("recommended");
                    trainData2.add(instance);
                }
            }
            {
                for (int i = 0; i < 100; i++) {
                    final SparseInstance instance = new SparseInstance();
                    instance.put(0, (double) rand.nextInt(20) + 60);
                    instance.setClassValue("high");
                    trainData2.add(instance);
                }
            }
        }
        knn2 = new LibSVM();
        knn2.buildClassifier(new DefaultDataset(trainData2));
        
        
        rClient = new ResourceClientImpl(SERVER_URL, clientId, clientSecret, username, password, TOKEN_URL, GRANT_TYPE, SCOPE);
        dataClient = new DataClientImpl(SERVER_URL, clientId, clientSecret, username, password, TOKEN_URL, GRANT_TYPE, SCOPE);
        
        long from = 1517040396000L;
        long to = 1527408397000L;
        for (String resLuminosityId : resHumidityIds) {
            annotateData(from, to, rClient.get(Long.valueOf(resLuminosityId)));
        }
        
        System.out.println(map2.toString());
        System.out.println(map.toString());
        
    }
    
    private static void annotateData(long from, long to, Optional<ResourceDTO> resourceDTO) {
        ArrayList<Double> data = getData(from, to, resourceDTO.get().getResourceId());
        for (Double datum : data) {
            String res = classify2(datum);
            
            map2.put(res, map2.get(res) + 1);
        }
        
        ArrayList<Double> dataOfficeHours = getDataOfficeHours(from, to, resourceDTO.get().getResourceId());
        for (Double datum : dataOfficeHours) {
            String res = classify2(datum);
            map.put(res, map.get(res) + 1);
        }
    }
    
    private static String classify2(final double d) {
        System.out.println(d);
        final SparseInstance instance = new SparseInstance();
        instance.put(0, d);
        return (String) knn2.classify(instance);
    }
    
    
    private static ArrayList<Double> getData(long from, long to, long id) {
        ArrayList<Double> dataList = new ArrayList<>();
        QueryTimeRangeResourceDataDTO dto = new QueryTimeRangeResourceDataDTO();
        dto.setQueries(new ArrayList<>());
        QueryTimeRangeResourceDataCriteriaDTO cDto = new QueryTimeRangeResourceDataCriteriaDTO();
        cDto.setFrom(from);
        cDto.setTo(to);
        cDto.setGranularity(Granularity.HOUR);
        cDto.setResultLimit(1000000);
        cDto.setResourceID(id);
        dto.getQueries().add(cDto);
        QueryTimeRangeResourceDataResultDTO res1 = dataClient.queryTimeRangeResourcesData(dto).get();
        for (QueryTimeRangeResourceDataCriteriaDTO s : res1.getResults().keySet()) {
            for (ResourceDataDTO resourceDataDTO : res1.getResults().get(s).getData()) {
                if (resourceDataDTO.getReading() > 0) {
                    dataList.add(resourceDataDTO.getReading());
                }
            }
        }
        
        return dataList;
    }
    
    private static ArrayList<Double> getDataOfficeHours(long from, long to, long id) {
        ArrayList<Double> dataList = new ArrayList<>();
        QueryTimeRangeResourceDataDTO dto = new QueryTimeRangeResourceDataDTO();
        dto.setQueries(new ArrayList<>());
        QueryTimeRangeResourceDataCriteriaDTO cDto = new QueryTimeRangeResourceDataCriteriaDTO();
        cDto.setFrom(from);
        cDto.setTo(to);
        cDto.setGranularity(Granularity.HOUR);
        cDto.setResultLimit(1000000);
        cDto.setResourceID(id);
        dto.getQueries().add(cDto);
        QueryTimeRangeResourceDataResultDTO res1 = dataClient.queryTimeRangeResourcesData(dto).get();
        for (QueryTimeRangeResourceDataCriteriaDTO s : res1.getResults().keySet()) {
            for (ResourceDataDTO resourceDataDTO : res1.getResults().get(s).getData()) {
                int hour = new Date(resourceDataDTO.getTimestamp()).getHours();
                if (hour < 18 && hour > 10) {
                    if (resourceDataDTO.getReading() > 0) {
                        dataList.add(resourceDataDTO.getReading());
                    }
                }
            }
        }
        
        return dataList;
    }
}
