package com.datapath.registryfileloader.service;

//TODO:needs save to another source as project or source code pars
public class GlossaryDataParsing {

//    @Data
//    @AllArgsConstructor
//    private static class Glo {
//        private String cod;
//        private String title;
//        private String description;
//    }
//
//    public static void main(String[] args) throws IOException {
//        InputStream is = Files.newInputStream(Path.of("/home/pylypchuk/Downloads/ДК 016_ 2010.docx"));
//        XWPFDocument xdoc = new XWPFDocument(is);
//
//        List<Glo> glos = new ArrayList<>();
//
//        List<IBodyElement> bodyElements = xdoc.getBodyElements();
//        for (int i = 0; i < bodyElements.size(); i++) {
//            IBodyElement iBodyElement = bodyElements.get(i);
//            if (iBodyElement instanceof XWPFParagraph p) {
//                if (p.getText().contains("Абетковий покажчик ")) {
//                    glos.addAll(extract(bodyElements, i));
//                }
//            }
//        }
//
//        new ObjectMapper().writeValue(new File("result.json"), glos);
//
//        is.close();
//    }
//
//    private static List<Glo> extract(List<IBodyElement> bodyElements, int l) {
//        List<Glo> glos = new ArrayList<>();
//
//        XWPFTable table;
//        try {
//            table = (XWPFTable) bodyElements.get(l + 1);
//        } catch (Exception e) {
//            return Collections.emptyList();
//        }
//
//        for (int i = 0; i < table.getNumberOfRows(); i++) {
//            XWPFTableRow sourceRow = table.getRow(i);
//
//            List<XWPFTableCell> cells = sourceRow.getTableCells();
//
//            String cod = cells.get(0).getText();
//            String title = cells.get(1).getText();
//            String description = cells.get(3).getText();
//
//            glos.add(new Glo(cod, title, description));
//        }
//
//        return glos;
//    }
}
