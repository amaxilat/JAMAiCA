package eu.organicity.annotation.jamaica.www.utils;


import eu.organicity.annotation.jamaica.dto.AnomalyConfigDTO;
import eu.organicity.annotation.jamaica.dto.ClassifConfigDTO;
import eu.organicity.annotation.jamaica.www.model.AnomalyConfig;
import eu.organicity.annotation.jamaica.www.model.ClassifConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.jubat.classifier.LabeledDatum;
import us.jubat.common.Datum;

import java.util.ArrayList;

public class Utils {

    /**
     * a log4j logger to print messages.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * Creates a {@link Datum} to be used in queries to a Jubatus instance.
     *
     * @param value the value of the {@link Datum}.
     * @return the created {@link Datum}.
     */
    public static Datum makeDatum(double value) {
        return new Datum().addNumber("value", value);
    }

    public static ArrayList<Datum> makeDatumList(double value) {
        final Datum datum = new Datum().addNumber("value", value);
        final ArrayList<Datum> data = new ArrayList<>();
        data.add(datum);
        return data;
    }

    /**
     * Creates a {@link LabeledDatum} to be used in queries to a Jubatus instance.
     *
     * @param label the label of the {@link Datum}.
     * @param value the value of the {@link Datum}.
     * @return the created {@link LabeledDatum}.
     */
    public static LabeledDatum makeTrainDatum(String label, double value) {
        return new LabeledDatum(label, makeDatum(value));
    }

    public static AnomalyConfigDTO newAnomalyConfigDTO(final AnomalyConfig storedConfig) {
        final AnomalyConfigDTO dto = new AnomalyConfigDTO();
        dto.setId(storedConfig.getId());
        dto.setIdPat(storedConfig.getIdPat());
        dto.setTypePat(storedConfig.getTypePat());
        dto.setAttribute(storedConfig.getAttribute());
        dto.setTagDomain(storedConfig.getTags());
        dto.setUrlExt(storedConfig.getUrlExt());
        dto.setContextBrokerUrl(storedConfig.getContextBrokerUrl());
        dto.setContextBrokerService(storedConfig.getContextBrokerService());
        dto.setContextBrokerServicePath(storedConfig.getContextBrokerServicePath());
        dto.setEnable(storedConfig.isEnable());
        return dto;
    }

    public static ClassifConfigDTO newClassifConfigDTO(final ClassifConfig storedConfig) {
        final ClassifConfigDTO dto = new ClassifConfigDTO();
        dto.setId(storedConfig.getId());
        dto.setIdPat(storedConfig.getIdPat());
        dto.setTypePat(storedConfig.getTypePat());
        dto.setAttribute(storedConfig.getAttribute());
        dto.setTagDomain(storedConfig.getTags());
        dto.setEnable(storedConfig.isEnable());
        dto.setUrlExt(storedConfig.getUrlExt());
        dto.setContextBrokerUrl(storedConfig.getContextBrokerUrl());
        dto.setContextBrokerService(storedConfig.getContextBrokerService());
        dto.setContextBrokerServicePath(storedConfig.getContextBrokerServicePath());
        dto.setUser(storedConfig.getUser());
        return dto;
    }
}