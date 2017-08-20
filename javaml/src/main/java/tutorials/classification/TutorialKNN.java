///**
// * This file is part of the Java Machine Learning Library
// * <p>
// * The Java Machine Learning Library is free software; you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation; either version 2 of the License, or
// * (at your option) any later version.
// * <p>
// * The Java Machine Learning Library is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// * <p>
// * You should have received a copy of the GNU General Public License
// * along with the Java Machine Learning Library; if not, write to the Free Software
// * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
// * <p>
// * Copyright (c) 2006-2009, Thomas Abeel
// * <p>
// * Project: http://java-ml.sourceforge.net/
// */
//package tutorials.classification;
//
//import libsvm.LibSVM;
//import net.sf.javaml.classification.Classifier;
//import net.sf.javaml.core.Dataset;
//import net.sf.javaml.core.Instance;
//import net.sf.javaml.core.SparseInstance;
//import net.sf.javaml.tools.data.FileHandler;
//
//import java.io.File;
//
///**
// * This tutorial show how to use a the k-nearest neighbors classifier.
// *
// * @author Thomas Abeel
// */
//public class TutorialKNN {
//    /**
//     * Shows the default usage of the KNN algorithm.
//     */
//    public static void main(String[] args) throws Exception {
//
//        /* Load a data set */
//        Dataset data = FileHandler.loadDataset(new File("/home/amaxilatis/repositories/dl4j-examples/dl4j-examples/src/main/resources/classification/in.csv"), 0, ",");
//        System.out.println(data.classes().first());
//
//        for (Instance datum : data) {
//            System.out.println(datum.getID() + ":" + datum.keySet());
//            System.out.println(datum.get(0));
//            System.out.println(datum.classValue());
//        }
//        /*
//         * Contruct a KNN classifier that uses 5 neighbors to make a decision.
//         */
//        Classifier knn = new LibSVM();
//        knn.buildClassifier(data);
//
//        /*
//         * Load a data set for evaluation, this can be a different one, but we
//         * will use the same one.
//         */
//        Dataset dataForClassification = FileHandler.loadDataset(new File("/home/amaxilatis/repositories/dl4j-examples/dl4j-examples/src/main/resources/classification/test.csv"), 0, ",");
//        /* Counters for correct and wrong predictions. */
//        int correct = 0, wrong = 0;
//        SparseInstance instance = new SparseInstance();
//        instance.put(0, 4.0);
//        Object apredictedClassValue = knn.classify(instance);
//        System.out.println("[" + 4.0 + "] pred:'" + apredictedClassValue + "'");
//        instance.put(0, 0.0);
//        apredictedClassValue = knn.classify(instance);
//        System.out.println("[" + 0.0 + "] pred:'" + apredictedClassValue + "'");
//
//        /* Classify all instances and check with the correct class values */
//        for (Instance inst : dataForClassification) {
//
//            Object predictedClassValue = knn.classify(inst);
//            Object realClassValue = inst.classValue();
//            if (predictedClassValue.toString().equals(realClassValue.toString())) {
//                correct++;
//            } else {
//                System.out.println("[" + inst + "] pred:'" + predictedClassValue + "' real:'" + realClassValue + "'");
//                wrong++;
//            }
//        }
//        System.out.println("Correct predictions  " + correct);
//        System.out.println("Wrong predictions " + wrong);
//
//    }
//
//}
