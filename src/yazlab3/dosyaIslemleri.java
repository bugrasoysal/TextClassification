package yazlab3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;
//https://github.com/ahmetaa/zemberek-nlp
import zemberek.morphology.TurkishMorphology;
import zemberek.normalization.TurkishSpellChecker;
import zemberek.tokenization.TurkishTokenizer;
import zemberek.tokenization.antlr.TurkishLexer;
import zemberek.morphology.analysis.*;
import java.util.HashMap;



public class dosyaIslemleri {

    
    public static String duzenle(String fileName) {

        fileName = fileName.toLowerCase();
        fileName = fileName.replaceAll("[^a-zA-Z ığüİşçö]", "");
      return  fileName;
    }


    public static String kelime_duzeltme(String fileName) throws IOException {

        TurkishTokenizer tokenizer = TurkishTokenizer.ALL;
        TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
        TurkishSpellChecker spellChecker = new TurkishSpellChecker(morphology);
        StringBuilder output = new StringBuilder();
        for (Token token : tokenizer.tokenize(fileName)) {
            String text = token.getText();
            if (analyzeToken(token) && !spellChecker.check(text)) {
                List<String> strings = spellChecker.suggestForWord(token.getText());
                if (!strings.isEmpty()) {
                    String suggestion = strings.get(0);
                  //  Log.info("Correction: " + text + " -> " + suggestion);
                    //zemberek in duzelttigi kelimeler
                    output.append(suggestion);
                } else {
                    output.append(text);
                }
            } else {
                output.append(text);
            }
        }
      //  Log.info(output);

       return output.toString().toLowerCase();
    }


    static boolean analyzeToken(Token token) {
        return token.getType() != TurkishLexer.NewLine
                && token.getType() != TurkishLexer.SpaceTab
                && token.getType() != TurkishLexer.UnknownWord
                && token.getType() != TurkishLexer.RomanNumeral
                && token.getType() != TurkishLexer.Unknown;
    }
   //https://github.com/ahmetaa/zemberek-nlp/blob/master/examples/src/main/java/zemberek/examples/normalization/CorrectDocument.java


    public static String kok_bulma(String fileName) throws IOException
    {
        TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
        String metin="";
        String[] words = fileName.split(" ");
        for (String word : words) {
        WordAnalysis results = morphology.analyze(word);
        for (SingleAnalysis result : results) {
          //  Log.info("Oflazer style       : " + AnalysisFormatters.OFLAZER_STYLE.format(result).split("\\+")[0]);
            // bulunan kokler
          metin+=(AnalysisFormatters.OFLAZER_STYLE.format(result).split("\\+")[0]+"_");
          //bulunan koklerle yeni bir string olusturuyoruz ve bosluk yerine "_" koyuyoruz

            break;
        }}


      return metin;
    }
    //https://github.com/ahmetaa/zemberek-nlp/blob/master/examples/src/main/java/zemberek/examples/morphology/AnalyzeWords.java

     public static List nGram(String dosya, int n)
     {
         List<String> ngram = new ArrayList<String>();
         String metin="";
         String metin2="";
         for (int i = 0;i<(dosya.length()-n);i++)
         {
             ngram.add(dosya.substring(i,i+n));
         }
         return ngram;

     }

}
