package com.bootcamp.persona.domain.exception;

public final class DomainConstants {

    private DomainConstants() {}

    public static final int MAX_ENROLLMENTS = 5;
    public static final String NAME_REQUIRED = "El nombre es obligatorio";
    public static final String EMAIL_REQUIRED = "El email es obligatorio";
    public static final String BOOTCAMP_IDS_REQUIRED = "Los ids de bootcamps son obligatorios";
    public static final String MAX_ENROLLMENTS_MSG = "Máximo se puede inscribir en 5 bootcamps al tiempo";
    public static final String OVERLAP_MSG = "No se puede inscribir en bootcamps que coincidan en fecha y duración";
    public static final String ALREADY_ENROLLED_MSG = "Ya está inscrito en este bootcamp";
    public static final String PERSON_REQUIRED="El objeto persona no debe ser nullo";

}
