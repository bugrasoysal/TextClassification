package yazlab3;
import java.io.*;
import static java.lang.Math.exp;
import static java.lang.Math.log;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.JFileChooser;
import static  yazlab3.dosyaIslemleri.*;

public class App 
{
        
        public static List<Textler> textList=new ArrayList<>();
        public static List<Textler.Ogrenme> ogrenmeList=new ArrayList<>();
        public static List<Textler.Test> testList=new ArrayList<>();

        public static List gramListYap(List gramList)
        {
            
            List<gram> Grams=new ArrayList<>();
            Set<String> hashG=new HashSet<>(gramList);
                
                for(String gram : hashG) 
                {
                    if((Collections.frequency(gramList, gram))>50)
                    {
                        Grams.add(new gram(gram,Collections.frequency(gramList, gram)));
                    }
                    
                }
            return Grams;
        }
        public static void dosyaOku(String dAdi,String etiket) throws IOException
        {
            String dosyaAdi=dAdi+etiket;
            File dosya=new File(dosyaAdi);
            File[] butunDosyalar= dosya.listFiles();
            for (File file : butunDosyalar)
            {
                String filename= file.getName();
                String data=new String(Files.readAllBytes(file.toPath()),"ISO-8859-9");
                List twoGram;
                List threeGram;  
                //data=duzenle(data);
                //data=kelime_duzeltme(data);
                //data=kok_bulma(data);
                twoGram=nGram(data,2);
                threeGram=nGram(data, 3);
                
               
                textList.add(new Textler(filename,etiket,gramListYap(twoGram),gramListYap(threeGram),twoGram.size(),threeGram.size()));


            }
        }
        public static void dosyaAyir(){

            Random r=new Random();
            int random;
            List<Integer> randomList=new ArrayList<>();
            for (int i = 0; i <((textList.size()*75)/100) ; i++) {

                random=r.nextInt(textList.size());
                while(true)
                {
                    if(!randomList.contains(random)){
                        ogrenmeList.add(new Textler.Ogrenme(textList.get(random)));
                        randomList.add(random);
                        break;
                    }
                    else {
                        random=r.nextInt(textList.size());
                    }
                }


            }
            for (int i = 0; i <((textList.size()*25)/100)+1 ; i++) {

                random=r.nextInt(textList.size());
                while(true)
                {
                    if(!randomList.contains(random)){
                        testList.add(new Textler.Test(textList.get(random)));
                        randomList.add(random);
                        break;
                    }
                    else {
                        random=r.nextInt(textList.size());
                    }
                }

            }

        }
        public static List bayes(String etiket,int gram)
        {
            int GramSize=0;
            int uniqueGramSize=0;
            int frekans=0;
            double bayes=1.0;
            List<Double> frekansList=new ArrayList<>();
            List<Double> bayesList=new ArrayList<>();

            for (int i = 0; i < ogrenmeList.size(); i++) 
            {
               
                if(ogrenmeList.get(i).text.etiket.equals(etiket))
                {
                  if(gram==2)  
                     GramSize=GramSize+ogrenmeList.get(i).text.twoGramSayisi;
                  else
                     GramSize=GramSize+ogrenmeList.get(i).text.threeGramSayisi;
                }
            }
            for (int i = 0; i < ogrenmeList.size(); i++) 
            {
                  if(gram==2)  
                     uniqueGramSize=uniqueGramSize+ogrenmeList.get(i).text.twoGram.size();
                  else
                     uniqueGramSize=uniqueGramSize+ogrenmeList.get(i).text.threeGram.size();
                
                
            }
            if(gram==2)
            {
                for (int i = 0; i < testList.size(); i++) 
                {
                for (int j = 0; j < testList.get(i).text.twoGram.size(); j++)
                {
                    
                    for (int k = 0; k < ogrenmeList.size(); k++)
                    {
                        if(ogrenmeList.get(k).text.etiket.equals(etiket))
                        {
                        for (int l = 0; l < ogrenmeList.get(k).text.twoGram.size(); l++) 
                        {
                            if(testList.get(i).text.twoGram.get(j).hece.equals(ogrenmeList.get(k).text.twoGram.get(l).hece))
                            {
                                frekans=frekans+ogrenmeList.get(k).text.twoGram.get(l).frekans;

                            }
                        }                     
                        }                 
                    }
                    frekansList.add(((double)(frekans+1)/(double)(GramSize+uniqueGramSize)));
                    frekans=0;
                }
                for (int q = 0; q < frekansList.size(); q++) 
                {
                    bayes=bayes*frekansList.get(q);
                }
                if(bayes!=1.0){
                    bayesList.add(bayes);
                }
                else{
                    bayesList.add(0.0);
                }
                    
                bayes=1.0;
                frekansList.clear();
            }
           
            }
            else
            {
                for (int i = 0; i < testList.size(); i++) 
                {
                for (int j = 0; j < testList.get(i).text.threeGram.size(); j++)
                {
                    
                    for (int k = 0; k < ogrenmeList.size(); k++)
                    {
                        if(ogrenmeList.get(k).text.etiket.equals(etiket))
                        {
                        for (int l = 0; l < ogrenmeList.get(k).text.threeGram.size(); l++) 
                        {
                            if(testList.get(i).text.threeGram.get(j).hece.equals(ogrenmeList.get(k).text.threeGram.get(l).hece))
                            {
                                frekans=frekans+ogrenmeList.get(k).text.threeGram.get(l).frekans;

                            }
                        }                     
                        }                 
                    }
                    frekansList.add(((double)(frekans+1)/(double)(GramSize+uniqueGramSize)));
                    
                    frekans=0;
                }
                for (int q = 0; q < frekansList.size(); q++) 
                {
                    bayes=bayes*frekansList.get(q);
                }
                if(bayes!=1.0){
                    bayesList.add(bayes);
                }
                else{
                    bayesList.add(0.0);
                }
                bayesList.add(bayes);
                bayes=1.0;
                frekansList.clear();
            }        
            }
            
            return bayesList;
            
        }
        public static void main(String [] args) throws IOException {


            List<String> temp=new ArrayList<>();
            double dogrueko=0.0,dogrumaga=0.0,dogrusag=0.0,dogruspor=0.0,dogrusiya=0.0,ekosayac=0.0, siyasayac=0.0, sporsayac=0.0, magasayac=0.0, sagsayac=0.0;
            double recallEko=0.0,recallSiyasi=0.0,recallSpor=0.0,recallMagazin=0.0,recallSaglik=0.0;
            double precisionEko=0.0,precisionSiyasi=0.0,precisionSpor=0.0,precisionMagazin=0.0,precisionSaglik=0.0;
            double fmeasureEko=0.0,fmeasureSiyasi=0.0,fmeasureSpor=0.0,fmeasureMagazin=0.0,fmeasureSaglik=0.0;
            String filename="D:\\yazlab3.1\\raw_texts\\";
            dosyaOku(filename,  "ekonomi");
            dosyaOku(filename,  "siyasi");
            dosyaOku(filename,  "spor");
            dosyaOku(filename,  "magazin");
            dosyaOku(filename,  "saglik");
            dosyaAyir();
            List<Double> ekoBayes2=bayes("ekonomi",2);
            List<Double> siyaBayes2=bayes("siyasi",2);
            List<Double> sporBayes2=bayes("spor",2);
            List<Double> magaBayes2=bayes("magazin",2);
            List<Double> saglikBayes2=bayes("saglik",2);
            List<Double> ekoBayes3=bayes("ekonomi",3);
            List<Double> siyaBayes3=bayes("siyasi",3);
            List<Double> sporBayes3=bayes("spor",3);
            List<Double> magaBayes3=bayes("magazin",3);
            List<Double> saglikBayes3=bayes("saglik",3);
            for (int i = 0; i < testList.size(); i++) {
               
                if((ekoBayes2.get(i)+ekoBayes3.get(i))>=(siyaBayes2.get(i)+siyaBayes3.get(i)) &&
                  (ekoBayes2.get(i)+ekoBayes3.get(i))>=(sporBayes2.get(i)+sporBayes3.get(i)) && 
                  (ekoBayes2.get(i)+ekoBayes3.get(i))>=(magaBayes2.get(i)+magaBayes3.get(i)) &&
                  (ekoBayes2.get(i)+ekoBayes3.get(i))>=(saglikBayes2.get(i)+saglikBayes3.get(i))){
                  temp.add("ekonomi");
                 
                
                }
                else if((siyaBayes2.get(i)+siyaBayes3.get(i))>=(ekoBayes2.get(i)+ekoBayes3.get(i)) &&
                  (siyaBayes2.get(i)+siyaBayes3.get(i))>=(sporBayes2.get(i)+sporBayes3.get(i)) && 
                  (siyaBayes2.get(i)+siyaBayes3.get(i))>=(magaBayes2.get(i)+magaBayes3.get(i)) &&
                  (siyaBayes2.get(i)+siyaBayes3.get(i))>=(saglikBayes2.get(i)+saglikBayes3.get(i))){
                    temp.add("siyasi");
                
              
                }
                else if((sporBayes2.get(i)+sporBayes3.get(i))>=(siyaBayes2.get(i)+siyaBayes3.get(i)) &&
                  (sporBayes2.get(i)+sporBayes3.get(i))>=(ekoBayes2.get(i)+ekoBayes3.get(i)) && 
                  (sporBayes2.get(i)+sporBayes3.get(i))>=(magaBayes2.get(i)+magaBayes3.get(i)) &&
                  (sporBayes2.get(i)+sporBayes3.get(i))>=(saglikBayes2.get(i)+saglikBayes3.get(i))){
                    temp.add("spor");
                    
              
                }
                else if((magaBayes2.get(i)+magaBayes3.get(i))>=(siyaBayes2.get(i)+siyaBayes3.get(i)) &&
                  (magaBayes2.get(i)+magaBayes3.get(i))>=(sporBayes2.get(i)+sporBayes3.get(i)) && 
                  (magaBayes2.get(i)+magaBayes3.get(i))>=(ekoBayes2.get(i)+ekoBayes3.get(i)) &&
                  (magaBayes2.get(i)+magaBayes3.get(i))>=(saglikBayes2.get(i)+saglikBayes3.get(i))){
                    temp.add("magazin");
               
                }
                else if((saglikBayes2.get(i)+saglikBayes3.get(i))>=(siyaBayes2.get(i)+siyaBayes3.get(i)) &&
                  (saglikBayes2.get(i)+saglikBayes3.get(i))>=(sporBayes2.get(i)+sporBayes3.get(i)) && 
                  (saglikBayes2.get(i)+saglikBayes3.get(i))>=(magaBayes2.get(i)+magaBayes3.get(i)) &&
                  (saglikBayes2.get(i)+saglikBayes3.get(i))>=(ekoBayes2.get(i)+ekoBayes3.get(i))){
                    temp.add("saglik");
                 
                }
                
            }
            for (int i = 0; i < temp.size(); i++) 
            {
              
                    if(temp.get(i).equals(testList.get(i).text.etiket))
                    {
                    if(temp.get(i).equals("ekonomi"))
                        dogrueko++;
                    if(temp.get(i).equals("spor"))
                        dogruspor++;
                    if(temp.get(i).equals("siyasi"))
                        dogrusiya++;
                    if(temp.get(i).equals("magazin"))
                        dogrumaga++;
                    if(temp.get(i).equals("saglik"))
                        dogrusag++;    
                    }
  
            }
            
            for (int i = 0; i < testList.size(); i++) 
            {
                if(testList.get(i).text.etiket=="ekonomi")
                    ekosayac++;
                else if(testList.get(i).text.etiket=="siyasi")
                    siyasayac++;
                else if(testList.get(i).text.etiket=="spor")
                    sporsayac++;
                else if(testList.get(i).text.etiket=="magazin")
                    magasayac++;
                else if(testList.get(i).text.etiket=="saglik")
                    sagsayac++;
            }
            precisionEko=dogrueko/testList.size();
            precisionSiyasi=dogrusiya/testList.size();
            precisionSpor=dogruspor/testList.size();
            precisionMagazin=dogrumaga/testList.size();
            precisionSaglik=dogrusag/testList.size();
            recallEko=dogrueko/ekosayac;
            recallSiyasi=dogrusiya/siyasayac;
            recallSpor=dogruspor/sporsayac;
            recallMagazin=dogrumaga/magasayac;
            recallSaglik=dogrusag/sagsayac;
            fmeasureEko=2*((precisionEko*recallEko)/(precisionEko+recallEko));
            fmeasureSiyasi=2*((precisionSiyasi*recallSiyasi)/(precisionSiyasi+recallSiyasi));
            fmeasureSpor=2*((precisionSpor*recallSpor)/( precisionSpor+recallSpor));
            fmeasureMagazin=2*((precisionMagazin*recallMagazin)/( precisionMagazin+recallMagazin));
            fmeasureSaglik=2*((precisionSaglik*recallSaglik)/( precisionSaglik+recallSaglik));


            System.out.println("Ekonomi Precision: "+precisionEko+" - "+"Ekonomi Recall: "+recallEko+" - "+"Ekonomi F-measure: "+fmeasureEko);
            System.out.println("Siyasi Precision: "+precisionSiyasi+" - "+"Siyasi Recall: "+recallSiyasi+" - "+"Siyasi F-measure: "+fmeasureSiyasi);
            System.out.println("Spor Precision: "+precisionSpor+" - "+"Spor Recall: "+recallSpor+" - "+"Spor F-measure: "+fmeasureSpor);
            System.out.println("Magazin Precision: "+precisionMagazin+" - "+"Magazin Recall: "+recallMagazin+" - "+"Magazin F-measure: "+fmeasureMagazin);
            System.out.println("Saglik Precision: "+precisionSaglik+" - "+"Saglik Recall: "+recallSaglik+" - "+"Saglik F-measure: "+fmeasureSaglik);

            
        }

    }
