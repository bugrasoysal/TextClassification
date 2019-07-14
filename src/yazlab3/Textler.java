/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yazlab3;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bugra
 */
public class Textler{

            List<yazlab3.gram> twoGram;
            List<yazlab3.gram> threeGram;
            String etiket;
            String dosyaAdi;
            int twoGramSayisi;
            int threeGramSayisi;
            
            public Textler(String dosyaAdi, String etiket, List twoGram, List threeGram,int twoGramSayisi, int threeGramSayisi){

                this.dosyaAdi=dosyaAdi;
                this.etiket= etiket;
                this.twoGram=twoGram;
                this.threeGram=threeGram;
                this.threeGramSayisi=threeGramSayisi;
                this.twoGramSayisi=twoGramSayisi;
}

public static class Ogrenme{
    
         
            Textler text;
            public Ogrenme(Textler text){
          
            this.text=text;
              
            }
}            
public static class Test{
    
            Textler text;
            public Test(Textler text){

            this.text=text;
 
            }            
            
}
}
