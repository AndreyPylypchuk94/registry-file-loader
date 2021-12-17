package com.datapath.registryfileloader;

import java.util.List;

public class Constants {
    private static final String XLSX_FORMAT = "XLSX";
    private static final String XLS_FORMAT = "XLS";
    private static final String PDF_FORMAT = "PDF";
    private static final String DOC_FORMAT = "DOC";
    private static final String DOCX_FORMAT = "DOCX";
    private static final String CSV_FORMAT = "CSV";
    private static final String TSV_FORMAT = "TSV";
    private static final String ODT_FORMAT = "ODT";
    private static final String JSON_FORMAT = "JSON";
    private static final String ODS_FORMAT = "ODS";

    public static final List<String> XLSX_FORMATS = List.of(XLSX_FORMAT);
    public static final List<String> XLS_FORMATS = List.of(XLS_FORMAT);
    public static final List<String> PDF_FORMATS = List.of(PDF_FORMAT);
    public static final List<String> DOC_FORMATS = List.of(DOC_FORMAT);
    public static final List<String> DOCX_FORMATS = List.of(DOCX_FORMAT);
    public static final List<String> TEXT_FORMATS = List.of(CSV_FORMAT, JSON_FORMAT, TSV_FORMAT);
    public static final List<String> ODF_FORMATS = List.of(ODT_FORMAT, ODS_FORMAT);
    public static final List<String> ODS_FORMATS = List.of(ODS_FORMAT);
}
