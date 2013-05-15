package systemprogramming;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
   public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner s=new Scanner(new FileInputStream("DEVF3.txt"));
        FileWriter fw=new FileWriter("DEV05.txt");
        BufferedWriter out = new BufferedWriter(fw);
        StringBuilder stringFinal=new StringBuilder();
        String start="";
        boolean flag=false;
        while(s.hasNext()){
            start=s.next();
            if(start.equals("load")){
                flag=true;
                break;
            }
        }
        if(!flag){
            System.out.println("This file isn't valid.\n");
            System.exit(0);
        }
        String startingAdress=s.next();
        StringTokenizer st=new StringTokenizer(s.nextLine());
        int countTokens=st.countTokens();
        String[] prog=new String[countTokens];
        for(int i=0;i<countTokens;i++){
            prog[i]=st.nextToken();
        }
        HashSet<String> syms=new HashSet<String>();
        HashSet<String> address=new HashSet<String>();
        LinkedList<String> ExterRef=new LinkedList<String>();
        for(int i=0;i<prog.length;i++){
            Scanner tempSc=new Scanner(new FileInputStream(prog[i]+".txt"));
            int line=1;
            HashMap<String ,String> mapfile=new HashMap<String, String>();
            LinkedList<String> list=new LinkedList<String>();
            String startingAddressOfthis="";
            String lengthOfThis="";
            while(tempSc.hasNext()){
                StringBuilder sb=new StringBuilder(tempSc.nextLine());
                int length=1;
                if(sb.charAt(0)=='H'){
                    StringBuilder tempW=new StringBuilder();
                    length+=6;
                    for(int j=length;j<length+6;j++){
                        tempW.append(sb.charAt(j));
                    }
                    startingAddressOfthis=tempW.toString().trim();
                    tempW=new StringBuilder();
                    length+=6;
                    for(int j=length;j<length+6;j++){
                        tempW.append(sb.charAt(j));
                    }
                    lengthOfThis=tempW.toString().trim();
                }
                while(sb.charAt(0)=='D'){
                    if(length>=sb.length()){
                        break;
                    }
                    StringBuilder tempSym=new StringBuilder();
                    for(int j=length;j<length+6;j++){
                        tempSym.append(sb.charAt(j));
                    }
                    length+=6;
                    StringBuilder tempAddress=new StringBuilder();
                    for(int j=length;j<length+6;j++){
                        tempAddress.append(sb.charAt(j));
                    }
                    length+=6;
                    String symb=tempSym.toString().trim();
                    String addr=tempAddress.toString().trim();
                    if(syms.contains(symb)){
                        System.out.println("ERROR : "+" at "+prog[i]+" at line "+line+" the External Sym "+symb+" is already predefined in another file.");
                        System.exit(-1);
                    }else if(mapfile.containsKey(symb)){
                        System.out.println("ERROR : "+" at "+prog[i]+" at line "+line+" the External Sym "+symb+" is already predefined in the same file.");
                        System.exit(-1);
                    }else{
                        if(address.contains(addr)){
                           System.out.println("ERROR : "+" at "+prog[i]+" at line "+line+" the External Sym "+symb+" has the address that is already full.");
                           System.exit(-1); 
                        }
                        else{
                            syms.add(symb);
                            int sAd=Integer.parseInt(addr,16);
                            sAd+=Integer.parseInt(startingAddressOfthis,16);
                            addr=Integer.toString(sAd, 16);
                            address.add(addr);
                            mapfile.put(symb,addr);
                            list.add(symb);
                        }
                    }
                }
                while(sb.charAt(0)=='R'){
                    //Check the external References
                    if(length>=sb.length()){
                        break;
                    }
                    StringBuilder tempSym=new StringBuilder();
                    for(int j=length;j<length+6;j++){
                        if(j>=sb.length()){
                            break;
                        }
                        tempSym.append(sb.charAt(j));
                    }
                    length+=6;
                    ExterRef.add(tempSym.toString().trim());
                }
                if(sb.charAt(0)=='E'){
                    break;
                }
                line++;
            }          
            //Write a table for each file PLEASE ...
            stringFinal.append(prog[i]).append(" ").append("\t      \t").append(startingAddressOfthis.toUpperCase())
                    .append("\t").append(lengthOfThis.toUpperCase()).append("\n");
            while(!list.isEmpty()){
                String temp=list.remove();
                for(int z=temp.length();z<6;z++){
                    temp+=" ";
                }
                String addr=mapfile.get(temp.trim());
                for(int z=addr.length();z<6;z++){
                    addr="0"+addr;
                }        
                stringFinal.append("      \t").append(temp)
                    .append("\t").append(addr.toUpperCase()).append("\n");
            }
            stringFinal.append("\n");
        }
        while(!ExterRef.isEmpty()){
            String temp=ExterRef.remove();
            if(!syms.contains(temp)){
               System.out.println("Error there is an invalid external ref on sym "+temp+".\n"); 
               System.exit(-1);
            }
        }
        out.write(stringFinal.toString());
        out.close();
    } 
}