import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    protected Map<String, List<PageEntry>> fullList = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        for (File pdf : pdfsDir.listFiles()) {
            String pdfName = pdf.getName();

            var doc = new PdfDocument(new PdfReader(pdf));

            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                var words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота
                for (var word : words) { // перебираем слова
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }

                for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                    List<PageEntry> list = new ArrayList<>();

                    if (fullList.containsKey(entry.getKey())) {
                        list = fullList.get(entry.getKey());
                    }

                    list.add(new PageEntry(pdfName, i, entry.getValue()));
                    fullList.put(entry.getKey(), list);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {

        String requiredWord = word.toLowerCase();

        if (fullList.containsKey(requiredWord)) {
            List<PageEntry> list = fullList.get(requiredWord);
            Collections.sort(list);
            return list;
        } else {
            System.out.println("Слово '" + word + "' в документах остутствует");
            return Collections.emptyList();
        }
    }
}