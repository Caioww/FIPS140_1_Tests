package com.example.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	
	private static final String VIRGULA = "\n";
    private static BufferedReader reader;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DemoApplication.class, args);
		
		List<String[]> lista = new ArrayList<String[]>();
		
		List<String> listaCerta = new ArrayList<String>();
		
		File file = new File(DemoApplication.class.getResource("/Chaves/CRIPTOGRAFIA.txt").getFile());
		 reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
         String linha = null;

         while ((linha = reader.readLine()) != null) {
             String[] dadosUsuario = linha.split(VIRGULA);
             lista.add(dadosUsuario);

        }
         for(String[] dados : lista) {
        	 String valida = Arrays.toString(dados);
        	 valida = valida.substring(1, valida.length()-1).replace("]", "").replace("[", "");
        	 listaCerta.add(valida.replaceAll("'", ""));
         }
         
         //Converte os valores em binário e armazena em uma lista
         List<String> listaBinario = new ArrayList<String>();
         for(String valores: listaCerta) {
        	 String result = convertStringToBinary(valores);
        	 listaBinario.add(result);
         }
         
        
         //Faz o Monobit Test para verificar se a chave pode ser considerar aleatória
         for(int i=0; i <listaBinario.size()-1; i++) {
        	 
        	 Boolean thePokerTestOk = false;
        	 Boolean theRunsTest = false;
        	 Boolean longRunsTest = false;
        	 
        	 Boolean  monobitTestOk = monobitTest(listaBinario.get(i));
        	 if(monobitTestOk) {
        		 thePokerTestOk = pokerTest(listaBinario.get(i));
        	 }
        	 if(thePokerTestOk) {
        		 theRunsTest = theRunsTest(listaBinario.get(i));
        	 }
        	 if(theRunsTest) {
        		 longRunsTest = longRunTest(listaBinario.get(i));
        	 }
        	 
        	 if(!(theRunsTest && thePokerTestOk && monobitTestOk && longRunsTest) ) {	
        		 System.out.println("Não Passou");
        		 System.out.println(i);
        	 }else {
        		 System.out.println("Passou");
        		 System.out.println(i);
        	 }
        	
        	 
         }
      
         
         
	}
	
	public static Boolean monobitTest(String x) {   
		int total = 0;
        for(int i=0; i<x.length();i++){
         char ch = x.charAt(i);
            String x1 = String.valueOf(ch);
         if(x1.equalsIgnoreCase("1")){
                total++;

            }

        }
        if(total<10346 && total > 9654) {
        	return true;
        }
        return false;
	
	}  
	
	public static Boolean pokerTest(String chaves) {   
		
		int[] count = new int[16];
		
        	
        	List<String> lista4Bits = new ArrayList<>();
            String arrayLista = "";
        	
            for(int i=0; i<20000; i+=4){
    			    			
    			retornaRepeticoes(i, count, chaves, lista4Bits, arrayLista );
    					
    		}
            
            float verificaPokerTest = 0;
            
            for(int j =0; j< 16; j++) {
            	verificaPokerTest+= (int)Math.pow(count[j],2);
            }
            double x =  16.0/5000.0 * (verificaPokerTest* 1.0) - 5000.0;
			
			return x < 57.4 && x > 1.03 ? true : false;
        	
      
	}  
	
	
	public static boolean longRunTest(String chaves) {
		
		int sequencia = 0;
		
		for(int i=0; i<chaves.length()-1 ;i++){
			char chProximo = chaves.charAt(i+1);
			String proximoValor = String.valueOf(chProximo);
			
	         char chAtual = chaves.charAt(i);
	         String valorAtual = String.valueOf(chAtual);
	        
	         if(valorAtual.equalsIgnoreCase(proximoValor) ){
	        	 sequencia++;

	           }else {
	        	   sequencia=0;
	           }
	         if(sequencia >= 34) {
	        	 return false;
	         }

	        }
		
		return true;
	}
		
	public static void retornaRepeticoes(int i, int[] count, String chavesBinario, List<String> lista4Bits, String arrayLista) {
		
			
			String arrayRetornado = populaVetor(i, chavesBinario, arrayLista);
			
			arrayRetornado.replace("[", "").replace("]", "");
	
			int number = Integer.parseInt(arrayRetornado,2 );
			count[number]++;
			
			
			
			
	}
	
	public final static boolean ValidatePoker(String binarystring)
	{
		int[] count = new int[16];
		int countTotal = 0;
		for (int i = 0; i < 16; i++)
		{
			count[i] = 0;
		}
		for (int i = 0; i < 5000; i += 4)
		{
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(String.format("%1$s", binarystring.charAt(i)));
			stringBuilder.append(String.format("%1$s", binarystring.charAt(i + 1)));
			stringBuilder.append(String.format("%1$s", binarystring.charAt(i + 2)));
			stringBuilder.append(String.format("%1$s", binarystring.charAt(i + 3)));
			int number = Integer.parseInt(String.valueOf(stringBuilder), 2);
			count[number]++;
		}

		for (int i = 0; i < 16; i++)
		{
			countTotal += (int)Math.pow(count[i], 2);
		}

		double x = (16.0 / 5000.0) * (countTotal * 1.0) - 5000.0;

		if (x > 1.03 && x < 57.4)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static  boolean theRunsTest(String binarystring)
	{
		int countZero = 0;
		int countUm = 0;
		int[] countZeroVetor = new int[6];
		int[] countUmVetor = new int[6];
		for (int i = 0; i < 20000; i++)
		{
			if (binarystring.charAt(i) == '0')
			{
				countZero++;
				if (i > 0 && countUm > 0)
				{
					if (countUm > 6)
					{
						countUmVetor[5]++;
					}
					else
					{
						countUmVetor[countUm - 1]++;
					}
				}
				countUm = 0;
			}
			else
			{
				countUm++;
				if (i > 0 && countZero > 0)
				{
					if (countZero > 6)
					{
						countZeroVetor[5]++;
					}
					else
					{
						countZeroVetor[countZero - 1]++;
					}
				}
				countZero = 0;
			}
		}
		if (countZeroVetor[0] > 2267 && countZeroVetor[0] < 2733 && countZeroVetor[1] > 1079 && countZeroVetor[1] < 1421 && countZeroVetor[2] > 502 && countZeroVetor[2] < 748 && countZeroVetor[3] > 223 && countZeroVetor[3] < 402 && countZeroVetor[4] > 90 && countZeroVetor[4] < 223 && countZeroVetor[5] > 90 && countZeroVetor[5] < 223 && countUmVetor[0] > 2267 && countUmVetor[0] < 2733 && countUmVetor[1] > 1079 && countUmVetor[1] < 1421 && countUmVetor[2] > 502 && countUmVetor[2] < 748 && countUmVetor[3] > 223 && countUmVetor[3] < 402 && countUmVetor[4] > 90 && countUmVetor[4] < 223 && countUmVetor[5] > 90 && countUmVetor[5] < 223)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	
	public static String populaVetor(int index, String chaves, String arrayLista) {
		
			StringBuilder builderString = new StringBuilder();
			builderString.append(String.format("%1$s", chaves.charAt(index)));
			builderString.append(String.format("%1$s", chaves.charAt(index+1)));
			builderString.append(String.format("%1$s", chaves.charAt(index+2)));
			builderString.append(String.format("%1$s", chaves.charAt(index+3)));

			
			index++;
			
			return arrayLista = builderString.toString();
	
		
	
	}
 
	 public static String convertStringToBinary(String input) {
		 int i = 0;
         String resultBinary = "";
         StringBuilder binaryString = null;
		 while (i < input.length()) {
	         int index = i, offset = i + 1;
	         binaryString = new
	         StringBuilder(Integer.toBinaryString(Integer.parseInt(input.substring(index,offset), 16)));
         while (binaryString.length() != (offset - index) * 4) {
        	 binaryString = binaryString.insert(0, "0");
         }
         resultBinary += binaryString;
         resultBinary += "";
         i++;

	    }
		 
		 return resultBinary;

	 }
}
